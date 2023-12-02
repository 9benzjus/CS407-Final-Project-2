package com.cs407.badgerbash;

import android.net.Uri;

public class EventInfo {
    private String name;

    private double lat;
    private double lon;
    private String createdBy;
    private String briefDescription;
    private String fullDescription;
//    private Uri imageUri;

    // Constructors
    public EventInfo() {

    }

    public EventInfo(String name, double lat, double lon, String createdBy, String briefDescription,
                     String fullDescription) {
        this.name = name;
        this.lat = lat;
        this.lon= lon;
        this.createdBy = createdBy;
        this.briefDescription=briefDescription;
        this.fullDescription=fullDescription;
//        this.imageUri=imageUri;
    }
}
