package com.waupi.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpClientGet {

	public static String SendHttpGet(String url/*, String data*/) {
		//System.out.println("!-- link=>"+url+"?json="+data);
		String return_str = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpPostRequest = new HttpGet(url);

			//httpPostRequest.setEntity(new StringEntity(data));
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			//httpPostRequest.setHeader("Accept-Encoding", "gzip"); 

			HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}
				return_str = convertStreamToString(instream);
				instream.close();
			} 
		}
		catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("!-- response exception=>"+return_str);
		return return_str;
	}

	private static BufferedReader reader = null;
	private static StringBuilder sb = null;
	
	private static String convertStreamToString(InputStream is) {
		reader = new BufferedReader(new InputStreamReader(is));
		sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				Log.e("Line", line);
				sb.append(line + "\n");
			}
		} catch (IOException e) {}
		finally {
			try {
				is.close();
			} catch (IOException e) {}
			line = null;
			reader = null;
		}
		return sb.toString();
	}
}