package com.example.project.APIConnect;
import com.google.gson.annotations.SerializedName;

public class CreateTourRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("name")
    private String name;

    @SerializedName("startDate")
    private long startDate;

    @SerializedName("endDate")
    private long endDate;

    @SerializedName("adults")
    private int adults;

    @SerializedName("childs")
    private int childs;

    @SerializedName("minCost")
    private float minCost;

    @SerializedName("maxCost")
    private float maxCost;

    @SerializedName("sourceLat")
    private float sourceLat;

    @SerializedName("sourceLong")
    private float sourceLong;

    @SerializedName("desLat")
    private float desLat;
    @SerializedName("desLong")
    private float desLong;

    public float getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(float sourceLat) {
        this.sourceLat = sourceLat;
    }

    public float getSourceLong() {
        return sourceLong;
    }

    public void setSourceLong(float sourceLong) {
        this.sourceLong = sourceLong;
    }

    public float getDesLat() {
        return desLat;
    }

    public void setDesLat(float desLat) {
        this.desLat = desLat;
    }

    public float getDesLong() {
        return desLong;
    }

    public void setDesLong(float desLong) {
        this.desLong = desLong;
    }


    @SerializedName("isPrivate")
    private boolean isPrivate;


    public String getname() {
        return name;
    }
    public void setname(int userId) {
        this.name = name;
    }

    public long getStartDate() {return  startDate;}
    public void setStartDate(){this.startDate=startDate;}

    public long getEndDate() {return  endDate;}
    public void setEndDate(){this.endDate= endDate;}

    public float getMinCost() {return  minCost;}
    public void setMinCost(){this.minCost= minCost;}

    public float getMaxCost() {return  maxCost;}
    public void setMaxCost(){this.maxCost= maxCost;}

    public int getAdults() {return  adults;}
    public void setAdults(){this.adults= adults;}

    public int getChilds() {return  childs;}
    public void setChilds(){this.childs= childs;}

    public boolean isPrivate() {
        return isPrivate;
    }
    public void setisPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
