import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
        id: 123,
        title: "",
        authors: [
            {
                name: ""
                location: ""
            }
        ],
        abstract: "",
        application_number: 123,
        pub_date: "",
        file_date: "",
        primary_class: "",
        other_classes: [
            other_class: ""
        ],
        international_classes: [
            international_class: ""
        ],
        field_of_search: [
            "", "", ""
        ]
        claims: [
            claim: {
                claim_number: 123,
                claim: "",
                referenced_claim_number: 123
            }
        ],
        description: [
            Background: [
                technical field: "ab",
                Background art: "art,
            ],
            brief: "summary",
            details: "more details"
        ]
 */

public class Main {

    private static final String FILE_EXTENSION_JSON = ".json";
    private static boolean isRunning = true;

    private static final ArrayList<String> DESCR_SECTS =
            new ArrayList<>(Arrays.asList("Technical Field", "Background Art" ));


    public static void main(String[] args) {



        System.out.println("Staring up...");

        while(isRunning) {
            System.out.print("Enter URL");

//            Scanner scanner = new Scanner(System.in);
//            System.out.println();
//            String searchUrl = scanner.nextLine();

            String googleUrl = "https://patents.google.com/patent/US7302680";
            String searchUrl = "http://www.freepatentsonline.com/7302680.html";
            searchUrl = "http://www.freepatentsonline.com/5802515.html";
//            searchUrl = "http://www.freepatentsonline.com/7802515.html";

            String[] nameArr = searchUrl.split("/");

            String fileName = nameArr[3].split("\\.", 0)[0];

            System.out.println("Processing...");

            Patent patent = new Patent();


            final WebClient client = new WebClient();
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);

            try {

                URL url = new URL(googleUrl);
                Document doc = Jsoup.parse(url, 10000000);
                Elements metaTags = doc.getElementsByTag("meta");

                for (Element metaTag : metaTags) {
                    String content = metaTag.attr("content");
                    String name = metaTag.attr("name");
                    String scheme = metaTag.attr("scheme");

//                    if ("citation_patent_application_number".equals(name)) {
//                        System.out.println(content);
//                    }
//                    if ("citation_patent_number".equals(name)) {
//                        System.out.println(content);
//                    }
//                    if ("DC.title".equals(name)) {
//                        System.out.println(content);
//                    }
//                    if ("DC.contributor".equals(name) && "inventor".equals(scheme)) {
//                        System.out.println(content);
//                    }
                }

//                System.out.println(doc.body());
                Element text = doc.getElementById("descriptionText");
                System.out.println(text);
                text = doc.selectFirst("patent-text#descriptionText");
                System.out.println(text);
                text = doc.selectFirst("#text > div > heading:nth-child(1)");
                System.out.println(text);
                text = doc.selectFirst("#descriptionText");
                System.out.println(text);
                text = doc.selectFirst("#text > div > p:nth-child(2)");
                System.out.println(text);

                Elements elms = doc.select("#text");
                for(Element e : elms) {
                    System.out.println(e.text());
                }
                elms = doc.select(".patent-result");
                for(Element e : elms) {
                    System.out.println(e.text());
                }

                elms = doc.getElementsByClass("patent-result");
                for(Element e : elms) {
                    System.out.println(e.text());
                }

//                System.out.println(text.className());
//                Elements paragraphs = text.select("p");
//                for(Element p : paragraphs) {
//                    System.out.println(p.text());
//                }

                /* end of jsoup */
                List<DomText> items;
                List<DomText> items2;

//                HtmlPage gpage = client.getPage(googleUrl);
//                items =  gpage.getByXPath("/html[1]/body[1]/search-app[1]/div[1]/search-result[1]/search-ui[1]/div[1]/div[1]/div[1]/div[1]/div[1]/result-container[1]/patent-result[1]/div[1]/div[1]/div[1]/div[2]/div[1]/section[1]/patent-text[1]/div[1]/div[1]/p[2]/text()");
//                if(items.isEmpty()){
//                    System.out.println("1 No description found on google patents!");
//                } else {
//                    for (DomText domText : items) {
//                        System.out.println(domText.toString());
//                    }
//                }
//
//                items =  gpage.getByXPath("/html[1]/body[1]/search-app[1]/div[1]/search-result[1]/search-ui[1]/div[1]/div[1]/div[1]/div[1]/div[1]/result-container[1]/patent-result[1]/div[1]/div[1]/div[1]/div[2]/div[1]/section[1]/patent-text[1]/div[1]/div[1]/heading[1]");
//                if(items.isEmpty()){
//                    System.out.println("2 No description found on google patents!");
//                } else {
//                    for (DomText domText : items) {
//                        System.out.println(domText.toString());
//                    }
//                }
//
//                items =  gpage.getByXPath("(//patent-text)[2]/*[@id=\"text\"]/*[p]/*");
//                if(items.isEmpty()){
//                    System.out.println("3 No description found on google patents!");
//                } else {
//                    for (DomText domText : items) {
//                        System.out.println(domText.toString());
//                    }
//                }
                /* description */

                HtmlPage page = client.getPage(searchUrl);

                String searchTerm = "Application Number:";

                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");

                if(items.isEmpty()){
                    System.out.println("No Application Number found!");
                } else {
                    for (DomText domText : items) {
                        patent.setId(domText.toString());
                    }
                }

                searchTerm = "Title:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/font[1]/b[1]/text()[1]");
                if(items.isEmpty()){
                    System.out.println("No Title found!");
                } else {
                    for (DomText domText : items) {
                        patent.setTitle(domText.toString());
                    }
                }

                searchTerm = "Application Number:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
                if(items.isEmpty()){
                    System.out.println("No Application Number found!");
                } else {
                    for (DomText domText : items) {
                        patent.setApplicationNumber(domText.toString());
                    }
                }

                searchTerm = "Inventors:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()");
                if(items.isEmpty()){
                    System.out.println("No Inventors found!");
                } else {
                    ArrayList<Author> authors = new ArrayList<Author>();
                    for (DomText domText : items) {
                        Author auth = new Author();
                        if(domText.toString().isEmpty()) {
                            continue;
                        }

                        String temp = domText.toString();
                        String[] arr = temp.split("\\(", 0);

                        auth.setName(arr[0]);
                        arr[1] = arr[1].replace(")", "");
                        auth.setLocation(arr[1]);
                        authors.add(auth);

                    }
                    patent.setAuthors(authors);
                }

