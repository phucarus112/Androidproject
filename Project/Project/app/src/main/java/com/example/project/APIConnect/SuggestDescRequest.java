package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SuggestDescRequest {
    @SerializedName("hasOneCoordinate")
    @Expose
    private boolean hasOneCoordinate;

    @SerializedName("coordList")
    @Expose
    private ArrayList<CoordList> coordList;

    public SuggestDescRequest(boolean hasOneCoordinate, ArrayList<CoordList> coordList) {
        this.hasOneCoordinate = hasOneCoordinate;
        this.coordList = coordList;
    }

    public static class CoordList{

        @SerializedName("coordinateSet")
        @Expose
        private ArrayList<LocationStopPoint> coordinateSet;

        public ArrayList<LocationStopPoint> getCoordinateSet() {
            return coordinateSet;
        }

        public CoordList(ArrayList<LocationStopPoint> coordinateSet) {
            this.coordinateSet = coordinateSet;
        }

        public void setCoordinateSet(ArrayList<LocationStopPoint> coordinateSet) {
            this.coordinateSet = coordinateSet;
        }
    }

    public boolean isHasOneCoordinate() {
        return hasOneCoordinate;
    }

    public void setHasOneCoordinate(boolean hasOneCoordinate) {
        this.hasOneCoordinate = hasOneCoordinate;
    }

    public ArrayList<CoordList> getCoordList() {
        return coordList;
    }

    public void setCoordList(ArrayList<CoordList> coordList) {
        this.coordList = coordList;
    }
}
