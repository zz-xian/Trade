package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.LoginContract;
import com.xiaoxian.trade.mvp.presenter.LoginPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class LoginActivityModule {
    private LoginContract.IView view;

    public LoginActivityModule(LoginContract.IView view) {
        this.view = view;
    }

    @Provides
    public LoginContract.IView provideView() {
        return view;
    }

    @Provides
    public LoginContract.IPresenter providePresenter() {
        return new LoginPresenter(view);
    }
}
