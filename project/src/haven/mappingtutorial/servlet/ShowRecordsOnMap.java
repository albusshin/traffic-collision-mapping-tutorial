package haven.mappingtutorial.servlet;

import haven.mappingtutorial.Config;
import haven.mappingtutorial.Helpers;
import haven.mappingtutorial.db.AdditionalConditions;
import haven.mappingtutorial.db.AdditionalConditions.WithCondition;
import haven.mappingtutorial.db.DBHelper;
import haven.mappingtutorial.model.CollisionRecord;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class ShowRecordsOnMap
 */
@WebServlet("/ShowRecordsOnMap")
public class ShowRecordsOnMap extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// The minimum amount of records to be inserted, to raise a warning that the action might take a long time
	private static final long WARNING_THRESHOLD = 10000; 
	public static final int SRID = 4326; //See http://postgis.net/docs/ST_SRID.html for more details
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowRecordsOnMap() {
        super();
    }

	/**
	 * This Servlet does the following things.
	 * First, get a list of records according to the parameters passed in;
	 * Then, insert these records to Carto DB to get a map visualization;
	 * Finally, returns the records metadata as a JSON String using the PrintWriter of the response object.
	 * 
	 * However, if the record size is too big, the Servlet doesn't directly insert the records to Carto DB.
	 * Instead, it asks the user for confirmation of inserting such a big load of data.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Calendar c = Calendar.getInstance();
		String startStr = request.getParameter("start"); //The start date passed in
		String endStr = request.getParameter("end"); //The end date passed in
		//The following parameters should be self-explained
		String paramWithDeaths = request.getParameter("withDeaths");
		String paramWithInjuries = request.getParameter("withInjuries");
		String paramWithPedestriansInvolved = request.getParameter("withPedestriansInvolved");
		String paramWithCyclistsInvolved = request.getParameter("withCyclistsInvolved");
		String paramWithMotoristsInvolved = request.getParameter("withMotoristsInvolved");
		String paramVehicleTypes = request.getParameter("vehicleTypes");
		String paramContributingFactors = request.getParameter("contributingFactors");
		String paramVehiclesInvolved = request.getParameter("vehiclesInvolved");
		String confirmed = request.getParameter("confirmed"); //Whether this request has been confirmed by the user.
		
		//Use this date format to parse the dates passed in as strings
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		try {
			start = format.parse(startStr);
			end = format.parse(endStr);
			System.out.println("start = " + start);
			System.out.println("end = " + end);
		} catch (ParseException e1) {
			//Return 404 not found
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		//Add the additional conditions into our SQL queries
		AdditionalConditions additionalConditions = new AdditionalConditions();
		try {
			if (paramWithDeaths.equals("true")) additionalConditions.withConditions.add(WithCondition.WithDeaths);
			if (paramWithInjuries.equals("true")) additionalConditions.withConditions.add(WithCondition.WithInjuries);
			if (paramWithPedestriansInvolved.equals("true")) additionalConditions.withConditions.add(WithCondition.WithPedestriansInvolved);
			if (paramWithCyclistsInvolved.equals("true")) additionalConditions.withConditions.add(WithCondition.WithCyclistsInvolved);
			if (paramWithMotoristsInvolved.equals("true")) additionalConditions.withConditions.add(WithCondition.WithMotoristsInvolved);
			if (paramVehicleTypes != null) {
				String[] vehicleTypes = paramVehicleTypes.split(",");
				for (String vehicleType : vehicleTypes){
					additionalConditions.vehicleTypes.add(vehicleType);
				}
			}
			if (paramContributingFactors != null) {
				String[] contributingFactors = paramContributingFactors.split(",");
				for (String contributingFactor : contributingFactors){
					additionalConditions.contributingFactors.add(contributingFactor);
				}
			}
			if (paramVehiclesInvolved != null) {
				additionalConditions.vehiclesInvolved = Integer.parseInt(paramVehiclesInvolved);
			}
		} catch (Exception e) {
			//Invalid input provided, return 400 not found
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		//Get the collision records from Vertica database, using our DBHelper class as the helper
		List<CollisionRecord> collisionRecords = new DBHelper().getCollisionRecordsIn(start, end, additionalConditions);
		
		// See whether the data amount exceeds the limit.
		// If so, and the confirmed tag is not set, We alert the user that this request might take a very long time.
		JSONObject retJson = new JSONObject(); //The JSON object to be returned
		PrintWriter writer = response.getWriter();
		if (collisionRecords.size() > WARNING_THRESHOLD && confirmed == null) {
			try {
				retJson.put("exceeded", true);
				retJson.put("amount", collisionRecords.size());
			} catch (JSONException e) { //Never happens
				e.printStackTrace();
			}
			writer.println(retJson);
			return;
		}
		
		String sql = "TRUNCATE TABLE nytc;";
		
		// Data for displaying chart
		int killedPersons = 0;
		int injuredPersons = 0;
		int killedPedestrians = 0;
		int injuredPedestrians = 0;
		int killedCyclists = 0;
		int injuredCyclists = 0;
		int killedMotorists = 0;
		int injuredMotorists = 0;
		if (collisionRecords.size() > 0) { // If the size == 0, we don't need to add the INSERT statement.
			sql += " INSERT INTO nytc (the_geom,"
					+ "persons_killed,"
					+ "persons_injured,"
					+ "pedestrians_injured,"
					+ "pedestrians_killed,"
					+ "cyclists_injured,"
					+ "cyclists_killed,"
					+ "motorists_injured,"
					+ "motorists_killed) VALUES ";
			
			for (int i = 0; i < collisionRecords.size(); i++) {
				CollisionRecord collisionRecord = collisionRecords.get(i);
				//This is how we convert text longitude and latitude to a geom point, using Post GIS format.
				//See http://postgis.net/docs/ST_GeomFromText.html for more details.
				sql += "(ST_GeomFromText(\'POINT(" + collisionRecord.getLongitude() + " " + collisionRecord.getLatitude() + ")', " + SRID + "), " +
						collisionRecord.getKilledPersons() + ", " + 
						collisionRecord.getInjuredPersons() + ", " + 
						collisionRecord.getInjuredPedestrians() + ", " + 
						collisionRecord.getKilledPedestrians() + ", " + 
						collisionRecord.getInjuredCyclist() + ", " + 
						collisionRecord.getKilledCyclist() + ", " + 
						collisionRecord.getInjuredMotorist() + ", " + 
						collisionRecord.getKilledMotorist() + 
						")";
				if (i == collisionRecords.size() - 1) sql += ";";
				else sql += ",";
				killedPersons += collisionRecord.getKilledPersons();
				injuredPersons += collisionRecord.getInjuredPersons();
				killedPedestrians += collisionRecord.getKilledPedestrians();
				injuredPedestrians += collisionRecord.getInjuredPedestrians();
				killedCyclists += collisionRecord.getKilledCyclist();
				injuredCyclists += collisionRecord.getInjuredCyclist();
				killedMotorists += collisionRecord.getKilledMotorist();
				injuredMotorists += collisionRecord.getInjuredMotorist();
			}
		}
		String url = "http://albusshin.cartodb.com/api/v2/sql";
		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("api_key", new Config().getCartoDBApiKey());
		postParams.put("q", sql);
		try {
			String ret = Helpers.sendPostRequest(url, postParams);
			Calendar now = Calendar.getInstance();
			System.out.println(now + ", " + c);
			System.out.println(ret);
		} catch (Exception e) {
			//404
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		try {
			retJson.put("exceeded", false);
			retJson.put("amount", collisionRecords.size());
			retJson.put("killedPersons", killedPersons);
			retJson.put("injuredPersons", injuredPersons);
			retJson.put("killedPedestrians", killedPedestrians);
			retJson.put("injuredPedestrians", injuredPedestrians);
			retJson.put("killedCyclists", killedCyclists);
			retJson.put("injuredCyclists", injuredCyclists);
			retJson.put("killedMotorists", killedMotorists);
			retJson.put("injuredMotorists", injuredMotorists);
		} catch (JSONException e) {
			//404
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		writer.println(retJson);
	}

}
