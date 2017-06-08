package com.letion.miracle.mvp.contract;

import com.letion.geetionlib.mvp.IModel;
import com.letion.geetionlib.mvp.IView;
import com.letion.miracle.mvp.ui.adapter.TestAdapter;

import java.util.List;


public interface TestContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void setAdapter(TestAdapter adapter);
        void finishRefreshing();
        void onItemClick(int position);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {
        List<String> getTests();
    }
}