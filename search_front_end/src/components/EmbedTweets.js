import React from 'react';

import { TwitterTimelineEmbed, TwitterShareButton, TwitterFollowButton, TwitterHashtagButton, TwitterMentionButton, TwitterTweetEmbed, TwitterMomentShare, TwitterDMButton, TwitterVideoEmbed, TwitterOnAirButton } from 'react-twitter-embed';

class EmbedTweets extends React.Component {
  constructor(props) {
    super(props);
    //this.props.results
  }
  
  render() {
    var renderedOutput = this.props.results.map(item => <TwitterTweetEmbed tweetId = {item.id} />)
    return (
      <div>
        {renderedOutput}
      </div>
    );
  }
}

export default EmbedTweets;
