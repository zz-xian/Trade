package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.ForgetContract;
import com.xiaoxian.trade.mvp.presenter.ForgetPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class ForgetActivityModule {
    private ForgetContract.IView view;

    public ForgetActivityModule(ForgetContract.IView view) {
        this.view = view;
    }

    @Provides
    public ForgetContract.IView provideView() {
        return view;
    }

    @Provides
    public ForgetContract.IPresenter providePresenter() {
        return new ForgetPresenter(view);
    }
}
