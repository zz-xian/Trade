package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.PublishContract;
import com.xiaoxian.trade.mvp.presenter.PublishPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class PublishActivityModule {
    private PublishContract.IView view;

    public PublishActivityModule(PublishContract.IView view) {
        this.view = view;
    }

    @Provides
    public PublishContract.IView provideView() {
        return view;
    }

    @Provides
    public PublishContract.IPresenter providePresenter() {
        return new PublishPresenter(view);
    }
}
