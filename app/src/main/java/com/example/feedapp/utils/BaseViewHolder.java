package com.example.feedapp.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    private int currentPosition;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void onBind(int position) {
        currentPosition = position;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}