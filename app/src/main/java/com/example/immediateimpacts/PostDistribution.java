package com.example.immediateimpacts;

public class PostDistribution {

    private String distId; //auto-input

    private String distDate; //default to today

    private String distTime; //if no date: default to now --- if date default to "no time given"

    private String distDonee; //can be blank

    private String distStreet; //can be blank

    private String distCity; //can be blank

    private String distState; //can be blank

    private String distItemName; //must be entered

    private String distQuantity; //can be blank

    private String distDescription; //can be blank


    public PostDistribution(String distId, String distDate, String distTime, String distDonee, String distStreet, String distCity, String distState, String distItemName, String distQuantity, String distDescription) {
        this.distId = distId;
        this.distDate = distDate;
        this.distTime = distTime;
        this.distDonee = distDonee;
        this.distStreet = distStreet;
        this.distCity = distCity;
        this.distState = distState;
        this.distItemName = distItemName;
        this.distQuantity = distQuantity;
        this.distDescription = distDescription;
    }


    public String getDistId() {
        return distId;
    }

    public String getDistDate() {
        return distDate;
    }

    public String getDistTime() {
        return distTime;
    }

    public String getDistDonee() {
        return distDonee;
    }

    public String getDistStreet() {
        return distStreet;
    }

    public String getDistCity() {
        return distCity;
    }

    public String getDistState() {
        return distState;
    }

    public String getDistItemName() {
        return distItemName;
    }

    public String getDistQuantity() {
        return distQuantity;
    }

    public String getDistDescription() {
        return distDescription;
    }
}
