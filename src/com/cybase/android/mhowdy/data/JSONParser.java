package com.cybase.android.mhowdy.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	public static final String RESULT = "result";
	public static final String RESULTS = "results";

	public JSONParser() {

	}

	public JSONObject getResponseObject(String URL, String target) {
		StringBuilder responseString = new StringBuilder();
		JSONObject responseObject = null;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					responseString.append(line);
				}
			} else {
				Log.e(JSONParser.class.toString(), "Status : failed");
			}

			responseObject = new JSONObject(responseString.toString());
			if ("OK".equalsIgnoreCase(responseObject.getString("status"))) {
				responseObject = responseObject.getJSONObject(target);
				Log.e(JSONParser.class.toString(), "Status : okay   Size : "
						+ responseObject.length());

			} else {
				responseObject = null;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseObject;

	}

	public JSONArray getResponseArray(String URL, String target) {
		StringBuilder responseString = new StringBuilder();
		JSONObject responseObject = null;
		JSONArray responseArray = null;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					responseString.append(line);
				}
			} else {
				Log.e(JSONParser.class.toString(), "Status : failed");
			}

			responseObject = new JSONObject(responseString.toString());
			if ("OK".equalsIgnoreCase(responseObject.getString("status"))) {
				responseArray = responseObject.getJSONArray(target);
				Log.e(JSONParser.class.toString(), "Status : okay   Size : "+ responseArray.length());

			} else {
				responseArray = null;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseArray;

	}
}
