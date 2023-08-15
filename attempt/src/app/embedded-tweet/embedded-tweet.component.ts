import { Component, Input, AfterViewInit, ViewChild, ElementRef } from '@angular/core';


declare var twttr: any; // Declare the twttr object

@Component({
  selector: 'app-embedded-tweet',
  templateUrl: './embedded-tweet.component.html',
  styleUrls: ['./embedded-tweet.component.css']
})
export class EmbeddedTweetComponent implements AfterViewInit {

  @Input() tweetIDs: string[] = []; // Input property to receive Tweet IDs

  @ViewChild('tweetContainer') tweetContainer!: ElementRef;

  ngAfterViewInit(): void {
    // Embed each tweet using tweetIDs array
    this.tweetIDs.forEach((tweetId) => {
      twttr.widgets.createTweet(tweetId, this.tweetContainer.nativeElement);
    });
  }

}
