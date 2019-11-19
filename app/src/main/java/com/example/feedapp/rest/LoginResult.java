package com.example.feedapp.rest;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("token")
    private String token;

    @SerializedName("expiresIn")
    private String expiry_token;

    @SerializedName("userId")
    private String userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String  getExpiry_token() {
        return expiry_token;
    }

    public void setExpiry_token(String expiry_token) {
        this.expiry_token = expiry_token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
