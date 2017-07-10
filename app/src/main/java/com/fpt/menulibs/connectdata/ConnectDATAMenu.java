package com.fpt.menulibs.connectdata;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConnectDATAMenu {
	private static int timeout = 7000;
	@SuppressWarnings("deprecation")
	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		@SuppressWarnings("deprecation")
		HttpClient httpclient = new DefaultHttpClient();
		try {
			// create HttpClient
			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {

		}

		return result;
	}
	public static String POST(String url, String data) {

		InputStream inputStream = null;
		String result = "";
		try {
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = timeout;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = timeout;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			//@SuppressWarnings("deprecation")
			//HttpClient httpclient = new DefaultHttpClient();

			// 2. make POST request to the given URL
			@SuppressWarnings("deprecation")
			HttpPost httpPost = new HttpPost(url);

			StringEntity se = new StringEntity(data);

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			//httpPost.setHeader("Accept", "application/json");
			//httpPost.setHeader("Content-Type", "application/json");
			//httpPost.setHeader("Content-Type", "text/html; charset=UTF-8");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null)
			{
				result = convertInputStreamToString(inputStream);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;


	}
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, "UTF-8"), 8);

		String line = "";
		String result = "";
		StringBuilder sb = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}

		result = sb.toString();
		inputStream.close();
		return result;

	}
	public static String getlinkToPlay(String url){
		String link = "";
		String response = null;
		response = ConnectDATAMenu.GET(url);
		try {
			JSONObject root = new JSONObject(response);
			JSONArray array = root.getJSONObject("root").getJSONArray("item");
			JSONObject item = array.getJSONObject(0);
			link = item.getString("url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return link;
	}
	public static String GetChapterID(String movieID){
		String chapterid = "";
		String url = "http://fbox-dial.fpt.vn/MobileWS.ashx?method=PayTVMobile_GetDetailVODMobile"
				+ "&movieid=" + movieID
				+ "&customerid=245337";
		String result = ConnectDATAMenu.GET(url);
		try {
			JSONObject root = new  JSONObject(result);
			JSONArray array = root.getJSONObject("Root").getJSONArray("item");
			JSONObject item = array.getJSONObject(0);
			JSONArray listchapter = item.getJSONArray("Chapter");
			JSONObject onechapter = listchapter.getJSONObject(0);
			chapterid = onechapter.getString("ChapterID");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return chapterid;
	}
	
}
