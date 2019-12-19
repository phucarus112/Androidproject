package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailServiceResponse {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("contact")
    @Expose
    private String contact;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("long")
    @Expose
    private double longtitude;

    @SerializedName("maxCost")
    @Expose
    private int maxCost;

    @SerializedName("minCost")
    @Expose
    private int minCost;

    @SerializedName("selfStarRatings")
    @Expose
    private int selfStarRatings;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("serviceTypeId")
    @Expose
    private int serviceTypeId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(int maxCost) {
        this.maxCost = maxCost;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setMinCost(int minCost) {
        this.minCost = minCost;
    }

    public int getSelfStarRatings() {
        return selfStarRatings;
    }

    public void setSelfStarRatings(int selfStarRatings) {
        this.selfStarRatings = selfStarRatings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
