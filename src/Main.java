import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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

        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        try {

            String searchUrl = "http://www.freepatentsonline.com/7302680.html";
            HtmlPage page = client.getPage(searchUrl);

            String searchTerm = "Application Number:";

            List<DomText> items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]") ;
            if(items.isEmpty()){
                System.out.println("No items found !");
            }else {
                for (DomText domText : items) {
                    System.out.println(domText);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
