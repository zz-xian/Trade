package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.KindContract;
import com.xiaoxian.trade.mvp.presenter.KindPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class KindModule {
    private KindContract.IView view;

    public KindModule(KindContract.IView view) {
        this.view = view;
    }

    @Provides
    public KindContract.IView provideView() {
        return view;
    }

    @Provides
    public KindContract.IPresenter providePresenter() {
        return new KindPresenter(view);
    }
}
