package com.zsdang.bookstore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zsdang.R;
import com.zsdang.beans.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BinyongSu on 2018/6/22.
 */

public class BookstoreRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int NAVICATION_ITEM_CNT = 10;
    public static final int BOOK_CNT_IN_CATEGORY = 5;

    private static final int VIEW_TYPE_BANNER = 0;
    private static final int VIEW_TYPE_NAVIGATION = 1;
    private static final int VIEW_TYPE_RECOMMEND = 2;
    private static final int VIEW_TYPE_NEW = 3;
    private static final int VIEW_TYPE_SERIALIZE = 4;
    private static final int VIEW_TYPE_END = 5;

    private List<Book> mRecommendBooks;
    private List<Book> mNewBooks;
    private List<Book> mSerializeBooks;
    private List<Book> mEndBooks;

    @Override
    public int getItemViewType(int position) {
        if (position == VIEW_TYPE_BANNER) {
            return VIEW_TYPE_BANNER;
        } else {
            return VIEW_TYPE_NAVIGATION;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_bookstore_category, parent, false);
        return new CategoryItem(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position + 2) {
            case VIEW_TYPE_RECOMMEND:
                CategoryItem recommendItem = (CategoryItem) holder;
                recommendItem.updateHead("重磅推荐");
                recommendItem.updateView(mRecommendBooks);
                break;
            case VIEW_TYPE_NEW:
                CategoryItem newItem = (CategoryItem) holder;
                newItem.updateHead("火热新书");
                newItem.updateView(mNewBooks);
                break;
            case VIEW_TYPE_SERIALIZE:
                CategoryItem serializeItem = (CategoryItem) holder;
                serializeItem.updateHead("热门连载");
                serializeItem.updateView(mSerializeBooks);
                break;
            case VIEW_TYPE_END:
                CategoryItem endItem = (CategoryItem) holder;
                endItem.updateHead("完本精选");
                endItem.updateView(mEndBooks);
                break;
        }

    }

    @Override
    public int getItemCount() {
        if (mRecommendBooks != null &&
                mNewBooks != null &&
                mSerializeBooks != null &&
                mEndBooks != null) {
            return 4;
        } else {
            return 0;
        }
    }

    public void notifyChange(List<Book> recommendBooks,
                             List<Book> newBooks,
                             List<Book> serializeBooks,
                             List<Book> endBooks) {
        mRecommendBooks = recommendBooks;
        mNewBooks = newBooks;
        mSerializeBooks = serializeBooks;
        mEndBooks = endBooks;
        notifyDataSetChanged();
    }


    private class NavicationItem extends RecyclerView.ViewHolder {

        private TextView navicationName;

        public NavicationItem(View itemView) {
            super(itemView);
            navicationName = (TextView) itemView.findViewById(R.id.title);
        }
    }
    private class CategoryItem extends RecyclerView.ViewHolder {

        private View categoryHead;
        private RelativeLayout categoryBody;

        public CategoryItem(View itemView) {
            super(itemView);
            categoryHead = itemView.findViewById(R.id.category_head);
            categoryBody = itemView.findViewById(R.id.category_body);
        }

        public void updateHead(String title) {
            TextView categoryTitle = categoryHead.findViewById(R.id.title);
            categoryTitle.setText(title);
        }

        public void updateView(List<Book> books) {
            for (int i = 0; i < categoryBody.getChildCount() && i < books.size(); i++) {
                Book book = books.get(i);
                View bookItemView = categoryBody.getChildAt(i);
                TextView bookName = bookItemView.findViewById(R.id.book_name);
                bookName.setText(book.getName());
                if (i < 2) {
                    TextView bookAuthor = bookItemView.findViewById(R.id.boot_category);
                    bookAuthor.setText(book.getAuthor());
                    TextView bookDesc = bookItemView.findViewById(R.id.book_desc);
                    bookDesc.setText(book.getDesc());
                }
            }
        }
    }
}
