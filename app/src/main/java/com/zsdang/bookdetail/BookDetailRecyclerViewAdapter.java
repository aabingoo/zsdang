package com.zsdang.bookdetail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsdang.ImageLoader;
import com.zsdang.LogUtils;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.bookcatalog.BookCatalogActivity;
import com.zsdang.data.DataManager;
import com.zsdang.data.GlobalConstant;
import com.zsdang.data.web.server.DataServiceManager;

import java.util.List;

/**
 * Created by aabingoo on 2017/9/3.
 */

public class BookDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_CHECKED_BOOK = 0;
    private final int VIEW_TYPE_OTHER_WRITTEN_BOOKS = 1;
    private final int VIEW_TYPE_SIMILAR_BOOKS = 2;
    private final int VIEW_TYPE_CATEGORY = 3;
    private final int VIEW_TYPE_OTHER_WRITTEN_BOOKS_EXPAND = 4;

    private final int CATEGORY_DEFAULT_NUM = 2;

    private final int OTHER_WRITTEN_BOOKS_DEFAULT_SHOW_NUM = 2;
    private final int SIMILAR_BOOKS_DEFAULT_SHOW_NUM = 3;

    private Book mBook;
    private List<Book> mOtherWrittenBooks;
    private List<Book> mSimilarBooks;
    private boolean isOtherWrittenBooksExpand = false;

    @Override
    public int getItemViewType(int position) {
        if (position < getCheckedBookNum()) {
            return VIEW_TYPE_CHECKED_BOOK;
        } else if (position == getCheckedBookNum()) {
            return VIEW_TYPE_CATEGORY;
        } else if (position < getOtherWrittenBooksNum() + getCheckedBookNum() + 1) {
            return VIEW_TYPE_OTHER_WRITTEN_BOOKS;
        } else if (position == getOtherWrittenBooksExpandPos()) {
            return VIEW_TYPE_OTHER_WRITTEN_BOOKS_EXPAND;
        } else if (position == getOtherWrittenBooksExpandPos() + 1) {
            return VIEW_TYPE_CATEGORY;
        } else {
            return VIEW_TYPE_SIMILAR_BOOKS;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType == VIEW_TYPE_SIMILAR_BOOKS) {
                        return 1;
                    } else {
                        return 3;
                    }
                }
            });
        }
    }

    /**
     * Position is calculated start from 0;
     * @return
     */
    private int getOtherWrittenBooksExpandPos() {
        return getCheckedBookNum() + 1 + getOtherWrittenBooksNum();
    }

    private int getCheckedBookNum() {
        if (mBook != null) {
            return 1;
        }
        return 0;
    }

    private int getOtherWrittenBooksNum() {
        if (mOtherWrittenBooks != null) {
            if (!isOtherWrittenBooksExpand) {
                if (mOtherWrittenBooks.size() > OTHER_WRITTEN_BOOKS_DEFAULT_SHOW_NUM) {
                    return OTHER_WRITTEN_BOOKS_DEFAULT_SHOW_NUM;
                }
            }
            return mOtherWrittenBooks.size();
        }
        return 0;
    }

    private int getSimilarBooksNum() {
        if (mSimilarBooks != null) {
            if (mSimilarBooks.size() > SIMILAR_BOOKS_DEFAULT_SHOW_NUM) {
                return SIMILAR_BOOKS_DEFAULT_SHOW_NUM;
            }
            return mSimilarBooks.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_CHECKED_BOOK:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_bookdetail_body, parent, false);
                return new BookDetailItem(parent.getContext(), view);
            case VIEW_TYPE_OTHER_WRITTEN_BOOKS:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_book_item, parent, false);
                return new WrittenBookItem(parent.getContext(), view);
            case VIEW_TYPE_OTHER_WRITTEN_BOOKS_EXPAND:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_book_expand_item, parent, false);
                return new ExpandItem(view);
            case VIEW_TYPE_SIMILAR_BOOKS:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_book_simple_item, parent, false);
                return new SimilarBookItem(parent.getContext(), view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_category_title_item, parent, false);
                return new CategoryTitleItem(parent.getContext(), view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_CHECKED_BOOK:
                BookDetailItem bookDetailItem = (BookDetailItem) holder;
                bookDetailItem.updateView(mBook);
                break;
            case VIEW_TYPE_OTHER_WRITTEN_BOOKS:
                WrittenBookItem writtenBookItem = (WrittenBookItem) holder;
                int writtenBookPos = position - (getCheckedBookNum() + 1);
                writtenBookItem.updateView(mOtherWrittenBooks.get(writtenBookPos));
                break;
            case VIEW_TYPE_OTHER_WRITTEN_BOOKS_EXPAND:
                ExpandItem expandItem = (ExpandItem) holder;
                expandItem.bind(position);
                break;
            case VIEW_TYPE_SIMILAR_BOOKS:
                SimilarBookItem similarBookItem = (SimilarBookItem) holder;
                int similarBookPos = position - (getOtherWrittenBooksExpandPos() + 2);
                similarBookItem.updateView(mSimilarBooks.get(similarBookPos));
                break;
            default:
//                CategoryTitleItem categoryTitleItem = (CategoryTitleItem) holder;
//                categoryTitleItem.updateView(mSimilarBooks);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return getCheckedBookNum() + getOtherWrittenBooksNum() + getSimilarBooksNum() + 3;
    }

    public void notifyDataSetChanged(Book book,
                                     List<Book> otherWrittenBooksBooks,
                                     List<Book> similarBooks) {
        if (book != null) {
            mBook = book;
            mOtherWrittenBooks = otherWrittenBooksBooks;
            mSimilarBooks = similarBooks;
            notifyDataSetChanged();
        }
    }

    private class BookDetailItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private ImageView bookCover;
        private TextView bookName;
        private TextView bookAuthor;
        private TextView bookDesc;
        private TextView bookLatestChapter;
        private TextView bookCatalog;
        private Button addToBookshelf;

        public BookDetailItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = itemView.findViewById(R.id.book_cover);
            bookName = itemView.findViewById(R.id.book_name);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookDesc = itemView.findViewById(R.id.book_desc);
            bookLatestChapter = itemView.findViewById(R.id.book_latest_chapter);
            bookCatalog = itemView.findViewById(R.id.book_catalog);
            addToBookshelf = itemView.findViewById(R.id.add_to_bookshelf_btn);
        }

        public void updateView(final Book book) {
            if (book != null) {
                String imgUrl = String.format(DataServiceManager.HOST_BOOK_COVER, book.getImg());
                Glide.with(inContext).load(imgUrl).into(bookCover);
                bookName.setText(book.getName());
                bookAuthor.setText(book.getAuthor());
                bookDesc.setText(book.getDesc());
                bookLatestChapter.setText(book.getLaestChapterName());
                bookCatalog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(inContext, BookCatalogActivity.class);
                        intent.putExtra(GlobalConstant.EXTRA_BOOK, book);
                        inContext.startActivity(intent);
                    }
                });

                addToBookshelf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataManager dataManager = new DataManager(inContext);
                        dataManager.addToBookshelf(book);
                    }
                });
            }
        }
    }

    private class CategoryTitleItem extends RecyclerView.ViewHolder {
        private Context inContext;

        public CategoryTitleItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
        }

    }

    private class WrittenBookItem extends RecyclerView.ViewHolder {
        private Context inContext;

        private ImageView bookCover;
        private TextView bookName;

        public WrittenBookItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = itemView.findViewById(R.id.book_cover);
            bookName = itemView.findViewById(R.id.book_name);
        }

        public void updateView(Book book) {
            if (book != null) {
                String imgUrl = String.format(DataServiceManager.HOST_BOOK_COVER, book.getImg());
                ImageLoader.loadImgInto(inContext, imgUrl, bookCover);

                bookName.setText(book.getName());
            }
        }

    }

    private class SimilarBookItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private ImageView bookCover;
        private TextView bookName;

        public SimilarBookItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = itemView.findViewById(R.id.book_cover);
            bookName = itemView.findViewById(R.id.book_name);
        }

        public void updateView(Book book) {
            if (book != null) {
                String imgUrl = String.format(DataServiceManager.HOST_BOOK_COVER, book.getImg());
                ImageLoader.loadImgInto(inContext, imgUrl, bookCover);

                bookName.setText(book.getName());
            }
        }
    }

    private class ExpandItem extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView navicationName;
        // It always be the position before expanded.
        private int itemPos = -1;

        public ExpandItem(View itemView) {
            super(itemView);
            navicationName = (TextView) itemView.findViewById(R.id.expand_item);
            navicationName.setOnClickListener(this);
        }

        public void bind(int position) {
            itemPos = position;
        }

        @Override
        public void onClick(View view) {
            if (itemPos != -1) {
                isOtherWrittenBooksExpand = !isOtherWrittenBooksExpand;
                if (isOtherWrittenBooksExpand) {
                    notifyItemRangeInserted(itemPos,
                            mOtherWrittenBooks.size() - OTHER_WRITTEN_BOOKS_DEFAULT_SHOW_NUM);
                    navicationName.setText("收起");
                } else {
                    notifyItemRangeRemoved(itemPos,
                            mOtherWrittenBooks.size() - OTHER_WRITTEN_BOOKS_DEFAULT_SHOW_NUM);
                    navicationName.setText("展开");
                }
            }
        }
    }
}
