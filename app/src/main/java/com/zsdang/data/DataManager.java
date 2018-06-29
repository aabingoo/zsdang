package com.zsdang.data;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;

import com.zsdang.beans.Book;
import com.zsdang.data.local.LocalBooksProvider;

import java.util.ArrayList;
import java.util.List;

import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_URL;

/**
 * Created by BinyongSu on 2018/6/7.
 */

public class DataManager {

    private static final String TAG = "DataManager";

    private Context mContext;

    public DataManager(Context context) {
        mContext = context;
    }

    public List<Book> queryLocalReadBooks(ContentObserver contentObserver) {
        List<Book> books = new ArrayList<>();
        // Get cursor
        Cursor cursor = mContext.getContentResolver().query(
                LocalBooksProvider.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            // Register ContentObserver
            mContext.getContentResolver().registerContentObserver(
                    LocalBooksProvider.CONTENT_URI,
                    true,
                    contentObserver);
            // Get books.
            if (cursor.moveToFirst()) {
                do {
//                    LogUtils.d("Query", "BOOKS_COLUMN_ID:" + cursor.getInt(cursor.getColumnIndex(BOOKS_COLUMN_ID))
//                            + " BOOKS_COLUMN_NAME:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_NAME))
//                            + " BOOKS_COLUMN_AUTHOR:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_AUTHOR))
//                            + " BOOKS_COLUMN_URL:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_URL))
//                            + " BOOKS_COLUMN_INTRODUCTION:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_INTRODUCTION))
//                            + " BOOKS_COLUMN_LATEST_CHAPTER:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER)));
                    books.add(new Book(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_URL))));
                } while (cursor.moveToNext());
            }
        }
        return books;
    }

}
