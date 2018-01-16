import java.util.ArrayList;

/*
description: [
            background: [
                technical field: xyz,
                background art: art,
            ]
            brief: brief summary,
            details: dis
        ]

 */
public class Description {
    private ArrayList<String> background;
    private String brief;
    private String details;

    public void setBackground(ArrayList<String> background) {
        this.background = background;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    private String description;
    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getBackground() {
        return background;
    }

    public String getBrief() {
        return brief;
    }

    public String getDetails() {
        return details;
    }

    public String getDescription() {
        return description;
    }
}
