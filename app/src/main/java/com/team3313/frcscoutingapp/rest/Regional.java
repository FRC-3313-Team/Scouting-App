package com.team3313.frcscoutingapp.rest;

/**
 * Created by oa10712 on 3/7/2018.
 */

public class Regional {
    private String uid;
    private String title;

    public Regional(String uid, String title) {
        this.uid = uid;
        this.title = title;
    }

    public String getUID() {
        return uid;
    }

    public String getTitle() {
        return title;
    }
}