                searchTerm = "Abstract:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
                if(items.isEmpty()){
                    System.out.println("No Abstract found!");
                } else {
                    for (DomText domText : items) {
                        patent.setAbstractText(domText.toString());
                    }
                }

                searchTerm = "Publication Date:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
                if(items.isEmpty()){
                    System.out.println("No Publication Date found!");
                } else {
                    for (DomText domText : items) {
                        patent.setPubDate(domText.toString());
                    }
                }

                searchTerm = "Filing Date:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
                if(items.isEmpty()){
                    System.out.println("No File Date found!");
                } else {
                    for (DomText domText : items) {
                        patent.setFileDate(domText.toString());
                    }
                }

                searchTerm = "Primary Class:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/a[1]/text()[1]");
                if(items.isEmpty()){
                    System.out.println("No Primary Class found!");
                } else {
                    for (DomText domText : items) {
                        patent.setPrimaryClass(domText.toString());
                    }
                }

                searchTerm = "Other Classes:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()");
                if(items.isEmpty()){
                    System.out.println("No Other Classes found!");
                } else {
                    ArrayList<String> otherClasses = new ArrayList<String>();
                    for (DomText domText : items) {
                        if(domText.toString().isEmpty()) {
                            continue;
                        }

                        String temp = domText.toString();
                        String[] arr = temp.split(",", 0);

                        for(String s: arr) {
                            otherClasses.add(s.trim());
                        }
                    }
                    patent.setOtherClasses(otherClasses);
                }

                searchTerm = "International Classes:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/i[*]/*[1]/text()");
                if(items.isEmpty()){
                    System.out.println("No International Classes found!");
                } else {
                    ArrayList<String> internationalClasses = new ArrayList<>();
                    for (DomText domText : items) {

                        String temp = domText.toString();
                        String[] arr = temp.split(";", 0);

                        for(String s: arr) {
                            internationalClasses.add(s.trim());
                        }
                    }
                    patent.setInternationalClasses(internationalClasses);
                }

                searchTerm = "Field of Search:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()");
                if(items.isEmpty()){
                    System.out.println("No Field of Search found!");
                } else {
                    ArrayList<String> fieldOfSearch = new ArrayList<>();
                    for (DomText domText : items) {
                        if(domText.toString().isEmpty()) {
                            continue;
                        }

                        String temp = domText.toString();
                        String[] arr = temp.split(",", 0);

                        for(String s: arr) {
                            fieldOfSearch.add(s);
                        }
                    }
                    patent.setFieldOfSearch(fieldOfSearch);
                }

                searchTerm = "Claims:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()");
                if(items.isEmpty()){
                    System.out.println("No Claims found!");
                } else {
                    ArrayList<Claim> claims = new ArrayList<>();
                    for (DomText domText : items) {
                        Claim claim = new Claim();

                        if(domText.toString().equals("What is claimed is:") || domText.toString().equals("We claim:")
                                || domText.toString().equals("What is claimed is")) {
                            continue;
                        }

                        String temp = domText.toString();
                        String[] arr = temp.split("^\\d*\\.*$", 0);
                        for(String s: arr) {
                            String[] claimArr = s.split("\\.", 2);


                            if(claimArr.length == 0 || claimArr[0].isEmpty())
//                                    ||
//                                    claimArr[0].equals("What is claimed is:") ||
//                                    claimArr[0].equals("We claim:"))
                                continue;


                            claim.setClaimNumber(Integer.parseInt(claimArr[0]));
                            claimArr[1] = claimArr[1].trim();
                            claim.setClaim(claimArr[1]);

                            String refClaim = "0";
                            Pattern p = Pattern.compile("(claim)\\s\\d+");
                            Matcher m = p.matcher(claimArr[1]);
                            if (m.find()) {
                                refClaim = m.group(0);
                                String[] claimNumArr = refClaim.split("\\s");

                                if(claimNumArr.length >= 2)
                                    refClaim = claimNumArr[1];
                            }

                            claim.setReferencedClaimNumber(Integer.parseInt(refClaim));
                            claims.add(claim);
                        }
                    }
                    patent.setClaims(claims);
                }



            searchTerm = "Description:";
