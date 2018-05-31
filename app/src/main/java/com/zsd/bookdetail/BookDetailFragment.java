package com.zsd.bookdetail;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsd.LogUtils;
import com.zsd.R;
import com.zsd.home.RecyclerViewItemTouchHandler;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class BookDetailFragment extends Fragment {

    private static final String TAG = "BookDetailFragment";

    private RecyclerView mBookDetailRv;
    private BookDetailRvAdapter mBookDetailRvAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);
        mBookDetailRv = rootView.findViewById(R.id.book_detail_rv);
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
                    }

                    @Override
                    public void onItemLongClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemLongClick:" + pos);
                    }
                }));
    }
}
