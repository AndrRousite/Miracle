package com.letion.miracle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letion.uikit.xrecycler.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu-feng on 2017/10/27.
 */
public class Home1Fragment extends Fragment {
    XRecyclerView xRecyclerView;
    Home1Adapter mAdapter;
    private int mCurrentPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        xRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new Home1Adapter();
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                loadHome1Data();
            }

            @Override
            public void onLoadMore() {
                mCurrentPage++;
                loadHome1Data();
            }
        });
        loadHome1Data();
    }

    private void loadHome1Data() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("这是第" + (i + 1) + "个Item");
        }
        if (mCurrentPage == 1) {
            mAdapter.setData(list);
        } else {
            mAdapter.addAll(list);
        }
    }
}
