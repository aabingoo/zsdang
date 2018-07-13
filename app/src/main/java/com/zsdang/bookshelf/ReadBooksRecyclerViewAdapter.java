package com.zsdang.bookshelf;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.data.web.server.DataServiceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class ReadBooksRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "ReadBooksRecyclerViewAdapter";

    private static final int VIEW_TYPE_READING = 0;

    private static final int VIEW_TYPE_READ = 1;

    private List<Book> mReadBooks;

    public ReadBooksRecyclerViewAdapter() {
        mReadBooks = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == VIEW_TYPE_READING) {
            return VIEW_TYPE_READING;
        } else {
            return VIEW_TYPE_READ;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType == VIEW_TYPE_READING) {
                        return 3;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LogUtils.d(TAG, "onCreateViewHolder");
        if (viewType == VIEW_TYPE_READING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_bookshelf_reading, parent, false);
            return new ReadingBookItem(parent.getContext(), view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_bookshelf_read_item, parent, false);
            return new ReadBookItem(parent.getContext(), view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //LogUtils.d(TAG, "onBindViewHolder");
        Book book = mReadBooks.get(position);
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_READING) {
            ReadingBookItem readingBookItem = (ReadingBookItem) holder;
            readingBookItem.updateView(book);
        } else {
            ReadBookItem readBookItem = (ReadBookItem) holder;
            readBookItem.updateView(book);
        }
    }

    @Override
    public int getItemCount() {
        return mReadBooks.size();
    }

    public void notifyDataSetChanged(final List<Book> books) {
        if (books != null) {
            //LogUtils.d(TAG, "notifyDataSetChanged:" + books.size());
            mReadBooks = books;
            notifyDataSetChanged();
        }
    }

    private class ReadingBookItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private ImageView bookCover;
        private TextView bookNameTv;
        private TextView latestChapterName;

        public ReadingBookItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = (ImageView) itemView.findViewById(R.id.reading_book_cover);
            bookNameTv = (TextView) itemView.findViewById(R.id.reading_book_name);
            latestChapterName = (TextView) itemView.findViewById(R.id.reading_book_latest_chapter);
        }

        public void updateView(Book book) {
            LogUtils.d(TAG, "updateView:" + book.getName());
            if (!TextUtils.isEmpty(book.getName())) {
                String imgUrl = String.format(DataServiceManager.HOST_BOOK_COVER, book.getImg());
                Glide.with(inContext).load(imgUrl).into(bookCover);
                bookNameTv.setText(book.getName());
                latestChapterName.setText(book.getLaestChapterName());
//                ImageLoader imageLoader = new ImageLoader(bookNameTv);
//                imageLoader.execute("1", "2", "3");
            }
        }
    }

    private class ReadBookItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private ImageView bookCover;
        private TextView bookNameTv;
        private TextView latestChapterName;

        public ReadBookItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = (ImageView) itemView.findViewById(R.id.read_book_cover);
            bookNameTv = (TextView) itemView.findViewById(R.id.read_book_name);
            latestChapterName = (TextView) itemView.findViewById(R.id.read_book_latest_chapter);
        }

        public void updateView(Book book) {
            LogUtils.d(TAG, "updateView:" + book.getName());
            if (!TextUtils.isEmpty(book.getName())) {
                LogUtils.d(TAG, "img:" + book.getImg() + "  id:" + book.getId());
                String imgUrl = String.format(DataServiceManager.HOST_BOOK_COVER, book.getImg());
                Glide.with(inContext).load(imgUrl).into(bookCover);
                bookNameTv.setText(book.getName());
                latestChapterName.setText(book.getLaestChapterName());
//                ImageLoader imageLoader = new ImageLoader(bookNameTv);
//                imageLoader.execute("1", "2", "3");
            }
        }
    }
}
