package com.zsdang.bookstore;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by BinyongSu on 2018/6/22.
 */

public class BookstoreRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_BANNER = 0;
    private static final int VIEW_TYPE_NAVIGATION = 1;
    private static final int VIEW_TYPE_RECOMMEND = 2;
    private static final int VIEW_TYPE_NEW = 3;
    private static final int VIEW_TYPE_SERIALIZE = 4;
    private static final int VIEW_TYPE_END = 5;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
