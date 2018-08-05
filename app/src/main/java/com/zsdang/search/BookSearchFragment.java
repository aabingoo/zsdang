package com.zsdang.search;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.data.GlobalConstant;


public class BookSearchFragment extends Fragment {


    public BookSearchFragment() {
        // Required empty public constructor
    }


    public static BookSearchFragment newInstance() {
        BookSearchFragment fragment = new BookSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_search, container, false);
        return rootView;
    }
}
