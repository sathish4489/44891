package com.cybase.android.mhowdy.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class FetchData {
	public static final String LOG_TAG = FetchData.class.getName();
	static String json = "";
	static JSONObject jObj = null;
	public JSONObject getJSONFromUrl(String strURL) throws Exception {

		BufferedReader in = null;
		
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(strURL));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			try {
				json = sb.toString();
				if (json.contains("{")
						&& (strURL.contains("Action/register") || strURL
								.contains("Action/resend_verification"))) {
					sb = new StringBuffer();
					String[] jsonEnds = json.split("\\{");
					for (int end = 1; end < jsonEnds.length; end++) {
						sb.append("{" + jsonEnds[end]);

					}
					json = sb.toString();
				}
				Log.e(LOG_TAG, "String JSON Value:" + json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jObj = new JSONObject(json);
		
		return jObj;
	}
}