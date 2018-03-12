const puppeteer = require('puppeteer');
const jsonfile = require('jsonfile');

async function extractPatent(urladdress, page) {
  const title_selector = '#title';
  const abstract_selector = '#text > abstract > div';;
  const pub_num_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > header > h2';
  const inventor_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > dl.important-people.style-scope.patent-result > dd:nth-child(3) > state-modifier';
  const inventors_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > dl.important-people.style-scope.patent-result';
  const important_people_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > dl.important-people.style-scope.patent-result';
  const description_selector = '#text > div';
  const claims_selector = '#claims > patent-text';
  const citations_table = '#wrapper > div.footer.style-scope.patent-result > div:nth-child(2) > div > div.tbody.style-scope.patent-result';
  const cited_by_table = '#wrapper > div.footer.style-scope.patent-result > div:nth-child(10) > div > div.tbody.style-scope.patent-result';
  const classifications_selector = '#classifications > classification-viewer';
  const classifications_div_selector = '#classifications > classification-viewer > div';
 
  await page.goto(urladdress);

  let patent = {};

  let title = await page.evaluate((sel) => {
    return document.querySelector(sel).innerHTML.replace('\n', '').trim();
  }, title_selector).catch(err => {
      console.error(err);
      console.error("error title")
  });
  
  let abstract = await page.evaluate((sel) => {
    return document.querySelector(sel).innerHTML;
  }, abstract_selector).catch(err => {
      console.error(err);
      console.error("error abstract")
  });

  let pub_num = await page.evaluate((sel) => {
    return document.querySelector(sel).firstChild.textContent;
  }, pub_num_selector).catch(err => {
      console.error(err);
      console.error("error pub_num")
  });
  
  let description = await page.evaluate((sel) => {
    var desc = document.querySelector(sel).textContent;
    desc = desc.replace('\n', '').trim();
    return desc;
  }, description_selector).catch(err => {
      console.error(err);
      console.error("error description")
  });

  let claims = await page.evaluate((sel) => {
    var claim = document.querySelector(sel).textContent;
    claim = claim.replace('\n', '').trim();
    return claim;
  }, claims_selector).catch(err => {
      console.error(err);
      console.error("error claims")
  });

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

    let priority_date = dict["Priority date"];
    let currAssignee = dict["Current Assignee"];
    let origAssignee = dict["Original Assignee"];

    return {
      priority_date,
      currAssignee,
      origAssignee,
      inventorList,
    }
  }, important_people_selector).catch(err => {
      console.error(err);
      console.error("error important_people ")
  });

  let tables = await page.evaluate((citations_sel, cited_by_sel, pub_num) => {
      var colNum = 5;

      let citationsList = document.querySelector(citations_sel);
      let citedByList = document.querySelector(cited_by_sel);

      var citations = [];
      var citedby = [];

      var numChildren = 0;
      // ignore last three rows
      // if(undefined !== citationsList && citationsList.length) {
        numChildren = citationsList.children.length - 3;
      
        for(var i = 0; i < numChildren; i++) {
            var item = {
              "Patent ID":"",
              "Publication number":"",
              "Priority date": "",
              "Publication date": "",
              "Assignee": "",
              "Title": ""
            };
            var row = citationsList.children[i];
            item["Patent ID"] = pub_num;
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
      // }
      // if(undefined !== citedByList && citedByList.length) {
        numChildren = citedByList.children.length;
        for(var i = 0; i < numChildren; i++) {
            var item = {
              "Patent ID":"",
              "Publication number":"",
              "Priority date": "",
              "Publication date": "",
              "Assignee": "",
              "Title": ""
            };

            var row = citedByList.children[i];
            item["Patent ID"] = pub_num;
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
      // }

      return {
        citations,
        citedby,
      }
  }, citations_table, cited_by_table, pub_num).catch(err => {
      console.error(err);
      console.error("error tables")
  });

  let classifications = await page.evaluate((classifications_selector, classifications_div_selector, pub_num) => {
    
    var classifications_list = [];
    var query = document.querySelector(classifications_selector);
    var val = query.children[0].children[0].children[0].children[0];
    var length = val.children.length
    val = val.children[length-2]
    var id = val.children[0].textContent
    var des = val.children[1].textContent
    item = {
      "Patent ID": pub_num,
      "Classification ID": id,
      "Classlification Class": des,
    }
    classifications_list.push(item);

    query = document.querySelector(classifications_div_selector);

    var numChildren = query.children.length;
    item["Patent"] = pub_num;
    for(var i = 0; i < numChildren-3; i++) {
      var val = query.children[i].children[0].children[0].children[0];
      var length = val.children.length
      val = val.children[length-2]
      var id = val.children[0].textContent
      var des = val.children[1].textContent
      item = {
        "Patent ID": pub_num,
        "classification id": id,
        "classlification class": des,
      }
      classifications_list.push(item);

    }
    return classifications_list;
  
  }, classifications_selector, classifications_div_selector, pub_num).catch(err => {
      console.error(err);
      console.error("error classifications")
  });

  patent.title = title;
  patent.abstract = abstract;
  patent.pub_num = pub_num;
  patent.description = description;
  patent.claims = claims;

  patent["Priority date"] = important_people.priority_date;
  patent["Current Assignee"] = important_people.currAssignee;
  patent["Original Assignee"] = important_people.origAssignee;
  patent["Inventors"] = important_people.inventorList;
  patent.classifications = classifications;
  
  if(undefined !== tables.citations) {
    patent["Citations Table"] = tables.citations;
  }
  if(undefined !== tables.citedby) {
    patent["Cited By Table"] = tables.citedby;
  }

  jsonfile.writeFile('exports/' + pub_num + '.json', patent, {spaces:2}, function(err) {
    if (err !== null) {
      console.log("error", err);
    }
  })
}

/*
 * timeout function using a promise
 */
async function timeout(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function getNumLinks(page, name) {
  await page.goto("https://patents.google.com/")
  const elementHandle = await page.$('input');
  
  await elementHandle.type(name);
  await elementHandle.press('Enter');
  await timeout(7000);
  return await page.evaluate(() => {
    var pages = [];
    var numResults = document.querySelector('#count > div.layout.horizontal.style-scope.search-results > span.flex.style-scope.search-results > span:nth-child(3)');
    numResults = numResults.innerHTML;

    let pageUrl = document.URL;
    pages.push(pageUrl);
    if(numResults > 10) {
      for(var i = 1; i < numResults/10; i++) {
        pages.push(pageUrl + '&page=' + i);
      }
    }
    return pages;
  });
}

async function getPageLinks(page, urladdress) {

  await page.goto(urladdress);
  await timeout(7000)

  let links = new Array()

  return await page.evaluate((links) => {
    const prefixSel = '#resultsContainer > section > search-result-item:nth-child(';
    const postfixSel = ') > article > div > div.flex.style-scope.search-result-item > h4.metadata.style-scope.search-result-item > a > span:nth-child(2)'
    const baseUrl = 'https://patents.google.com/patent/';

    for (var i = 3; i < 23; i+=2) {
        var query = document.querySelector(prefixSel + i + postfixSel);
        // check if page has all 10 results or not
        if(query && query.innerHTML) {
          let fullUrl = baseUrl + query.innerHTML;
          links.push(fullUrl);
        }
    }
    return links;

  }, links).catch(err => {
    console.error(err);
    console.error("error in getPageLinks");
  });
}

async function run() {
  const browser = await puppeteer.launch({
    args: ['--disable-dev-shm-usage']
  });
  const name = 'amarnath gupta';
  // const name = 'steve jobs'
  const page = await browser.newPage();
  const resultPages = await getNumLinks(page, name);
 
  for(var i = 0; i < resultPages.length; i++) {
    const links = await getPageLinks(page, resultPages[i]);
    if(undefined !== links && links.length) {
      // console.log("links = ", links)
      for(var j = 0; j < links.length; j++) {
        urladdress = links[j];
        console.debug(j + ".", urladdress)
        await extractPatent(urladdress, page).catch(err => {
          console.error(err);
          console.error("error!! ", j + ".", links[j])
        });
      }
    }
  }

  browser.close();
}

run();
