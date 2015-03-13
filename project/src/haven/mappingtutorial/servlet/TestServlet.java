package haven.mappingtutorial.servlet;

import haven.mappingtutorial.Constants;
import haven.mappingtutorial.Helpers;
import haven.mappingtutorial.db.AdditionalCondition;
import haven.mappingtutorial.db.DBHelper;
import haven.mappingtutorial.model.CollisionRecord;

import java.io.IOException;
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

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		String startStr = request.getParameter("start");
		String endStr = request.getParameter("end");
		String withDeaths = request.getParameter("withDeaths");
		String withInjuries = request.getParameter("withInjuries");
		String withPedestriansInvolved = request.getParameter("withPedestriansInvolved");
		String withCyclistsInvolved = request.getParameter("withCyclistsInvolved");
		String withMotoristsInvolved = request.getParameter("withMotoristsInvolved");
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
		List<AdditionalCondition> additionalConditions = new ArrayList<AdditionalCondition>();
		if (withDeaths.equals("true")) additionalConditions.add(AdditionalCondition.WithDeaths);
		if (withInjuries.equals("true")) additionalConditions.add(AdditionalCondition.WithInjuries);
		if (withPedestriansInvolved.equals("true")) additionalConditions.add(AdditionalCondition.WithPedestriansInvolved);
		if (withCyclistsInvolved.equals("true")) additionalConditions.add(AdditionalCondition.WithCyclistsInvolved);
		if (withMotoristsInvolved.equals("true")) additionalConditions.add(AdditionalCondition.WithMotoristsInvolved);
		List<CollisionRecord> collisionRecords = new DBHelper().getCollisionRecordsIn(start, end, additionalConditions);
		String sql = "TRUNCATE TABLE nytc; INSERT INTO nytc (the_geom,"
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
		
	}

}
