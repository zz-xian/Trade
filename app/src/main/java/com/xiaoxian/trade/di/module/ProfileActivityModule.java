package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.ProfileContract;
import com.xiaoxian.trade.mvp.presenter.ProfilePresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class ProfileActivityModule {
    private ProfileContract.IView view;

    public ProfileActivityModule(ProfileContract.IView view) {
        this.view = view;
    }

    @Provides
    public ProfileContract.IView provideView() {
        return view;
    }

    @Provides
    public ProfileContract.IPresenter providePresenter() {
        return new ProfilePresenter(view);
    }
}
