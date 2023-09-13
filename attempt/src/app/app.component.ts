import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Tweet {
  text: string;
  id: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  searchQuery: string = '';
  formattedSearchQuery: string = '';
  tweetIds: string[] = [];
  gotResult: boolean = false;

  title = 'Twitter Search Engine';

  constructor(private http: HttpClient) {}
  
  getSearchQuery(query: string) {
    this.searchQuery = query;
    // Format string and assign it to class variable
    this.formattedSearchQuery = query.trim().toLowerCase().replace(/\s+/g, '+');
    console.log(`Formatted Query String: '${this.formattedSearchQuery}'`);
  }

  parseTweetIds(response: any) {
    const tweets: Tweet[] = response.result;
    this.tweetIds = tweets.map(tweet => tweet.id);

    console.log(
      this.tweetIds.length > 0 ? 
        `Tweet Ids: ${this.tweetIds.join('\n')}` : 
        'No Tweets Returned'
    );
    this.gotResult = true;
  }
  
  getTweetIds(unformattedQuery: string) {
    this.getSearchQuery(unformattedQuery);
    
    const url = 'http://localhost:8080/search';
    const query = this.formattedSearchQuery;
  
    this.http.get(url, { params: { query } })
      .subscribe((response: any) => {
        this.parseTweetIds(response);
      });
  }

  refreshPage() {
    // Use JavaScript's window.location to reload the page
    window.location.reload();
  }
}

