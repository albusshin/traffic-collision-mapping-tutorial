package haven.mappingtutorial.db;

import haven.mappingtutorial.model.CollisionRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DB class contains helpers of database operations
 */
public class DBHelper {
	static final String USERNAME = "dbadmin"; //username of database
	static final String PASSWORD = "password";
	static final String VERTICAHOST = "192.168.1.110"; //Vertica Host IP Address. Change this constant according to your setup
	static final String PORTNUMBER = "5433"; //Vertica Host Port Number. Change this constant according to your setup
	static final String DATABASENAME = "NYTrafficCollision";  //Database Name for this Tutorial
	

	/**
	 * Opens a connection to the database.
	 * @return
	 * @throws SQLException
	 */
	private Connection connectToDB() throws SQLException{
		try {
			//Try find the driver class
			Class.forName("com.vertica.Driver");
		} catch (ClassNotFoundException e) {
			// Could not find the driver class. Likely an issue with finding the .jar file.
			System.err.println("Could not find the JDBC driver class.");
			e.printStackTrace();
			return null; // Bail out. We cannot do anything further.
		}
		//Try connect to the database.
		Connection conn = DriverManager.getConnection("jdbc:vertica://"
				+ VERTICAHOST + ":" + PORTNUMBER + "/" + DATABASENAME,
				USERNAME, PASSWORD);
		//return the connection for further usage.
		return conn;
	}
	
	/**
	 * convert the ResultSet to a List of CollisionRecord
	 * @param rs - the result set
	 * @return the converted ArrayList of CollisionRecord as a List object
	 * @throws SQLException
	 */
	private List<CollisionRecord> getListWithResultSet(ResultSet rs) throws SQLException {
		List<CollisionRecord> collisionRecords = new ArrayList<CollisionRecord>();
		while (rs.next()) {
			//Iterate the whole result set
			//Create a new TweetInfo object, fill the fields and then add to the ArrayList
			CollisionRecord collisionRecord = new CollisionRecord();
			
			collisionRecord.setDate(rs.getDate("date"));
			collisionRecord.setTime(rs.getTime("time"));
			collisionRecord.setBorough(rs.getString("borough"));
			collisionRecord.setZipCode(rs.getString("zip_code"));
			collisionRecord.setLatitude(rs.getDouble("latitude"));
			collisionRecord.setLongitude(rs.getDouble("longitude"));
			collisionRecord.setLocation(rs.getString("location"));
			collisionRecord.setOnStreetName(rs.getString("on_street_name"));
			collisionRecord.setCrossStreetName(rs.getString("cross_street_name"));
			collisionRecord.setOffStreetName(rs.getString("off_street_name"));
			collisionRecord.setInjuredPersons(rs.getInt("number_of_persons_injured"));
			collisionRecord.setKilledPersons(rs.getInt("number_of_persons_killed"));
			collisionRecord.setInjuredPedestrians(rs.getInt("number_of_pedestrians_injured"));
			collisionRecord.setKilledPedestrians(rs.getInt("number_of_pedestrians_killed"));
			collisionRecord.setInjuredCyclist(rs.getInt("number_of_cyclist_injured"));
			collisionRecord.setKilledCyclist(rs.getInt("number_of_cyclist_killed"));
			collisionRecord.setInjuredMotorist(rs.getInt("number_of_motorist_injured"));
			collisionRecord.setKilledMotorist(rs.getInt("number_of_motorist_killed"));
			collisionRecord.setContributingFactorVehicle1(rs.getString("contributing_factor_vehicle_1"));
			collisionRecord.setContributingFactorVehicle2(rs.getString("contributing_factor_vehicle_2"));
			collisionRecord.setContributingFactorVehicle3(rs.getString("contributing_factor_vehicle_3"));
			collisionRecord.setContributingFactorVehicle4(rs.getString("contributing_factor_vehicle_4"));
			collisionRecord.setContributingFactorVehicle5(rs.getString("contributing_factor_vehicle_5"));
			collisionRecord.setUniqueKey(rs.getLong("unique_key"));
			collisionRecord.setVehicleTypeCode1(rs.getString("vehicle_type_code_1"));
			collisionRecord.setVehicleTypeCode2(rs.getString("vehicle_type_code_2"));
			collisionRecord.setVehicleTypeCode3(rs.getString("vehicle_type_code_3"));
			collisionRecord.setVehicleTypeCode4(rs.getString("vehicle_type_code_4"));
			collisionRecord.setVehicleTypeCode5(rs.getString("vehicle_type_code_5"));
			
			collisionRecords.add(collisionRecord);
			
		}
		System.out.println(collisionRecords.size());
		return collisionRecords;
	}

