package com.zsdang.home;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.zsdang.LogUtils;
import com.zsdang.beans.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class ReadBooksLoader extends AsyncTaskLoader<List<Book>> {

    private ForceLoadContentObserver mObserver;
    private List<Book> mBooks;

    public ReadBooksLoader(Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
    }

    @Override
    protected void onStartLoading() {
        LogUtils.d("ReadBooksLoader", "onStartLoading");
        if (mBooks != null) {
            deliverResult(mBooks);
        }

        if (takeContentChanged() || mBooks == null) {
            forceLoad();
        }
    }

    @Override
    public List<Book> loadInBackground() {
        LogUtils.d("ReadBooksLoader", "loadInBackground");

        List<Book> books = new ArrayList<>();

        books.add(new Book("凡人修仙之仙界篇", "https://www.qu.la/book/3353/"));
        books.add(new Book("圣墟", "https://www.qu.la/book/24868/"));
        books.add(new Book("凡人修仙传", "http://www.qu.la/book/401/"));
        books.add(new Book("凡人修仙传", "http://www.qu.la/book/401/"));
        books.add(new Book("遮天", "http://www.qu.la/book/394/"));
        return books;
    }

    @Override
    public void deliverResult(List<Book> books) {
        LogUtils.d("ReadBooksLoader", "deliverResult");
        if (isReset()) {
            return;
        }

        mBooks = books;
        if (isStarted()) {
            super.deliverResult(mBooks);
        }
    }

    @Override
    public void reset() {
        LogUtils.d("ReadBooksLoader", "reset");
        super.reset();

        // Ensure the loader is stopped
        onStartLoading();

        mBooks = null;
    }

    @Override
    protected void onStopLoading() {
        LogUtils.d("ReadBooksLoader", "onStopLoading");
        cancelLoad();
    }
}
