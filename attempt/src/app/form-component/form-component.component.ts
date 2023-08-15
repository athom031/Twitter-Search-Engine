import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Tweet {
  text: string;
  id: string;
}

@Component({
  selector: 'app-form-component',
  templateUrl: './form-component.component.html',
  styleUrls: ['./form-component.component.css'],
})
export class FormComponentComponent {
  formData: any = {}; // Object to hold form data
  tweetIDs: string[] = []; // Object to hold array of up to 10 tweet IDs (to later embed)
  
  constructor(private http: HttpClient) {}

  submitForm() {

    const url = 'http://localhost:8080/search';
    const query = this.formData.query.split(' ').join('+');

    this.http.get(url, { params: { query } })
      .subscribe((response: any) => {
        const tweets: Tweet[] = response.result;
        this.tweetIDs = tweets.map(tweet => tweet.id);
        console.log(this.tweetIDs);
      });

    
    this.resetForm();

  }

  resetForm() {
    this.formData = {};
    this.tweetIDs = [];
  }
}
