package com.zsdang.bookshelf;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.bookdetail.BookDetailActivity;
import com.zsdang.data.local.LocalBooksProvider;

import java.util.ArrayList;
import java.util.List;

import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_AUTHOR;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_URL;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class BookShelfFragment extends Fragment {

    private static final String TAG = "BookShelfFragment";

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
        return rootView;

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
                        if (mBooks != null && pos < mBooks.size()) {
                            enterBookDetail(mBooks.get(pos));
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemLongClick:" + pos);
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
        contentValues.put(BOOKS_COLUMN_URL, "http://asdf.com" + i++);
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
        contentValues.put(BOOKS_COLUMN_URL, "http://asdf.com" + updateI++);
        getActivity().getContentResolver().update(LocalBooksProvider.CONTENT_URI, contentValues, BOOKS_COLUMN_ID + "=1", null);
    }

    private void enterBookDetail(Book book) {
        if (isAdded()) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, BookDetailActivity.class);
            intent.putExtra("book", book);
            activity.startActivity(intent);
        }
    }
}
