package com.zsdang.reading;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zsdang.data.GlobalConstant;
import com.zsdang.data.web.DataRequestCallback;
import com.zsdang.data.web.crawler.HtmlCrawler;
import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.data.web.server.DataServiceManager;

import org.json.JSONObject;

/**
 * Created by BinyongSu on 2018/6/1.
 */

public class ReadingFragment extends Fragment {

    private static final String TAG = "ReadingFragment";

    private String[] mChapter;

    private TextView mBodyTv;
    private View mLoadingView;

    private String mBookId;
    private String mChapterId;

    private String mTitle;
    private String mContent;

    private Handler mHandler;

    public static ReadingFragment newInstance(String bookId, String chapterId) {
        ReadingFragment fragment = new ReadingFragment();
        Bundle args = new Bundle();
        args.putString(GlobalConstant.EXTRA_BOOK_ID, bookId);
        args.putString(GlobalConstant.EXTRA_CHAPTER_ID, chapterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get bookid and chapterid.
        Bundle bundle = getArguments();
        if (bundle != null) {
            mBookId = bundle.getString(GlobalConstant.EXTRA_BOOK_ID);
            mChapterId = bundle.getString(GlobalConstant.EXTRA_CHAPTER_ID);
        }

        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reading, container, false);
//        mLoadingView = rootView.findViewById(R.id.loading_pb);
//        mLoadingView.setVisibility(View.VISIBLE);
//        titleTv = rootView.findViewById(R.id.title_tv);
        mBodyTv = rootView.findViewById(R.id.body_tv);
//        titleTv.setText(mChapter[0]);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        bodyTv.setMovementMethod(ScrollingMovementMethod.getInstance());
//        HtmlCrawler htmlCrawler = new HtmlCrawler();
//        htmlCrawler.crawlBody(mChapter[1],
//                new HtmlCrawler.CrawlerListener() {
//                    @Override
//                    public void crawlerDone(final String content) {
//                        bodyTv.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mLoadingView.setVisibility(View.GONE);
//                                bodyTv.setText(content);
//                            }
//                        });
//                    }
//                }
//        );
        loadChapterContent();
    }

    private void loadChapterContent() {
        LogUtils.d(TAG, "loadChapterContent");

        if (TextUtils.isEmpty(mBookId) || TextUtils.isEmpty(mChapterId)) return;

        DataServiceManager dataServiceManager = new DataServiceManager();
        dataServiceManager.queryBookChapterContent(mBookId, mChapterId, new DataRequestCallback() {
            @Override
            public void onFailure() {
                mHandler.post(notofyLoadError);
            }

            @Override
            public void onSuccess(@NonNull String result) {
                try {
                    JSONObject pageJson = new JSONObject(result);
                    JSONObject bookJson = pageJson.getJSONObject("data");
                    mTitle = bookJson.getString("cname");
                    mContent = bookJson.getString("content");
                    mHandler.post(notofyAdapterRunnable);
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on queryBookstore.");
                }
            }
        });
    }

    private Runnable notofyAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mBodyTv.setText(mTitle + "\n\n" + mContent);
        }
    };

    private Runnable notofyLoadError = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getActivity(), "Load Failure", Toast.LENGTH_SHORT).show();
        }
    };
}
