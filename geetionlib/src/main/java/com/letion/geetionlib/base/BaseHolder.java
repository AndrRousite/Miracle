package com.letion.geetionlib.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.letion.geetionlib.util.KnifeUtil;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by liu-feng on 2017/6/8.
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
        AutoUtils.autoSize(itemView);//适配
        KnifeUtil.bindTarget(this, itemView);//绑定
    }

    /**
     * 设置数据
     * 刷新界面
     *
     * @param
     * @param position
     */
    public abstract void setData(T data, int position);

    /**
     * 释放资源
     */
    protected void release() {

    }

}
