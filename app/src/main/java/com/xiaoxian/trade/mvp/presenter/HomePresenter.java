package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.HomeContract;
import com.xiaoxian.trade.mvp.model.Banner;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HomePresenter implements HomeContract.IPresenter {
    private HomeContract.IView mView;

    public HomePresenter(HomeContract.IView view) {
        this.mView = view;
    }

    @Override
    public void loadBanner(final List<Banner> bannerList) {
        BmobQuery<Banner> query = new BmobQuery<>();
        boolean isCache = query.hasCachedResult(Banner.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Banner>() {
            @Override
            public void done(List<Banner> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        bannerList.add(list.get(i));
                    }
                    mView.showBanner(bannerList);
                }
            }
        });
    }

    @Override
    public void loadGoods() {
        BmobQuery<Goods> query = new BmobQuery<>();
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
                        mView.parseUser(userList);
                    }
                }
            }
        });
    }
}
