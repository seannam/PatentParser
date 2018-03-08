const puppeteer = require('puppeteer');
const jsonfile = require('jsonfile');

(async () => {
  const browser = await puppeteer.launch();
  const page = await browser.newPage();
  const resultsURL = "https://patents.google.com/?inventor=amarnath+gupta&oq=amarnath+gupta";
  await page.goto(resultsURL);
  const selector = "#resultsContainer > section > search-result-item:nth-child(3) > article > div > div.flex.style-scope.search-result-item > h4.metadata.style-scope.search-result-item > a > span:nth-child(2)";

  let links = new Array()

  setTimeout(async function(){
    let link = await page.evaluate((sel, links) => {
      for (var i = 3; i < 23; i+=2) {
        var query = document.querySelector('#resultsContainer > section > search-result-item:nth-child(' 
                                       + i + ') > article > div > div.flex.style-scope.search-result-item > h4.metadata.style-scope.search-result-item > a > span:nth-child(2)')
        links.push(query.innerHTML)
      }
      return links;

      // return document.querySelector('#resultsContainer > section > search-result-item:nth-child(3) > article > div > div.flex.style-scope.search-result-item > h4.metadata.style-scope.search-result-item > a > span:nth-child(2)').innerHTML
    }, selector, links).catch(err => {
      console.error(err);
      console.error("error!!!")
    });
    console.log(link)
  },5000, links);

  setTimeout(async function(){
    await browser.close();
  }, 6000);
})();