package haven.mappingtutorial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class Helpers {

	
	/**
	 * send HTTP Get request with HttpComponent library
	 * 
	 * @param url
	 * @return the response text
	 * @throws Exception
	 */
	public static String sendGetRequest(String url) throws Exception {
		 
 
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
 
		//Print the request and response to console
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
 
		BufferedReader reader = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
 
		return result.toString();
	}
	

	/**
	 * send HTTP Post request with HttpComponent library
	 * 
	 * @param url
	 * @return the response text
	 * @throws Exception
	 */
	public static String sendPostRequest(String url, HashMap<String, String> params) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		for (String s : params.keySet()) {
			urlParameters.add(new BasicNameValuePair(s, params.get(s)));
		}
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		HttpResponse response = client.execute(post);
		//Print the request and response to console
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Code : " 
                + response.getStatusLine().getStatusCode());
 
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line); 
		}
		return result.toString();
	}
}