//            List<HtmlBreak> htmlBreak =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]");
            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()");
//            items2 = page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/*");

            List<HtmlParagraph> paragraphList = page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/*");

            /*
            for(HtmlParagraph p: paragraphList) {
                String temp = p.asText().toString();
                System.out.println(temp);
            }
            */

            ArrayList<String> arrlst = new ArrayList<>();

            if(items.isEmpty()){
                System.out.println("No Description found!");
            } else {
//                System.out.println("items length = " + items.size());
                Description description = new Description();
                Background bg = new Background();

                int k = 0;
                for (DomText domText : items) {

                    String s = domText.toString();
                    int offset = 1;
                    String temp = "";
                    if(isUpperCase(s)) {
                        if((k+offset) < items.size()) {

                            String itemString = items.get(k + offset).toString();
                            k += offset; // k is 1

                            if(s.equals("BACKGROUND")) {
                                if(itemString.equals("1. Technical Field")) {
                                    itemString = items.get(k + offset).toString(); // k + 2
                                    bg.setTechField(itemString);
                                }

                                if(itemString.equals("2. Background Art")) {

                                    itemString = items.get(k + 2).toString();
                                    while(!isUpperCase(itemString)) {
                                        itemString = items.get(k + offset).toString();
                                        temp = temp.concat(itemString);
                                        offset++;
                                    }
                                    bg.setBackground(temp);
                                }
                            } else if(s.equals("BRIEF DESCRIPTION OF THE DRAWINGS")) {
                                offset = 2;
                                while(!isUpperCase(itemString)) {
                                    itemString = items.get(k + offset).toString();
                                    temp = temp.concat(itemString);
                                    offset++;
                                }
                                description.setDescription(temp);
                            } else if(s.equals("DETAILED DISCUSSION")) {
                                offset = 2;
                                while(!isUpperCase(itemString)) {
                                    itemString = items.get(k + offset).toString();
                                    temp = temp.concat(itemString);
                                    offset++;
                                }
                                description.setDetails(temp);
                            }

                            arrlst.add(itemString);
//                            System.out.println(s + " | " + itemString);
                        }
                    }
                    k++;
                }

//                if(!arrlst.isEmpty()) {
//                    for(String str: arrlst) {
//                        System.out.println(str);
//                    }
//                }


//                boolean marker = false;
//                while(!marker) {
//                    if (items.get(k).toString().equals("BACKGROUND")) {
//                        bg.add(items.get(k+1).toString());
//                        marker = true;
//                    }
//                }

                description.setBackground(bg);
                description.setDescription(String.join(" ", items.toString()));
//                patent.setDescription(description);
            }


            ObjectMapper mapper = new ObjectMapper();

            // write to file
            try {
                String pathname = fileName + FILE_EXTENSION_JSON;
                mapper.writeValue(new File(pathname), patent);
                System.out.println("Success! Result is in file " + pathname);
            } catch (IOException e) {
                e.printStackTrace();
            }

            } catch(Exception e){
                e.printStackTrace();
            }

//            prompt();
            isRunning = false;
        }

    }

    public static void prompt() {
        boolean input = false;
        System.out.println("Parse another URL? Enter 'yes' to continue or 'no' to quit");
        Scanner scanner = new Scanner(System.in);
        String next = scanner.nextLine();
        do {
            switch(next) {
                case "y":
                case "yes":
                    isRunning = true;
                    input = true;
                    break;
                case "n":
                case "no":
                    isRunning = false;
                    input = true;
                    break;
                default:
                    System.out.println("Please enter 'yes' to continue or 'no' to quit");
                    break;
            }
        } while(input == false);

    }

    public static boolean isUpperCase(String s) {
        for (int i=0; i<s.length(); i++) {
            if (!Character.isUpperCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
