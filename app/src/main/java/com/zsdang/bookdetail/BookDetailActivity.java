package com.zsdang.bookdetail;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.data.GlobalConstant;

public class BookDetailActivity extends AppCompatActivity {

    private static final String TAG = "BookDetailActivity";

    private RecyclerView mBookDetailRv;
    private BookDetailRecyclerViewAdapter mBookDetailRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detial);

        Intent intent = getIntent();
        if (intent != null) {
            Book book = intent.getParcelableExtra(GlobalConstant.EXTRA_BOOK);
            BookDetailFragment bookDetailFragment = BookDetailFragment.newInstance(book);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager. beginTransaction();
            transaction.replace(R.id.book_detail_fragment_fl, bookDetailFragment).commit();
        }
    }
}
