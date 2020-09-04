import React from 'react'
var FontAwesome = require('react-fontawesome');


function Navbar() {
    return (
        
        <nav className="navbar fixed-top navbar-expand-lg " id="sectionsNav">
        <div className="container">
          <div className="navbar-translate">
            <a className="navbar-brand" href="">
              Twitter Search Enginer </a>
            <button className="navbar-toggler" type="button" data-toggle="collapse" aria-expanded="false" aria-label="Toggle navigation">
              <span className="sr-only">Toggle navigation</span>
              <span className="navbar-toggler-icon"></span>
              <span className="navbar-toggler-icon"></span>
              <span className="navbar-toggler-icon"></span>
            </button>
          </div>
          <div className="collapse navbar-collapse">
            <ul className="navbar-nav ml-auto">
              <li className="nav-item">
                <a className="nav-link" rel="tooltip" title="" data-placement="bottom" href="https://github.com/athom031/TwitterCrawlAndSearch">
                    Github Repo
                    <FontAwesome
                        className='fab fa-github'
                        name='github'
                        size='2x'
                        style={{ textShadow: '0 1px 0 rgba(0, 0, 0, 0.1)' }}
                    />
                </a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    );
}

export default Navbar;