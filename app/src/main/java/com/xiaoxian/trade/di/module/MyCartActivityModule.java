package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.MyCartContract;
import com.xiaoxian.trade.mvp.presenter.MyCartPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class MyCartActivityModule {
    private MyCartContract.IView view;

    public MyCartActivityModule(MyCartContract.IView view) {
        this.view = view;
    }

    @Provides
    public MyCartContract.IView provideView() {
        return view;
    }
    @Provides
    public MyCartContract.IPresenter providePresenter() {
        return new MyCartPresenter(view);
    }
}
