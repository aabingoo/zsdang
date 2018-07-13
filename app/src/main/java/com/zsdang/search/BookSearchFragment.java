package com.zsdang.search;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.data.web.server.DataServiceManager;

;

public class BookSearchFragment extends Fragment implements SearchView.OnQueryTextListener,
        Toolbar.OnMenuItemClickListener{

    private Toolbar mToolbar;
    private SearchView mSearchView;

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
        mToolbar = rootView.findViewById(R.id.search_toolbar);
        mSearchView = rootView.findViewById(R.id.sv_book_search);
        mSearchView.setOnQueryTextListener(this);
//        mToolbar.inflateMenu(R.menu.search_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(this);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
//        getActivity().setActionBar(mToolbar);
//        setHasOptionsMenu(true);
        return rootView;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.search_toolbar_menu, menu);
//    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.d("suby1", "id:" + item.getTitle());
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        LogUtils.d("suby1", "onQueryTextSubmit:" + s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        LogUtils.d("suby1", "onQueryTextChange:" + s);
        return false;
    }

    private void startSearchBooks(String keyword) {
        DataServiceManager dataServiceManager = new DataServiceManager();
    }
}
