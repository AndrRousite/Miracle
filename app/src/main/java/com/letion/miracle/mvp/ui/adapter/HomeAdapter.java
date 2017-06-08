package com.letion.miracle.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.letion.geetionlib.base.BaseHolder;
import com.letion.geetionlib.base.BaseRecyclerAdapter;

/**
 * Created by liu-feng on 2017/6/8.
 */
public class HomeAdapter extends BaseRecyclerAdapter<String> {
    public HomeAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected BaseHolder<String> onCreateDefaultViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new HomeHolder(view);
    }

    class HomeHolder extends BaseHolder<String> {
        TextView mTextView;

        public HomeHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }

        @Override
        public void setData(String data, int position) {
            mTextView.setText(data);
        }

    }
}
