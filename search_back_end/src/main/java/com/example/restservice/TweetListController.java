package com.example.restservice;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import build_lucene.LuceneBuilder;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.valuesource.LongFieldSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

@RestController
public class TweetListController {

	@GetMapping("/search")
	public TweetList search(@RequestParam(value = "query", defaultValue = "") String query) {
		TweetList test = new TweetList();
		LuceneBuilder luceneIndex = new LuceneBuilder();
		try {
			luceneIndex.buildSearcher();
			TopDocs hits = luceneIndex.search(query,50);
			if (hits.scoreDocs.length == 0) { return test; }

			Set<String> user_hash = new HashSet<String>();
			int count_bef = user_hash.size();

			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				if(user_hash.size() >= 10) { break; }

				Document doc = luceneIndex.getSearcher().doc(scoreDoc.doc);

				count_bef = user_hash.size();
				user_hash.add(doc.get("user"));
				if(user_hash.size() > count_bef) {
					String url = "unavailable";
					if(doc.get("url") != null) { url = doc.get("url"); }
					test.addTweet(doc.get("text"), "@" + doc.get("user"), doc.get("datetime"),
							"Latitude: " + doc.get("latitude"), "Longitude: " + doc.get("longitude"), url);
				}
			}
			//if(luceneIndex.getParser() == null) { throw new org.apache.lucene.queryparser.classic.ParseException("error"); }
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
				e.printStackTrace();
		} catch (java.io.IOException e) {
				e.printStackTrace();
		}

		return test;
	}
}
