import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent {

  searchQuery: string = '';


  @Output() searchSubmit: EventEmitter<string> = new EventEmitter<string>();

  onSearchSubmit() {
    // After submitting form, emit search query to App Component
    console.log(`Unformatted Query submitted: '${this.searchQuery}'`);
    this.searchSubmit.emit(this.searchQuery);
  }

}