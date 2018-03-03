const puppeteer = require('puppeteer');
var cheerio = require('cheerio');

(async () => {
//  const browser = await puppeteer.launch({headless: false}); // default is true
  const browser = await puppeteer.launch();
  const page = await browser.newPage();

  const title_selector = '#title';
  const abstract_selector = '#text > abstract > div';;
  const pub_num_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > header > h2';
  const inventor_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > dl.important-people.style-scope.patent-result > dd:nth-child(3) > state-modifier';
  const inventors_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > dl.important-people.style-scope.patent-result';
  const important_people_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > dl.important-people.style-scope.patent-result';
  const description_selector = '#text > div';
  const claims_selector = '#claims > patent-text';
  const classifications_selector = '#classifications > classification-viewer > div';

  await page.goto('https://patents.google.com/patent/US7302680');
  
  let title = await page.evaluate((sel) => {
    return document.querySelector(sel).innerHTML.replace('\n', '');
  }, title_selector);

  let abstract = await page.evaluate((sel) => {
    return document.querySelector(sel).innerHTML;
  }, abstract_selector);

  let pub_num = await page.evaluate((sel) => {
    return document.querySelector(sel).firstChild.textContent;
  }, pub_num_selector);

  let inventors = await page.evaluate((sel) => {

    let inventor1 = document.querySelector(sel).firstChild.textContent;
    let inventor2 = inventor1.nextSibling;
    console.log("inventor2 = ", inventor2)
    return {
      inventor1,
      inventor2,
    }
  }, inventor_selector);

  let important_people = await page.evaluate((sel) => {
    let list = [];
    let dict = {
      "Inventor": [],
      "Current Assignee": "",
      "Original Assignee": "",
      "Priority date": "",
    };
    let people = document.querySelector(sel).childNodes;

    for(var i = 4; i < people.length; i++) {
      var person = people[i].textContent;
      person = person.trim();
      
      if(person !== '' || person.length > 0) {
        if(person.startsWith("Priority date") ) {
        } else if(person.startsWith("Current Assignee")) {
        } else if(person.startsWith("Original Assignee")) {
        } else if (person === "Inventor") {
        } else {
          list.push(person);
        }
      }
    }

    let pdateIndex = list.length - 1;
    let origAssigneeIndex = pdateIndex - 1;
    let currAssigneeIndex = origAssigneeIndex - 1;
    let lastInventorIndex = currAssigneeIndex - 1;

    var length = list.length;
    dict["Priority date"] = list[pdateIndex];
    dict["Current Assignee"] = list[currAssigneeIndex];
    dict["Original Assignee"] = list[origAssigneeIndex];
    var inventorList = [];
    for(var i = 0; i < currAssigneeIndex; ++i) {
      inventorList.push(list[i]);
    }
    dict["Inventor"] = inventorList;

    return {
      list,
      dict,
    }
  }, important_people_selector);

  let description = await page.evaluate((sel) => {

    let desc = document.querySelector(sel).textContent;
    return desc;
  }, description_selector);  

  let claims = await page.evaluate((sel) => {

    let claims = document.querySelector(sel).textContent;
    return claims;
  }, claims_selector);  
  
  const citations_table = '#wrapper > div.footer.style-scope.patent-result > div:nth-child(2) > div > div.tbody.style-scope.patent-result';
  const cited_by_table = '#wrapper > div.footer.style-scope.patent-result > div:nth-child(2)';

  let citationsTable = await page.evaluate((citations_sel, cited_by_sel) => {
      var colNum = 5;

      let citationsList = document.querySelector(citations_sel);
      let citedByList = document.querySelector(cited_by_sel);

      var citations = [];
      var citedby = [];

      var item = {
        "Publication number":"",
        "Priority date": "",
        "Publication date": "",
        "Assignee": "",
        "Title": ""
      };

      // ignore last three rows
      var numChildren = citationsList.children.length - 3;
      for(var i = 0; i < numChildren; i++) {
          
          var row = citationsList.children[i];
          // item["patent_id"] = pub_num;

          // set each key to the corresponding value
          for(var j = 0; j < colNum; j++) {
            if(j == 0) {
              // publication number has different structure than the rest
              item[Object.keys(item)[0]] = row.children[0].children[1].textContent;
            } else {
              // remove newlines and trailing whitespace
              item[Object.keys(item)[j]] = row.children[j].textContent.replace('\n', '').trim();
            }
          }
          citations.push(item);
      }

      // ignore last three rows
      // var numChildren = citationsList.children.length - 3;
      // for(var i = 0; i < numChildren; i++) {
      //     var row = citationsList.children[i];
      //     // item["patent_id"] = pub_num;
      //     item["Publication number"] = row.children[0].children[1].textContent;
      //     item["Priority date"] = row.children[1].textContent;
      //     item["Publication date"] = row.children[2].textContent;
      //     item["Assignee"] = row.children[3].textContent;
      //     item["Title"] = row.children[4].textContent.trim;
          
      //     citedby.push(item)
      // }

      return {
        citations,
        citedby,
      }

  }, citations_table, cited_by_table);

  console.log(citationsTable)



  await browser.close();
})();

