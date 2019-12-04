package com.example.project.APIConnect;

import android.net.Uri;
import android.renderscript.Long2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.http.Url;

public class Tour {

    @SerializedName("id")
    @Expose
    private Integer id;
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
    @SerializedName("avatar")
    @Expose
    private Url avatar;

    public Integer getId() {
        return id;
    }
    public Tour(String minc,String maxc,String endDate, String startDate,int ID,String name,int status,int childs,int dault,Boolean is,Url a )
    {
        this.minCost=minc;
        this.maxCost=maxc;
        this.endDate=endDate;
        this.startDate=startDate;
        this.id=ID;
        this.name=name;
        this.status=status;
        this.childs=childs;
        this.adults=dault;
        this.isPrivate=is;
    }
    public void setId(Integer id) {
        this.id = id;
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
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - E, dd MM yyyy ");
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
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - E, dd MM yyyy ");
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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Url getAvatar() {
        return avatar;
    }

    public void setAvatar(Url avatar) {
        this.avatar = avatar;
    }

}