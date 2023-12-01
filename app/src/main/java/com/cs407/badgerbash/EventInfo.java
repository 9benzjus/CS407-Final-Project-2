package com.cs407.badgerbash;

import android.net.Uri;

public class EventInfo {
    private String name;

    private String location;
    private String createdBy;
    private String briefDescription;
    private String fullDescription;
    private String tags;

    private Uri imageUri;

    // Constructors
    public EventInfo() {

    }

    public EventInfo(String name, String location, String createdBy, String briefDescription,
                     String fullDescription, String tags, Uri imageUri) {
        this.name = name;
        this.location = location;
        this.createdBy = createdBy;
        this.briefDescription=briefDescription;
        this.fullDescription=fullDescription;
        this.tags=tags;
        this.imageUri=imageUri;
    }
}
