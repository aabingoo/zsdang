package com.zsdang.bookdetail;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.zsdang.data.GlobalConstant;
import com.zsdang.data.web.DataRequestCallback;
import com.zsdang.data.web.server.DataServiceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BinyongSu on 2018/5/31.
 */

public class BookDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "BookDetailFragment";

    public static final String ARG_PARAM_BOOK = "book";

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private Book mBook;
    private List<Book> mOtherWrittenBooks;
    private List<Book> mSimilarBooks;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<String> mChapterNameList = new ArrayList<>();
    private RecyclerView mBookDetailRv;
    private BookDetailRecyclerViewAdapter mBookDetailRecyclerViewAdapter;
    private Handler mHandler;

    private ImageView mBookCover;
    private TextView mBookName;
    private TextView mBookCategoryAndAuthor;

    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(GlobalConstant.EXTRA_BOOK, book);
        LogUtils.d(TAG, "book id:" + book.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mBook = bundle.getParcelable(GlobalConstant.EXTRA_BOOK);
            LogUtils.d(TAG, "mBook id:" + mBook.getId());
        }

        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_detail, container, false);
        // ToolBar
        mToolbar = rootView.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mToolbar.inflateMenu(R.menu.toolbar_menu);

        // Checked book view
        mBookCover = rootView.findViewById(R.id.book_cover);
        mBookName = rootView.findViewById(R.id.book_name);
        mBookCategoryAndAuthor = rootView.findViewById(R.id.book_category_and_author);

        // RecyclerView
        mBookDetailRv = rootView.findViewById(R.id.book_detail_rv);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        // AppBarLayout
        mAppBarLayout = rootView.findViewById(R.id.appbar_layout);
        // Handle the issue that SwipeRefreshLayout refresh behaviour abnormal
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        // Init RecyclerView and set its adapter
        mBookDetailRv.setLayoutManager(new GridLayoutManager(activity, 3));
        mBookDetailRecyclerViewAdapter = new BookDetailRecyclerViewAdapter();
        mBookDetailRv.setAdapter(mBookDetailRecyclerViewAdapter);
