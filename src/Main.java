import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
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
//                    patent.setAbstractText(domText.toString());
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

//            searchTerm = "Other Classes:";
//            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
//            if(items.isEmpty()){
//                System.out.println("No Other Classes found!");
//            } else {
//                for (DomText domText : items) {
//                    System.out.println(domText);
//                    patent.setOtherClasses(domText.toString());
//                }
//            }


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
