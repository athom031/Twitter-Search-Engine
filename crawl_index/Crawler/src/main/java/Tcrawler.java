import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import org.json.simple.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Tcrawler {
    //variable to track and create new files
    public static final AtomicInteger num = new AtomicInteger(-1);
    //variable to create json files
    private static final int MB_SIZE = 1000 * 1024;
    //Variable for creating json files for grading
    private static final int KB_SIZE = 1024;
    //Data structure for class implementing StatusListener streaming API
    private static LinkedBlockingQueue<Status> statuses = new LinkedBlockingQueue<Status>();
    private static int numKB = 10 * KB_SIZE; // default data that can be overwritten
    private static int numJSON = 3;    // default data that can be overwritten

    /**
     * main runs the crawler
     *  generating json files
     *      based on English language tweets
     *      and geolocation constraints of country border
     * @param args optional command line arguments
     *      determine size and number of json file
     *      if none given -> default
     * @throws IOException if errors occur
     */
    public static void main(String args[]) throws InterruptedException {
        // ---------------------- SETUP JSON FILE PARAMS ---------------------- \\
        if (args.length == 3) {
            try {
                int numJ = Integer.parseInt(args[0]);
                int sizeJ = Integer.parseInt(args[1]);
                String typeJ = args[2];

                if ((numJ > 0) && (sizeJ > 0) && (typeJ.equals("MB") || typeJ.equals("KB"))) {
                    numJSON = numJ;      //change numKB value based on user input
                    if(typeJ == "KB") {
                        numKB = numJ * KB_SIZE; //change numKB value based on user input
                    } else {
                        numKB = numJ * MB_SIZE;
                    }
                    System.out.println("Successful User Input");
                    System.out.println("Generating json files - "+ numJSON +" files at "+ sizeJ + " " + typeJ + " each.");
                }
                else {
                    //System.out.println("Hello");
                    System.out.println("User Input Error: Default Input");
                    System.out.println("Generating json files - "+ numJSON +" files at "+ numKB/KB_SIZE + " KB each.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("User Input Error: Default Input");
                System.out.println("Generating json files - "+ numJSON +" files at "+ numKB/KB_SIZE + " KB each.");
            }
        }
        else {
            System.out.println("Default Input");
            System.out.println("Generating json files - "+ numJSON +" files at "+ numKB/KB_SIZE + " KB each.");
        }

        //Generate the ConfigurationBuilder object for connection to Twitter API
        //Alex Thomas Twitter dev keys, do not share
        ConfigurationBuilder cb = new ConfigurationBuilder();
        /*cb.setDebugEnabled(true)
                .setOAuthConsumerKey(INSERT KEY)
                .setOAuthConsumerSecret(INSERT KEY)
                .setOAuthAccessToken(INSERT KEY)
                .setOAuthAccessTokenSecret(INSERT KEY);
        */

        //Implementation of StatusListener. Twitter4J will use this to create a thread, consuming the stream.
        StatusListener listener = new StatusListener() {

            /**
             * onException method follows Twitter4j recommendation (prints the stack trace)
             * @param ex is the exception thrown
             */
            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg) {
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
            }

            @Override
            public void onStallWarning(StallWarning warning) {
            }

            /**
             * onStatus method checks if a status on the stream is geolocation enabled.
             *      If so, it adds it to the blocking queue.
             *      If not, it does nothing with it.
             * @param status is a new status received from the stream
             */
            @Override
            public void onStatus(Status status) {
                if (status.getGeoLocation() != null) {
                    statuses.add(status);
                }
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }
        }; // StatusListener()

        //Start building the filter for the Twitter stream
        //USA bounding box from http//www.naturalearthdata.com/download/110m/cultural/ne_110m_admin_0_countries.zip
        double[][] USA = { {-171.791110603, 18.91619, },
                { -66.96466, 71.3577635769,  } };

        FilterQuery fq = new FilterQuery();

        //Set filter to English and add bounding box coordinates
        fq.language("en");
        fq.locations(USA);

        //Open the Twitter API stream using the cb auth object
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        //Add the listener using implementation of StatusListener
        twitterStream.addListener(listener);

        //Filter the stream
        twitterStream.filter(fq);

        //Generate threads to read from stream and write to files.
        TcrawlerWriter t1 = new TcrawlerWriter();
        TcrawlerWriter t2 = new TcrawlerWriter();
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.exit(0);
    }  //main()

    //Static nested class TcrawlerWriter creates threads that read from the Twitter API and writes to files.
    static class TcrawlerWriter extends Thread {
        public void run() {
            //File writing thread, needs to be wrapped in try, catch
            try {
                File file;
                FileWriter fw;
                BufferedWriter bw;
                //Capture the url from Twitter4j to not bug API
                String url;
                //Array to move urls from Twitter4j to not bug API
                URLEntity[] urlEntities;
                int fileNum = num.incrementAndGet();
                //Data structure to dump API statuses into
                ArrayList<Status> tweets = new ArrayList<Status>();

                while (fileNum < numJSON) {
                    file = new File(String.format("%03d", fileNum) + ".json");
                    fw = new FileWriter(file);
                    bw = new BufferedWriter(fw);

                    while (file.length() < numKB) {
                        //Atomic operation, blocks on if being used and waits for blocking queue to be non empty.
                        statuses.drainTo(tweets);

                        //For every tweet pulled from the blocking queue
                        for (Status tweet : tweets) {
                            //Generate a JSON object and start populating it
                            JSONObject obj = new JSONObject();
                            obj.put("Text", tweet.getText());
                            obj.put("Timestamp", tweet.getCreatedAt().getTime());
                            obj.put("Datetime", tweet.getCreatedAt().toString());
                            obj.put("Latitude", tweet.getGeoLocation().getLatitude());
                            obj.put("Longitude", tweet.getGeoLocation().getLongitude());
                            obj.put("User", tweet.getUser().getScreenName());

                            //Call Twitter4j API to get the URLs (if any)
                            urlEntities = tweet.getURLEntities();
                            if (urlEntities.length > 0) {
                                //Try to see if the URL is valid
                                try {
                                    //Convert only the first URL to its expanded form (dump the Twitter URL format)
                                    url = urlEntities[0].getExpandedURL();
                                    obj.put("URL", url);
                                    obj.put("URLTitle", Jsoup.connect(url).get().title());
                                } catch (NullPointerException ex) {
                                    ex.printStackTrace();
                                } catch (HttpStatusException ex) {
                                    //If jSoup fails, give a stack trace and list URL Title as a 404 error
                                    obj.put("URLTitle", "404 Not Found"); //URL didn't connect
                                    ex.printStackTrace();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            //Write the json string to file
                            bw.write(obj.toJSONString() + "\n");
                        }
                        tweets.clear();
                    }
                    bw.close();

                    //Get the next file number and see if we need to farm more tweets.
                    fileNum = num.incrementAndGet();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }  ////Static nested class TcrawlerWriter

}  //public class Tcrawler
