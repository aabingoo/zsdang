package com.zsdang.bookshelf;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;

import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class BookShelfFragment extends Fragment implements Toolbar.OnMenuItemClickListener,
        ActionMode.Callback {

    private static final String TAG = "BookShelfFragment";

    private RecyclerView mReadBooksRecyclerView;
    private ReadBooksRecyclerViewAdapter mReadBooksRecyclerViewAdapter;

    // Show the select item number at ActionMode
    private TextView mSelectDeleteTextView;

    // Handle the user action from BookShelfFragment
    private BookShelfActionHandler mActionHandler;

    private ActionMode mActionMode;

    public static BookShelfFragment newInstance() {
        BookShelfFragment fragment = new BookShelfFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionHandler = new BookShelfActionHandler(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_shelf, container, false);

        // RecuclerView
        mReadBooksRecyclerView = rootView.findViewById(R.id.read_books_rv);

        // Delete item at ActionMode
        mSelectDeleteTextView = rootView.findViewById(R.id.tv_action_mode_delete_select);
        mSelectDeleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionHandler.deleteBooksFromBookShelf();
            }
        });

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
                        mActionHandler.handleItemClick(pos);
                    }

                    @Override
                    public void onItemLongClick(View view, int pos) {
                        LogUtils.d(TAG, "onItemLongClick:" + pos);
                        mActionHandler.handleItemLongPress(pos);
                    }
                }));

        mActionHandler.startLoadBookShelfBooks();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_item:
                mActionHandler.handleSearch();
                break;
        }

        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
        mReadBooksRecyclerViewAdapter.notifyActionMode(true);
        mActionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_all:
                mActionHandler.handleSelectAll();
                mReadBooksRecyclerViewAdapter.notifyDataSetChanged();
                break;
            case R.id.no_select_all:
                mActionHandler.handleClearAllSelect();
                updateSelectDeleteTextView(0);
                mReadBooksRecyclerViewAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        updateSelectDeleteTextView(0);
        mActionHandler.prepareOutActionMode();
        mReadBooksRecyclerViewAdapter.notifyActionMode(false);
    }

    /**
     * Update RecyclerView after books are loaded.
     * @param books
     */
    public void updateAfterLoadBooks(List<Book> books) {
        mReadBooksRecyclerViewAdapter.notifyDataSetChanged(books);
    }

    private void updateSelectDeleteTextView(int selectCnt) {
        if (selectCnt > 0) {
            mSelectDeleteTextView.setTextColor(getActivity().getColor(R.color.color_red_select));
            mSelectDeleteTextView.setEnabled(true);
        } else {
            mSelectDeleteTextView.setTextColor(getActivity().getColor(R.color.color_red_no_select));
            mSelectDeleteTextView.setEnabled(false);
        }
        String text = String.format(getString(R.string.action_mode_delete_select), selectCnt);
        mSelectDeleteTextView.setText(text);

    }

    public void updateAfterDeleteBooks() {
        updateSelectDeleteTextView(0);
    }

    public void updateAfterClickItem(int selectCnt, int clickPos) {
        updateSelectDeleteTextView(selectCnt);
        mReadBooksRecyclerViewAdapter.notifySelectUpdate(clickPos);
    }

    public void updateAfterLongPressItem(int selectCnt) {
        updateSelectDeleteTextView(selectCnt);
    }

    public void finishActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }
}
