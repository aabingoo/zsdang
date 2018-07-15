package com.zsdang.my;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zsdang.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment";

    private Toolbar mToolbar;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        mToolbar = rootView.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        return rootView;
    }

}
