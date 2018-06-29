package com.zsdang.bookdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsdang.R;
import com.zsdang.beans.Book;

import java.util.List;

/**
 * Created by aabingoo on 2017/9/3.
 */

public class BookDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int VIEW_TYPE_BODY = 0;
    private static int VIEW_TYPE_SIMILAR_BOOK = 1;

    private Book mBook;
    private List<Book> mSimilarBooks;

    @Override
    public int getItemViewType(int position) {
        if (position == VIEW_TYPE_BODY) {
            return VIEW_TYPE_BODY;
        } else {
            return VIEW_TYPE_SIMILAR_BOOK;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_BODY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_bookdetail_body, parent, false);
            return new BookDetailItem(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_bookdetail_similar_book, parent, false);
            return new SimilarBookItem(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_BODY) {
            BookDetailItem bookDetailItem = (BookDetailItem) holder;
            bookDetailItem.updateView(mBook);
        } else {
            SimilarBookItem similarBookItem = (SimilarBookItem) holder;
            similarBookItem.updateView(mSimilarBooks);
        }


//        List<String> list = new ArrayList<>();
//        list.addAll(mBook.getChapters().keySet());
//        BookDetailItem bookDetailItem = (BookDetailItem) holder;
//        bookDetailItem.updateView(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (mBook != null && mSimilarBooks != null) {
            return 2;
        } else {
            return 0;
        }
    }

    public void notifyDataSetChanged(Book book, List<Book> similarBooks) {
        if (book != null) {
            mBook = book;
            mSimilarBooks = similarBooks;
            notifyDataSetChanged();
        }
    }

    private class BookDetailItem extends RecyclerView.ViewHolder {

        private TextView bookName;
        private TextView bookAuthor;
        private TextView bookDesc;
        private TextView bookLatestChapter;

        public BookDetailItem(View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookDesc = itemView.findViewById(R.id.book_desc);
            bookLatestChapter = itemView.findViewById(R.id.book_latest_chapter);
        }

        public void updateView(Book book) {
            if (book != null) {
                bookName.setText(book.getName());
                bookAuthor.setText(book.getAuthor());
                bookDesc.setText(book.getDesc());
            }
        }
    }

    private class SimilarBookItem extends RecyclerView.ViewHolder {

        private LinearLayout similarBookContainer;

        public SimilarBookItem(View itemView) {
            super(itemView);
            similarBookContainer = (LinearLayout) itemView;
        }

        public void updateView(List<Book> similarBooks) {
            for (int i = 0; i < similarBookContainer.getChildCount()
                    && similarBooks != null && i < similarBooks.size(); i++) {
                Book book = similarBooks.get(i);
                View similarBookItem = similarBookContainer.getChildAt(i);
                TextView bookName = similarBookItem.findViewById(R.id.book_name);
                bookName.setText(book.getName());
            }
        }
    }
}
