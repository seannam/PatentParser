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

import java.util.ArrayList;

public class Patent {
    private String id;
    private String title;
    private ArrayList<Author> authors;
    private String abstractText;
    private double applicationNumber;
    private String pubDate;
    private String fileDate;
    private String primaryClass;
    private ArrayList<String> otherClasses;
    private ArrayList<String> internationalClasses;
    private ArrayList<String> fieldOfSearch;
    private ArrayList<Claim> claims;
}
