package com.example.feedapp.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMessage {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    @Expose
    private List<DataMessage> data;

    @SerializedName("userId")
    private String userId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataMessage> getData() {
        return data;
    }

    public void setData(List<DataMessage> data) {
        this.data = data;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
