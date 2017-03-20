package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.DetailContract;
import com.xiaoxian.trade.mvp.presenter.DetailPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class DetailActivityModule {
    private DetailContract.IView view;

    public DetailActivityModule(DetailContract.IView view) {
        this.view = view;
    }

    @Provides
    public DetailContract.IView provideView() {
        return view;
    }

    @Provides
    public DetailContract.IPresenter providePresenter() {
        return new DetailPresenter(view);
    }
}
