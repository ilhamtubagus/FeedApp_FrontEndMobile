package com.example.feedapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.feedapp.R;
import com.example.feedapp.adapter.FeedPostsAdapter;
import com.example.feedapp.rest.ApiClient;
import com.example.feedapp.rest.FeedService;
import com.example.feedapp.rest.ListPosts;
import com.example.feedapp.rest.Post;
import com.example.feedapp.utils.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Feed extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rv_feed)
    RecyclerView recyclerView;
    @BindView(R.id.srl_feed)
    SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private FeedPostsAdapter feedPostsAdapter;
    private final FeedService feedService = ApiClient.getClient().create(FeedService.class);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, v);

        swipeRefreshLayout.setOnRefreshListener(this);

        linearLayoutManager = new LinearLayoutManager(getContext());
        feedPostsAdapter = new FeedPostsAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);

        fetchData();
        return v;
    }
    private void fetchData(){
        SessionManager sessionManager = new SessionManager(getContext());
        Call<ListPosts> call = feedService.getPosts(sessionManager.getToken(getContext()));
        call.enqueue(new Callback<ListPosts>() {
            @Override
            public void onResponse(Call<ListPosts> call, Response<ListPosts> response) {
                Log.w("Log => ",new Gson().toJson(response.body()));
                if (response.isSuccessful()){
                    ListPosts listPosts = response.body();
                    List<Post> posts = listPosts.getPosts();
                    feedPostsAdapter.setPosts(posts);
                    recyclerView.setAdapter(feedPostsAdapter);
                    Toast.makeText(getContext(),  listPosts.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListPosts> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(),  "on fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
