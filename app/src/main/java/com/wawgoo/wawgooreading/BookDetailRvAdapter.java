package com.wawgoo.wawgooreading;

import android.support.v4.text.TextUtilsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wawgoo.wawgooreading.beans.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by aabingoo on 2017/9/3.
 */

public class BookDetailRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Book mBook = new Book();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("RvTest", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_book_detail_item, parent, false);
        return new BookDetailItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("RvTest", "onBindViewHolder pos:" + position + "  str:");
        List<String> list = new ArrayList<>();
        list.addAll(mBook.getChapters().keySet());
        BookDetailItem bookDetailItem = (BookDetailItem) holder;
        bookDetailItem.updateView(list.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("RvTest", "getItemCount:" + mBook.getChapters().size());
        return mBook.getChapters().size();
    }

    public void notifyDataSetChanged(final Book book) {
        if (book != null) {
            Log.d("CrawlerTest", "notifyDataSetChanged:" + book.getChapters().size());
            mBook = book;
            notifyDataSetChanged();
        }
    }

    private class BookDetailItem extends RecyclerView.ViewHolder {

        private TextView titleTv;

        public BookDetailItem(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.book_detail_item_title);
        }

        public void updateView(String title) {
            if (!TextUtils.isEmpty(title)) {
                titleTv.setText(title);
            }
        }
    }
}
