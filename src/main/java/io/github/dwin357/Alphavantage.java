package io.github.dwin357;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class Alphavantage {
	// https://www.alphavantage.co/documentation/
	private final String API_KEY = "1UUPAELVXBY4JLH2";
	private final String USER_AGENT = "Mozilla/5.0";
	
	public Alphavantage() {
		super();
	}
	
	public static void main(String[] args) throws IOException {
		Alphavantage av = new Alphavantage();
		
		System.out.println("sending get");
		String response = av.alpha();
		System.out.println("alpha is...");
		System.out.println(response);
	}
	
	private String get() throws IOException {
		String url = "http://www.google.com/search?q=dog*";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int status = con.getResponseCode();
		System.out.println("response " + status);

		BufferedReader in = 
				new BufferedReader(
						new InputStreamReader(
								con.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	
	public String alpha() throws IOException {
		StringBuffer url = new StringBuffer();
		url.append("https://www.alphavantage.co/query");
		url.append("?");
		url.append("function=TIME_SERIES_INTRADAY");
		url.append("&");
		url.append("symbol=MSFT");
		url.append("&");
		url.append("interval=60min");
		url.append("&");
		url.append("apikey="+API_KEY);
		
		
		URL obj = new URL(url.toString());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int status = con.getResponseCode();
		System.out.println("response " + status);

		BufferedReader in = 
				new BufferedReader(
						new InputStreamReader(
								con.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		// https://crunchify.com/java-how-to-parse-jsonobject-and-jsonarrays/
		JSONObject json = new JSONObject(response.toString());
		String tz = json.getJSONObject("Meta Data").getString("6. Time Zone");
		
		return tz;
		
		
	}

}
