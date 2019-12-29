package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationStopPoint {

    @SerializedName("lat")
    @Expose
    private double lat;

    public LocationStopPoint(double lat, double longtitude) {
        this.lat = lat;
        this.longtitude = longtitude;
    }

    @SerializedName("long")
    @Expose
    private double longtitude;

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
}
