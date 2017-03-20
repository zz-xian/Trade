package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.DetailContract;
import com.xiaoxian.trade.mvp.model.Cart;
import com.xiaoxian.trade.mvp.model.Collect;
import com.xiaoxian.trade.mvp.model.Goods;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class DetailPresenter implements DetailContract.IPresenter {
    private DetailContract.IView mView;

    public DetailPresenter(DetailContract.IView view) {
        this.mView = view;
    }

    @Override
    public void queryCollect(String userId) {
        BmobQuery<Collect> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userId);
        boolean isCache = query.hasCachedResult(Collect.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Collect>() {
            @Override
            public void done(List<Collect> list, BmobException e) {
                if (e == null) {
                    mView.queryCollectSuccess(list);
                } else {
                    mView.queryCollectError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void queryCart(String userId) {
        BmobQuery<Cart> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", userId);
        boolean isCache = query.hasCachedResult(Cart.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Cart>() {
            @Override
            public void done(List<Cart> list, BmobException e) {
                if (e == null) {
                    mView.queryCartSuccess(list);
                } else {
                    mView.queryCartError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void saveCollect(String userId, Goods goods) {
        Collect collect = new Collect();
        collect.setGoodsId(goods.getObjectId());
        collect.setUserId(userId);
        collect.setTitle(goods.getTitle());
        collect.setDesc(goods.getDesc());
        collect.setImages(goods.getImages());
        collect.setPrice(goods.getPrice());
        collect.setKind(goods.getKind());
        collect.setSubKind(goods.getSubKind());
        collect.setCondition(goods.getCondition());
        collect.setMount(goods.getMount());
        collect.setPhone(goods.getPhone());
        collect.setLocation(goods.getLocation());
        collect.setCity(goods.getCity());
        collect.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mView.saveCollectSuccess();
                } else {
                    mView.saveCollectError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void saveCart(String userId, Goods goods) {
        Cart cart = new Cart();
        cart.setGoodsId(goods.getObjectId());
        cart.setUserId(userId);
        cart.setTitle(goods.getTitle());
        cart.setDesc(goods.getDesc());
        cart.setImages(goods.getImages());
        cart.setPrice(goods.getPrice());
        cart.setKind(goods.getKind());
        cart.setSubKind(goods.getSubKind());
        cart.setCondition(goods.getCondition());
        cart.setMount(goods.getMount());
        cart.setPhone(goods.getPhone());
        cart.setLocation(goods.getLocation());
        cart.setCity(goods.getCity());
        cart.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mView.saveCartSuccess();
                } else {
                    mView.saveCartError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void deleteCollect(String collectId) {
        Collect collect = new Collect();
        collect.setObjectId(collectId);
        collect.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.deleteCollectSuccess();
                } else {
                    mView.deleteCollectError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void deleteCart(String cartId) {
        Cart cart = new Cart();
        cart.setObjectId(cartId);
        cart.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.deleteCartSuccess();
                } else {
                    mView.deleteCartError(e.getLocalizedMessage());
                }
            }
        });
    }
}
