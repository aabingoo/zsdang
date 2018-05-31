package com.zsd.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zsd.LogUtils;
import com.zsd.R;
import com.zsd.beans.Book;
import com.zsd.bookdetail.BookDetailActivity;

import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private RecyclerView mReadBooksRecyclerView;
    private ReadBooksRecyclerViewAdapter mReadBooksRecyclerViewAdapter;
    private TextView textView;
    private Button mContinueReadingBtn;

    // Callbacks for ReadBooksLoader
    private LoaderManager.LoaderCallbacks<List<Book>> mReadBooksCallbacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mReadBooksRecyclerView = rootView.findViewById(R.id.read_books_rv);
        mContinueReadingBtn = rootView.findViewById(R.id.continue_reading_btn);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        mContinueReadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueReading();
            }
        });

        // Init RecyclerView and set its adapter
        mReadBooksRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mReadBooksRecyclerViewAdapter = new ReadBooksRecyclerViewAdapter();
        mReadBooksRecyclerView.setAdapter(mReadBooksRecyclerViewAdapter);
        mReadBooksRecyclerView.addOnItemTouchListener(new RecyclerViewItemTouchHandler(activity,
                new RecyclerViewItemTouchHandler.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemClick:" + pos);
                        enterBookDetail();
                    }

                    @Override
                    public void onItemLongClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemLongClick:" + pos);
                    }
                }));

        // Init callbacks
        mReadBooksCallbacks = new LoaderManager.LoaderCallbacks<List<Book>>() {
            @Override
            public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
                LogUtils.d(TAG, "onCreateLoader");
                return new ReadBooksLoader(activity);
            }

            @Override
            public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
                LogUtils.d(TAG, "onLoadFinished:" + books);
                if (!isAdded()) {
                    return;
                }

                mReadBooksRecyclerViewAdapter.notifyDataSetChanged(books);
            }

            @Override
            public void onLoaderReset(Loader<List<Book>> loader) {
                LogUtils.d(TAG, "onLoaderReset");
            }
        };

        loadReadBooks();
    }

    private void loadReadBooks() {
        if (isAdded() && mReadBooksCallbacks != null) {
            getLoaderManager().restartLoader(2, null, mReadBooksCallbacks);
        }
    }

    private void continueReading() {
        LogUtils.d(TAG, "onLoaderReset");

    }

    private void enterBookDetail() {
        if (isAdded()) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, BookDetailActivity.class);
            activity.startActivity(intent);
        }
    }
}
