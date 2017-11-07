/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.letion.miracle.mvp.ui.adapter;

import android.view.View;

import com.letion.geetionlib.base.adapter.BaseRecyclerAdapter;
import com.letion.geetionlib.base.adapter.BaseViewHolder;
import com.letion.miracle.R;
import com.letion.miracle.mvp.model.entity.User;
import com.letion.miracle.mvp.ui.holder.UserItemHolder;

import java.util.List;

/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * Created by JessYan on 09/04/2016 12:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class UserAdapter extends BaseRecyclerAdapter<User> {
    public UserAdapter(List<User> infos) {
        super(infos);
    }

    @Override
    public BaseViewHolder<User> getViewHolder(View v, int viewType) {
        return new UserItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycle_list;
    }

    @Override
    public int getItemCount() {
        return mInfos.size();
    }
}
