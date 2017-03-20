package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.ResultContract;
import com.xiaoxian.trade.mvp.presenter.ResultPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class ResultActivityModule {
    private ResultContract.IView view;

    public ResultActivityModule(ResultContract.IView view) {
        this.view = view;
    }

    @Provides
    public ResultContract.IView provideView() {
        return view;
    }

    @Provides
    public ResultContract.IPresenter providePresenter() {
        return new ResultPresenter(view);
    }
}
