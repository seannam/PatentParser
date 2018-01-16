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
        ],
        claims: [
            claim: {
                claim_number: 123,
                claim: "",
                referenced_claim_number: 123
            }
        ],
        description: [
            background: [
                technical field: xyz,
                background art: art,
            ]
            brief: brief summary,
            details: dis
        ]
 */

import java.util.ArrayList;

public class Patent {
    private String id;
    private String title;
    private ArrayList<Author> authors;
    private String abstractText;
    private String applicationNumber;
    private String pubDate;
    private String fileDate;
    private String primaryClass;
    private ArrayList<String> otherClasses;
    private ArrayList<String> internationalClasses;
    private ArrayList<String> fieldOfSearch;
    private ArrayList<Claim> claims;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getPrimaryClass() {
        return primaryClass;
    }

    public void setPrimaryClass(String primaryClass) {
        this.primaryClass = primaryClass;
    }

    public ArrayList<String> getOtherClasses() {
        return otherClasses;
    }

    public void setOtherClasses(ArrayList<String> otherClasses) {
        this.otherClasses = otherClasses;
    }

    public ArrayList<String> getInternationalClasses() {
        return internationalClasses;
    }

    public void setInternationalClasses(ArrayList<String> internationalClasses) {
        this.internationalClasses = internationalClasses;
    }

    public ArrayList<String> getFieldOfSearch() {
        return fieldOfSearch;
    }

    public void setFieldOfSearch(ArrayList<String> fieldOfSearch) {
        this.fieldOfSearch = fieldOfSearch;
    }

    public ArrayList<Claim> getClaims() {
        return claims;
    }

    public void setClaims(ArrayList<Claim> claims) {
        this.claims = claims;
    }
}
