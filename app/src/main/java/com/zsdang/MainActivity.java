package com.zsdang;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zsdang.db.LocalBooksProvider;
import com.zsdang.home.HomeFragment;
import com.zsdang.test.TestActivity;

import static com.zsdang.db.LocalBooksDbOpenHelper.BOOKS_COLUMN_AUTHOR;
import static com.zsdang.db.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.db.LocalBooksDbOpenHelper.BOOKS_COLUMN_URL;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager. beginTransaction();
        transaction.replace(R.id.fragment_fl, homeFragment).commit();

//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);


//        LocalBooksDbOpenHelper helper = new LocalBooksDbOpenHelper(this);
//        helper.deleteTable(helper.getWritableDatabase());
//        helper.onCreate(helper.getWritableDatabase());



    }
}
