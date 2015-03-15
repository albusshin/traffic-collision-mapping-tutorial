package haven.mappingtutorial;

import java.io.InputStream;

import org.json.JSONObject;

public class Config {
	
	//To understand how this function works,
	//see http://stackoverflow.com/a/5445161/1831275
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	private JSONObject getConfigJSON() {
		ClassLoader loader = Config.class.getClassLoader();
		InputStream is = loader.getResourceAsStream("haven/mappingtutorial/config.json");
		try {
			return new JSONObject(convertStreamToString(is));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getCartoDBApiKey() {
		JSONObject configJSON = this.getConfigJSON();
		try {
			return configJSON.getString("cartoDBApiKey");
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public DBConfig getDBConfig() {
		JSONObject configJSON = this.getConfigJSON();
		try {
			configJSON = configJSON.getJSONObject("database");
			DBConfig dbConfig = this.new DBConfig();
			dbConfig.username = configJSON.getString("username");
			dbConfig.password = configJSON.getString("password");
			dbConfig.verticaHostIP = configJSON.getString("verticaHostIP");
			dbConfig.portNumber = configJSON.getString("portNumber");
			dbConfig.databaseName = configJSON.getString("databaseName");
			return dbConfig;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public class DBConfig {
		public String username;
		public String password;
		public String verticaHostIP;
		public String portNumber;
		public String databaseName;
	}
}
