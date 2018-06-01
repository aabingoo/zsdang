package com.zsdang.reading;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zsdang.HtmlCrawler;
import com.zsdang.LogUtils;
import com.zsdang.R;

/**
 * Created by BinyongSu on 2018/6/1.
 */

public class ReadingFragment extends Fragment {

    private static final String TAG = "ReadingFragment";

    private String[] mChapter;

    private TextView titleTv;
    private TextView bodyTv;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mChapter = bundle.getStringArray("chapter");
            LogUtils.d(TAG, "name:" + mChapter[0] + " url:" + mChapter[1]);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reading, container, false);
        titleTv = rootView.findViewById(R.id.title_tv);
        bodyTv = rootView.findViewById(R.id.body_tv);
        titleTv.setText(mChapter[0]);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bodyTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        HtmlCrawler htmlCrawler = new HtmlCrawler();
        htmlCrawler.crawlBody(mChapter[1],
                new HtmlCrawler.CrawlerListener() {
                    @Override
                    public void crawlerDone(final String content) {
                        bodyTv.post(new Runnable() {
                            @Override
                            public void run() {
                                bodyTv.setText(content);
                            }
                        });
                    }
                }
        );

    }
}
