const puppeteer = require('puppeteer');
const jsonfile = require('jsonfile');

(async () => {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  // const resultsURL = "https://patents.google.com/?inventor=amarnath+gupta&oq=amarnath+gupta";
  // await page.goto(resultsURL);

  await page.goto("https://patents.google.com/")
  const elementHandle = await page.$('input');
  const name = 'amarnath gupta';
  await elementHandle.type(name);
  await elementHandle.press('Enter');
  
  let links = new Array();

  setTimeout(async function(){
    let link = await page.evaluate((links) => {

      const prefixSel = '#resultsContainer > section > search-result-item:nth-child(';
      const postfixSel = ') > article > div > div.flex.style-scope.search-result-item > h4.metadata.style-scope.search-result-item > a > span:nth-child(2)';
      const baseUrl = 'https://patents.google.com/patent/';

      for (var i = 3; i < 23; i+=2) {
        var query = document.querySelector(prefixSel + i + postfixSel);
        let fullUrl = baseUrl + query.innerHTML;
        links.push(fullUrl);
      }
      var numResults = document.querySelector('#count > div.layout.horizontal.style-scope.search-results > span.flex.style-scope.search-results > span:nth-child(3)');
      numResults = numResults.innerHTML;

      return {
        links,
      };

    }, links).catch(err => {
      console.error(err);
      console.error("error!!!")
    });
    console.log(link)
  },5000, links);

  setTimeout(async function(){
    // console.log(links)
    await browser.close();
  }, 6000);
})();