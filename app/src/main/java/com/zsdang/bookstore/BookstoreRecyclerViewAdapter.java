package com.zsdang.bookstore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zsdang.ImageLoader;
import com.zsdang.R;
import com.zsdang.beans.Book;
import com.zsdang.bookdetail.BookDetailActivity;
import com.zsdang.data.GlobalConstant;

import java.util.List;

/**
 * Created by BinyongSu on 2018/6/22.
 */

public class BookstoreRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int NAVICATION_ITEM_CNT = 10;
    public static final int BOOK_CNT_IN_CATEGORY = 5;

    private static final int VIEW_TYPE_BANNER = 0; //暂不处理
    private static final int VIEW_TYPE_NAVIGATION = 1;
    private static final int VIEW_TYPE_CATEGORY = 2;
    private static final int VIEW_TYPE_HORIZONTAL = 3;
    private static final int VIEW_TYPE_VETICAL = 4;

    String[] mNavicionTitles = {"玄幻奇幻", "武侠仙侠", "都市情言", "历史军事", "科幻灵异", "网游竞技", "女生频道", "同人小说"};
    String[] mCategoryTitles = {"重磅推荐", "火热新书", "热门连载", "完本精选"};
    private int[] mNavicationImgIds = {R.drawable.ic_bs_nav_xuanhuan, R.drawable.ic_bs_nav_wuxia, R.drawable.ic_bs_nav_city, R.drawable.ic_bs_nav_history,
                            R.drawable.ic_bs_nav_ghost, R.drawable.ic_bs_nav_game, R.drawable.ic_bs_nav_girl, R.drawable.ic_bs_nav_humen};
    private List<Book> mRecommendBooks;
    private List<Book> mNewBooks;
    private List<Book> mSerializeBooks;
    private List<Book> mEndBooks;

    @Override
    public int getItemViewType(int position) {
        int recommendCategoryPos = getNavicationCnt() + 1;
        int newCategoryPos = recommendCategoryPos + getRecommendBooksCntWithCategoryItem();
        int serializeCategoryPos = newCategoryPos + getNewBooksCntWithCategoryItem();
        int endCategoryPos = serializeCategoryPos + getSerializeBooksCntWithCategoryItem();

        if (position == VIEW_TYPE_BANNER) {
            return VIEW_TYPE_BANNER;
        } else if (position <= getNavicationCnt()){
            return VIEW_TYPE_NAVIGATION;
        } else if (recommendCategoryPos < position && position < newCategoryPos) {
            int modsize = mRecommendBooks.size() % 3;
            return (position - recommendCategoryPos <= modsize) ? VIEW_TYPE_HORIZONTAL : VIEW_TYPE_VETICAL;
        } else if (newCategoryPos < position && position < serializeCategoryPos) {
            int modsize = mNewBooks.size() % 3;
            return (position - newCategoryPos <= modsize) ? VIEW_TYPE_HORIZONTAL : VIEW_TYPE_VETICAL;
        } else if (serializeCategoryPos < position && position < endCategoryPos) {
            int modsize = mSerializeBooks.size() % 3;
            return (position - serializeCategoryPos <= modsize) ? VIEW_TYPE_HORIZONTAL : VIEW_TYPE_VETICAL;
        }  else if (endCategoryPos < position) {
            int modsize = mEndBooks.size() % 3;
            return (position - endCategoryPos <= modsize) ? VIEW_TYPE_HORIZONTAL : VIEW_TYPE_VETICAL;
        } else {
            return VIEW_TYPE_CATEGORY;
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
                    switch (viewType) {
                        case VIEW_TYPE_NAVIGATION:
                            return 3;

                        case VIEW_TYPE_VETICAL:
                            return 4;

                        default:
                            return 12;
                    }
                }
            });
        }
    }

    private int getNavicationCnt() {
        return mNavicionTitles.length;
    }

    private int getRecommendBooksCntWithCategoryItem() {
        if (mRecommendBooks != null) {
            int size = mRecommendBooks.size();
            if (size > 0) {
                return size + 1;
            }
        }
        return 0;
    }

    private int getNewBooksCntWithCategoryItem() {
        if (mNewBooks != null) {
            int size = mNewBooks.size();
            if (size > 0) {
                return size + 1;
            }
        }
        return 0;
    }

    private int getSerializeBooksCntWithCategoryItem() {
        if (mSerializeBooks != null) {
            int size = mSerializeBooks.size();
            if (size > 0) {
                return size + 1;
            }
        }
        return 0;
    }

    private int getEndBooksCnWithCategoryItem() {
        if (mEndBooks != null) {
            int size = mEndBooks.size();
            if (size > 0) {
                return size + 1;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_BANNER:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_bookstore_banner_item, parent, false);
                return new BannerItem(view);

            case VIEW_TYPE_NAVIGATION:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_bookstore_navigation_item, parent, false);
                return new NavicationItem(view);

            case VIEW_TYPE_HORIZONTAL:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_book_item, parent, false);
                return new BookHorizontalItem(parent.getContext(), view);

            case VIEW_TYPE_VETICAL:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_book_simple_item, parent, false);
                return new BookVeticalItem(parent.getContext(), view);

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.view_category_title_item, parent, false);
                return new CategoryItem(parent.getContext(), view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_TYPE_BANNER:
                BannerItem bannerItem = (BannerItem) holder;
                break;

            case VIEW_TYPE_NAVIGATION:
                NavicationItem navicationItem = (NavicationItem) holder;
                navicationItem.updateView(position - 1);
                break;

            case VIEW_TYPE_HORIZONTAL:
                BookHorizontalItem bookHorizontalItem = (BookHorizontalItem) holder;
                bookHorizontalItem.updateView(getBookByPos(position));
                break;

            case VIEW_TYPE_VETICAL:
                BookVeticalItem bookVeticalItem = (BookVeticalItem) holder;
                bookVeticalItem.updateView(getBookByPos(position));
                break;

            default:
                CategoryItem categoryItem = (CategoryItem) holder;
                categoryItem.updateView(mCategoryTitles[getCategoryIndex(position)]);
        }

    }

    private int getCategoryIndex(int position) {
        int recommendCategoryPos = getNavicationCnt() + 1;
        int newCategoryPos = recommendCategoryPos + getRecommendBooksCntWithCategoryItem();
        int serializeCategoryPos = newCategoryPos + getNewBooksCntWithCategoryItem();

        if (position == recommendCategoryPos) {
            return 0;
        } else if (position == newCategoryPos) {
            return 1;
        } else if (position == serializeCategoryPos) {
            return 2;
        } else {
            return 3;
        }
    }

    private Book getBookByPos(int position) {
        int recommendCategoryPos = getNavicationCnt() + 1;
        int newCategoryPos = recommendCategoryPos + getRecommendBooksCntWithCategoryItem();
        int serializeCategoryPos = newCategoryPos + getNewBooksCntWithCategoryItem();
        int endCategoryPos = serializeCategoryPos + getSerializeBooksCntWithCategoryItem();

        if (recommendCategoryPos < position && position < newCategoryPos) {
            return mRecommendBooks.get(position - recommendCategoryPos - 1);
        } else if (newCategoryPos < position && position < serializeCategoryPos) {
            return mNewBooks.get(position - newCategoryPos - 1);
        } else if (serializeCategoryPos < position && position < endCategoryPos) {
            return mSerializeBooks.get(position - serializeCategoryPos - 1);
        }  else {
            return mEndBooks.get(position - endCategoryPos - 1);
        }
    }

    @Override
    public int getItemCount() {
        return 1 + getNavicationCnt() + getRecommendBooksCntWithCategoryItem()
                + getNewBooksCntWithCategoryItem() + getSerializeBooksCntWithCategoryItem()
                + getEndBooksCnWithCategoryItem();
    }

    public void notifyChange(List<Book> recommendBooks,
                             List<Book> newBooks,
                             List<Book> serializeBooks,
                             List<Book> endBooks) {
        mRecommendBooks = recommendBooks;
        mNewBooks = newBooks;
        mSerializeBooks = serializeBooks;
        mEndBooks = endBooks;
        notifyDataSetChanged();
    }

    private class BannerItem extends RecyclerView.ViewHolder {

        private TextView navicationName;

        public BannerItem(View itemView) {
            super(itemView);
            navicationName = itemView.findViewById(R.id.title);
        }
    }

    private class NavicationItem extends BaseViewHolder {

        private Context inContext;
        private ImageView navicationImg;
        private TextView navicationName;

        public NavicationItem(View itemView) {
            super(itemView);
            inContext = itemView.getContext();
            navicationImg = itemView.findViewById(R.id.navigation_img);
            navicationName = itemView.findViewById(R.id.navigation_title);
        }

        public void updateView(int position) {
            navicationImg.setBackground(inContext.getDrawable(mNavicationImgIds[position]));
            navicationName.setText(mNavicionTitles[position]);
        }

        @Override
        public void onItemClick() {

        }
    }

    private class BookHorizontalItem extends BaseViewHolder{

        private Context inContext;
        private ImageView bookCover;
        private TextView bookName;
        private TextView bookCategory;
        private TextView bookDesc;
        private Book inBook;

        public BookHorizontalItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = itemView.findViewById(R.id.book_cover);
            bookName = itemView.findViewById(R.id.book_name);
            bookCategory = itemView.findViewById(R.id.book_category);
            bookDesc = itemView.findViewById(R.id.book_desc);
        }

        public void updateView(Book book) {
            inBook = book;
            bookName.setText(book.getName());
            ImageLoader.loadImgInto(inContext, book.getImg(), bookCover);

            String categoryAndAuthorStr = String.format(
                    inContext.getString(R.string.check_book_category_and_author),
                    book.getCategory(), book.getAuthor());
            bookCategory.setText(categoryAndAuthorStr);

            bookDesc.setText(book.getDesc());
        }

        @Override
        public void onItemClick() {
            Intent intent = new Intent(inContext, BookDetailActivity.class);
            intent.putExtra(GlobalConstant.EXTRA_BOOK, inBook);
            inContext.startActivity(intent);
        }
    }

    private class BookVeticalItem extends BaseViewHolder {

        private Context inContext;
        private ImageView bookCover;
        private TextView bookName;
        private Book inBook;

        public BookVeticalItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            bookCover = itemView.findViewById(R.id.book_cover);
            bookName = itemView.findViewById(R.id.book_name);
        }

        public void updateView(Book book) {
            inBook = book;
            bookName.setText(book.getName());
            ImageLoader.loadImgInto(inContext, book.getImg(), bookCover);
        }

        @Override
        public void onItemClick() {
            Intent intent = new Intent(inContext, BookDetailActivity.class);
            intent.putExtra(GlobalConstant.EXTRA_BOOK, inBook);
            inContext.startActivity(intent);
        }
    }



    private class CategoryItem extends RecyclerView.ViewHolder {

        private Context inContext;
        private TextView categoryTitle;

        public CategoryItem(Context context, View itemView) {
            super(itemView);
            inContext = context;
            categoryTitle = itemView.findViewById(R.id.title);
        }

        public void updateView(String title) {
            categoryTitle.setText(title);
        }
    }

    private abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public BaseViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick();
        }

        public abstract void onItemClick();
    }
}
