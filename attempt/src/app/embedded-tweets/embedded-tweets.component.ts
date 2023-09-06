import { Component, Input, EventEmitter, Output, AfterViewChecked, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

declare var twttr: any; // Declare the twttr object

interface Tweet {
  text: string;
  id: string;
}

@Component({
  selector: 'app-embedded-tweets',
  templateUrl: './embedded-tweets.component.html',
  styleUrls: ['./embedded-tweets.component.css']
})
export class EmbeddedTweetComponent implements OnInit {
  @Input() formattedSearchQuery: string = '';

  tweetIds: string[] = []; // Object to hold array of up to 10 tweet IDs

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    if(this.formattedSearchQuery) {
      this.getTweetIds();
    }
  }

  getTweetIds() {
    const url = 'http://localhost:8080/search';
    const query = this.formattedSearchQuery;

    this.http.get(url, { params: { query } })
      .subscribe((response: any) => {
        const tweets: Tweet[] = response.result;
        this.tweetIds = tweets.map(tweet => tweet.id);
        console.log(this.tweetIds);
      });
  }

}

  // @ViewChild('tweetContainer') tweetContainer!: ElementRef;
  
  // ngAfterViewInit(): void {
  //     // Embed each tweet using tweetIDs array
  //     // this.tweetIDs.forEach((tweetId) => {
  //     //   twttr.widgets.createTweet(tweetId, this.tweetContainer.nativeElement);
  //     // });
  // }
  // @Input() tweetIDs: string[] = []; // Input property to receive Tweet IDs

  // @ViewChild('tweetContainer') tweetContainer!: ElementRef;

  // ngAfterViewInit(): void {
  //   // Embed each tweet using tweetIDs array
  //   this.tweetIDs.forEach((tweetId) => {
  //     twttr.widgets.createTweet(tweetId, this.tweetContainer.nativeElement);
  //   });
  // }

