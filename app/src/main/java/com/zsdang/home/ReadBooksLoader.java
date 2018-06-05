package com.zsdang.home;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

import com.zsdang.LogUtils;
import com.zsdang.beans.Book;
import com.zsdang.db.LocalBooksProvider;

import java.util.ArrayList;
import java.util.List;

import static com.zsdang.db.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.db.LocalBooksDbOpenHelper.BOOKS_COLUMN_URL;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class ReadBooksLoader extends AsyncTaskLoader<List<Book>> {

    private static final String TAG = "ReadBooksLoader";

    private ReadBooksContentObserver mObserver;
    private List<Book> mBooks;

    public ReadBooksLoader(Context context) {
        super(context);
        mObserver = new ReadBooksContentObserver();
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

        List<Book> books = new ArrayList<>();

//        books.add(new Book("凡人修仙之仙界篇", "https://www.qu.la/book/3353/"));
//        books.add(new Book("圣墟", "https://www.qu.la/book/24868/"));
//        books.add(new Book("凡人修仙传", "http://www.qu.la/book/401/"));
//        books.add(new Book("凡人修仙传", "http://www.qu.la/book/401/"));
//        books.add(new Book("遮天", "http://www.qu.la/book/394/"));

        Cursor cursor = getContext().getContentResolver().query(LocalBooksProvider.CONTENT_URI, null, null, null);
        getContext().getContentResolver().registerContentObserver(LocalBooksProvider.CONTENT_URI, true, mObserver);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
//                    LogUtils.d("Query", "BOOKS_COLUMN_ID:" + cursor.getInt(cursor.getColumnIndex(BOOKS_COLUMN_ID))
//                            + " BOOKS_COLUMN_NAME:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_NAME))
//                            + " BOOKS_COLUMN_AUTHOR:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_AUTHOR))
//                            + " BOOKS_COLUMN_URL:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_URL))
//                            + " BOOKS_COLUMN_INTRODUCTION:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_INTRODUCTION))
//                            + " BOOKS_COLUMN_LATEST_CHAPTER:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER)));

                    books.add(new Book(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_NAME)), cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_URL))));
                } while (cursor.moveToNext());
            }
        }

        return books;
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
