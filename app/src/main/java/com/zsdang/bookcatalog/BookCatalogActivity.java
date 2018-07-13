package com.zsdang.bookcatalog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.data.GlobalConstant;
import com.zsdang.data.web.DataRequestCallback;
import com.zsdang.data.web.server.DataServiceManager;
import com.zsdang.reading.ReadingActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookCatalogActivity extends Activity
        implements AdapterView.OnItemClickListener {

    private static final String TAG = "BookCatalogActivity";

    private Book mBook;
    private ListView mCatalogListView;
    private BookCatalogListViewAdapter mBookCatalogListViewAdapter;
    private Handler mHandler;
    private List<String> mChapterIdList;
    private List<String> mChapterTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_catalog);

        Intent intent = getIntent();
        if (intent != null) {
            mBook = intent.getParcelableExtra(GlobalConstant.EXTRA_BOOK);
            if (mBook != null) {
                mChapterIdList = mBook.getChapterIdList();
                mChapterTitleList = mBook.getChapterTitleList();
            }
        }

        mHandler = new Handler();

        mCatalogListView = (ListView) findViewById(R.id.book_catalog);
        mBookCatalogListViewAdapter = new BookCatalogListViewAdapter();
        mCatalogListView.setAdapter(mBookCatalogListViewAdapter);
        mCatalogListView.setOnItemClickListener(this);

        loadCatalog();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String chapterId = mChapterIdList.get(mChapterIdList.size() - position - 1);
        Intent intent = new Intent(this, ReadingActivity.class);
        intent.putExtra(GlobalConstant.EXTRA_BOOK_ID, mBook.getId());
        intent.putExtra(GlobalConstant.EXTRA_CHAPTER_ID, chapterId);
        startActivity(intent);
    }

    private void loadCatalog() {
        LogUtils.d(TAG, "loadCatalog");
        if (mBook == null || TextUtils.isEmpty(mBook.getId())) return;
        if (mChapterIdList != null && !mChapterIdList.isEmpty()) return;

        mChapterIdList = new ArrayList<>();
        mChapterTitleList = new ArrayList<>();
        DataServiceManager dataServiceManager = new DataServiceManager();
        dataServiceManager.queryBookCatalog(mBook.getId(), new DataRequestCallback() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(@NonNull String result) {
                try {
                    JSONObject pageJson = new JSONObject(result);
                    JSONObject bookJson = pageJson.getJSONObject("data");
                    getChaptersMap(bookJson, mChapterIdList, mChapterTitleList);

                    mHandler.post(notofyAdapterRunnable);
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on queryBookCatalog.");
                }
            }
        });
    }

    private Runnable notofyAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mBookCatalogListViewAdapter.notifyChange(mChapterTitleList);
        }
    };

    private void getChaptersMap(JSONObject bookJson, List<String> idList, List<String> titleList) {
        HashMap<String, String> result = new HashMap<>();
        try {
            JSONArray volumeListJson = bookJson.getJSONArray("list");
            for (int i = 0; i < volumeListJson.length(); i++) {
                JSONObject volumeJson = volumeListJson.getJSONObject(i);
                JSONArray chapterListJson = volumeJson.getJSONArray("list");
                for (int j = 0; j < chapterListJson.length(); j++) {
                    JSONObject chapterJson = chapterListJson.getJSONObject(j);
                    if (chapterJson.getInt("hasContent") == 1) {
                        idList.add(chapterJson.getString("id"));
                        titleList.add(chapterJson.getString("name"));
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "Exception on queryBookCatalog.");
        }
    }
}
