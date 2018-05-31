package com.zsd;

import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashMap;

/**
 * Created by aabingoo on 2017/8/12.
 */

public class HtmlCrawler implements IWebCrawler {

    private final String TAG = HtmlCrawler.class.getSimpleName();

    private final String BR_REPLACE_STRING = "$br$";

    public interface CrawlerListener {
        void crawlerDone(String content);
    }

    @Override
    public void crawlerBookContent(final String bookUrl, final CrawlerBookContentListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(bookUrl)) {
                    try {
                        LinkedHashMap<String, String> chapters = new LinkedHashMap<>();
                        Log.d("CrawlerTest", "crawlerBookContent() - Start to crawl uri:" + bookUrl);
                        // Get Html content.
                        Document document = Jsoup.connect(bookUrl).get();
                        document = Jsoup.parse(document.toString());

                        // Find tag of chapter list.
                        Element divElement = document.select("div#list").first();
                        Elements chapterElements = divElement.child(0).select("dl > dd");
                        LogUtils.d(TAG, "Chapter size:" + chapterElements.size());

                        // Loop the chapter list.
                        for (Element chapterElement: chapterElements) {
                            Element aElement = chapterElement.child(0);
                            String hrefStr = aElement.attr("href");
                            String chapterStr = aElement.text();
                            chapters.put(chapterStr, hrefStr);
                        }

                        if (listener != null) {
                            listener.crawlerFinish(chapters);
                        }

                    } catch (Exception e) {
                        LogUtils.e(TAG, e.toString());
                    }
                }
            }
        }).start();
    }

    @Override
    public void crawlerBookChapterContent(String chapterUrl, CrawlerBookChapterContentListener listener) {

    }

    public void crawler(final String uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtils.d(TAG, "Start to crawl uri:" + uri);
                    // Get Html content.
                    Document document = Jsoup.connect(uri).get();
                    document = Jsoup.parse(document.toString());

                    // Find tag of chapter list.
                    Element divElement = document.select("div#list").first();
                    Elements chapterElements = divElement.child(0).select("dl > dd");
                    LogUtils.d(TAG, "Chapter size:" + chapterElements.size());

                    // Loop the chapter list.
                    for (Element chapterElement: chapterElements) {
                        Element aElement = chapterElement.child(0);
                        String hrefStr = aElement.attr("href");
                        String chapterStr = aElement.text();
                        LogUtils.d(TAG, "Chapter href:" + hrefStr + "   content:" + chapterStr);
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, e.toString());
                }
            }
        }).start();
    }

    public void crawlBody(final String uri, final CrawlerListener crawlerListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtils.d(TAG, "Start to crawl uri:" + uri);
                    String str = "asdfa<br/>sdfa";
                    // Get Html content and handle <br/>.
                    Document document = Jsoup.connect(uri).get();
                    document = Jsoup.parse(handleContentHtml(document.html()));

                    // Find content tag and get content.
                    Element divElement = document.select("div#content").first();
                    String content = handleContent(divElement.text());

                    // Callback
                    crawlerListener.crawlerDone(content);
                } catch (Exception e) {
                    LogUtils.e(TAG, e.toString());
                }
            }
        }).start();
    }

    private String handleContentHtml(String html) {
        // Replace <br/>
        html = html.replace("<br>", BR_REPLACE_STRING);
        html = html.replace("<br/>", BR_REPLACE_STRING);
        html = html.replace("<br />", BR_REPLACE_STRING);
        return html;
    }

    private String handleContent(String content) {
        // Replace with "\n"
        return content.replace(BR_REPLACE_STRING, "\n");
    }

}
