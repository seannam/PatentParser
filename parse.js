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

  const url1 = 'https://patents.google.com/patent/US7302680';
  const url2 = 'https://patents.google.com/patent/US20080235657A1/en';
  await page.goto(url1);

  
  let title = await page.evaluate((sel) => {
    return document.querySelector(sel).innerHTML.replace('\n', '');
  }, title_selector);

  let abstract = await page.evaluate((sel) => {
    return document.querySelector(sel).innerHTML;
  }, abstract_selector);

  let pub_num = await page.evaluate((sel) => {
    return document.querySelector(sel).firstChild.textContent;
  }, pub_num_selector);

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
  const cited_by_table = '#wrapper > div.footer.style-scope.patent-result > div:nth-child(10) > div > div.tbody.style-scope.patent-result';

  let citationsTable = await page.evaluate((citations_sel, cited_by_sel) => {
      var colNum = 5;

      let citationsList = document.querySelector(citations_sel);
      let citedByList = document.querySelector(cited_by_sel);

      var citations = [];
      var citedby = [];

      // ignore last three rows
      var numChildren = citationsList.children.length - 3;
      for(var i = 0; i < numChildren; i++) {
          var item = {
            "Publication number":"",
            "Priority date": "",
            "Publication date": "",
            "Assignee": "",
            "Title": ""
          };
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

      numChildren = citedByList.children.length;
      for(var i = 0; i < numChildren; i++) {
          var item = {
            "Publication number":"",
            "Priority date": "",
            "Publication date": "",
            "Assignee": "",
            "Title": ""
          };

          var row = citedByList.children[i];
          // item["patent_id"] = pub_num;
          
          if(row.classList.contains("tr")) {
            // set each key to the corresponding value
            for(var j = 0; j < colNum; j++) {
              
              if(j == 0) {
                // publication number has different structure than the rest
                if(row.children[j].children[1]) {
                  item[Object.keys(item)[0]] = row.children[0].children[1].textContent;
                  item["addToList"] = 1;
                }
              } else {
                // remove newlines and trailing whitespace
                if(row.children[j].textContent) {
                  item[Object.keys(item)[j]] = row.children[j].textContent.replace('\n', '').trim();
                  item["addToList"] = 1;
                }
              }
            }
            if(item["addToList"] === 1) {
              delete item.addToList;
              citedby.push(item);  
            }
          }
         
      }

      return {
        citations,
        citedby,
      }

  }, citations_table, cited_by_table);


  const classifications_selector = '#classifications > classification-viewer';
  const classifications_div_selector = '#classifications > classification-viewer > div';
  let classifications = await page.evaluate((classifications_selector, classifications_div_selector) => {
    
    var list = [];
    var query = document.querySelector(classifications_selector);
    var val = query.children[0].children[0].children[0].children[0];
    var length = val.children.length
    val = val.children[length-2]
    var id = val.children[0].textContent
    var des = val.children[1].textContent
    item = {
      "classification_id": id,
      "classlification_class": des,
    }
    list.push(item);

    query = document.querySelector(classifications_div_selector);

    var numChildren = query.children.length;

    for(var i = 0; i < numChildren-3; i++) {
      var val = query.children[i].children[0].children[0].children[0];
      var length = val.children.length
      val = val.children[length-2]
      var id = val.children[0].textContent
      var des = val.children[1].textContent
      item = {
      "classification_id": id,
      "classlification_class": des,
      }
      list.push(item);

    }

    return {
      list,
      numChildren,
    };
  }, classifications_selector, classifications_div_selector);

  console.log(classifications);

  await browser.close();
})();

/*
var query2 = document.querySelector('#classifications > classification-viewer');

var val = query2.children[0].children[0].children[0].children[0];
var length = val.children.length
val = val.children[length-2]
var id = val.children[0].textContent
var des = val.children[1].textContent
console.log(id, des)
*/