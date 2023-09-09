// tweet-embed.component.ts

declare var twttr: any;

import { Component, Input, OnInit, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-tweet-embed',
  templateUrl: './tweet-embed.component.html',
  styleUrls: ['./tweet-embed.component.css']
})
export class TweetEmbedComponent implements OnInit {
  @Input() tweetId: string = '';
  @ViewChild('tweetContainer', { static: true }) tweetContainer!: ElementRef;

  constructor() { }

  ngOnInit(): void {
    // Embed the tweet using this.tweetId and this.tweetContainer.nativeElement
    twttr.widgets.createTweet(this.tweetId, this.tweetContainer.nativeElement);
  }
}
