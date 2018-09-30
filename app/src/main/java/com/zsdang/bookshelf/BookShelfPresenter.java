package com.zsdang.bookshelf;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.bookdetail.BookDetailActivity;
import com.zsdang.data.DataManager;
import com.zsdang.data.GlobalConstant;
import com.zsdang.search.BookSearchActivity;
import com.zsdang.view.WarnDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BookShelfPresenter {

    private final String TAG = "BookShelfPresenter";

    private static final int MESSAGE_UPDATE_LATEST_CHAPTER = 1;

    private BookShelfFragment mfragment;

    // 不是线程安全的
    private List<Book> mBookShelfBooks;

    private DataManager mDataManager;

    private Handler mHandler;

    /**
     * True: init or refresh BookShelfFragment.
     */
    private boolean mCheckUpdateAfterLoaded = true;


    // Record the select books number at ActionMode
    private int mSelectCnt = 0;
    private boolean mIsActionMode = false;

    public BookShelfPresenter(@NonNull BookShelfFragment fragment) {
        mfragment = fragment;
        mHandler = new Handler();//new BookShelfHandler();
        mDataManager = new DataManager(mfragment.getContext());
    }

    private final int BOOK_SHELF_LOADER_ID = 2;

    // Callbacks for ReadBooksLoader
    private LoaderManager.LoaderCallbacks<List<Book>> mReadBooksCallbacks
            = new LoaderManager.LoaderCallbacks<List<Book>>() {
        @Override
        public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
            LogUtils.d(TAG, "onCreateLoader");
            return new ReadBooksLoader(mfragment.getContext());
        }

        @Override
        public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
            LogUtils.d(TAG, "onLoadFinished - book size:" + books.size() + "  mCheckUpdateAfterLoaded:" + mCheckUpdateAfterLoaded);

            mBookShelfBooks = books;

            // Notify RecyclerView to update
            mfragment.updateAfterLoadBooks(mBookShelfBooks);

            // Check if need to check update after load finished.
            if (mCheckUpdateAfterLoaded) {
                // Set to false in case check repeatly.
                mCheckUpdateAfterLoaded = false;

                // Remove all message
                mHandler.removeCallbacks(updateLatestChapterRun);

                for (final Book book: mBookShelfBooks) {
                    mDataManager.queryLatestChapter(book, new DataManager.QueryBooksCallback() {
                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onSuccess(List<Book> books) {
                            if (books != null && books.size() == 1) {
                                Book returnBook = books.get(0);
                                LogUtils.d(TAG, "returnBook.getLatestChapterTitle():" + returnBook.getLatestChapterTitle());
                                if (book != null &&
                                        !book.getLatestChapterId().equals(returnBook.getLatestChapterId())) {
                                    book.setLatestChapterId(returnBook.getLatestChapterId());
                                    book.setLatestChapterTitle(returnBook.getLatestChapterTitle());
                                    book.setLatestChapterDate(returnBook.getLatestChapterDate());
                                    book.setLatestChapterIsRead(true);
                                    mHandler.post(updateLatestChapterRun);
                                }
                            }
                        }
                    });
                }
            }

        }

        @Override
        public void onLoaderReset(Loader<List<Book>> loader) {
            LogUtils.d(TAG, "onLoaderReset");
        }
    };

    private Runnable updateLatestChapterRun = new Runnable() {
        @Override
        public void run() {
            mfragment.updateAfterLoadBooks(mBookShelfBooks);
        }
    };

    /**
     * It isn't under UI thread.
     */
    private DataManager.QueryBooksCallback mQueryBooksCallback = new DataManager.QueryBooksCallback() {
        @Override
        public void onFailure() {

        }

        @Override
        public void onSuccess(List<Book> books) {

        }
    };

    public void startLoadBookShelfBooks() {
        mfragment.getLoaderManager().restartLoader(BOOK_SHELF_LOADER_ID, null, mReadBooksCallbacks);
    }

    public void handleItemClick(int pos) {
        int realPos = pos;
        if (mBookShelfBooks != null && mBookShelfBooks.size() > 0) {
            if (!mBookShelfBooks.get(0).getIsReading()) {
                realPos -= 1;
            }
            if (realPos >= 0) {
                Book clickItem = mBookShelfBooks.get(realPos);
                if (!mIsActionMode) {
                    if (!clickItem.getIsReading()) {
                        gotoBookDetailActivity(clickItem);
                    }
                } else if (mIsActionMode) {
                    clickItem.setSelect(!clickItem.getSelect());
                    if (clickItem.getSelect()) {
                        mSelectCnt++;
                    } else {
                        mSelectCnt--;
                    }
                    mfragment.updateAfterClickItem(mSelectCnt, pos);
                }
            }
        }
    }

    public void handleItemLongPress(int pos) {
        int realPos = pos;
        if (!mIsActionMode && mBookShelfBooks != null && mBookShelfBooks.size() > 0) {
            if (!mBookShelfBooks.get(0).getIsReading()) {
                realPos -= 1;
            }
            if (realPos >= 0) {
                mBookShelfBooks.get(realPos).setSelect(true);
                mSelectCnt++;
                mfragment.updateAfterLongPressItem(mSelectCnt);
                enterActionMode();
            }
        }
    }

    public void deleteBooksFromBookShelf() {
        if (mBookShelfBooks != null) {
            final WarnDialogFragment dialogFragment = WarnDialogFragment.newInstance();
            dialogFragment.showDialog(mfragment.getFragmentManager(),
                    mfragment.getString(R.string.delete_warning),
                    new WarnDialogFragment.CancelOkCallback() {
                        @Override
                        public void onCancel() {
                            dialogFragment.dismiss();
                        }

                        @Override
                        public void onOk() {
                            performDeleteBooksFromBookShelf();
                            dialogFragment.dismiss();
                        }
                    });
        }
    }

    /**
     * Perform the delete operation after click OK
     */
    private void performDeleteBooksFromBookShelf() {
        List<Book> deleteBooks = new ArrayList<>();
        for (Book book: mBookShelfBooks) {
            if (book.getSelect()) {
                deleteBooks.add(book);
            }
        }

        // Remove form DB
        int deleteRowCnt = mDataManager.deleteFromBookshelf(deleteBooks);

        if (deleteRowCnt > 0) {
            mBookShelfBooks.removeAll(deleteBooks);
            mSelectCnt -= deleteRowCnt;

            // Notify to update view
            mfragment.updateAfterDeleteBooks();
            if (mSelectCnt == 0) {
                mfragment.finishActionMode();
            }

            // Toast
            String deleteSuccessStr = String.format(mfragment.getString(R.string.delete_success), deleteRowCnt);
            Toast.makeText(mfragment.getContext(), deleteSuccessStr, Toast.LENGTH_SHORT).show();
        }
    }

    public void handleSearch() {
        Intent intent = new Intent(mfragment.getContext(), BookSearchActivity.class);
        mfragment.startActivity(intent);
    }

    public void gotoBookDetailActivity(Book clickBook) {
        Intent intent = new Intent(mfragment.getContext(), BookDetailActivity.class);
        intent.putExtra(GlobalConstant.EXTRA_BOOK, clickBook);
        mfragment.startActivity(intent);
    }

    public void enterActionMode() {
        mIsActionMode = true;
        mfragment.getActivity().startActionMode(mfragment);
    }

    public void prepareOutActionMode() {
        mIsActionMode = false;
        handleClearAllSelect();
    }

    public void handleClearAllSelect() {
        mSelectCnt = 0;
        for (Book book: mBookShelfBooks) {
            book.setSelect(false);
        }
    }

    public void handleSelectAll() {
        mSelectCnt = mBookShelfBooks.size();
        for (Book book: mBookShelfBooks) {
            book.setSelect(true);
        }
    }
}
