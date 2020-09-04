# Twitter Search Engine

This project a search engine for crawled tweet data which includes:
1) Twitter crawler to save tweets as JSON data.
2) Lucene Index builder that makes query searching efficient.
3) Spring back end framework to link query results to front end. 
4) React front end to display relevant tweets with twitter embed component.

## Demo Images
1. Search Home Page
![Search](https://github.com/athom031/TwitterCrawlAndSearch/blob/master/demo_img/front_end_search.png)
2. Search Results
![Results](https://github.com/athom031/TwitterCrawlAndSearch/blob/master/demo_img/front_end_result.png)

## Getting Started

### Prerequisites
1) Create an Oracle account and download [JDK 1.8](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
2) Download [Maven binaries](http://maven.apache.org/download.cgi)
3) Download [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/index.html)

* Helpful Resource: https://www.journaldev.com/2348/install-maven-mac-os
 
### Installing
Open java files with maven (importing the pom.xml files).<br/>
For the react front end download saved dependencies
```
npm init
```

#### Crawler
1) Update [TCrawler.java](https://github.com/athom031/TwitterCrawlAndSearch/blob/master/crawl_index/Crawler/src/main/java/Tcrawler.java) with [Twitter Dev](https://developer.twitter.com/en/apply-for-access).
2) Run the crawler to create JSON tweet data.
* optional @param: numJSON - number of desired json files
* optional @param: numKB - size of each json file
* optional @param: jtype - "MB" or "KB"

#### Lucene Builder
Run the luceneBuilder to create Lucene Index. (should clear index directory first)
* optional @param: indexDir - where the index should be created
* optional @param: jsonDir  - where lies the json tweet data

#### Spring Back End
[Spring cors framework](https://spring.io/guides/gs/rest-service-cors/) used to allow cross origin requests. Import with pom.xml like the other java classes. But run with:
```
./mvnw spring-boot:run
```
- PORT 8080 => example get request => http://localhost:8080/search?query=happiness

#### React Front End
After downloading the saved dependencies:
```
npm start
```
- PORT 3000 

## Authors

* **Alex Thomas** - [Github](https://github.com/athom031)

## Resources
* [Spring Back End](https://spring.io/guides/gs/rest-service/)
* [React Front End](https://reactjs.org/docs/create-a-new-react-app.html)
* [Lucene Index](https://lucene.apache.org/core/)
* [Twitter 4j](http://twitter4j.org/en/)
* [Twitter Dev Account](https://developer.twitter.com/en/apply-for-access)
* [Installing Maven and JDK](https://www.journaldev.com/2348/install-maven-mac-os)
* [Bootstrap Nav Bar](https://getbootstrap.com/docs/4.5/components/navbar/)
* [React Embed Tweet](https://www.npmjs.com/package/react-twitter-embed)

## Inspiration: 
CS 172: Information Retrieval Course Projec