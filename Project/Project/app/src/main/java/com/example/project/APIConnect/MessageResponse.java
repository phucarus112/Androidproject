package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessageResponse {

    @SerializedName("message")
    @Expose
    private ArrayList<returnMessage> returnMessages;

    public ArrayList<returnMessage> getReturnMessages() {
        return returnMessages;
    }

    public void setReturnMessages(ArrayList<returnMessage> returnMessages) {
        this.returnMessages = returnMessages;
    }

    public class returnMessage{
        @SerializedName("location")
        @Expose
        private String location;

        @SerializedName("param")
        @Expose
        private String param;

        @SerializedName("msg")
        @Expose
        private String msg;

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
    }
}
