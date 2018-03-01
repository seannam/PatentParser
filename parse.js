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
  // const cited_by_table_selector = '#wrapper > div.footer.style-scope.patent-result > div:nth-child(10) > div > div.tbody.style-scope.patent-result';
  // const cited_by_table_row_selector = '#wrapper > div.footer.style-scope.patent-result > div:nth-child(10) > div > div.tbody.style-scope.patent-result > div:nth-child(';
  // const cited_by_table_body = '#wrapper > div.footer.style-scope.patent-result > div:nth-child(10) > div > div.tbody.style-scope.patent-result';
  const important_people_selector = '#wrapper > div:nth-child(2) > div.flex-2.style-scope.patent-result > section > dl.important-people.style-scope.patent-result';
  await page.goto('https://patents.google.com/patent/US7302680');
  // await page.screenshot({path: 'US7302680.png'});

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
    let people = document.querySelector(sel).childNodes;
    let inventor1 = document.querySelector(sel).childNodes[6];
    let inventor1_sib = inventor1.nextSibling.textContent;

    inventor1 = inventor1.textContent;
    let inventor2 = document.querySelector(sel).childNodes[7].textContent;
    for(var i = 6; i < people.length; i++) {
      var person = people[i].textContent;
      person = person.trim();
      if(person !== '' || person.length > 0) {
        if(person.startsWith("Priority date")) {
          list.push("Priority date");
        } else if(person.startsWith("Current Assignee")) {
          list.push("Current Assignee");
        } else {
          list.push(person);
        }
      }
    }

    return {
      list,
    }
  }, important_people_selector);

  // console.log(inventors)
  console.log(important_people)
  // let cited_by = await page.evaluate((sel, sel_row) => {
  //   let date = [];
  //   console.log(sel_row+"1)")
  //   let elms = document.querySelectorAll(sel_row+"1)");
  //   console.log(elms);

  //   for(var e in elms) {
  //     let text = e.innerHTML;
  //     console.log("text:\t", text)
  //   }
  //   return document.querySelector(sel);
  //   // return data;
  // }, cited_by_table_selector, cited_by_table_row_selector);

  await browser.close();
})();


// #wrapper > div.footer.style-scope.patent-result > div:nth-child(10) > div > div.tbody.style-scope.patent-result > div:nth-child(1)
// #wrapper > div.footer.style-scope.patent-result > div:nth-child(10) > div > div.tbody.style-scope.patent-result > div:nth-child(2)