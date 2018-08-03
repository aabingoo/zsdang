package com.zsdang.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsdang.ImageLoader;
import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.data.DataManager;
import com.zsdang.data.web.server.DataServiceManager;

import java.util.List;

public class BookSearchResultRecyclerViewAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "BookSearchResultRecyclerViewAdpater";

    private List<Book> mSearchedBooks;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_search_book_item, parent, false);
        return new SearchedBookItem(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchedBookItem searchedBookItem = (SearchedBookItem) holder;
        searchedBookItem.updateView(mSearchedBooks.get(position));
    }

    @Override
    public int getItemCount() {
        if (mSearchedBooks != null) {
            return mSearchedBooks.size();
        }
        return 0;
    }

    public void notifyDataSetChanged(List<Book> searchedBooks) {
        if (searchedBooks != null) {
            mSearchedBooks = searchedBooks;
            notifyDataSetChanged();
        }
    }

    private class SearchedBookItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private ImageView inBookCover;
        private TextView inBookNameTv;
        private TextView inBookAuthorTv;
        private TextView inDesc;
        private Button inAddToBookshelfBtn;


        public SearchedBookItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            inBookCover = itemView.findViewById(R.id.im_book_cover);
            inBookNameTv = itemView.findViewById(R.id.tv_book_name);
            inBookAuthorTv = itemView.findViewById(R.id.tv_book_author);
            inDesc = itemView.findViewById(R.id.tv_book_desc);
            inAddToBookshelfBtn = itemView.findViewById(R.id.btn_add_to_bookshelf);
        }

        public void updateView(final Book book) {
            LogUtils.d(TAG, "updateView:" + book.getName());

            // image
            ImageLoader.loadImgInto(inContext, book.getImg(), inBookCover);

            // name
            inBookNameTv.setText(book.getName());

            // author
            inBookAuthorTv.setText(book.getAuthor());

            // desc
            inDesc.setText(book.getDesc());

            inAddToBookshelfBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataManager dataManager = new DataManager(inContext);
                    dataManager.addToBookshelf(book);
                }
            });
        }
    }
}
