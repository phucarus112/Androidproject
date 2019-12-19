package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StatusResponse {
    @SerializedName("totalToursGroupedByStatus")
    @Expose
    private ArrayList<Status> listStatus;

    public ArrayList<Status> getListStatus() {
        return listStatus;
    }

    public void setListStatus(ArrayList<Status> listStatus) {
        this.listStatus = listStatus;
    }

    public class Status{
        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("total")
        @Expose
        private int total;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
