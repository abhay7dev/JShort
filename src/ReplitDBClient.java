/*

ReplitDBClient (v1.3) by EpicGamer007: a Java client for ReplitDB
Copyright (C) 2021  Abhay Bhat

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ReplitDBClient{

	// Vars

	private String url;

	private boolean encoded = true;

	private final HttpClient httpClient = HttpClient.newBuilder()
		.version(HttpClient.Version.HTTP_2)
		.build();

	// End Vars

	// Constructors

	public ReplitDBClient() {
		this(System.getenv("REPLIT_DB_URL"), true);
	}

	public ReplitDBClient(String url) {
		this(url, true);
	}

	public ReplitDBClient(boolean encoded) {
		this(System.getenv("REPLIT_DB_URL"), encoded);
	}

	public ReplitDBClient(String url, boolean encoded) {
		this.url = url;
		this.encoded = encoded;
	}

	// End Constructors

	// Set Value(s)

	public void set(String key, String value) throws IOException, InterruptedException {

		Map<Object, Object> data = new HashMap<>();

		if (encoded) {
			key = encode(key);
			value = encode(value);
		}
		data.put(key, value); 

		HttpRequest request = HttpRequest.newBuilder()
			.POST(ofFormData(data))
			.uri(URI.create(this.url))
			.setHeader("Content-Type", "application/x-www-form-urlencoded")
			.build();

		httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public void set(Map<String, String> pairs) throws IOException, InterruptedException {
		for (Map.Entry<String, String> entry : pairs.entrySet()){
			set(entry.getKey(), entry.getValue());
		}
	}

	// End Set Values(s)

	// Get Value(s)

	public String get(String key) throws IOException, InterruptedException {
		if (encoded) key = encode(key);

		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(this.url + "/" + key))
			.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (encoded) {
			return decode(response.body());
		} else {
			return response.body();
		}
	}

	public String[] get(String... keys) throws IOException, InterruptedException {
		String[] toRet = new String[keys.length];

		for (int i = 0; i < toRet.length; i++) {
			toRet[i] = get(keys[i]);
		}

		return toRet;
	}

	public String[] list() throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(this.url + "?prefix"))
			.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		String[] tokens = new String[0];
		if(encoded) {
			tokens = decode(response.body()).split("\n");
		} else {
			tokens = response.body().split("\n");
		}

		return tokens;
	}

	public String[] list(String pre) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(this.url + "?prefix=" + pre))
			.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		String[] tokens = new String[0];
		if(encoded) {
			tokens = decode(response.body()).split("\n");
		} else {
			tokens = response.body().split("\n");
		}

		return tokens;

	}

	public Map<String, String> getAll() throws IOException, InterruptedException {

		Map<String, String> toRet = new HashMap<String, String>();

		for (String k: list()) {
			if (encoded) {
				toRet.put(decode(k), get(k));
			} else {
				toRet.put(k, get(k));
			}
		}

		return toRet;
	}

	// End Get Value(s)

	// Delete Value(s)

	public void delete(String key) throws IOException, InterruptedException {

		if(encoded) {
			key = encode(key);
		}

		HttpRequest request = HttpRequest.newBuilder()
			.DELETE()
			.uri(URI.create(this.url + "/" + key))
			.build();

		httpClient.send(request, HttpResponse.BodyHandlers.ofString());

	}

	public void delete(String... keys) throws IOException, InterruptedException {

		for(String key: keys) {
			delete(key);  
		}

	}

	public void empty() throws IOException, InterruptedException {

		for(String key: getAll().keySet()) {
			delete(key);
		}

	}

	// End Delete Value(s)

	// Utilities

	private static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {

		StringBuilder builder = new StringBuilder();

		for (Map.Entry<Object, Object> entry :data.entrySet()) {

			if (builder.length() > 0) {
				builder.append("&");
			}

			builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));

			builder.append("=");

			builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));

		}

		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}

	public static String encode(String encodeMe) {

		if (encodeMe == null) {
			return "";
		}

		try {

			String encoded = URLEncoder.encode(encodeMe, StandardCharsets.UTF_8.name());

			return encoded;

		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	private String decode(String s) {
		if (s == null) {
			return "";
		}

		try {
			
			String decoded = URLDecoder.decode(s, StandardCharsets.UTF_8.name());
			return decoded;

		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	// End Utilities

}