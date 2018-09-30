package com.zsdang.data.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.zsdang.LogUtils;

public class LocalBooksProvider extends ContentProvider {

    private static final String TAG = "LocalBooksProvider";

    private static final String AUTHORITY = "com.zsdang.data.local.LocalBooksProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LocalBooksDbOpenHelper.BOOKS_TABLE_NAME);

    private static final UriMatcher URI_MATCHER;

    private static final int MATCH_CODE_MULTIPLE= 1;

    private static final int MATCH_CODE_SINGLE = 2;

    private LocalBooksDbOpenHelper mLocalBooksDbOpenHelper;

    private SQLiteDatabase mSQLiteDatabase;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, LocalBooksDbOpenHelper.BOOKS_TABLE_NAME, MATCH_CODE_MULTIPLE);
        URI_MATCHER.addURI(AUTHORITY, LocalBooksDbOpenHelper.BOOKS_TABLE_NAME + "/#", MATCH_CODE_SINGLE);
    }

    public LocalBooksProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        mSQLiteDatabase = mLocalBooksDbOpenHelper.getWritableDatabase();
        int deleteRows = mSQLiteDatabase.delete(LocalBooksDbOpenHelper.BOOKS_TABLE_NAME, selection, selectionArgs);
        LogUtils.d(TAG, "delete:" + deleteRows);
        if (deleteRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw null;
    }



    @Override
    public Uri insert(Uri uri, ContentValues values) {
        mSQLiteDatabase = mLocalBooksDbOpenHelper.getWritableDatabase();
        long id = mSQLiteDatabase.insert(LocalBooksDbOpenHelper.BOOKS_TABLE_NAME, null, values);
        LogUtils.d(TAG, "insert:" + id);
        Uri appendedUri = null;
        if (id > 0) {
            appendedUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return appendedUri;
    }

    @Override
    public boolean onCreate() {
        LogUtils.d(TAG, "oncreate");
        mLocalBooksDbOpenHelper = new LocalBooksDbOpenHelper(getContext());
        if (mLocalBooksDbOpenHelper != null) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        mSQLiteDatabase = mLocalBooksDbOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = mSQLiteDatabase.query(LocalBooksDbOpenHelper.BOOKS_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        } catch (Exception e) {
            mLocalBooksDbOpenHelper.onUpgrade(mLocalBooksDbOpenHelper.getWritableDatabase(), 0, 1);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        mSQLiteDatabase = mLocalBooksDbOpenHelper.getWritableDatabase();
        int updateRows = mSQLiteDatabase.update(LocalBooksDbOpenHelper.BOOKS_TABLE_NAME, values, selection, selectionArgs);
        LogUtils.d(TAG, "update:" + updateRows);
        if (updateRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateRows;
    }
}
