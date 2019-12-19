package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListToursHistoryResponse {

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("tours")
    @Expose
    private List<TourHistory> tours = null;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<TourHistory> getTours() {
        return tours;
    }

    public void setTours(List<TourHistory> tours) {
        this.tours = tours;
    }
}
