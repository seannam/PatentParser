import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
        ]
 */

public class Main {

    public static void main(String[] args) {
        Patent patent = new Patent();
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = "http://www.freepatentsonline.com/7302680.html";
            HtmlPage page = client.getPage(searchUrl);

            String searchTerm = "Application Number:";

            List<DomText> items;

            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
            if(items.isEmpty()){
                System.out.println("No Application Number found!");
            } else {
                for (DomText domText : items) {
//                    System.out.println(domText);
                    patent.setId(domText.toString());
                }
            }

            searchTerm = "Title:";
            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/font[1]/b[1]/text()[1]");
            if(items.isEmpty()){
                System.out.println("No Title found!");
            } else {
                for (DomText domText : items) {
//                    System.out.println(domText);
                    patent.setTitle(domText.toString());
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
//                        System.out.println("empty");
                        continue;
                    }

//                    System.out.println(domText);

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
                    //System.out.println(domText);
                    patent.setAbstractText(domText.toString());
                }
            }

            searchTerm = "Application Number:";
            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
            if(items.isEmpty()){
                System.out.println("No Application Number found!");
            } else {
                for (DomText domText : items) {
//                    System.out.println(domText);
                    patent.setApplicationNumber(domText.toString());
                }
            }

            searchTerm = "Publication Date:";
            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
            if(items.isEmpty()){
                System.out.println("No Publication Date found!");
            } else {
                for (DomText domText : items) {
//                    System.out.println(domText);
                    patent.setPubDate(domText.toString());
                }
            }

            searchTerm = "Filing Date:";
            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
            if(items.isEmpty()){
                System.out.println("No File Date found!");
            } else {
                for (DomText domText : items) {
//                    System.out.println(domText);
                    patent.setFileDate(domText.toString());
                }
            }

            searchTerm = "Primary Class:";
            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/a[1]/text()[1]");
            if(items.isEmpty()){
                System.out.println("No Primary Class found!");
            } else {
                for (DomText domText : items) {
//                    System.out.println(domText);
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

//                    System.out.println(domText);

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
//                    System.out.println(domText.toString());

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
//                    System.out.println(domText);

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
                    if(domText.toString().equals("What is claimed is")) {
                        continue;
                    }
                    //System.out.println(domText);

                    String temp = domText.toString();
                    String[] arr = temp.split("^\\d*\\.*$", 0);
                    for(String s: arr) {
//                        System.out.println(s);
                        String[] claimArr = s.split("\\.", 2);

                        if(claimArr[0].isEmpty()|| claimArr[1].isEmpty()) {
                            continue;
                        }

//                        System.out.println(claimArr[0]);
//                        System.out.println(claimArr[1].trim());

                        claim.setClaimNumber(Integer.parseInt(claimArr[0]));
                        claimArr[1] = claimArr[1].trim();
                        claim.setClaim(claimArr[1]);

                        String[] refClaimArr = claimArr[1].split("(claim)\\s\\d+", 0);
//                        String refClaim = refClaimArr[0].replaceAll("(claim )", "");

                        String refClaim = "0";
                        Pattern p = Pattern.compile("(claim)\\s\\d+");
                        Matcher m = p.matcher(claimArr[1]);
                        if (m.find()) {
//                            System.out.println(m.group(0)); // whole matched expression
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



            ObjectMapper mapper = new ObjectMapper();
            try {
                String jsonString = mapper.writeValueAsString(patent);
                System.out.println(jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            // write to file
            try {
                mapper.writeValue(new File("7302680.json"), patent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch(Exception e){
            e.printStackTrace();
        }


        
    }
}
