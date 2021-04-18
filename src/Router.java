import express.DynExpress;

import express.http.RequestMethod;
import express.http.request.Request;
import express.http.response.Response;

import express.utils.Status;

import java.util.HashMap;
import java.util.Map;

import java.nio.file.*;

public class Router {

	private ReplitDBClient client;
	private Map<String, String> cache;

	public Router() {
		client = new ReplitDBClient();
		try {
			cache = client.getAll();
		} catch(Exception ex) {
			System.out.println("Error initializing cache");
		}
	}

	@DynExpress()
	public void getIndex(Request req, Response res) {
		res.send(Paths.get("../src/views/index.html"));
	}

	@DynExpress(context="/create", method = RequestMethod.POST)
	public void postCreate(Request req, Response res) {

		String e = "";

		String key = null;
		String value = null;
		
		for(java.util.Map.Entry<String, String> entry: req.getFormQuerys().entrySet()) {
			e += "<br>" + entry;

			if(entry.getKey().equals("id")) {
				key = entry.getValue();
			} else if(entry.getKey().equals("url")) {
				value = entry.getValue();
			}
		}
		
		res.setContentType("text/html");

		if(key.equals("create")) {
			res.send("<!DOCTYPE html><html lang=\"en\"> <head><meta charset=\"utf-8\"> <title>Url created!</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><link rel=\"stylesheet\" href=\"/styles/style.css\"><link rel=\"icon\"  href=\"/favicon.ico\"/></head><body>Illegal id <code>create</code> has been passed as id.</body></html>");
			return;
		} else if(cache.containsKey(key)) {
			res.send("<!DOCTYPE html><html lang=\"en\"> <head><meta charset=\"utf-8\"> <title>Url created!</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><link rel=\"stylesheet\" href=\"/styles/style.css\"><link rel=\"icon\"  href=\"/favicon.ico\"/></head><body>The key <code>" + key + "</code> is taken.</body></html>");
			return;
		}

		if(key == null) {
			while(key == null || cache.containsKey(key)) {
				key = generateRandomKey();
			}
		}

		System.out.println("New entry - [" + key + " = " + value + "]");

		try {
			client.set(key, value);
		} catch(Exception ex) {
			res.setStatus(Status._500);
			res.send("Error setting value. Shortening url is unsuccesful.");
		}
		cache.put(key, value);
		
		res.send(
			"<!DOCTYPE html><html lang=\"en\"> <head><meta charset=\"utf-8\"> <title>Url created!</title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><link rel=\"stylesheet\" href=\"/styles/style.css\"><link rel=\"icon\"  href=\"/favicon.ico\"/></head><body><h1>The url was succesfully created with this being passed:<br><code>" +
			e +
			"</code><br><br>Your url is at <a href=\"/" + key +
			"\" id=\"the-url\">" + 
			"/" + key + 
			"</a></h1></body></html>"
		);

	}

	@DynExpress(context = "/favicon.ico")
	public void getFavicon(Request req, Response res) {

		res.redirect("/images/favicon.ico");

	}

	@DynExpress(context="/:id")
	public void getId(Request req, Response res) {
		if(cache.get(req.getParam("id")) != null) {
			res.redirect(cache.get(req.getParam("id")));
		} else {
			res.sendStatus(Status._404);
		}
	}

	@DynExpress(context = "*")
	public void get404(Request req, Response res) {
		res.sendStatus(Status._404);
	}

	public String generateRandomKey() {

		String result = "";
		char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
		
		for (int i = 0; i < 10; i++) {
			result += characters[
				new java.util.Random().nextInt(characters.length)
			];
		}
		return result;

	}
}