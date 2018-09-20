package com.zsdang.search;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsdang.R;
import com.zsdang.beans.Book;

import java.util.List;

;

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

    public static BookSearchResultFragment newInstance() {
        BookSearchResultFragment fragment = new BookSearchResultFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    public void updateSearchedBooks(List<Book> books) {
        mBookSearchResultRecyclerViewAdpater.notifyDataSetChanged(books);
    }
}
