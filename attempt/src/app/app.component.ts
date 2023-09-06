import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  searchQuery: string = '';
  formattedSearchQuery: string = '';
  title = 'Twitter Search Engine';
  
  getSearchQuery(query: string) {
    this.searchQuery = query;
    // Format string and assign it to class variable
    this.formattedSearchQuery = query.trim().toLowerCase().replace(/\s+/g, '+');
    console.log(`Formatted Query String: '${this.formattedSearchQuery}'`);
  }

  refreshPage() {
    // Use JavaScript's window.location to reload the page
    window.location.reload();
  }
}

