package com.example.project.APIConnect;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointStat {
    @SerializedName("point")
    @Expose
    private Integer point;
    @SerializedName("total")
    @Expose
    private String total;

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getTotal() {
        return Integer.parseInt(total);
    }

    public void setTotal(String total) {
        this.total = total;
    }
}