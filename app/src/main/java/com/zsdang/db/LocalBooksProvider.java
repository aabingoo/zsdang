package com.zsdang.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.zsdang.db.LocalBooksDbOpenHelper.TABLE_NAME;

public class LocalBooksProvider extends ContentProvider {

    private static final String TAG = "LocalBooksProvider";

    private static final String AUTHORITY = "com.zsdang.db.LocalBooksProvider";

    private static final UriMatcher URI_MATCHER;

    private static final int MATCH_CODE_MULTIPLE= 1;

    private static final int MATCH_CODE_SINGLE = 2;

    private LocalBooksDbOpenHelper mLocalBooksDbOpenHelper;

    private SQLiteDatabase mSQLiteDatabase;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME, MATCH_CODE_MULTIPLE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME + "/#", MATCH_CODE_SINGLE);
    }

    public LocalBooksProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        mLocalBooksDbOpenHelper = new LocalBooksDbOpenHelper(getContext());
        if (mLocalBooksDbOpenHelper != null) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
