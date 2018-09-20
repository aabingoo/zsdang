package com.zsdang.test;


import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.data.local.LocalBooksProvider;
import com.zsdang.view.BookPager;

import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_AUTHOR;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_DESCRIPTION;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_LATEST_CHAPTER_TITLE;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_IMG_NAME;

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

    private BookPager bookPager;

    private String[] mProjection = new String[] {
            BOOKS_COLUMN_ID,
            BOOKS_COLUMN_NAME,
            BOOKS_COLUMN_AUTHOR,
            BOOKS_COLUMN_IMG_NAME,
            BOOKS_COLUMN_DESCRIPTION,
            BOOKS_COLUMN_LATEST_CHAPTER_TITLE};

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.test_activity, container, false);
//        init(view);
        bookPager = view.findViewById(R.id.bookpager);
        return view;
    }


//    public void init(View rootView) {
//        insertBtn = (Button) rootView.findViewById(R.id.insert);
//        queryBtn = (Button) rootView.findViewById(R.id.query);
//        deleteBtn = (Button) rootView.findViewById(R.id.delete);
//        updateBtn = (Button) rootView.findViewById(R.id.update);
//        updateDBBtn = (Button) rootView.findViewById(R.id.updateDB);
//
//        insertBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onTabClick(View v) {
//                insert();
//            }
//        });
//
//        queryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onTabClick(View v) {
//                query(null, null);
//            }
//        });
//
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onTabClick(View v) {
//                delete();
//            }
//        });
//
//        updateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onTabClick(View v) {
//
//            }
//        });
//
//        updateDBBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onTabClick(View v) {
//                LocalBooksDbOpenHelper helper = new LocalBooksDbOpenHelper(getActivity());
//                helper.onUpgrade(helper.getWritableDatabase(), 0, 1);
//            }
//        });
//
//        ((Button) rootView.findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onTabClick(View v) {
////                Intent intent = new Intent(getActivity(), BookSearchActivity.class);
////                startActivity(intent);
//
//                Log.d("suby1", "level:" + mLevel);
//                setRoloLevel("3");
//            }
//        });
//
////        tv.setText("默认我们是设置成收起状态的，在收起状态时，我们设置当前行数为最大可显示行数，并且按钮显示出来默认我们是设且出来默认我们是设且出来默认我们是设且按钮显示出来");
////        tv.setText("默认");
////        ExpandableTextView t2v = (ExpandableTextView) rootView.findViewById(R.id.expand_tv2);
////        t2v.setText("aaa");
//    }

    public int i = 0;

    public void insert() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_ID, i);
        contentValues.put(BOOKS_COLUMN_NAME, "fanren" + i);
        contentValues.put(BOOKS_COLUMN_AUTHOR, "妄语" + i);
        contentValues.put(BOOKS_COLUMN_IMG_NAME, "http://asdf.com" + i++);
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
                            + " BOOKS_COLUMN_IMG_NAME:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_IMG_NAME))
                            + " BOOKS_COLUMN_DESCRIPTION:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_DESCRIPTION))
                            + " BOOKS_COLUMN_LATEST_CHAPTER_TITLE:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER_TITLE)));
                } while (cursor.moveToNext());
            }
        }
    }
    public int deleteJ = 50;
    public void delete() {
        getActivity().getContentResolver().delete(LocalBooksProvider.CONTENT_URI, null, null);
    }

    private String mLevel = "2";
    public void setRoloLevel(String level) {
        mLevel = level;
    }

}
