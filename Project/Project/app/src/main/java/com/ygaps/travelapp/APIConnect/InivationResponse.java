package com.ygaps.travelapp.APIConnect;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ygaps.travelapp.APIConnect.TourInvation;

import java.util.List;

public class InivationResponse {
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("tours")
    @Expose
    private List<TourInvation> tours = null;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<TourInvation> getTours() {
        return tours;
    }

    public void setTours(List<TourInvation> tours) {
        this.tours = tours;
    }

}