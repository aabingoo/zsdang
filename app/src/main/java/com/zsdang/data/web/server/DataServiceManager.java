package com.zsdang.data.web.server;

import android.text.TextUtils;

import com.zsdang.LogUtils;
import com.zsdang.data.web.DataRequestCallback;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by BinyongSu on 2018/6/20.
 */

public class DataServiceManager {
    public static final String TAG = "DataServiceManager";

    public static final String HOST = "http://quapp.1122dh.com/";
    public static final String HOST_CHANNEL = HOST + "%s.html";
    public static final String MAN_CHANNEL = "v4/biquge/man";
    public static final String HOST_DETAIL = HOST + "info/%s.html";
    public static final String HOST_CATALOG = HOST + "book/%s/";
    public static final String HOST_CHAPTER = HOST + "book/%s/%s.html";
    public static final String HOST_BOOK_COVER = HOST + "BookFiles/BookImages/%s";
    public static final String HOST_BOOK_SEARCH = HOST + "/Search.aspx?isSearchPage=1&key=%s&page=%d";


    private OkHttpClient mOkHttpClient;

    public DataServiceManager() {
        mOkHttpClient = new OkHttpClient();
    }

    public void queryLatestChapter(String bookId) {

    }

    public void queryBookshelf() {

    }

    public void queryBookDetial(String bookId, final DataRequestCallback callback) {
        LogUtils.d(TAG, "queryBookDetial");
        String url = String.format(HOST_DETAIL,  bookId);
        request(url, callback);
    }

    public void queryBookstore(final DataRequestCallback callback) {
        // TODO: check the network status
        LogUtils.d(TAG, "queryBookstore");
        String url = String.format(HOST_CHANNEL, MAN_CHANNEL);
        request(url, callback);
    }

    public void queryBookCatalog(String bookId, final DataRequestCallback callback) {
        LogUtils.d(TAG, "queryBookCatalog");
        String url = String.format(HOST_CATALOG, bookId);
        request(url, callback);
    }

    public void queryBookChapterContent(String bookId, String chapterId, DataRequestCallback callback) {
        LogUtils.d(TAG, "queryBookChapterContent");
        String url = String.format(HOST_CHAPTER, bookId, chapterId);
        request(url, callback);
    }

    public void searchBooksByPage(String keyword, int pageNum, final DataRequestCallback callback) {
        LogUtils.d(TAG, "searchBooksByPage");
        String url = String.format(HOST_BOOK_SEARCH, keyword, pageNum);
        request(url, callback);
    }

    private void request(String url, final DataRequestCallback callback) {
        LogUtils.d(TAG, "request");
        Request request = new Request.Builder()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d(TAG, "request - onFailure");
                if (callback != null) {
                    callback.onFailure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.d(TAG, "onResponse:" + response.message() + " " + response.isSuccessful());
                if (!response.isSuccessful()) {
                    // TODO
                    if (callback != null) {
                        callback.onFailure();
                    }
                    return;
                }
                boolean status = false;
                String result = response.body().string();
                try {
                    if (!TextUtils.isEmpty(result)) {
                        JSONObject pageJson = new JSONObject(result);
                        if (pageJson.getInt("status") == 1
                                && pageJson.getString("info").equals("success")) {
                            status = true;
                        }
                    }
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on query.");
                }
                if (callback != null) {
                    if (status) {
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure();
                    }
                }
            }
        });
    }
}
