package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddStopPointRequest {

    @SerializedName("tourId")
    @Expose
    private String tourId;

    @SerializedName("stopPoints")
    @Expose
    private ArrayList<StopPointObject> stopPoints ;

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public ArrayList<StopPointObject> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(ArrayList<StopPointObject> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public AddStopPointRequest(String tourId, ArrayList<StopPointObject> stopPoints) {
        this.tourId = tourId;
        this.stopPoints = stopPoints;
    }
}
