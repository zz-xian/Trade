package com.xiaoxian.trade.mvp.presenter;

import android.util.Log;

import com.xiaoxian.trade.mvp.contract.NearContract;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NearPresenter implements NearContract.IPresenter {
    private NearContract.IView mView;

    public NearPresenter(NearContract.IView view) {
        this.mView = view;
    }

    @Override
    public void loadGoods(String city) {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("city", city);
        query.order("-createdAt");
        boolean isCache = query.hasCachedResult(Goods.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e == null) {
                    Log.i("Near", list.size() + "Goods");
                    mView.loadGoodsSuccess(list);
                } else {
                    mView.loadGoodsError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void parseGoodsUser(final List<Goods> goodsList) {
        final List<User> userList = new ArrayList<>();
        BmobQuery<User> query = new BmobQuery<>();
        boolean isCache = query.hasCachedResult(User.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < goodsList.size(); i++) {
                        for (User user : list) {
                            if (user.getObjectId().endsWith(goodsList.get(i).getUserId())) {
                                userList.add(user);
                            }
                        }
                    }
                    if (userList.size() == goodsList.size()) {
                        Log.i("Near", userList.size() + "Users");
                        mView.parseUser(userList);
                    }
                }
            }
        });
    }
}
