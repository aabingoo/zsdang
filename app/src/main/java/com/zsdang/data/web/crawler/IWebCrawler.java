package com.zsdang.data.web.crawler;

import java.util.LinkedHashMap;

/**
 * Created by aabingoo on 2017/9/5.
 */

public interface IWebCrawler {

    interface CrawlerBookContentListener {
        void crawlerFinish(LinkedHashMap<String, String> chapters);
    }

    interface CrawlerBookChapterContentListener {
        void crawlerFinish();
    }

    void crawlerBookContent(String bookUrl, CrawlerBookContentListener listener);

    void crawlerBookChapterContent(String chapterUrl, CrawlerBookChapterContentListener listener);
}
