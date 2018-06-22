package com.zsdang.data.web.server;

import com.zsdang.LogUtils;
import com.zsdang.beans.Book;
import com.zsdang.data.web.IDataRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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
    public static final String HTML_SUFFIX = ".html";
    public static final String BOOK_DETAIL = "info/";
    public static final String MAN_CHANNEL = "v4/biquge/man";

    private OkHttpClient mOkHttpClient;

    public DataServiceManager() {
        mOkHttpClient = new OkHttpClient();
    }

    public void queryLatestChapter(String bookId) {

    }

    public void queryBookshelf() {

    }

    public void queryBookDetial(String bookId, final IDataRequest callback) {
        LogUtils.d(TAG, "queryBookDetial");
        request(HOST + BOOK_DETAIL + bookId + HTML_SUFFIX, callback);
    }

    public void queryBookstore(final IDataRequest callback) {
        LogUtils.d(TAG, "queryBookstore");
        Request request = new Request.Builder()
                .url(HOST + MAN_CHANNEL)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d(TAG, "queryBookstore - onFailure");
                if (callback != null) {
                    callback.onFailure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // TODO
                    if (callback != null) {
                        callback.onFailure();
                    }
                    return;
                }
                LogUtils.d(TAG, "queryBookstore - onResponse:" + response.body().string());
                try {
                    JSONObject pageJson = new JSONObject(response.body().string());
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on queryBookstore.");
                    if (callback != null) {
                        callback.onFailure();
                    }
                }
            }
        });
    }

    public void request(String url, final IDataRequest callback) {
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
                if (!response.isSuccessful()) {
                    // TODO
                    if (callback != null) {
                        callback.onFailure();
                    }
                    return;
                }
                LogUtils.d(TAG, "request - onResponse:" + response.body().string());
                if (callback != null) {
                    callback.onSuccess();
                }



                try {
                    JSONObject pageJson = new JSONObject(response.body().string());
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on request.");
                }
            }
        });
    }
}
