import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css'
import Navbar from './components/Navbar'
import EmbedTweets from './components/EmbedTweets'
import axios from 'axios';
//Navbar from 'react-bootstrap/Navbar'
import './App.scss';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state =  {
      showResults : false,
      query: "",
      results: [],
    }
  }

  handleSubmit = event => {
    let url =  'http://localhost:8080/search?query=';
    let query = this.state.query.split(' ').join('+');
    url += query;
    
    axios.get(url)
      .then((response) => {
        if(response.status == 200) {
          this.setState({results: response.data.result})
          console.log(this.state.results);
        }
        this.setState({showResults: true})
      })
    event.preventDefault();
  }

  handleChange = event => {
    this.setState({query: event.target.value});
  }

  reset = event => {
    this.setState({
      showResults : false,
      query: "",
      results: [],
    })
  }

  render() {
    const isHome = this.state.showResults;
    return (
      <div>
        <div className="Base">
          <div className ="layer"></div>
          <div className="Nav">
            <Navbar/>
          </div>
          <div className="content">
            <div>
              {!isHome 
                ?
                  <div className="home">
                    <div className = "title">
                        Your Search Starts With Us.
                    </div>
                    <div className = "search">
                      Enter your search for any tweet below <br/><br/>
                      <form onSubmit={this.handleSubmit}>
                        <label>Query: </label>
                        <input type="text" onChange={this.handleChange} />
                      </form>
                    </div>
                  </div>
                :
                <div>
                  {this.state.results.length === 0
                    ?
                    <div>
                    <div className="norescont">
                        <div className = "title">
                            Curious...
                        </div>
                        <div className = "nores">
                          It seems out of 4.5 million tweets, your search query: <br/>
                          <div className = "query">
                            &nbsp;&nbsp;&nbsp;<i>{this.state.query}</i><br/>
                          </div>
                          does not match any in our collection. <br/>
                          Though this number may seem daunting, the Fall of 2020 was a trying time. <br/>
                          Keeping this in mind, try a more relevant search. <br/> <br/>
                        </div>
                      </div>
                      <div className="btnctr">
                        <button type="button" onClick={this.reset} className="btn">Search Again</button>
                      </div>
                    </div>
                    :
                    <div>
                      <div className="home">
                        <div className = "poem">
                          <i>Tweets from Sea to shining Sea <br/>
                          About the Fall of 2020 for you and me.</i> <br/> <br/>
                        </div>
                      </div>

                      <div className="rightscroll">
                        <div className = "poem">
                          <i>Query: {this.state.query} </i><br/> <br/>
                        </div>
                        <button type="button" onClick={this.reset} className="btn">Search Again</button> <br/>
                      </div>

                      <div className="results">
                        <EmbedTweets results = {this.state.results}/>
                      </div>


                      <button type="button" onClick={this.reset} className="btn">Search Again</button> <br/>
                      
                    </div>
                  }
                </div>
              }
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default App;
