package com.zsdang.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zsdang.LogUtils;

/**
 * Created by BinyongSu on 2018/6/1.
 */

public class LocalBooksDbOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "LocalBooksDbOpenHelper";

    public static final String DATABASE_NAME = "zsd.db";
    public static final int DATABASE_VERSION = 2;
    public static final String BOOKS_COLUMN_ID = "id";
    public static final String BOOKS_COLUMN_NAME = "name";
    public static final String BOOKS_COLUMN_AUTHOR = "author";
    public static final String BOOKS_COLUMN_IMG_NAME = "img_name";
    public static final String BOOKS_COLUMN_DESCRIPTION = "description";
    public static final String BOOKS_COLUMN_CATEGORY_ID = "category_id";
    public static final String BOOKS_COLUMN_CATEGORY = "category";
    public static final String BOOKS_COLUMN_FIRST_CHAPTER_ID = "first_chapter_id";
    public static final String BOOKS_COLUMN_LATEST_CHAPTER_ID = "latest_chapter_id";
    public static final String BOOKS_COLUMN_LATEST_CHAPTER_TITLE = "latest_chapter_title";
    public static final String BOOKS_COLUMN_LATEST_CHAPTER_DATE = "latest_chapter_date";
    public static final String BOOKS_COLUMN_LATEST_CHAPTER_ISREAD = "latest_chapter_isread";
    public static final String BOOKS_COLUMN_STATUS = "status";

    public static final String CHAPTERS_COLUMN_BOOK_ID = "book_id";
    public static final String CHAPTERS_COLUMN_CHAPTER_ID = "chapter_id";
    public static final String CHAPTERS_COLUMN_CHAPTER_TITLE = "chapter_title";
    public static final String CHAPTERS_COLUMN_CHAPTER_DATE = "chapter_date";
    public static final String CHAPTERS_COLUMN_CACHE_ADDR = "cache_addr";

    public static final String BOOKS_TABLE_NAME = "books";
    private final String CREATE_BOOKS_TABLE_SQL = "CREATE TABLE " + BOOKS_TABLE_NAME + "("
            +  "id TEXT NOT NULL PRIMARY KEY,"
            +  "name TEXT NOT NULL,"
            +  "author TEXT NOT NULL,"
            +  "img_name TEXT NOT NULL,"
            +  "description TEXT,"
            +  "category_id TEXT,"
            +  "category TEXT,"
            +  "first_chapter_id TEXT,"
            +  "latest_chapter_id TEXT,"
            +  "latest_chapter_title TEXT,"
            +  "latest_chapter_date TEXT,"
            +  "latest_chapter_isread INTEGER,"
            +  "status TEXT,"
            +  "unique(name,author,img_name)"
            +  ");";

    public final String CHAPTERS_TABLE_NAME = "chapters";
    private final String CREATE_CHAPTERS_TABLE_SQL = "CREATE TABLE " +  CHAPTERS_TABLE_NAME + "("
            + "book_id TEXT NOT NULL,"
            + "chapter_id TEXT NOT NULL,"
            + "chapter_title TEXT NOT NULL,"
            + "chapter_date TEXT NOT NULL,"
            + "cache_addr TEXT NOT NULL,"
            + "PRIMARY KEY(bookid, chapter_id)"
            + ");";

    public LocalBooksDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtils.d(TAG, "craete table:" + BOOKS_TABLE_NAME);
        db.execSQL(CREATE_BOOKS_TABLE_SQL);
        db.execSQL(CREATE_CHAPTERS_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(db);
        onCreate(db);
    }

    private void deleteTable(SQLiteDatabase db) {
        String delete_table_sql = "drop table if exists " + BOOKS_TABLE_NAME;
        db.execSQL(delete_table_sql);

        delete_table_sql = "drop table if exists " + CHAPTERS_TABLE_NAME;
        db.execSQL(delete_table_sql);
    }
}
