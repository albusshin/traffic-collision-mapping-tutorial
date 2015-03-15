package haven.mappingtutorial;

import org.json.JSONObject;

public class Config {
	private static JSONObject getConfigJSON() {
		try {
			String retStr = Helpers.sendGetRequest("http://localhost:8080/traffic-collision-mapping-tutorial/config/config.json");
			return new JSONObject(retStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String getCartoDBApiKey() {
		JSONObject configJSON = getConfigJSON();
		try {
			return configJSON.getString("cartoDBApiKey");
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public DBConfig getDBConfig() {
		JSONObject configJSON = getConfigJSON();
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
