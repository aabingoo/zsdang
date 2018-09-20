package com.zsdang.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zsdang.LogUtils;
import com.zsdang.R;


public class WarnDialogFragment extends DialogFragment {

    private String mContent;
    private CancelOkCallback mCallback;
    private TextView mContentTv;
    private TextView mCancelTv;
    private TextView mOkTv;

    public interface CancelOkCallback {
        void onCancel();
        void onOk();
    }

    public WarnDialogFragment() {
        // Required empty public constructor
    }

    public static WarnDialogFragment newInstance() {
        WarnDialogFragment fragment = new WarnDialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("Dialog", "onCreateDialog");
        Activity activity = getActivity();
        View dialogView = activity.getLayoutInflater().inflate(R.layout.fragment_warn_dialog, null);
        initView(dialogView);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setView(dialogView);

        return builder.create();
    }

    private void initView(View view) {
        mContentTv = view.findViewById(R.id.dialog_content);
        mContentTv.setText(mContent);

        mCancelTv = view.findViewById(R.id.dialog_cancel);
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onCancel();
                }
                dismiss();
            }
        });

        mOkTv = view.findViewById(R.id.dialog_ok);
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onOk();
                }
                dismiss();
            }
        });
    }

    public void showDialog(FragmentManager fragmentManager, String content, final CancelOkCallback cancelOkCallback) {
        Log.d("Dialog", "showDialog");

        mContent = content;
        mCallback = cancelOkCallback;

        show(fragmentManager, "CancelOkDialog");
    }
}
