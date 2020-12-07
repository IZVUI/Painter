package com.example.painter;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

final class MyDetailsLookup extends ItemDetailsLookup {

    private final RecyclerView mRecyclerView;

    MyDetailsLookup(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public @Nullable
    ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            if (holder instanceof RecyclerView.ViewHolder) {
                return null;
               // return ((RecyclerView.ViewHolder) holder);
            }
        }
        return null;
    }
}