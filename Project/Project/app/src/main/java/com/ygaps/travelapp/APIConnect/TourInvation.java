package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TourInvation {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("hostId")
    @Expose
    private String hostId;
    @SerializedName("hostName")
    @Expose
    private String hostName;
    @SerializedName("hostPhone")
    @Expose
    private String hostPhone;
    @SerializedName("hostEmail")
    @Expose
    private String hostEmail;
    @SerializedName("hostAvatar")
    @Expose
    private Object hostAvatar;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("minCost")
    @Expose
    private String minCost;
    @SerializedName("maxCost")
    @Expose
    private String maxCost;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("adults")
    @Expose
    private Integer adults;
    @SerializedName("childs")
    @Expose
    private Integer childs;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostPhone() {
        return hostPhone;
    }

    public void setHostPhone(String hostPhone) {
        this.hostPhone = hostPhone;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public Object getHostAvatar() {
        return hostAvatar;
    }

    public void setHostAvatar(Object hostAvatar) {
        this.hostAvatar = hostAvatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStartDate() {
        try{
            long t=Long.parseLong(startDate);
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MM yyyy ");
            Date date = new Date(t);
            return sdf.format(date).toString();}
        catch (Exception e){}
        return "No end date";
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        try{
            long t=Long.parseLong(endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MM yyyy ");
            Date date = new Date(t);
            return sdf.format(date).toString();}
        catch (Exception e){}
        return "No end date";
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getAdults() {
        return adults;
    }

    public void setAdults(Integer adults) {
        this.adults = adults;
    }

    public Integer getChilds() {
        return childs;
    }

    public void setChilds(Integer childs) {
        this.childs = childs;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getCreatedOn() {
        try{
            long t=Long.parseLong(createdOn);
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MM yyyy ");
            Date date = new Date(t);
            return sdf.format(date).toString();}
        catch (Exception e){}
        return "No end date";
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}

