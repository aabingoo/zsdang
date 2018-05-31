package com.zsd.home;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.zsd.LogUtils;
import com.zsd.beans.Book;

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

        books.add(new Book("1", "11"));
        books.add(new Book("12", "112"));
        books.add(new Book("13", "113"));
        books.add(new Book("14", "114"));
        books.add(new Book("15", "115"));
        books.add(new Book("1", "11"));
        books.add(new Book("12", "112"));
        books.add(new Book("13", "113"));
        books.add(new Book("14", "114"));
        books.add(new Book("15", "115"));
        books.add(new Book("1", "11"));
        books.add(new Book("12", "112"));
        books.add(new Book("13", "113"));
        books.add(new Book("14", "114"));
        books.add(new Book("15", "115"));
        books.add(new Book("1", "11"));
        books.add(new Book("12", "112"));
        books.add(new Book("13", "113"));
        books.add(new Book("14", "114"));
        books.add(new Book("15", "115"));
        books.add(new Book("1", "11"));
        books.add(new Book("12", "112"));
        books.add(new Book("13", "113"));
        books.add(new Book("14", "114"));
        books.add(new Book("15", "115"));

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
