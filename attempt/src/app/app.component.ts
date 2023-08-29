import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  searchQuery: string = '';
  title = 'Twitter Search Engine';
  
  getSearchQuery(query: string) {
    this.searchQuery = query;
  }

  refreshPage() {
    // Use JavaScript's window.location to reload the page
    window.location.reload();
  }
}

