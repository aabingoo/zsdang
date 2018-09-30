package com.zsdang.search;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView.LayoutParams;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.Utils;
import com.zsdang.beans.Book;

import java.util.List;

public class BookSearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, Toolbar.OnMenuItemClickListener,
        BookSearchFragment.OnTabClickListener {

    private final String TAG = "BookSearchActivity";

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private BookSearchFragment mBookSearchFragment;
    private BookSearchResultFragment mBookSearchResultFragment;

    private BookSearchActionHandler mBookSearchActionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBarColor(this, R.color.color_theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        initToolbar();

        mBookSearchFragment = BookSearchFragment.newInstance();
        mBookSearchResultFragment = BookSearchResultFragment.newInstance();
        mBookSearchActionHandler = new BookSearchActionHandler(this);

        openBookSearchFragment();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.search_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.search_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(this);

        // Init SearchView
        mSearchView = mToolbar.findViewById(R.id.searchview_item);
        // Background and LayoutParams
        mSearchView.setBackground(getDrawable(R.drawable.bg_searchview));
        int searchWidth = (int) getResources().getDimension(R.dimen.searchview_width);
        int searchHeight = (int) getResources().getDimension(R.dimen.searchview_height);
        LayoutParams params = new LayoutParams(searchWidth, searchHeight);
        params.gravity = Gravity.CENTER;
        mSearchView.setLayoutParams(params);

        // Expand default
        mSearchView.setIconifiedByDefault(false);

        // Hint
        mSearchView.setQueryHint("搜索书名或作者");

        // Set listener
        mSearchView.setOnQueryTextListener(this);

        // Text style
        TextView textView = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setTextSize(14);

        // Search icon
        ImageView searchIcon = mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        int searchIconSize = (int) (textView.getTextSize() * 1.25);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(searchIconSize, searchIconSize);
        lp.gravity = Gravity.CENTER;
        searchIcon.setLayoutParams(lp);
        searchIcon.setImageResource(R.drawable.ic_search);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.search_submit_item) {
            mBookSearchActionHandler.searchBooks(mSearchView.getQuery().toString(), 1);
        }
        return false;
    }

    public boolean onQueryTextSubmit(String keyword) {
        LogUtils.d(TAG, "onQueryTextSubmit:" + keyword);
        mBookSearchActionHandler.searchBooks(keyword, 1);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        LogUtils.d(TAG, "onQueryTextChange:" + s);
        if (TextUtils.isEmpty(s)) {
            openBookSearchFragment();
        }
        return false;
    }

    @Override
    public void onTabClick(String keyword) {
        mSearchView.setQuery(keyword, true);
    }

    private void openBookSearchFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_fl, mBookSearchFragment).commit();
    }

    public void openBookSearchResultFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_fl, mBookSearchResultFragment).commit();
    }

    public void updateSearchedBooks(List<Book> books) {
        if (mBookSearchResultFragment != null) {
            mBookSearchResultFragment.updateSearchedBooks(books);
        }
    }
}
