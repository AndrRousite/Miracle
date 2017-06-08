package com.letion.miracle.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.letion.geetionlib.base.integration.IRepositoryManager;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.geetionlib.mvp.BaseModel;
import com.letion.miracle.mvp.contract.HomeContract;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;


@ActivityScope
public class HomeModel extends BaseModel implements HomeContract.Model {
    private Gson mGson;
    private Application mApplication;

    @Inject
    public HomeModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public List<String> getItems() {
        LinkedList<String> mStringList = new LinkedList<>();
        mStringList.add("“锤子下拉”，东半球最优雅的下拉控件");
        mStringList.add("https://github.com/hougr/SmartisanPull");
        mStringList.add("模仿“锤子阅读”的下拉效果");
        mStringList.add("仅供学习交流，请勿用于商业用途。");
        mStringList.add("----------分隔线----------");
        mStringList.add("下面是我做的过程中觉得有意思的地方：");
        mStringList.add("（下面把“可刷新”的最小距离叫刷新距离）");
        mStringList.add("1、下拉时先把item0上面的分隔线滚动出来，");
        mStringList.add("   该分隔线在下拉过程中一直显示，");
        mStringList.add("   直到header完全消失，它才重新藏起来");
        mStringList.add("2、到达刷新距离前，提示语逐渐清晰");
        mStringList.add("3、到达刷新距离时，两圆弧刚好各转半圈，");
        mStringList.add("   两圆弧间的两个缺口处于同一水平线");
        mStringList.add("4、刷新距离以下，摩擦系数越来越大");
        mStringList.add("5、如果手指向上返回，动画逐渐回到原始状态");
        mStringList.add("6、两圆弧的旋转始终是平滑的，只有速度变化");
        mStringList.add("7、最后，两圆弧逐渐过渡成线段，消失在两端");
        mStringList.add("喜欢的话，可以在github上赏我一颗star");
        return mStringList;
    }
}