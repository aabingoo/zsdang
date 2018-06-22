package com.zsdang.test;


import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.data.local.LocalBooksDbOpenHelper;
import com.zsdang.data.local.LocalBooksProvider;
import com.zsdang.data.web.server.DataServiceManager;

import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_AUTHOR;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_INTRODUCTION;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_LATEST_CHAPTER;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    private static final String TAG = "TestFragment";

    private Button insertBtn;
    private Button queryBtn;
    private Button deleteBtn;
    private Button updateBtn;
    private Button updateDBBtn;

    private String[] mProjection = new String[] {
            BOOKS_COLUMN_ID,
            BOOKS_COLUMN_NAME,
            BOOKS_COLUMN_AUTHOR,
            BOOKS_COLUMN_URL,
            BOOKS_COLUMN_INTRODUCTION,
            BOOKS_COLUMN_LATEST_CHAPTER};

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.test_activity, container, false);
        init(view);
        return view;
    }


    public void init(View rootView) {
        insertBtn = (Button) rootView.findViewById(R.id.insert);
        queryBtn = (Button) rootView.findViewById(R.id.query);
        deleteBtn = (Button) rootView.findViewById(R.id.delete);
        updateBtn = (Button) rootView.findViewById(R.id.update);
        updateDBBtn = (Button) rootView.findViewById(R.id.updateDB);

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query(null, null);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        updateDBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalBooksDbOpenHelper helper = new LocalBooksDbOpenHelper(getActivity());
                helper.onUpgrade(helper.getWritableDatabase(), 0, 1);
            }
        });

        ((Button) rootView.findViewById(R.id.queryBookstore)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataServiceManager manager = new DataServiceManager();
//                manager.queryBookstore();
            }
        });
    }

    public int i = 0;

    public void insert() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_NAME, "fanren" + i);
        contentValues.put(BOOKS_COLUMN_AUTHOR, "妄语" + i);
        contentValues.put(BOOKS_COLUMN_URL, "http://asdf.com" + i++);
        getActivity().getContentResolver().insert(LocalBooksProvider.CONTENT_URI,contentValues);
    }

    public void query(String selection, String[] selectionArgs) {
        Cursor cursor = getActivity().getContentResolver().query(LocalBooksProvider.CONTENT_URI,
                mProjection, selection, selectionArgs, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    LogUtils.d("Query", "BOOKS_COLUMN_ID:" + cursor.getInt(cursor.getColumnIndex(BOOKS_COLUMN_ID))
                            + " BOOKS_COLUMN_NAME:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_NAME))
                            + " BOOKS_COLUMN_AUTHOR:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_AUTHOR))
                            + " BOOKS_COLUMN_URL:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_URL))
                            + " BOOKS_COLUMN_INTRODUCTION:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_INTRODUCTION))
                            + " BOOKS_COLUMN_LATEST_CHAPTER:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER)));
                } while (cursor.moveToNext());
            }
        }
    }
    public int deleteJ = 50;
    public void delete() {
        getActivity().getContentResolver().delete(LocalBooksProvider.CONTENT_URI, BOOKS_COLUMN_ID + "=" + deleteJ--, null);
    }

}
