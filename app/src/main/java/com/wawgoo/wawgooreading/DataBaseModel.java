package com.wawgoo.wawgooreading;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.wawgoo.wawgooreading.beans.Book;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by aabingoo on 2017/9/3.
 */

public class DataBaseModel {

    private Context mContext;

    public interface QueryBookDetailListener {
        void queryDone(LinkedHashMap<String, String> chapters);
    }

    public DataBaseModel(@NonNull Context context) {
        mContext = context;
    }

    /**
     * Query for describtion and chapters
     * @param listener
     */
    public void queryWebBookDetail(final Book book, final QueryBookDetailListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = book.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    IWebCrawler crawler = new HtmlCrawler();
                    crawler.crawlerBookContent(url, new IWebCrawler.CrawlerBookContentListener() {
                        @Override
                        public void crawlerFinish(LinkedHashMap<String, String> chapters) {
                            if (listener != null) {
                                listener.queryDone(chapters);
                            }
                        }
                    });

                }
            }
        }).start();

        if (listener != null) {
        }
    }
}
