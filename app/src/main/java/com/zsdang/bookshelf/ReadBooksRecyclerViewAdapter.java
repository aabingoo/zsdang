package com.zsdang.bookshelf;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsdang.ImageLoader;
import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;

import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class ReadBooksRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "ReadBooksRecyclerViewAdapter";

    /**
     * While there is no reading book, should return 1 to display no reading book.
     */
    private static final int DEFAULT_READING_ITEM_NUMBER = 1;

    /**
     * While there are no read books, should return 3 to display ways to add books to BS.
     */
    private static final int DEFAULT_READ_NULL_ITEM_NUMBER = 3;

    private static final int VIEW_TYPE_READING = 0;

    private static final int VIEW_TYPE_READ = 1;

    private static final int VIEW_TYPE_READING_NULL = 2;

    private static final int VIEW_TYPE_READ_NULL = 3;

    private List<Book> mReadBooks;

    private boolean mIsActionMode = false;

    public ReadBooksRecyclerViewAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        if (position == VIEW_TYPE_READING){
            if (hasReadBook() && mReadBooks.get(0).getIsReading()) {
                return VIEW_TYPE_READING;
            } else {
                return VIEW_TYPE_READING_NULL;
            }
        } else {
            if (hasReadBook()) {
                return VIEW_TYPE_READ;
            } else {
                return VIEW_TYPE_READ_NULL;
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType == VIEW_TYPE_READING || viewType == VIEW_TYPE_READING_NULL) {
                        return 3;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LogUtils.d(TAG, "onCreateViewHolder");
        View view = null;
        switch (viewType) {
            case VIEW_TYPE_READING:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_bookshelf_reading_item, parent, false);
                return new ReadingBookItem(parent.getContext(), view);

            case VIEW_TYPE_READ:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_bookshelf_read_item, parent, false);
                return new ReadBookItem(parent.getContext(), view);

            case VIEW_TYPE_READING_NULL:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_bookshelf_reading_null_item, parent, false);
                return new ReadingNullItem(view);

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_bookshelf_read_null_item, parent, false);
                return new ReadNullItem(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_READING:
                ReadingBookItem readingBookItem = (ReadingBookItem) holder;
                Book readingBook = mReadBooks.get(position);
                readingBookItem.bindView(readingBook);
                break;

            case VIEW_TYPE_READ:
                ReadBookItem readBookItem = (ReadBookItem) holder;
                int realPos = position;
                if (!mReadBooks.get(0).getIsReading()) {
                    realPos = position - 1;
                }
                Book readBook = mReadBooks.get(realPos);
                readBookItem.updateView(readBook);
                break;

            case VIEW_TYPE_READING_NULL:
                ReadingNullItem readingNullItem = (ReadingNullItem) holder;
                readingNullItem.bindView();
                break;

            case VIEW_TYPE_READ_NULL:
                ReadNullItem readNullItem = (ReadNullItem) holder;
                readNullItem.bindView(position - 1);
                break;
        }
    }

    private boolean hasReadBook() {
        if (mReadBooks != null && mReadBooks.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        if (hasReadBook()) {
            if (mReadBooks.get(0).getIsReading()) {
                return mReadBooks.size();
            }
            return DEFAULT_READING_ITEM_NUMBER + mReadBooks.size();
        } else {
            return DEFAULT_READING_ITEM_NUMBER + DEFAULT_READ_NULL_ITEM_NUMBER;
        }
    }

    public void notifyDataSetChanged(final List<Book> books) {
        if (books != null) {
            //LogUtils.d(TAG, "notifyDataSetChanged:" + books.size());
            mReadBooks = books;
            notifyDataSetChanged();
        }
    }

    public void notifyActionMode(boolean isActionMode) {
        mIsActionMode = isActionMode;
        notifyDataSetChanged();
    }

    public void notifySelectUpdate(int pos) {
        LogUtils.d(TAG, "notifySelectUpdate:" + pos);
        notifyItemChanged(pos);
    }

    private class ReadingBookItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private View inItemView;
        private ImageView bookCover;
        private TextView bookNameTv;
        private TextView latestChapterName;
        private TextView progressTv;

        public ReadingBookItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            inItemView = itemView;
            bookCover = (ImageView) itemView.findViewById(R.id.reading_book_cover);
            bookNameTv = (TextView) itemView.findViewById(R.id.reading_book_name);
            latestChapterName = (TextView) itemView.findViewById(R.id.reading_book_latest_chapter);
            progressTv = (TextView) itemView.findViewById(R.id.reading_book_progress);
        }

        public void bindView(Book book) {
            LogUtils.d(TAG, "bindView:" + book.getName());
            if (!TextUtils.isEmpty(book.getName())) {
                // book cover
                ImageLoader.loadImgInto(inContext, book.getImg(), bookCover);
                if (mIsActionMode) {
                    if (book.getSelect()) {
                        bookCover.setForeground(inContext.getDrawable(R.drawable.bg_book_cover_select));
                    } else {
                        bookCover.setForeground(inContext.getDrawable(R.drawable.bg_book_cover_no_select));
                    }
                } else {
                    bookCover.setForeground(null);
                }

                // Book name
                bookNameTv.setText(book.getName());

                // latest chapter
                String latestChapterStr = String.format(
                        inContext.getString(R.string.reading_book_latest_chapter),
                        book.getLatestChapterTitle());
                latestChapterName.setText(latestChapterStr);

                // Read progress
                progressTv.setText(Html.fromHtml(inContext.getString(R.string.reading_book_progress)));
            }
        }
    }

    private class ReadBookItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private ImageView bookCover;
        private TextView bookNameTv;
        private TextView latestChapterName;

        public ReadBookItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = (ImageView) itemView.findViewById(R.id.read_book_cover);
            bookNameTv = (TextView) itemView.findViewById(R.id.read_book_name);
            latestChapterName = (TextView) itemView.findViewById(R.id.read_book_latest_chapter);
        }

        public void updateView(Book book) {
            LogUtils.d(TAG, "updateView:" + book.getName());
            if (!TextUtils.isEmpty(book.getName())) {
                LogUtils.d(TAG, "img:" + book.getImg() + "  id:" + book.getId());
                // book cover
                ImageLoader.loadImgInto(inContext, book.getImg(), bookCover);
                if (mIsActionMode) {
                    if (book.getSelect()) {
                        bookCover.setForeground(inContext.getDrawable(R.drawable.bg_book_cover_select));
                    } else {
                        bookCover.setForeground(inContext.getDrawable(R.drawable.bg_book_cover_no_select));
                    }
                } else {
                    bookCover.setForeground(null);
                }

                bookNameTv.setText(book.getName());

                // latest chapter
                if (book.getLatestChapterIsRead()) {
                    latestChapterName.setTextColor(inContext.getColor(R.color.color_black_text));
                } else {
                    latestChapterName.setTextColor(Color.RED);
                }
                latestChapterName.setText( book.getLatestChapterTitle());
            }
        }
    }

    private class ReadingNullItem extends RecyclerView.ViewHolder {

        public ReadingNullItem(View itemView) {
            super(itemView);
        }

        public void bindView() {

        }

    }

    private class ReadNullItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private ImageView inImage;
        private TextView inName;
        private String[] inNames;


        public ReadNullItem(View itemView) {
            super(itemView);
            inImage = itemView.findViewById(R.id.bs_null_item_image);
            inName = itemView.findViewById(R.id.bs_null_item_name);
            inContext = itemView.getContext();
            inNames = inContext.getResources().getStringArray(R.array.bs_read_null_item_name);
        }

        public void bindView(int pos) {
            // item image
            TypedArray ar = inContext.getResources().obtainTypedArray(R.array.bs_read_null_item_drawable);
            inImage.setImageResource(ar.getResourceId(pos, 0));
            ar.recycle();

            // item name
            inName.setText(inNames[pos]);
        }

    }
}
