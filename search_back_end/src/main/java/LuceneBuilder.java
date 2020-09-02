package build_lucene;

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


public class LuceneBuilder {
    //Destination dir for Lucene index files
    static final String INDEX_DIR = "../complete/index";
    //Location dir of json object files
    static final String JSON_DIR = "../complete/Crawler";

    //Variables accessed through class instance
    String indexDir;
    String JSONdir;
    IndexWriter indexWriter = null;
    IndexSearcher searcher = null;
    QueryParser parser = null;

    /**
     * Default constructor
     */
    public LuceneBuilder() {
        this.indexDir = INDEX_DIR;
        this.JSONdir = JSON_DIR;
    }  //LuceneBuilder()

    /**
     * Parameterized constructor
     * @param indexDir => dir to store Lucene index
     * @param JSONdir => dir to get JSON files (tweets)
     */
    @SuppressWarnings("unused") //causes no error if function is not used
    public LuceneBuilder(String indexDir, String JSONdir) {
        this.indexDir = indexDir;
        this.JSONdir = JSONdir;
    }  //LuceneBuilder(String indexDir, String JSONdir)


    /**
     * getIndexWriter() - creates/opens the index for writing
     * @param indexDir => dir where index is created/opened
     * @return the IndexWriter for Lucene index
     * @throws IOException when directory open is bad
     */
    public IndexWriter getIndexWriter(String indexDir) throws IOException {
        //Directory indexDir = new RAMDirectory(); //use to put directory in RAM
        Directory dir = FSDirectory.open(new File(indexDir).toPath());
        IndexWriterConfig luceneConfig = new IndexWriterConfig(new StandardAnalyzer());

        return(new IndexWriter(dir, luceneConfig));
    }  //getIndexWriter()

