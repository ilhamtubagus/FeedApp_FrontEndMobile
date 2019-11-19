package com.example.feedapp.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface FeedService {
    @GET("feed/posts")
    Call<ListPosts> getPosts(@Header("Authorization") String Authorization);
}
