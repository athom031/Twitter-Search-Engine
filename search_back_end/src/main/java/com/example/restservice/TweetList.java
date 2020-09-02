package com.example.restservice;
import java.util.ArrayList;
import org.json.simple.JSONObject;

//import buildlucene.LuceneBuilder;

public class TweetList {
	private final ArrayList<JSONObject> result;

	public TweetList() {
		result = new ArrayList<JSONObject>();
	}

	public void addTweet(String text, String user, String date, String lat, String lng, String url) {
		JSONObject obj = new JSONObject();
		obj.put("text", text);
		obj.put("user", user);
		obj.put("date", date);
		obj.put("lat",  lat);
		obj.put("lng",  lng);
		obj.put("url",  url);

		this.result.add(obj);
	}

	public ArrayList<JSONObject> getResult() { return result; }
}
/*
public class Greeting {

	private final long id;
	private final String content;

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
*/