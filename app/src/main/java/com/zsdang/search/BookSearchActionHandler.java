package com.zsdang.search;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.zsdang.beans.Book;
import com.zsdang.data.DataManager;

import java.util.List;

public class BookSearchActionHandler {

    private BookSearchActivity mBookSearchActivity;
    private DataManager mDataManager;
    private Handler mHandler;

    public BookSearchActionHandler(@NonNull BookSearchActivity activity) {
        mBookSearchActivity = activity;
        mDataManager = new DataManager(mBookSearchActivity);
        mHandler = new Handler();
    }

    public void searchBooks(String keyword, int page) {
        keyword = keyword.trim();
        if (!TextUtils.isEmpty(keyword)) {
            // TODO: Add to local history

            // openBookSearchResultFragment
            mBookSearchActivity.openBookSearchResultFragment();

            // start to serarch
            mDataManager.searchBooksByPage(keyword, page, new DataManager.QueryBooksCallback() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess(final List<Book> books) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mBookSearchActivity.updateSearchedBooks(books);
                        }
                    });
                }
            });
        } else {
            Toast.makeText(mBookSearchActivity, "输入不能为空！", Toast.LENGTH_SHORT).show();
        }
    }
}
