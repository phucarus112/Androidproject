package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PointServiceResponse {

    @SerializedName("pointStats")
    private ArrayList<PointServiceObject> pointStats;

    public ArrayList<PointServiceObject> getPointStats() {
        return pointStats;
    }

    public void setPointStats(ArrayList<PointServiceObject> pointStats) {
        this.pointStats = pointStats;
    }
}
