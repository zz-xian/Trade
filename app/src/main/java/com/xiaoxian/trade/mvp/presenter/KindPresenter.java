package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.KindContract;
import com.xiaoxian.trade.mvp.model.Kind;
import com.xiaoxian.trade.mvp.model.SubKind;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class KindPresenter implements KindContract.IPresenter {
    private KindContract.IView mView;

    public KindPresenter(KindContract.IView view) {
        this.mView = view;
    }

    @Override
    public void loadKind() {
        BmobQuery<Kind> query = new BmobQuery<>();
        boolean isCache = query.hasCachedResult(Kind.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Kind>() {
            @Override
            public void done(List<Kind> list, BmobException e) {
                if (e == null) {
                    mView.showKind(list);
                } else {
                    mView.loadError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void loadSubKind(Kind kind) {
        BmobQuery<SubKind> query = new BmobQuery<>();
        query.addWhereEqualTo("kindId", kind.getObjectId());
        boolean isCache = query.hasCachedResult(SubKind.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<SubKind>() {
            @Override
            public void done(List<SubKind> list, BmobException e) {
                if (e == null) {
                    mView.showSubKind(list);
                } else {
                    mView.loadError(e.getLocalizedMessage());
                }
            }
        });
    }
}
