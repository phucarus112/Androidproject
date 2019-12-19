package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InfoTourResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("hostId")
    @Expose
    private String hostId;

    @SerializedName("status")
    @Expose
    private int status;

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
    private int adults;

    @SerializedName("childs")
    @Expose
    private int childs;

    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("stopPoints")
    @Expose
    private ArrayList<StopPointObjectEdit> stopPointObjectArrayList;

    @SerializedName("comments")
    @Expose
    private ArrayList<CommentObject> commentObjectArrayList;

    @SerializedName("members")
    @Expose
    private ArrayList<MemberObject> memberObjectArrayList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChilds() {
        return childs;
    }

    public void setChilds(int childs) {
        this.childs = childs;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<StopPointObjectEdit> getStopPointObjectArrayList() {
        return stopPointObjectArrayList;
    }

    public void setStopPointObjectArrayList(ArrayList<StopPointObjectEdit> stopPointObjectArrayList) {
        this.stopPointObjectArrayList = stopPointObjectArrayList;
    }

    public ArrayList<CommentObject> getCommentObjectArrayList() {
        return commentObjectArrayList;
    }

    public void setCommentObjectArrayList(ArrayList<CommentObject> commentObjectArrayList) {
        this.commentObjectArrayList = commentObjectArrayList;
    }

    public ArrayList<MemberObject> getMemberObjectArrayList() {
        return memberObjectArrayList;
    }

    public void setMemberObjectArrayList(ArrayList<MemberObject> memberObjectArrayList) {
        this.memberObjectArrayList = memberObjectArrayList;
    }
}
