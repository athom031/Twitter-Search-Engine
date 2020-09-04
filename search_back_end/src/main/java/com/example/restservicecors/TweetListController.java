package com.example.restservicecors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
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
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/search")
	public TweetList search(@RequestParam(value = "query", defaultValue = "") String query) {
		TweetList resultTweets = new TweetList();
		LuceneBuilder luceneIndex = new LuceneBuilder();
		try {
			luceneIndex.buildSearcher();
			TopDocs hits = luceneIndex.search(query,50);
			if (hits.scoreDocs.length == 0) { return resultTweets; }

			Set<String> id_hash = new HashSet<String>();
			//make sure we don't get the same id everytime

			int count_bef = id_hash.size();

			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				if(id_hash.size() >= 10) { break; }
				//if we have collected 10 unique relevant tweets exit loop

				Document doc = luceneIndex.getSearcher().doc(scoreDoc.doc);

				count_bef = id_hash.size(); // count before attempt add
				id_hash.add(doc.get("id"));
				if(id_hash.size() > count_bef) { //(sz has ++) -> set has another unique element
					resultTweets.addTweet(doc.get("text"), doc.get("id"));
				}
			}
			//if(luceneIndex.getParser() == null) { throw new org.apache.lucene.queryparser.classic.ParseException("error"); }
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			e.printStackTrace();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		return resultTweets;
	}
}


	/*@GetMapping("/greeting-javaconfig")
	public Greeting greetingWithJavaconfig(@RequestParam(required=false, defaultValue="World") String name) {
		System.out.println("==== in greeting ====");
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

}
*/