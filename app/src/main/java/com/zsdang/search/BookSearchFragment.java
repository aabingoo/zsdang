package com.zsdang.search;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zsdang.R;
import com.zsdang.view.TabLayout;


public class BookSearchFragment extends Fragment {

    private OnTabClickListener mListener;
    private TabLayout mPopularTl;
    private TabLayout mHistoryTl;
    private String[] mPopularStrArray = new String[] {"圣墟", "遮天", "凡人修仙传", "凡人修仙之仙界篇"};

    public interface OnTabClickListener {
        void onTabClick(String keyword);
    }

    public BookSearchFragment() {
        // Required empty public constructor
    }


    public static BookSearchFragment newInstance() {
        BookSearchFragment fragment = new BookSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener = (OnTabClickListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_search, container, false);
        mPopularTl = rootView.findViewById(R.id.popular_search_tl);
        mHistoryTl = rootView.findViewById(R.id.search_history_tl);
        initPopularTabLayout();
        return rootView;
    }

    private void initPopularTabLayout() {
        // Remove all children
        mPopularTl.removeAllViews();

        for (String str: mPopularStrArray) {
            // Create TextView
            final TextView textView = new TextView(getContext());
            textView.setText(str);
            textView.setBackground(getContext().getDrawable(R.drawable.bg_tab));
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setOnClickListener(mTextViewClickListener);

            // Add to TabLayout
            mPopularTl.addView(textView);
        }
    }

    View.OnClickListener mTextViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onTabClick(((TextView)v).getText().toString());
            }
        }
    };
}
