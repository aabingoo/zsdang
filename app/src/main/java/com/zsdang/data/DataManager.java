package com.zsdang.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;

import com.zsdang.LogUtils;
import com.zsdang.beans.Book;
import com.zsdang.data.local.LocalBooksProvider;

import java.util.ArrayList;
import java.util.List;

import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_AUTHOR;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_INTRODUCTION;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_LATEST_CHAPTER;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_IMG_NAME;

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
//                            + " BOOKS_COLUMN_IMG_NAME:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_IMG_NAME))
//                            + " BOOKS_COLUMN_INTRODUCTION:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_INTRODUCTION))
//                            + " BOOKS_COLUMN_LATEST_CHAPTER:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER)));
                    Book book = new Book(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_AUTHOR)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_IMG_NAME)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_INTRODUCTION))
                    );
                    book.setLaestChapterName(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER)));
                    books.add(book);

//                    int localFileNameIndex = cursor.getColumnIndex("local_filename");
//                    if (localFileNameIndex >= 0) {
//                        File file = new File(cursor.getString(localFileNameIndex));
//                        file.getAbsolutePath()
//                    }

                } while (cursor.moveToNext());
            }
        }
        return books;
    }

    public void addToBookshelf(Book book) {
        LogUtils.d("Query", "BOOKS_COLUMN_ID:" + book.getId()
                            + " BOOKS_COLUMN_NAME:" + book.getName()
                            + " BOOKS_COLUMN_AUTHOR:" + book.getAuthor()
                            + " BOOKS_COLUMN_IMG_NAME:" + book.getImg()
                            + " BOOKS_COLUMN_INTRODUCTION:" + book.getDesc()
                            + " BOOKS_COLUMN_LATEST_CHAPTER:" + book.getLaestChapterName());
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_ID, book.getId());
        contentValues.put(BOOKS_COLUMN_NAME, book.getName());
        contentValues.put(BOOKS_COLUMN_AUTHOR, book.getAuthor());
        contentValues.put(BOOKS_COLUMN_IMG_NAME, book.getImg());
        contentValues.put(BOOKS_COLUMN_INTRODUCTION, book.getDesc());
        contentValues.put(BOOKS_COLUMN_LATEST_CHAPTER, book.getLaestChapterName());
        mContext.getContentResolver().insert(LocalBooksProvider.CONTENT_URI,contentValues);
    }

    public void deleteFromBookshelf(List<Book> books) {
        String where = formWhere(books);
        mContext.getContentResolver().delete(LocalBooksProvider.CONTENT_URI, where, null);
    }

    private String formWhere(List<Book> books) {
        String where = null;
        if (books != null) {
            if (books.size() > 1) {
                where = "id IN (";
                for (Book book: books) {
                    where += book.getId() + ",";
                }
                where = where.substring(0, where.length() - 1) + ")";
            } else {
                where = "id = " + books.get(0).getId();
            }
        }
        return where;
    }

}
