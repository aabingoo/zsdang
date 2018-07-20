package com.zsdang.bookstore;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.data.web.DataRequestCallback;
import com.zsdang.data.web.server.DataServiceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookStoreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "BookStoreFragment";

    private Toolbar mToolbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mBookstoreRecyclerView;

    private BookstoreRecyclerViewAdapter mBookstoreRecyclerViewAdapter;

    private Handler mHandler;

    private List<Book> mRecommendBooks = new ArrayList<>();
    private List<Book> mNewBooks = new ArrayList<>();
    private List<Book> mSerializeBooks = new ArrayList<>();
    private List<Book> mEndBooks = new ArrayList<>();

    public static BookStoreFragment newInstance() {
        BookStoreFragment fragment = new BookStoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_store, container, false);
//        mToolbar = rootView.findViewById(R.id.toolbar);
//        mToolbar.setFitsSystemWindows(true);
//        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mSwipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mBookstoreRecyclerView = rootView.findViewById(R.id.bookstore_rv);
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.d(TAG, "onHiddenChanged:" + hidden);
//        if (hidden) {
//            mToolbar.setFitsSystemWindows(false);
//        } else {
//            mToolbar.setFitsSystemWindows(true);
//        }
//        mToolbar.requestApplyInsets();
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
//        mToolbar.setFitsSystemWindows(false);
//        mToolbar.requestApplyInsets();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        // Init RecyclerView and set its adapter
        mBookstoreRecyclerView.setLayoutManager(new GridLayoutManager(activity, 12));
        mBookstoreRecyclerViewAdapter = new BookstoreRecyclerViewAdapter();
        mBookstoreRecyclerView.setAdapter(mBookstoreRecyclerViewAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        loadBookstoreData();
    }

    private void loadBookstoreData() {
        mSwipeRefreshLayout.setRefreshing(true);
        DataServiceManager dataServiceManager = new DataServiceManager();
        dataServiceManager.queryBookstore(new DataRequestCallback() {
            @Override
            public void onFailure() {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(@NonNull String result) {
                LogUtils.d(TAG, "onSuccess");
                try {
                    JSONObject pageJson = new JSONObject(result);
                    JSONArray categoryList = pageJson.getJSONArray("data");
                    for (int i = 0; i < categoryList.length(); i++) {
                        JSONObject categoryItem = categoryList.getJSONObject(i);
                        String categoryTitle = categoryItem.getString("Category");
                        if (categoryTitle.equals("重磅推荐")) {
                            JSONArray bookList = categoryItem.getJSONArray("Books");
                            LogUtils.d(TAG, "category 重磅推荐:" + bookList.length());
                            mRecommendBooks = getBooks(bookList);
                        } else if (categoryTitle.equals("火热新书")) {
                            JSONArray bookList = categoryItem.getJSONArray("Books");
                            LogUtils.d(TAG, "category 火热新书:" + bookList.length());
                            mNewBooks = getBooks(bookList);
                        } else if (categoryTitle.equals("热门连载")) {
                            JSONArray bookList = categoryItem.getJSONArray("Books");
                            LogUtils.d(TAG, "category 热门连载:" + bookList.length());
                            mSerializeBooks = getBooks(bookList);
                        } else if (categoryTitle.equals("完本精选")) {
                            JSONArray bookList = categoryItem.getJSONArray("Books");
                            LogUtils.d(TAG, "category 完本精选:" + bookList.length());
                            mEndBooks = getBooks(bookList);
                        }
                    }
                    mHandler.post(notofyAdapterRunnable);
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on queryBookstore.");
                }
            }
        });
    }

    public List<Book> getBooks(JSONArray jsonArray) {
        List<Book> result = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookItem = jsonArray.getJSONObject(i);
                Book book = new Book(bookItem.getString("Id"),
                        bookItem.getString("Name"),
                        bookItem.getString("Author"),
                        bookItem.getString("Img"),
                        bookItem.getString("Desc"));
                result.add(book);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "Exception on queryBookstore.");
        }
        return result;
    }

    private Runnable notofyAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mBookstoreRecyclerViewAdapter.notifyChange(mRecommendBooks, mNewBooks, mSerializeBooks, mEndBooks);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onRefresh() {
        LogUtils.d(TAG, "onRefresh");
        loadBookstoreData();
    }
}
