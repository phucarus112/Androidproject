package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;

public class SuggestDescResponse {

    @SerializedName("stopPoints")
    @Expose
    private ArrayList<SuggestDescObjectResponse> list;

    public ArrayList<SuggestDescObjectResponse> getList() {
        return list;
    }

    public void setList(ArrayList<SuggestDescObjectResponse> list) {
        this.list = list;
    }
}
