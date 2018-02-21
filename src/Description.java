import java.util.ArrayList;

/*
description: [
        Background: [
            technical field: xyz,
            Background art: art,
        ]
        brief: brief summary,
        details: dis
    ]
 */

public class Description {

    public Background getBackground() {
        return background;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    private Background background;

    private String brief;
    private String details;


    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public String getBrief() {
        return brief;
    }

    public String getDetails() {
        return details;
    }


    private String description;
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

}
