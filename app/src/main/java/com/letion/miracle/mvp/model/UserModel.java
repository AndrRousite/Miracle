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
package com.letion.miracle.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.letion.geetionlib.base.integration.IRepositoryManager;
import com.letion.geetionlib.di.scope.ActivityScope;
import com.letion.geetionlib.mvp.BaseModel;
import com.letion.miracle.mvp.contract.UserContract;
import com.letion.miracle.mvp.model.api.cache.CommonCache;
import com.letion.miracle.mvp.model.api.service.UserService;
import com.letion.miracle.mvp.model.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

/**
 * ================================================
 * 展示 Model 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.3">Model wiki 官方文档</a>
 * Created by JessYan on 09/04/2016 10:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@ActivityScope
public class UserModel extends BaseModel implements UserContract.Model {
    public static final int USERS_PER_PAGE = 10;

    @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getUsers(lastIdQueried, USERS_PER_PAGE))
                .flatMap(listObservable -> mRepositoryManager.obtainCacheService(CommonCache.class)
                        .getUsers(listObservable
                                , new DynamicKey(lastIdQueried)
                                , new EvictDynamicKey(update))
                        .map(listReply -> listReply.getData()));

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {

    }

}
