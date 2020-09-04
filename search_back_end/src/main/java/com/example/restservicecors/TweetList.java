package com.example.restservicecors;

import java.util.ArrayList;
import org.json.simple.JSONObject;

public class TweetList {
	private final ArrayList<JSONObject> result;

	public TweetList() {
		result = new ArrayList<JSONObject>();
	}

	public void addTweet(String text, String id) {
		JSONObject obj = new JSONObject();
		obj.put("text", text);
		obj.put("id", id);

		this.result.add(obj);
	}

	public ArrayList<JSONObject> getResult() { return result; }
}

/*
public class Greeting {

	private final long id;
	private final String content;

	public Greeting() {
		this.id = -1;
		this.content = "";
	}

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