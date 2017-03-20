package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.MyCollectContract;
import com.xiaoxian.trade.mvp.presenter.MyCollectPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class MyCollectActivityModule {
    private MyCollectContract.IView view;

    public MyCollectActivityModule(MyCollectContract.IView view) {
        this.view = view;
    }

    @Provides
    public MyCollectContract.IView provideView() {
        return view;
    }
    @Provides
    public MyCollectContract.IPresenter providePresenter() {
        return new MyCollectPresenter(view);
    }
}
