package com.zsdang.bookdetail;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsdang.DataBaseModel;
import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.home.RecyclerViewItemTouchHandler;
import com.zsdang.reading.ReadingActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class BookDetailFragment extends Fragment {

    private static final String TAG = "BookDetailFragment";

    private Book mBook;
    private List<String> mChapterNameList = new ArrayList<>();
    private RecyclerView mBookDetailRv;
    private BookDetailRvAdapter mBookDetailRvAdapter;
    private View mLoadingView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mBook = bundle.getParcelable("book");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);
        mBookDetailRv = rootView.findViewById(R.id.book_detail_rv);
        mLoadingView = rootView.findViewById(R.id.loading_pb);
        mLoadingView.setVisibility(View.VISIBLE);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        // Init RecyclerView and set its adapter
        mBookDetailRv.setLayoutManager(new LinearLayoutManager(activity));
        mBookDetailRvAdapter = new BookDetailRvAdapter();
        mBookDetailRv.setAdapter(mBookDetailRvAdapter);
        mBookDetailRv.addOnItemTouchListener(new RecyclerViewItemTouchHandler(activity,
                new RecyclerViewItemTouchHandler.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemClick:" + pos);
                        if (mBook != null && mChapterNameList != null && pos < mChapterNameList.size()) {
                            String chapterName = mChapterNameList.get(pos);
                            String chapterUrl = mBook.getChapters().get(chapterName);
                            String bookUrl = mBook.getUrl();
                            if (!bookUrl.endsWith("/")) {
                                bookUrl += "/";
                            }
                            int last = chapterUrl.lastIndexOf("/");
                            if (last >= 0) {
                                chapterUrl = chapterUrl.substring(last + 1);
                            }
                            chapterUrl = bookUrl + chapterUrl;
                            LogUtils.d(TAG, "book url:" + bookUrl + "name:" + chapterName + " url:" + chapterUrl);
                            enterReadingScreen(chapterName, chapterUrl);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemLongClick:" + pos);
                    }
                }));

        queryBookDetail();

    }

    private void enterReadingScreen(String chapterName, String chapterUrl) {
        if (isAdded()) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, ReadingActivity.class);
            intent.putExtra("chapter", new String[]{chapterName, chapterUrl});
            activity.startActivity(intent);
        }
    }

    public void queryBookDetail() {
        DataBaseModel model = new DataBaseModel(getActivity());
        model.queryWebBookDetail(mBook, new DataBaseModel.QueryBookDetailListener() {
            @Override
            public void queryDone(final LinkedHashMap<String, String> chapters) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d("CrawlerTest", "queryWebBookDetail() chapters size:" + chapters.size());
                        if (chapters != null && chapters.size() > 0) {
                            mBook.setChapters(chapters);
                            mChapterNameList.clear();
                            mChapterNameList.addAll(chapters.keySet());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLoadingView.setVisibility(View.GONE);
                                    mBookDetailRvAdapter.notifyDataSetChanged(mBook);
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
