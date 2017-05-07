package com.joy.util.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClient {

	private static final int TIME_OUT = 3000;

	private HttpClient() {
	}

	public static String post(String url, Map<String, Object> params) throws Exception {
		URL httpUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection) httpUrl.openConnection();
		con.setRequestMethod("POST");
		con.setDoOutput(params != null);
		con.setDoInput(true);
		con.setUseCaches(false);
		con.setConnectTimeout(TIME_OUT);
		con.setReadTimeout(TIME_OUT);
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("Charsert", "UTF-8");

		try {
			if (params != null && !params.isEmpty()) {
				try (OutputStream os = con.getOutputStream()) {
					StringBuilder buf = new StringBuilder();
					for (Entry<String, Object> entry : params.entrySet()) {
						buf.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
					}
					buf.deleteCharAt(buf.length() - 1);
					os.write(buf.toString().getBytes("UTF-8"));
					os.flush();
				}
			}

			try (InputStream is = con.getInputStream()) {
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line = null;
				StringBuilder buf = new StringBuilder();
				while ((line = br.readLine()) != null) {
					buf.append(line);
				}
				return buf.toString();
			}
		} finally {
			con.disconnect();
		}
	}

	public static String get(String url, Map<String, Object> params) throws Exception {

		StringBuilder buf = new StringBuilder(url);
		if (params != null && !params.isEmpty()) {
			buf.append("?");
			for (Entry<String, Object> entry : params.entrySet()) {
				buf.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			buf.deleteCharAt(buf.length() - 1);
		}

		URL httpUrl = new URL(buf.toString());
		HttpURLConnection con = (HttpURLConnection) httpUrl.openConnection();
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setRequestProperty("Charsert", "UTF-8");
		con.setRequestMethod("GET");
		con.setDoOutput(false);
		con.setDoInput(true);
		con.setUseCaches(false);
		con.setConnectTimeout(TIME_OUT);
		con.setReadTimeout(TIME_OUT);

		try (InputStream is = con.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
			String line = null;
			StringBuilder buf1 = new StringBuilder();
			while ((line = br.readLine()) != null) {
				buf1.append(line);
			}
			return buf1.toString();
		} finally {
			con.disconnect();
		}
	}
}
