package com.letion.miracle.mvp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by liu-feng on 2017/6/7.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    public List<String> mItemStringList;
    Callback itemClick;

    public interface Callback {
        void onItemClick(int position);
    }

    public TestAdapter(List<String> stringList, Callback itemClick) {
        super();
        mItemStringList = stringList;
        this.itemClick = itemClick;
    }


    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestAdapter.ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(mItemStringList.get(i));
    }

    @Override
    public int getItemCount() {
        return mItemStringList.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            if (itemClick != null)
                itemView.setOnClickListener((v) -> itemClick.onItemClick(getPosition()));//点击事件
        }
    }
}
