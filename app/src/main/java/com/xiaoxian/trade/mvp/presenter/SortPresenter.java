package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.SortContract;
import com.xiaoxian.trade.mvp.model.Sort;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SortPresenter implements SortContract.IPresenter {
    private SortContract.IView mView;

    public SortPresenter(SortContract.IView view) {
        this.mView = view;
    }

    @Override
    public void loadSort() {
        BmobQuery<Sort> query = new BmobQuery<>();
        boolean isCache = query.hasCachedResult(Sort.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(new FindListener<Sort>() {
            @Override
            public void done(List<Sort> list, BmobException e) {
                if (e == null) {
                    mView.showSort(list);
                } else {
                    mView.showSortError(e.getLocalizedMessage());
                }
            }
        });
    }
}
