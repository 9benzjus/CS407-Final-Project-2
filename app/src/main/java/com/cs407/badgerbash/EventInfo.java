package com.cs407.badgerbash;

import android.net.Uri;

public class EventInfo {
    private String name;

    private String lat;
    private String lon;
    private String createdBy;
    private String briefDescription;
    private String fullDescription;
    private String selectedTime;
//    private Uri imageUri;

    // Constructors
    public EventInfo() {

    }

    public EventInfo(String name, String lat, String lon, String createdBy, String briefDescription,
                     String fullDescription, String selectedTime) {
        this.name = name;
        this.lat = lat;
        this.lon= lon;
        this.createdBy = createdBy;
        this.briefDescription=briefDescription;
        this.fullDescription=fullDescription;
        this.selectedTime=selectedTime;
//        this.imageUri=imageUri;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }
    public String getSelectedTime(){
        return selectedTime;
    }
}
