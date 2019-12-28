package com.example.project.APIConnect;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListCommentServiceResponse {

    @SerializedName("feedbackList")
    @Expose
    private ArrayList<CommentUser> commentUsers;

    public ArrayList<CommentUser> getCommentUsers() {
        return commentUsers;
    }

    public void setCommentUsers(ArrayList<CommentUser> commentUsers) {
        this.commentUsers = commentUsers;
    }

    public class CommentUser
    {
        @SerializedName("id")
        @Expose
        private int id;

        @SerializedName("userId")
        @Expose
        private String userId;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("avatar")
        @Expose
        private String avatar;

        @SerializedName("feedback")
        @Expose
        private String feedback;

        @SerializedName("point")
        @Expose
        private int point;

        @SerializedName("createdOn")
        @Expose
        private String createdOn;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }
    }




}
