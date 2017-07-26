package io.github.dwin357;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String response = av.yahoo();
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
	
	public String yahoo() throws IOException {
		//https://stackoverflow.com/questions/44030983/yahoo-finance-url-not-working/44495145#44495145
		
		URL cookieThief = new URL("https://finance.yahoo.com/quote/SPY");
		ArrayList<String> cookieJar = new ArrayList<String>();
		URLConnection con = cookieThief.openConnection();
		for (Entry<String, List<String>> entry : con.getHeaderFields().entrySet()) {
			if (entry.getKey() == null || !entry.getKey().equals("Set-Cookie")) {
				continue;
			}
			for (String cookie : entry.getValue()) {
				cookieJar.add(cookie);
			}
		}
		
		String crumb = null;
		InputStream is = con.getInputStream();
		InputStreamReader irdr = new InputStreamReader(is);
		BufferedReader rsv = new BufferedReader(irdr);
		
		Pattern crumbPattern = Pattern.compile(".*\"CrumbStore\":\\{\"crumb\":\"([^\"]+)\"\\}.*");
		String line = null;
		while (crumb == null && (line = rsv.readLine()) != null) {
			Matcher matcher = crumbPattern.matcher(line);
			if (matcher.matches()) {
				crumb = matcher.group(1);
			}
		}
		rsv.close();
		
		String qu = "https://query1.finance.yahoo.com/v7/finance/download";
		qu = qu + "/IBM";
		qu = qu + "?period1=1493425217&period2=1496017217&interval=1d&events=history";
		qu = qu + "&crumb=" + crumb;
		URL ask = new URL(qu);
		URLConnection aCon = ask.openConnection();
		aCon.setRequestProperty("Cookie", cookieJar.get(0));
			
		StringBuffer quote = new StringBuffer();
		BufferedReader qReader = new BufferedReader(
				new InputStreamReader(
						aCon.getInputStream()));
		
		line = null;
		while ((line = qReader.readLine()) != null) {
			quote.append(line);
		}
		
//		System.out.println("cookieJar size " + cookieJar.size());
//		System.out.println("cooke 1 " + cookieJar.get(0));
//		System.out.println("crumb " + crumb);
//		System.out.println("q " + quote.toString());

		return quote.toString();
	}

}
