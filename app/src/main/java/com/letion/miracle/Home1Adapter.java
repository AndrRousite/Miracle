package com.letion.miracle;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letion.uikit.supertext.CommonTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu-feng on 2017/10/27.
 */
public class Home1Adapter extends RecyclerView.Adapter<Home1Adapter.ViewHolder> {
    protected List<String> data = new ArrayList<>();


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_home1, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String arg = data.get(position);
        holder.ctv.setLeftTextString(TextUtils.isEmpty(arg) ? "" : arg).setRightTextString
                (TextUtils.isEmpty(arg) ? "" : arg);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    public void setData(List<String> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    @NonNull
    public void addAll(List<String> data) {
        this.data.addAll(data);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CommonTextView ctv;

        public ViewHolder(View itemView) {
            super(itemView);
            ctv = itemView.findViewById(R.id.ctv);
        }
    }
}