//        mBookDetailRv.addOnItemTouchListener(new RecyclerViewItemTouchHandler(activity,
//                new RecyclerViewItemTouchHandler.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int pos) {
//                        LogUtils.d(TAG, "onItemClick:" + pos);
//                        if (mBook != null && mChapterNameList != null && pos < mChapterNameList.size()) {
//                            String chapterName = mChapterNameList.get(pos);
//                            String chapterUrl = mBook.getChapters().get(chapterName);
//                            String bookUrl = mBook.getUrl();
//                            if (!bookUrl.endsWith("/")) {
//                                bookUrl += "/";
//                            }
//                            int last = chapterUrl.lastIndexOf("/");
//                            if (last >= 0) {
//                                chapterUrl = chapterUrl.substring(last + 1);
//                            }
//                            chapterUrl = bookUrl + chapterUrl;
//                            LogUtils.d(TAG, "book url:" + bookUrl + "name:" + chapterName + " url:" + chapterUrl);
//                            enterReadingScreen(chapterName, chapterUrl);
//                        }
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, int pos) {
//                        LogUtils.d(TAG, "onItemLongClick:" + pos);
//                    }
//                }));

//        queryBookDetail();
        loadBookDetail();

    }

    private void loadBookDetail() {

        if (mBook == null || TextUtils.isEmpty(mBook.getId())) return;

        mSwipeRefreshLayout.setRefreshing(true);
        DataServiceManager dataServiceManager = new DataServiceManager();
        dataServiceManager.queryBookDetial(mBook.getId(), new DataRequestCallback() {
            @Override
            public void onFailure() {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(@NonNull String result) {
                try {
                    JSONObject pageJson = new JSONObject(result);
                    JSONObject bookJson = pageJson.getJSONObject("data");
                    mBook = getBook(bookJson);
                    mOtherWrittenBooks = getOtherWrittenBooks(bookJson);
                    mSimilarBooks = getSimilarBooks(bookJson);

                    mHandler.post(notifyAdapterRunnable);
                } catch (Exception e) {
                    LogUtils.d(TAG, "Exception on queryBookstore.");
                }
            }
        });
    }

    private Book getBook(JSONObject bookJson) {
        Book book = new Book();
        try{
            book = new Book(bookJson.getString("Id"),
                    bookJson.getString("Name"),
                    bookJson.getString("Author"),
                    bookJson.getString("Img"),
                    bookJson.getString("Desc"),
                    bookJson.getString("CName"),
                    bookJson.getString("LastChapterId"),
                    bookJson.getString("LastChapter"));
        } catch (Exception e) {
            LogUtils.d(TAG, "Exception on queryBookstore.");
        }
        return book;
    }

    private List<Book> getOtherWrittenBooks(JSONObject bookJson) {
        List<Book> otherWrittenBooks = new ArrayList<>();
        try {
            JSONArray similarBooksJson = bookJson.getJSONArray("SameUserBooks");
            for (int i = 0; i < similarBooksJson.length(); i++) {
                JSONObject similarBookItem = similarBooksJson.getJSONObject(i);
                Book book = new Book(similarBookItem.getString("Id"),
                        similarBookItem.getString("Name"),
                        similarBookItem.getString("Img"));
                book.setLatestChapterTitle(similarBookItem.getString("LastChapter"));
                otherWrittenBooks.add(book);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "Exception at getOtherWrittenBooks.");
        }
        return otherWrittenBooks;
    }

    private List<Book> getSimilarBooks(JSONObject bookJson) {
        List<Book> similarBooks = new ArrayList<>();
        try {
            JSONArray similarBooksJson = bookJson.getJSONArray("SameCategoryBooks");
            for (int i = 0; i < similarBooksJson.length(); i++) {
                JSONObject similarBookItem = similarBooksJson.getJSONObject(i);
                Book book = new Book(similarBookItem.getString("Id"),
                        similarBookItem.getString("Name"),
                        similarBookItem.getString("Img"));
                similarBooks.add(book);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "Exception at getSimilarBooks.");
        }
        return similarBooks;
    }

    private Runnable notifyAdapterRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAdded() && mBook != null) {
                ImageLoader.loadImgInto(getContext(), mBook.getImg(), mBookCover);
                mBookName.setText(mBook.getName());
                String caaStr = String.format(getContext().getString(R.string.check_book_category_and_author),
                        mBook.getCategory(), mBook.getAuthor());
                mBookCategoryAndAuthor.setText(caaStr);

                mBookDetailRecyclerViewAdapter.notifyDataSetChanged(mBook, mOtherWrittenBooks, mSimilarBooks);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    public void onRefresh() {
        LogUtils.d(TAG, "onRefresh");
        loadBookDetail();
    }

//    private void enterReadingScreen(String chapterName, String chapterUrl) {
//        if (isAdded()) {
//            Activity activity = getActivity();
//            Intent intent = new Intent(activity, ReadingActivity.class);
//            intent.putExtra("chapter", new String[]{chapterName, chapterUrl});
//            activity.startActivity(intent);
//        }
//    }

//    public void queryBookDetail() {
//        DataBaseModel model = new DataBaseModel(getActivity());
//        model.queryWebBookDetail(mBook, new DataBaseModel.QueryBookDetailListener() {
//            @Override
//            public void queryDone(final LinkedHashMap<String, String> chapters) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LogUtils.d("CrawlerTest", "queryWebBookDetail() chapters size:" + chapters.size());
//                        if (chapters != null && chapters.size() > 0) {
//                            mBook.setChapters(chapters);
//                            mChapterNameList.clear();
//                            mChapterNameList.addAll(chapters.keySet());
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mLoadingView.setVisibility(View.GONE);
//                                    mBookDetailRecyclerViewAdapter.notifyDataSetChanged(mBook);
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        });
//    }
}
