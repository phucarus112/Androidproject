package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class listCommentResponse {
    @SerializedName("commentList")
    @Expose
    private List<commentList> commentList = null;

    public List<commentList> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<commentList> commentList) {
        this.commentList = commentList;
    }
}