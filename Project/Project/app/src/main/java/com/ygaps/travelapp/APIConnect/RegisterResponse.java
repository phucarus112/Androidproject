package com.ygaps.travelapp.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RegisterResponse {
    @SerializedName("message")
    @Expose
    private ArrayList<BodyMessage> list;

    public ArrayList<BodyMessage> getList() {
        return list;
    }

    public void setList(ArrayList<BodyMessage> list) {
        this.list = list;
    }

    public class BodyMessage{
        @SerializedName("location")
        @Expose
        private String location;

        @SerializedName("param")
        @Expose
        private String param;

        @SerializedName("msg")
        @Expose
        private String msg;

        @SerializedName("value")
        @Expose
        private String value;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
