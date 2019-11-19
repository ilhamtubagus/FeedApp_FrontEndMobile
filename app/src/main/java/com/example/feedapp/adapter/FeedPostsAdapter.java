package com.example.feedapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedapp.R;
import com.example.feedapp.rest.Post;
import com.example.feedapp.utils.BaseViewHolder;

import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedPostsAdapter extends RecyclerView.Adapter<FeedPostsAdapter.ItemViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private List<Post> posts;

    public void setPosts(List<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void addPosts(List<Post> posts){
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_author)
        TextView author;
        @BindView(R.id.tv_dateCreated)
        TextView dateCreated;
        @BindView(R.id.tv_diffDate)
        TextView diffDate;
        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            /*title = itemView.findViewById(R.id.tv_title);
            author = itemView.findViewById(R.id.tv_author);
            dateCreated = itemView.findViewById(R.id.tv_dateCreated);*/
        }

        private void onBind(int position){
            final Post post = posts.get(position);
            title.setText(post.getTitle());
            author.setText(post.getCreator().getName());
            dateCreated.setText(post.getCreatedAt());
            diffDate.setText(post.getDateDiff());
        }
    }
}
