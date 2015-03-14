package haven.mappingtutorial.servlet;

import haven.mappingtutorial.Constants;
import haven.mappingtutorial.Helpers;
import haven.mappingtutorial.db.AdditionalCondition;
import haven.mappingtutorial.db.DBHelper;
import haven.mappingtutorial.model.CollisionRecord;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// The minimum amount of records to be inserted, to raise a warning that the action might take a long time
	private static final long WARNING_THRESHOLD = 10000; 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Calendar c = Calendar.getInstance();
		String startStr = request.getParameter("start"); //The start date passed in
		String endStr = request.getParameter("end"); //The end date passed in
		//The following parameters should be self-explained
		String withDeaths = request.getParameter("withDeaths");
		String withInjuries = request.getParameter("withInjuries");
		String withPedestriansInvolved = request.getParameter("withPedestriansInvolved");
		String withCyclistsInvolved = request.getParameter("withCyclistsInvolved");
		String withMotoristsInvolved = request.getParameter("withMotoristsInvolved");
		
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
			//TODO
			e1.printStackTrace();
		}
		//Add the additional conditions into our SQL queries
		List<AdditionalCondition> additionalConditions = new ArrayList<AdditionalCondition>();
		if (withDeaths.equals("true")) additionalConditions.add(AdditionalCondition.WithDeaths);
		if (withInjuries.equals("true")) additionalConditions.add(AdditionalCondition.WithInjuries);
		if (withPedestriansInvolved.equals("true")) additionalConditions.add(AdditionalCondition.WithPedestriansInvolved);
		if (withCyclistsInvolved.equals("true")) additionalConditions.add(AdditionalCondition.WithCyclistsInvolved);
		if (withMotoristsInvolved.equals("true")) additionalConditions.add(AdditionalCondition.WithMotoristsInvolved);
		
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
			writer.println(retJson);
			return;
		}
		
		String sql = "TRUNCATE TABLE nytc;";
		if (collisionRecords.size() > 0) { // If the size == 0, we don't need to add the INSERT statement.
			sql += " INSERT INTO nytc (the_geom,"
					+ "persons_killed,"
					+ "persons_injured,"
					+ "pedestrains_injured,"
					+ "pedestrains_killed,"
					+ "cyclists_injured,"
					+ "cyclists_killed,"
					+ "motorists_injured,"
					+ "motorists_killed) VALUES ";
			
			for (int i = 0; i < collisionRecords.size(); i++) {
				CollisionRecord collisionRecord = collisionRecords.get(i);
				sql += "(ST_GeomFromText(\'POINT(" + collisionRecord.getLongitude() + " " + collisionRecord.getLatitude() + ")', 4326), " +
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
			}
		}
		String url = "http://albusshin.cartodb.com/api/v2/sql";
		HashMap<String, String> postParams = new HashMap<String, String>();
		postParams.put("api_key", Constants.CARTODB_APIKEY);
		postParams.put("q", sql);
		try {
			String ret = Helpers.sentPostRequest(url, postParams);
			Calendar now = Calendar.getInstance();
			System.out.println(now + ", " + c);
			System.out.println(ret);
		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}
		

		try {
			retJson.put("exceeded", false);
			retJson.put("amount", collisionRecords.size());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(retJson);
	}

}
