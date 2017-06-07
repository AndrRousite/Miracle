package com.letion.miracle.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by liu-feng on 2017/6/7.
 */
public class TestAdapter extends BaseAdapter {
    public List<String> mItemStringList;
    public LayoutInflater mLayoutInflater;

    public TestAdapter(Context context, List<String> stringList) {
        mItemStringList = stringList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mItemStringList == null)
            return 0;
        return mItemStringList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemStringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String arg = mItemStringList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(arg);
        return convertView;
    }

    public final class ViewHolder {
        TextView mTextView;
    }
}
