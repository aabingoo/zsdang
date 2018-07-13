package com.zsdang.bookcatalog;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zsdang.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by BinyongSu on 2018/6/28.
 */

public class BookCatalogListViewAdapter extends BaseAdapter {

    private List<String> mChapters;

    public BookCatalogListViewAdapter() {
        mChapters = new ArrayList<>();
    }

    public void notifyChange(@NonNull List<String> chapters) {
        mChapters = chapters;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mChapters.size();
    }

    @Override
    public Object getItem(int position) {
        return mChapters.get(mChapters.size() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_book_catalog_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.chapterTitleTV = convertView.findViewById(R.id.chapter_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.chapterTitleTV.setText(mChapters.get(mChapters.size() - position - 1));

        return convertView;
    }

    private class ViewHolder {
        TextView chapterTitleTV;
    }
}
