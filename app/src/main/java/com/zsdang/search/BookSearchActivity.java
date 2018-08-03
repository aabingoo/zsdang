package com.zsdang.search;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zsdang.R;
import com.zsdang.Utils;

public class BookSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBarTransparent(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        BookSearchResultFragment bookSearchResultFragment = BookSearchResultFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_fl, bookSearchResultFragment).commit();
    }
}
