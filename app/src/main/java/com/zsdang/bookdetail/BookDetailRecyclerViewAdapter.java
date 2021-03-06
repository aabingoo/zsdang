package com.zsdang.bookdetail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zsdang.ImageLoader;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.bookcatalog.BookCatalogActivity;
import com.zsdang.data.DataManager;
import com.zsdang.data.GlobalConstant;
import com.zsdang.reading.ReadingActivity;
import com.zsdang.view.ExpandableTextView;
import com.zsdang.view.WarnDialogFragment;

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
    private int[] mCategorySrcList = {R.drawable.ic_bd_nvg_read, R.drawable.ic_bd_nvg_add_to_bs, R.drawable.ic_bd_nvg_catalog, R.drawable.ic_bd_nvg_cache};
    private int[] mCategoryTitleIds = {R.string.author_other_books, R.string.similar_books_recommend};

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
                        R.layout.view_bookdetail_checked_book_item, parent, false);
                return new BookDetailItem(parent.getContext(), view);
            case VIEW_TYPE_OTHER_WRITTEN_BOOKS:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_bookdetail_other_written_item, parent, false);
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
                CategoryTitleItem categoryTitleItem = (CategoryTitleItem) holder;
                categoryTitleItem.updateView(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mBook == null) return 0;
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

        private ExpandableTextView bookDesc;
        private TextView bookLatestChapter;

        private LinearLayout toolsNavigationLl;
        private int[] toolsNavigationImgArray;
        private String[] toolsNavigationTitleArray;

        public BookDetailItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookDesc = itemView.findViewById(R.id.book_desc);
            bookLatestChapter = itemView.findViewById(R.id.book_latest_chapter);
            toolsNavigationLl = itemView.findViewById(R.id.ll_tools_navigation);
            toolsNavigationTitleArray = inContext.getResources().getStringArray(R.array.bookdetail_tools_navigation_title_array);
        }

        public void updateView(final Book book) {
            if (book != null) {

                // tools navigation
                for (int i = 0; i < toolsNavigationLl.getChildCount(); i++) {
                    View childView = toolsNavigationLl.getChildAt(i);
                    ImageView toolImg = childView.findViewById(R.id.navigation_img);
                    toolImg.setBackground(inContext.getDrawable(mCategorySrcList[i]));
                    TextView toolTitle = childView.findViewById(R.id.navigation_title);
                    toolTitle.setText(toolsNavigationTitleArray[i]);

                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.start_read:
                                    break;
                                case R.id.add_to_bookshelf:
                                    DataManager dataManager = new DataManager(inContext);
                                    dataManager.addToBookshelf(book);
                                    break;
                                case R.id.complete_catalog:
                                    Intent intent = new Intent(inContext, BookCatalogActivity.class);
                                    intent.putExtra(GlobalConstant.EXTRA_BOOK, book);
                                    inContext.startActivity(intent);
                                    break;
                                case R.id.cache_book:

                                    break;
                            }
                        }
                    });
                }

                // book desc
                bookDesc.setText(book.getDesc());

                // book latest chapter
                bookLatestChapter.setText(book.getLatestChapterTitle() + " >");
                bookLatestChapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(inContext, ReadingActivity.class);
                        intent.putExtra(GlobalConstant.EXTRA_BOOK_ID, book.getId());
                        intent.putExtra(GlobalConstant.EXTRA_CHAPTER_ID, book.getLatestChapterId());
                        inContext.startActivity(intent);
                    }
                });
            }
        }
    }

    private class CategoryTitleItem extends RecyclerView.ViewHolder {
        private Context inContext;
        private TextView title;
        private TextView more;

        public CategoryTitleItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            title = itemView.findViewById(R.id.title);
            more = itemView.findViewById(R.id.more);
        }

        public void updateView(int pos) {
            Log.d("suby1", "pos:" + pos);
            if (pos == 1) {
                // title
                String titleStr = String.format(inContext.getString(mCategoryTitleIds[0]), mBook.getAuthor());
                title.setText(titleStr);

                more.setVisibility(View.GONE);
            } else {
                title.setText(inContext.getString(mCategoryTitleIds[1]));

                more.setVisibility(View.VISIBLE);
            }
        }

    }

    private class WrittenBookItem extends RecyclerView.ViewHolder {
        private Context inContext;

        private ImageView bookCover;
        private TextView bookName;
        private TextView bookLatestChapter;

        public WrittenBookItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = itemView.findViewById(R.id.book_cover);
            bookName = itemView.findViewById(R.id.book_name);
            bookLatestChapter = itemView.findViewById(R.id.book_latest_chapter);
        }

        public void updateView(Book book) {
            if (book != null) {
                ImageLoader.loadImgInto(inContext, book.getImg(), bookCover);

                bookName.setText(book.getName());

                bookLatestChapter.setText("最新章节\n" + book.getLatestChapterTitle());
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
                ImageLoader.loadImgInto(inContext, book.getImg(), bookCover);

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
                    navicationName.setText(view.getContext().getString(R.string.collapsed_string));
                } else {
                    notifyItemRangeRemoved(itemPos,
                            mOtherWrittenBooks.size() - OTHER_WRITTEN_BOOKS_DEFAULT_SHOW_NUM);
                    navicationName.setText(view.getContext().getString(R.string.expend_string));
                }
            }
        }
    }
}
