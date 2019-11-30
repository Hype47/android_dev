package com.example.eventby3;

/*
 * Data model for the News Data
 */

public class NewsDataClass {

    private String title, url, snips;

    public void NewsData (String title, String url, String snips) {

        this.title = title;
        this.url = url;
        this.snips = snips;

    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSnips() {
        return snips;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setSnips(String snips) {
        this.snips = snips;
    }

}