	public List<CollisionRecord> getCollisionRecords(int count) {
		//Use try with resource statement to open a connection to DB,
		//so that the connection is automatically closed whether there
		//is a exception thrown or not.
		try ( Connection conn = connectToDB() ){
			//Create a SQL statement to query, using SELECT statement
			String sql = "SELECT * FROM public.nypd_motor_vehicle_collisions limit ?;";
			//Create a statement to query the database
			PreparedStatement stmt = conn.prepareStatement(sql);
			//Set PreparedStatement parameters, starting from 1. To understand why the parameter starts from 1,
			//see http://stackoverflow.com/questions/616135/in-jdbc-why-do-parameter-indexes-for-prepared-statements-begin-at-1-instead-of
			stmt.setInt(1, count);
			
			//Query the database using the SQL statement, and get the result set
			ResultSet rs = stmt.executeQuery();
			
			//Construct an ArrayList of TweetInfos
			List<CollisionRecord> collisionRecords = getListWithResultSet(rs);
			
			//Close the ResultSet
			rs.close();
			return collisionRecords;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//If something went wrong, return null
		return null;
	}
	
	public List<CollisionRecord> getCollisionRecordsIn(Date start, Date end) {
		try ( Connection conn = connectToDB() ){
			String sql = "SELECT * FROM public.nypd_motor_vehicle_collisions where date >= ? and date <= ?;";
			//Create a statement to query the database
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDate(1, new java.sql.Date(start.getTime()));
			stmt.setDate(2, new java.sql.Date(end.getTime()));
			
			//Query the database using the SQL statement, and get the result set
			ResultSet rs = stmt.executeQuery();
			
			//Construct an ArrayList of TweetInfos
			List<CollisionRecord> collisionRecords = getListWithResultSet(rs);
			
			//Close the ResultSet
			rs.close();
			return collisionRecords;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//If something went wrong, return null
		return null;
	};
	
	public List<CollisionRecord> getCollisionRecordsIn(Date start, Date end, List<AdditionalCondition> additionalConditions) {
		try ( Connection conn = connectToDB() ){
			String sql = "SELECT * FROM public.nypd_motor_vehicle_collisions where date >= ? and date <= ?";
			for (AdditionalCondition additionalCondition : additionalConditions) {
				switch (additionalCondition) {
				case WithInjuries:
					sql += " and number_of_persons_injured > 0";
					break;
				case WithDeaths:
					sql += " and number_of_persons_killed > 0";
					break;
				case WithPedestriansInvolved:
					sql += " and (number_of_pedestrians_injured > 0 or number_of_pedestrians_killed > 0)";
					break;
				case WithCyclistsInvolved:
					sql += " and (number_of_cyclist_injured > 0 or number_of_cyclist_killed > 0)";
					break;
				case WithMotoristsInvolved:
					sql += " and (number_of_motorist_injured > 0 or number_of_motorist_killed > 0)";
					break;
				default:
					break;
				}
			}
			sql += ";";
			System.out.println(sql);
			//Create a statement to query the database
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDate(1, new java.sql.Date(start.getTime()));
			stmt.setDate(2, new java.sql.Date(end.getTime()));
			
			//Query the database using the SQL statement, and get the result set
			ResultSet rs = stmt.executeQuery();
			
			//Construct an ArrayList of TweetInfos
			List<CollisionRecord> collisionRecords = getListWithResultSet(rs);
			
			//Close the ResultSet
			rs.close();
			return collisionRecords;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//If something went wrong, return null
		return null;
	};
	
}
