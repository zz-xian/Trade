package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.ResultContract;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ResultPresenter implements ResultContract.IPresenter {
    private ResultContract.IView mView;

    public ResultPresenter(ResultContract.IView view) {
        this.mView = view;
    }

    @Override
    public void queryGoodsByKey(final String key) {
        final List<Goods> resultList = new ArrayList<>();
        BmobQuery<Goods> query = new BmobQuery<>();
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
                    for (Goods goods : list) {
                        if (goods.getTitle().contains(key) || goods.getDesc().contains(key) || goods.getLocation().contains(key)) {
                            resultList.add(goods);
                        }
                    }
                    mView.showKeyResult(resultList);
                } else {
                    mView.loadGoodsError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void queryGoodsByKind(String kind) {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("kind", kind);
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
                    mView.showSubKindResult(list);
                } else {
                    mView.loadGoodsError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void queryGoodsBySubKind(String subKind) {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("subKind", subKind);
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
                    mView.showSubKindResult(list);
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
                        mView.parseUser(userList);
                    }
                }
            }
        });
    }
}
