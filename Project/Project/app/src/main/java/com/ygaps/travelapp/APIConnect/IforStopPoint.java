package com.ygaps.travelapp.APIConnect;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IforStopPoint {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("arrivalAt")
    @Expose
    private String arrivalAt;
    @SerializedName("leaveAt")
    @Expose
    private String leaveAt;
    @SerializedName("minCost")
    @Expose
    private String minCost;
    @SerializedName("maxCost")
    @Expose
    private String maxCost;
    @SerializedName("serviceTypeId")
    @Expose
    private Integer serviceTypeId;
    @SerializedName("avatar")
    @Expose
    private Object avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getArrivalAt() {
        try{
            long t=Long.parseLong(arrivalAt);
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MM yyyy ");
            Date date = new Date(t);
            return sdf.format(date).toString();}
        catch (Exception e){}
        return "No start date";
    }

    public void setArrivalAt(String arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public String getLeaveAt() {
        try{
            long t=Long.parseLong(leaveAt);
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MM yyyy ");
            Date date = new Date(t);
            return sdf.format(date).toString();}
        catch (Exception e){}
        return "No start date";
    }

    public void setLeaveAt(String leaveAt) {
        this.leaveAt = leaveAt;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }


}