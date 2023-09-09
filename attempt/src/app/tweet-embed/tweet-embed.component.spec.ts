import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TweetEmbedComponent } from './tweet-embed.component';

describe('TweetEmbedComponent', () => {
  let component: TweetEmbedComponent;
  let fixture: ComponentFixture<TweetEmbedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TweetEmbedComponent]
    });
    fixture = TestBed.createComponent(TweetEmbedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
