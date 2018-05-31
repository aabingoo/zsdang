package com.zsd.home;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zsd.LogUtils;
import com.zsd.R;
import com.zsd.beans.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class ReadBooksRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "ReadBooksRecyclerViewAdapter";

    private List<Book> mReadBooks;

    public ReadBooksRecyclerViewAdapter() {
        mReadBooks = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtils.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_read_book_item, parent, false);
        return new ReadBookItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogUtils.d(TAG, "onBindViewHolder");
        Book book = mReadBooks.get(position);
        ReadBookItem readBookItem = (ReadBookItem) holder;
        readBookItem.updateView(book);
    }

    @Override
    public int getItemCount() {
        return mReadBooks.size();
    }

    public void notifyDataSetChanged(final List<Book> books) {
        if (books != null) {
            LogUtils.d(TAG, "notifyDataSetChanged:" + books.size());
            mReadBooks = books;
            notifyDataSetChanged();
        }
    }

    private class ReadBookItem extends RecyclerView.ViewHolder {

        private TextView bookNameTv;

        public ReadBookItem(View itemView) {
            super(itemView);
            bookNameTv = (TextView) itemView.findViewById(R.id.read_book_name_tv);
        }

        public void updateView(Book book) {
            if (!TextUtils.isEmpty(book.getName())) {
                bookNameTv.setText(book.getName());
            }
        }
    }
}
