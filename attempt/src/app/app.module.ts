import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { SearchBarComponent } from './search-bar/search-bar.component';


import { AppRoutingModule } from './app-routing.module';

import { HttpClientModule } from '@angular/common/http';
import { TweetEmbedComponent } from './tweet-embed/tweet-embed.component';
// import { EmbeddedTweetComponent } from './embedded-tweets/embedded-tweets.component';

@NgModule({
  declarations: [
    AppComponent,
    // EmbeddedTweetComponent,
    SearchBarComponent,
    TweetEmbedComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
