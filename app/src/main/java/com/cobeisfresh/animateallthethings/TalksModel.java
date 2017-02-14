package com.cobeisfresh.animateallthethings;

import java.io.Serializable;

/**
 * Created by Marijan on 06/02/2017.
 */
public class TalksModel implements Serializable {

    private String image;
    private String title;
    private String description;
    private String date;

    public TalksModel(String image, String title, String description, String date) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
