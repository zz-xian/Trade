package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.MyPublishContract;
import com.xiaoxian.trade.mvp.presenter.MyPublishPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class MyPublishActivityModule {
    private MyPublishContract.IView view;

    public MyPublishActivityModule(MyPublishContract.IView view) {
        this.view = view;
    }

    @Provides
    public MyPublishContract.IView provideView() {
        return view;
    }

    @Provides
    public MyPublishContract.IPresenter providePresenter() {
        return new MyPublishPresenter(view);
    }
}
