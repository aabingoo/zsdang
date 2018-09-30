package com.zsdang.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.zsdang.LogUtils;
import com.zsdang.beans.Book;
import com.zsdang.data.local.LocalBooksProvider;
import com.zsdang.data.web.DataRequestCallback;
import com.zsdang.data.web.server.DataServiceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_AUTHOR;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_CATEGORY;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_CATEGORY_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_FIRST_CHAPTER_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_DESCRIPTION;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_LATEST_CHAPTER_DATE;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_LATEST_CHAPTER_ID;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_LATEST_CHAPTER_ISREAD;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_LATEST_CHAPTER_TITLE;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_NAME;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_IMG_NAME;
import static com.zsdang.data.local.LocalBooksDbOpenHelper.BOOKS_COLUMN_STATUS;

/**
 * Created by BinyongSu on 2018/6/7.
 */

public class DataManager {

    private static final String TAG = "DataManager";

    private Context mContext;

    public interface QueryBooksCallback {
        void onFailure();
        void onSuccess(List<Book> books);
    }

    public DataManager(Context context) {
        mContext = context;
    }

    public List<Book> queryLocalReadBooks(ContentObserver contentObserver) {
        List<Book> books = new ArrayList<>();
        // Get cursor
        Cursor cursor = mContext.getContentResolver().query(
                LocalBooksProvider.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            // Register ContentObserver
            mContext.getContentResolver().registerContentObserver(
                    LocalBooksProvider.CONTENT_URI,
                    true,
                    contentObserver);
            // Get books.
            if (cursor.moveToFirst()) {
                do {
//                    LogUtils.d("Query", "BOOKS_COLUMN_ID:" + cursor.getInt(cursor.getColumnIndex(BOOKS_COLUMN_ID))
//                            + " BOOKS_COLUMN_NAME:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_NAME))
//                            + " BOOKS_COLUMN_AUTHOR:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_AUTHOR))
//                            + " BOOKS_COLUMN_IMG_NAME:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_IMG_NAME))
//                            + " BOOKS_COLUMN_DESCRIPTION:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_DESCRIPTION))
//                            + " BOOKS_COLUMN_LATEST_CHAPTER_TITLE:" + cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER_TITLE)));
                    Book book = new Book(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_AUTHOR)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_IMG_NAME)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_CATEGORY_ID)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_CATEGORY)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_FIRST_CHAPTER_ID)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER_ID)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER_TITLE)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER_DATE)),
                            cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_STATUS))
                    );
                    book.setLatestChapterTitle(cursor.getString(cursor.getColumnIndex(BOOKS_COLUMN_LATEST_CHAPTER_TITLE)));
                    books.add(book);

                } while (cursor.moveToNext());
            }
        }
        return books;
    }

    public void addToBookshelf(Book book) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BOOKS_COLUMN_ID, book.getId());
        contentValues.put(BOOKS_COLUMN_NAME, book.getName());
        contentValues.put(BOOKS_COLUMN_AUTHOR, book.getAuthor());
        contentValues.put(BOOKS_COLUMN_IMG_NAME, book.getImg());
        contentValues.put(BOOKS_COLUMN_DESCRIPTION, book.getDesc());
        contentValues.put(BOOKS_COLUMN_CATEGORY_ID, book.getCategoryId());
        contentValues.put(BOOKS_COLUMN_CATEGORY, book.getCategory());
        contentValues.put(BOOKS_COLUMN_FIRST_CHAPTER_ID, book.getFirstChapterId());
        contentValues.put(BOOKS_COLUMN_LATEST_CHAPTER_ID, book.getLatestChapterId());
        contentValues.put(BOOKS_COLUMN_LATEST_CHAPTER_TITLE, book.getLatestChapterTitle());
        contentValues.put(BOOKS_COLUMN_LATEST_CHAPTER_DATE, book.getLatestChapterDate());
//        contentValues.put(BOOKS_COLUMN_LATEST_CHAPTER_ISREAD, book.getLatestChapterDate());
        contentValues.put(BOOKS_COLUMN_STATUS, book.getStatus());

        Uri result = mContext.getContentResolver().insert(LocalBooksProvider.CONTENT_URI,contentValues);
        LogUtils.d(TAG, "addToBookshelf:" + result);
    }

    public int deleteFromBookshelf(List<Book> books) {
        String where = formWhere(books);
        return mContext.getContentResolver().delete(LocalBooksProvider.CONTENT_URI, where, null);
    }

    private String formWhere(List<Book> books) {
        String where = null;
        if (books != null) {
            if (books.size() > 1) {
                where = "id IN (";
                for (Book book: books) {
                    where += book.getId() + ",";
                }
                where = where.substring(0, where.length() - 1) + ")";
            } else {
                where = "id = " + books.get(0).getId();
            }
        }
        return where;
    }

    /**
     * Async
     * @param checkBook
     * @param callback
     */
    public void queryLatestChapter(final Book checkBook, final QueryBooksCallback callback) {
        DataServiceManager dataServiceManager = new DataServiceManager();
        dataServiceManager.queryBookDetial(checkBook.getId(), new DataRequestCallback() {
            @Override
            public void onFailure() {
                if (callback != null) {
                    callback.onFailure();
                }
            }

            @Override
            public void onSuccess(@NonNull String result) {
                try {
                    JSONObject pageJson = new JSONObject(result);
                    JSONObject bookJson = pageJson.getJSONObject("data");

                    List<Book> books = new ArrayList<>();
                    Book book = new Book(bookJson.getString("Id"),
                            bookJson.getString("Name"),
                            bookJson.getString("Author"),
                            bookJson.getString("Img"),
                            bookJson.getString("Desc"),
                            bookJson.getString("CId"),
                            bookJson.getString("CName"),
                            bookJson.getString("FirstChapterId"),
                            bookJson.getString("LastChapterId"),
                            bookJson.getString("LastChapter"),
                            bookJson.getString("LastTime"),
                            bookJson.getString("BookStatus"));
                    books.add(book);
                    if (callback != null) {
                        callback.onSuccess(books);
                    }
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on queryLatestChapter:" + e.toString());
                }
            }
        });
    }

    /**
     * Async
     * @param keyword
     * @param pageNum
     * @param callback
     */
    public void searchBooksByPage(String keyword, int pageNum, final QueryBooksCallback callback) {
        DataServiceManager dataServiceManager = new DataServiceManager();
        dataServiceManager.searchBooksByPage(keyword, pageNum, new DataRequestCallback() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(@NonNull String result) {
                LogUtils.d(TAG, "onSuccess:" + result);
                try {
                    JSONObject pageJson = new JSONObject(result);
                    if (callback != null) {
                        callback.onSuccess(getSearchedBooks(pageJson));
                    }
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on queryBookstore.");
                }
            }
        });
    }

    private List<Book> getSearchedBooks(JSONObject jsonObject) {
        List<Book> result = new ArrayList<>();
        try {
            JSONArray bookArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject bookJson = bookArray.getJSONObject(i);
                Book book = new Book(bookJson.getString("Id"),
                        bookJson.getString("Name"),
                        bookJson.getString("Author"),
                        bookJson.getString("Img"),
                        bookJson.getString("Desc"),
                        bookJson.getString("CName"),
                        bookJson.getString("LastChapterId"),
                        bookJson.getString("LastChapter"),
                        bookJson.getString("UpdateTime"),
                        bookJson.getString("BookStatus"));
                result.add(book);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "Exception on queryBookstore.");
        }
        return result;
    }

}
