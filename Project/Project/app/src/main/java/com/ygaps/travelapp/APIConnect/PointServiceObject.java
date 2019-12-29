package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointServiceObject {
        @SerializedName("point")
        @Expose
        private int point;

        @SerializedName("total")
        @Expose
        private int total;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
