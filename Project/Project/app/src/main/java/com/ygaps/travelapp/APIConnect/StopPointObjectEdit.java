package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopPointObjectEdit {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("provinceId")
    @Expose
    private int provinceId;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("long")
    @Expose
    private double longtitude;

    @SerializedName("arrivalAt")
    @Expose
    private long arrivalAt;

    @SerializedName("leaveAt")
    @Expose
    private long leaveAt;

    @SerializedName("serviceTypeId")
    @Expose
    private int serviceTypeId;

    @SerializedName("minCost")
    @Expose
    private float minCost;

    @SerializedName("maxCost")
    @Expose
    private float maxCost;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("serviceId")
    @Expose
    private int serviceId;

    @SerializedName("index")
    @Expose
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
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

    public long getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(long arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public long getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(long leaveAt) {
        this.leaveAt = leaveAt;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public float getMinCost() {
        return minCost;
    }

    public void setMinCost(float minCost) {
        this.minCost = minCost;
    }

    public float getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(float maxCost) {
        this.maxCost = maxCost;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
