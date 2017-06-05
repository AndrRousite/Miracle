package com.letion.miracle.mvp.ui.adapter;

import android.view.View;

import com.letion.miracle.R;
import com.letion.miracle.mvp.model.entity.User;
import com.letion.miracle.mvp.ui.holder.UserItemHolder;

import java.util.List;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class UserAdapter extends DefaultAdapter<User> {
    public UserAdapter(List<User> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<User> getHolder(View v, int viewType) {
        return new UserItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycle_list;
    }
}
