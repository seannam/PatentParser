const puppeteer = require('puppeteer');

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

  let citations = await page.evaluate((classificationsSelector) => {

    var list = [];
    let cites = document.querySelectorAll(classificationsSelector)['values'];
    
    for(c in cites) {
      list.push(c);
    }

    return list;

  }, classifications_selector);

  /*
  let cited_by = await page.evaluate((sel, sel_row) => {
    let date = [];
    console.log(sel_row+"1)")
    let elms = document.querySelectorAll(sel_row+"1)");
    console.log(elms);

    for(var e in elms) {
      let text = e.innerHTML;
      console.log("text:\t", text)
    }
    return document.querySelector(sel);
    // return data;
  }, cited_by_table_selector, cited_by_table_row_selector);
  */



  await browser.close();
})();