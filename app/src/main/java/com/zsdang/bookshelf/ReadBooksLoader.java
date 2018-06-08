package com.zsdang.bookshelf;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.zsdang.LogUtils;
import com.zsdang.beans.Book;
import com.zsdang.data.DataManager;

import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class ReadBooksLoader extends AsyncTaskLoader<List<Book>> {

    private static final String TAG = "ReadBooksLoader";

    private ReadBooksContentObserver mObserver;
    private List<Book> mBooks;
    private DataManager mDataManager;

    public ReadBooksLoader(Context context) {
        super(context);
        mObserver = new ReadBooksContentObserver();
        mDataManager = new DataManager(context);
    }

    @Override
    protected void onStartLoading() {
        LogUtils.d(TAG, "onStartLoading - "
                + " takeContentChanged():" + takeContentChanged() + " mBooks == null:" + (mBooks == null));
        if (mBooks != null) {
            deliverResult(mBooks);
        }

        if (takeContentChanged() || mBooks == null) {
            forceLoad();
        }
    }

    @Override
    public List<Book> loadInBackground() {
        LogUtils.d(TAG, "loadInBackground");
        return mDataManager.queryLocalReadBooks(mObserver);
    }

    @Override
    public void deliverResult(List<Book> books) {
        LogUtils.d(TAG, "deliverResult");
        if (isReset()) {
            return;
        }

        mBooks = books;
        if (isStarted()) {
            super.deliverResult(mBooks);
        }
    }

    @Override
    protected void onReset() {
        LogUtils.d(TAG, "onReset");
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        mBooks = null;
    }

    @Override
    protected void onStopLoading() {
        LogUtils.d(TAG, "onStopLoading");
        cancelLoad();
    }

    private final class ReadBooksContentObserver extends ContentObserver {

        public ReadBooksContentObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            LogUtils.d(TAG, "onChange");
            onContentChanged();
        }
    }
}
