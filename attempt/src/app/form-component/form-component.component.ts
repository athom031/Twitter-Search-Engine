import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import axios from 'axios';

@Component({
  selector: 'app-form-component',
  templateUrl: './form-component.component.html',
  styleUrls: ['./form-component.component.css']
})
export class FormComponentComponent {
  formData: any = {}; // Object to hold form data
  
  constructor(private http: HttpClient) {}

  submitForm() {
    const url = 'http://localhost:8080/search?query=';
    const query = this.formData.query.split(' ').join('+');

    axios.get(url + query)
      .then((response) => {
        if(response.status == 200) {
          console.log(response.data.result);
        }
      })
    
    this.resetForm();
  }

  resetForm() {
    this.formData = {};
  }
}
