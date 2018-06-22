package com.zsdang.bookstore;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsdang.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookStoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookStoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookStoreFragment extends Fragment {

    private static final String TAG = "BookStoreFragment";

    private RecyclerView mBookstoreRecyclerView;

    private BookstoreRecyclerViewAdapter mBookstoreRecyclerViewAdapter;

    public static BookStoreFragment newInstance() {
        BookStoreFragment fragment = new BookStoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_store, container, false);
        mBookstoreRecyclerView = rootView.findViewById(R.id.bookstore_rv);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        // Init RecyclerView and set its adapter
        mBookstoreRecyclerView.setLayoutManager(new GridLayoutManager(activity, 3));//new LinearLayoutManager(activity));
        mBookstoreRecyclerViewAdapter = new BookstoreRecyclerViewAdapter();
        mBookstoreRecyclerView.setAdapter(mBookstoreRecyclerViewAdapter);
    }

}
