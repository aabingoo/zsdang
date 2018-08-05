package com.zsdang.search;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.data.GlobalConstant;
import com.zsdang.data.web.DataRequestCallback;
import com.zsdang.data.web.server.DataServiceManager;

;import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookSearchResultFragment extends Fragment {

    private final String TAG = "BookSearchResultFragment";

    private String mKeyword;
    private Handler mHandler;
    private List<Book> mSearchedBooks;
    private RecyclerView mBookSearchRecyclerView;
    private BookSearchResultRecyclerViewAdpater mBookSearchResultRecyclerViewAdpater;


    public BookSearchResultFragment() {
        // Required empty public constructor
    }

    public static BookSearchResultFragment newInstance(String keyword) {
        BookSearchResultFragment fragment = new BookSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(GlobalConstant.EXTRA_KEYWORD, keyword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mKeyword = bundle.getString(GlobalConstant.EXTRA_KEYWORD);
        }

        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_search_result, container, false);

        mBookSearchRecyclerView = rootView.findViewById(R.id.rv_book_search);
        mBookSearchResultRecyclerViewAdpater = new BookSearchResultRecyclerViewAdpater();
        mBookSearchRecyclerView.setAdapter(mBookSearchResultRecyclerViewAdpater);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        // Init RecyclerView and set its adapter
        mBookSearchRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        startSearchBooksByPage(mKeyword, 1);
    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.search_toolbar_menu, menu);
//    }


//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        LogUtils.d(TAG, "id:" + item.getTitle());
//        return false;
//    }



    private void startSearchBooksByPage(String keyword, int pageNum) {
        DataServiceManager dataServiceManager = new DataServiceManager();
        dataServiceManager.searchBooksByPage(keyword, pageNum, new DataRequestCallback() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(@NonNull String result) {
                LogUtils.d(TAG, "onSuccess:" + result);
                try {
                    JSONObject pageJson = new JSONObject(result);
                    mSearchedBooks = getSearchedBooks(pageJson);

                    mHandler.post(notifyAdapterRunnable);
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on queryBookstore.");
                }
            }
        });
    }

    private List<Book> getSearchedBooks(JSONObject jsonObject) {
        List<Book> result = new ArrayList<>();
        try {
            JSONArray bookArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject bookJson = bookArray.getJSONObject(i);
                Book book = new Book(bookJson.getString("Id"),
                        bookJson.getString("Name"),
                        bookJson.getString("Author"),
                        bookJson.getString("Img"),
                        bookJson.getString("Desc"),
                        bookJson.getString("LastChapterId"),
                        bookJson.getString("LastChapter"));
                result.add(book);
            }

            mHandler.post(notifyAdapterRunnable);
        } catch (Exception e) {
            LogUtils.d(TAG, "Exception on queryBookstore.");
        }
        return result;
    }

    private Runnable notifyAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            mBookSearchResultRecyclerViewAdpater.notifyDataSetChanged(mSearchedBooks);
        }
    };
}
