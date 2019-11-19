package com.example.feedapp.rest;

import com.google.gson.annotations.SerializedName;

public class Creator {
    @SerializedName("_id")
    private String _id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
