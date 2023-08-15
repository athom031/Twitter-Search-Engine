import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmbeddedTweetComponent } from './embedded-tweet.component';

describe('EmbeddedTweetComponent', () => {
  let component: EmbeddedTweetComponent;
  let fixture: ComponentFixture<EmbeddedTweetComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmbeddedTweetComponent]
    });
    fixture = TestBed.createComponent(EmbeddedTweetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
