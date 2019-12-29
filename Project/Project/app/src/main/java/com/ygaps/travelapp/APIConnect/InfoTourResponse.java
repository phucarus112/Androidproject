package com.ygaps.travelapp.APIConnect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class InfoTourResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("hostId")
    @Expose
    private String hostId;
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
    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;
    @SerializedName("stopPoints")
    @Expose
    private List<StopPointObjectEdit> stopPoints = null;
    @SerializedName("comments")
    @Expose
    private List<InforComment> comments = null;
    @SerializedName("members")
    @Expose
    private List<MyMember> members = null;

    public Integer getId() {
        return id;
    }
    public InfoTourResponse(){};
    public void setId(Integer id) {
        this.id = id;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
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
        return "No start date";
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
        return "No start date";
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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public List<StopPointObjectEdit> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<StopPointObjectEdit> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public List<InforComment> getComments() {
        return comments;
    }

    public void setComments(List<InforComment> comments) {
        this.comments = comments;
    }

    public List<MyMember> getMembers() {
        return members;
    }

    public void setMembers(List<MyMember> members) {
        this.members = members;
    }

}