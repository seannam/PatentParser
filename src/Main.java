import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;


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

    private static final String FILE_EXTENSION_JSON = ".json";
    private static boolean isRunning = true;

    public static void main(String[] args) {

        System.out.println("Staring up...");

        while(isRunning) {
            System.out.print("Enter URL");

            Scanner scanner = new Scanner(System.in);
            System.out.println();
            String searchUrl = scanner.nextLine();

            String[] nameArr = searchUrl.split("/");

            String fileName = nameArr[3].split("\\.", 0)[0];

            System.out.println("Processing...");

            Patent patent = new Patent();


            final WebClient client = new WebClient();
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);

            try {
                //String searchUrl = "http://www.freepatentsonline.com/7302680.html";
                HtmlPage page = client.getPage(searchUrl);

                String searchTerm = "Application Number:";

                List<DomText> items;
                List<DomText> items2;

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

                searchTerm = "Application Number:";
                items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()[1]");
                if(items.isEmpty()){
                    System.out.println("No Application Number found!");
                } else {
                    for (DomText domText : items) {
                        patent.setApplicationNumber(domText.toString());
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

                        if(domText.toString().equals("What is claimed is") || domText.toString().equals("We claim:")) {
                            continue;
                        }

                        String temp = domText.toString();
                        String[] arr = temp.split("^\\d*\\.*$", 0);
                        for(String s: arr) {
                            String[] claimArr = s.split("\\.", 2);


                            if(claimArr.length == 0 || claimArr[0].isEmpty()) {
                                continue;
                            }

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
            //List<HtmlBreak> HtmlBreak =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]");
            items =  page.getByXPath("//*[text()='" + searchTerm + "']/../*[2]/text()");
            //items = page.getByXPath("//*[text()='" + searchTerm + "']/../");
            if(items.isEmpty()){
                System.out.println("No Description found!");
            } else {
                ArrayList<Description> desArr = new ArrayList<>();
                System.out.println("items length = " + items.size());
                Description description = new Description();
                for (DomText domText : items) {
                }
                description.setDescription(String.join(" ", items.toString()));
                desArr.add(description);
                patent.setDescriptions(desArr);
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

            prompt();
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
}
