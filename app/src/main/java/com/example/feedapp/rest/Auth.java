package com.example.feedapp.rest;

import com.example.feedapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface Auth {
    @POST("auth/login")
    @FormUrlEncoded
    Call<LoginResult> login(@Field("email") String email, @Field("password") String password);

    @POST("auth/refresh-token")
    Call<LoginResult> refreshToken(@Header("Authorization") String Authorization);

    @PUT("auth/signup")
    @FormUrlEncoded
    Call<ResponseMessage> signUp(@Field("email") String email, @Field("name") String name, @Field("password") String password, @Field("passwordConfirmation") String passwordConfirmation);
}