package com.zsdang.bookshelf;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.bookdetail.BookDetailActivity;
import com.zsdang.data.GlobalConstant;
import com.zsdang.data.local.LocalBooksProvider;
import com.zsdang.search.BookSearchActivity;

import java.util.ArrayList;
import java.util.List;

import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_AUTHOR;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_IMG_NAME;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class BookShelfFragment extends Fragment implements Toolbar.OnMenuItemClickListener,
        ActionMode.Callback {

    private static final String TAG = "BookShelfFragment";

    private Toolbar mToolbar;
    private RecyclerView mReadBooksRecyclerView;
    private ReadBooksRecyclerViewAdapter mReadBooksRecyclerViewAdapter;
    private List<Book> mBooks;

    // Callbacks for ReadBooksLoader
    private LoaderManager.LoaderCallbacks<List<Book>> mReadBooksCallbacks;

    public static BookShelfFragment newInstance() {
        BookShelfFragment fragment = new BookShelfFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBooks = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_shelf, container, false);
        mReadBooksRecyclerView = rootView.findViewById(R.id.read_books_rv);
//        mToolbar = rootView.findViewById(R.id.toolbar);
//        mToolbar.setFitsSystemWindows(true);
//        mToolbar.inflateMenu(R.menu.toolbar_menu);
//        mToolbar.setOnMenuItemClickListener(this);
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
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_item:
                Intent intent = new Intent(getActivity(), BookSearchActivity.class);
                startActivity(intent);
                break;
        }

        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LogUtils.d(TAG, "onActivityCreated");

        final Activity activity = getActivity();

        // Init RecyclerView and set its adapter
        mReadBooksRecyclerView.setLayoutManager(new GridLayoutManager(activity, 3));//new LinearLayoutManager(activity));
        mReadBooksRecyclerViewAdapter = new ReadBooksRecyclerViewAdapter();
        mReadBooksRecyclerView.setAdapter(mReadBooksRecyclerViewAdapter);
        mReadBooksRecyclerView.addOnItemTouchListener(new RecyclerViewItemTouchHandler(activity,
                new RecyclerViewItemTouchHandler.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemClick:" + pos);
                        if (mBooks != null && 0 < pos && pos < mBooks.size()) {
                            enterBookDetail(mBooks.get(pos));
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemLongClick:" + pos);
                        if (mBooks != null && 0 < pos && pos < mBooks.size()) {
                            enterActionMode();
                        }
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

                mBooks = books;
                mReadBooksRecyclerViewAdapter.notifyDataSetChanged(mBooks);
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
        insert();
    }

    public int i = 20;

    public void insert() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_NAME, "fanren" + i);
        contentValues.put(BOOKS_COLUMN_AUTHOR, "妄语" + i);
        contentValues.put(BOOKS_COLUMN_IMG_NAME, "http://asdf.com" + i++);
        getActivity().getContentResolver().insert(LocalBooksProvider.CONTENT_URI,contentValues);
    }
    public int deleteJ = 50;
    public void delete() {
        getActivity().getContentResolver().delete(LocalBooksProvider.CONTENT_URI, BOOKS_COLUMN_ID + "=" + deleteJ--, null);
    }
    public int updateI = 100;
    public void update() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_NAME, "fanren" + updateI);
        contentValues.put(BOOKS_COLUMN_AUTHOR, "妄语" + updateI);
        contentValues.put(BOOKS_COLUMN_IMG_NAME, "http://asdf.com" + updateI++);
        getActivity().getContentResolver().update(LocalBooksProvider.CONTENT_URI, contentValues, BOOKS_COLUMN_ID + "=1", null);
    }

    private void enterBookDetail(Book book) {
        if (isAdded()) {
            LogUtils.d(TAG, "bookid:" + book.getId());
            Activity activity = getActivity();
            Intent intent = new Intent(activity, BookDetailActivity.class);
            intent.putExtra(GlobalConstant.EXTRA_BOOK, book);
            activity.startActivity(intent);
        }
    }

    private void enterActionMode() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.startActionMode(this);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }
}
