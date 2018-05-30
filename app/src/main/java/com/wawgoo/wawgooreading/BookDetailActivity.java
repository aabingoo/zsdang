package com.wawgoo.wawgooreading;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wawgoo.wawgooreading.beans.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private RecyclerView mBookDetailRv;
    private BookDetailRvAdapter mBookDetailRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detial);
        Log.d("RvTest", "BookDetailActivity - onCreate");

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Book book = new Book("fanren", "http://www.qu.la/book/401/");
        queryBookDetail(book);
    }

    public void initView() {
        mBookDetailRv = (RecyclerView) findViewById(R.id.book_detail_rv);
        mBookDetailRv.setLayoutManager(new LinearLayoutManager(this));
        mBookDetailRvAdapter = new BookDetailRvAdapter();
        mBookDetailRv.setAdapter(mBookDetailRvAdapter);
    }

    public void queryBookDetail(final Book book) {
        DataBaseModel model = new DataBaseModel(this);
        model.queryWebBookDetail(book, new DataBaseModel.QueryBookDetailListener() {
            @Override
            public void queryDone(final LinkedHashMap<String, String> chapters) {
                BookDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("CrawlerTest", "queryWebBookDetail() chapters size:" + chapters.size());
                        if (chapters != null && chapters.size() > 0) {
                            book.setChapters(chapters);
                            BookDetailActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mBookDetailRvAdapter.notifyDataSetChanged(book);
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
