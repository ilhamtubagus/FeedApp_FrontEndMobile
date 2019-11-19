package com.example.feedapp.rest;

import com.google.gson.annotations.SerializedName;

public class DataMessage {
    @SerializedName("field")
    private String field;

    @SerializedName("message")
    private String message;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
