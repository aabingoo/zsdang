package com.zsdang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by BinyongSu on 2018/6/1.
 */

public class LocalBooksDbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "zsd.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "books";
    public static final String BOOKS_COLUMN_ID = "id";
    public static final String BOOKS_COLUMN_NAME = "name";
    public static final String BOOKS_COLUMN_AUTHOR = "author";
    public static final String BOOKS_COLUMN_URL = "url";
    public static final String BOOKS_COLUMN_INTRODUCTION = "introduction";
    public static final String BOOKS_COLUMN_LATEST_CHAPTER = "latest_chapter";

    public LocalBooksDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_sql = "CREATE TABLE " +  TABLE_NAME + "("
                +  "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +  "name TEXT NOT NULL,"
                +  "author TEXT NOT NULL,"
                +  "url TEXT NOT NULL,"
                +  "introduction TEXT,"
                +  "latest_chapter TEXT,"
                +  "unique(name,author,url)"
                +  ");";
        db.execSQL(create_table_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTable(db);
        onCreate(db);
    }

    private void deleteTable(SQLiteDatabase db) {
        String delete_table_sql = "drop table if exists " + TABLE_NAME;
        db.execSQL(delete_table_sql);
    }
}