    /**
     * parseJSONFiles() - open JSON file in given dir and add them to JSON Array List
     * @param JSONdir => dir where JSON files are located
     * @return Array List of JSON Objects of Tweets
     * @throws IOException when opening the file directory goes bad
     */
    public ArrayList <JSONObject> parseJSONFiles(String JSONdir) throws IOException {
        BufferedReader br;
        String jsonString;
        JSONParser parser = new JSONParser();
        ArrayList <JSONObject> jsonArrayList = new ArrayList<>();

        //visit dir that holds JSON files
        File dir = new File(JSONdir);

        //process every json object in given dir
        File[] jsonFiles = dir.listFiles((dir1, filename) -> filename.endsWith(".json"));

        //each newline in json object is a tweet
        for (File jsonFile : requireNonNull(jsonFiles)) {

            //create br to read tweets
            br = new BufferedReader(new FileReader(jsonFile.getCanonicalPath()));

            //while buffer has tweets to read
            while ((jsonString = br.readLine()) != null) {
                //parse the tweet
                try {
                    jsonArrayList.add((JSONObject) parser.parse(jsonString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            //close br and open next text file
            br.close();
        }
        return jsonArrayList;
    }  //parseJSONFiles()

    /**
     * buildDocument() - build doc for index from JSON object of Tweet
     * @param jsonObject => farmed and parsed Tweet
     * @return document version of Tweet
     */
    public Document buildDocument(JSONObject jsonObject) {
        String url;
        Document doc = new Document();

        // Add text field so that the text is what is searchable
        doc.add(new TextField("text",(String) jsonObject.get("Text"), Field.Store.YES));
        doc.add(new StoredField("user",(String) jsonObject.get("User")));
        doc.add(new StoredField("datetime",(String) jsonObject.get("Datetime")));
        doc.add(new StoredField("latitude", (double) jsonObject.get("Latitude")));
        doc.add(new StoredField("longitude", (double) jsonObject.get("Longitude")));

        if ((url = (String) jsonObject.get("URL")) != null) {
            doc.add(new StoredField("url", url));
        }
        // Give timestamp field to boost on most recent tweets
        doc.add(new NumericDocValuesField("timestamp", (long) jsonObject.get("Timestamp")));

        // Add as numeric field
        doc.add(new LongPoint("time", (long) jsonObject.get("Timestamp")));
        doc.add(new DoublePoint("lat", (double) jsonObject.get("Latitude")));
        doc.add(new DoublePoint("long", (double) jsonObject.get("Longitude")));

        return doc;
    }  //buildDocument()

    /**
     * indexTweets() - add json Tweets to Lucene index
     * @param jsonArrayList - array list of JSON objects (each being a Tweet)
     * @param indexWriter - index writer for Lucene index
     */
    public void indexTweets(ArrayList <JSONObject> jsonArrayList, IndexWriter indexWriter) {
        Document doc;
        for (JSONObject jsonObject : jsonArrayList) {
            doc = buildDocument(jsonObject);
            try {
                indexWriter.addDocument(doc);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }  //indexTweets()

    /**
     * search() - given lucene index, use query to find most relevant tweet (adding on timestamp for boosting)
     * @param q => string query for tweet, user is looking for
     * @param numResults => number of max relevant results user wants
     * @return TopDocs object that has the search results in order.
     * @throws org.apache.lucene.queryparser.classic.ParseException
     *      a ParseException thrown when the Lucene parser goes wrong
     *      different parse exception from JSON
     *          added to library directly
     * @throws IOException is thrown when the index cannot be located
     */
    public TopDocs search(String q, int numResults) throws org.apache.lucene.queryparser.classic.ParseException,
            IOException {
        Query termQuery = parser.parse(q);
        // Build time boosting based on timestamp to give higher relevance to more recent tweets
        FunctionQuery dateBoost = new FunctionQuery (new LongFieldSource("timestamp"));
        // Other than that use default boost values
        CustomScoreQuery query = new CustomScoreQuery(termQuery, dateBoost);
        return searcher.search(query, numResults);
    }  //search()

    /**
     * buildSearcher() - create IndexSearcher and Parser out of Index
     *      and text as the default field to search
     * @throws IOException when opening the index goes wrong
     */
    public void buildSearcher() throws IOException {
        IndexReader rdr = DirectoryReader.open(FSDirectory.open(new File(indexDir).toPath()));
        searcher = new IndexSearcher(rdr);
        parser = new QueryParser("text", new StandardAnalyzer());
    }  //buildSearcher()

    /**
     * buildIndex() - uses the dir information to build or append a Lucene index
     * @throws IOException for any number of issues related to opening files that cannot be found
     */
    @SuppressWarnings("unused")
    public void buildIndex() throws IOException {
        indexWriter = getIndexWriter(indexDir);
        ArrayList <JSONObject> jsonArrayList = parseJSONFiles(JSONdir);
        indexTweets(jsonArrayList, indexWriter);
        indexWriter.close();
    } //buildIndex()

    public String getIndexDir() { return this.indexDir; }
    public String getJSONDir() { return this.JSONdir; }
    public IndexWriter getIndexWriter() { return this.indexWriter; }
    public IndexSearcher getSearcher() { return this.searcher; }
    public QueryParser getParser() { return this.parser; }

}  //public class LuceneBuilder











    /*
    public static void main(String[] args) throws IOException, org.apache.lucene.queryparser.classic.ParseException {
        LuceneBuilder luceneIndex = null;  //String indexDir, String JSONdir
        if (args.length == 2) {
            try {
                System.out.println("Generating Lucene index in directory "+ args[0] +" using .json files located in directory "+ args[1] +".");
                luceneIndex = new LuceneBuilder(args[0], args[1]);
                luceneIndex.buildIndex();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Invalid input. Check command line parameters to make sure directory locations are correct.");
                System.exit(-1);
            }
        }
        else{
            //IF ALREADY DEFINED INDEX
            System.out.println("Default run. Generating Lucene index in directory "+ "/index" +" using .json files located in directory "+ "/Crawler" +".");
            luceneIndex = new LuceneBuilder();

            */
// IF INDEX DIR IS EMPTY
            /*
            try {
                System.out.println("Default run. Generating Lucene index in directory "+ "/index" +" using .json files located in directory "+ "/Crawler" +".");
                luceneIndex = new LuceneBuilder();
                luceneIndex.buildIndex();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Invalid input. Check the file structure to make sure Lucene index can use directory "+ INDEX_DIR +" and .json files are located in directory "+ JSON_DIR +".");
                System.exit(-1);
            } */
        /*}

        luceneIndex.buildSearcher();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String query;

        System.out.println("Search the index for top 10 results by inputting something. Type 'exit' to exit.");
        System.out.print("Search for: ");
        while (!(query = reader.readLine()).equals("exit")) {

            TopDocs hits = luceneIndex.search(query, 50);

            if (hits.scoreDocs.length == 0) {
                System.out.println("No farmed Tweets match that search term.");
                System.out.println();
            }
            Set<String> user_hash = new HashSet<String>();
            //System.out.println(user_hash.size());
            int count_bef = user_hash.size();

            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                if(user_hash.size() >= 10) { break; }
                Document doc = luceneIndex.searcher.doc(scoreDoc.doc);

                count_bef = user_hash.size();
                user_hash.add(doc.get("user"));
                if(user_hash.size() > count_bef) {
                    System.out.println(doc.get("text"));
                    System.out.println("@" + doc.get("user"));
                    System.out.println(doc.get("datetime"));
                    System.out.println("Latitude: " + doc.get("latitude"));
                    System.out.println("Longitude: " + doc.get("longitude"));
                    if (doc.get("url") != null) {
                        System.out.println(doc.get("url"));
                    }
                    System.out.println();
                }
            }
            System.out.println("Search the index again for top 10 results by inputting something. Type 'exit' to exit.");
            System.out.print("Search for: ");
        }
        System.exit(0);
    }  //main() */