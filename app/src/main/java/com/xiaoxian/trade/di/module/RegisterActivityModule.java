package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.RegisterContract;
import com.xiaoxian.trade.mvp.presenter.RegisterPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class RegisterActivityModule {
    private RegisterContract.IView view;

    public RegisterActivityModule(RegisterContract.IView view) {
        this.view = view;
    }

    @Provides
    public RegisterContract.IView provideView() {
        return view;
    }

    @Provides
    public RegisterContract.IPresenter providePresenter() {
        return new RegisterPresenter(view);
    }
}
