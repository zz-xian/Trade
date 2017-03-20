package com.xiaoxian.trade.di.module;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.mvp.contract.HomeContract;
import com.xiaoxian.trade.mvp.contract.NearContract;
import com.xiaoxian.trade.mvp.contract.SelfContract;
import com.xiaoxian.trade.mvp.contract.SortContract;
import com.xiaoxian.trade.mvp.presenter.HomePresenter;
import com.xiaoxian.trade.mvp.presenter.NearPresenter;
import com.xiaoxian.trade.mvp.presenter.SelfPresenter;
import com.xiaoxian.trade.mvp.presenter.SortPresenter;

import dagger.Module;
import dagger.Provides;

@UserScope
@Module
public class MainActivityModule {
    private HomeContract.IView homeView;
    private NearContract.IView nearView;
    private SortContract.IView sortView;
    private SelfContract.IView selfView;

    public MainActivityModule(HomeContract.IView view) {
        this.homeView = view;
    }

    public MainActivityModule(NearContract.IView view) {
        this.nearView = view;
    }

    public MainActivityModule(SortContract.IView view) {
        this.sortView = view;
    }

    public MainActivityModule(SelfContract.IView view) {
        this.selfView = view;
    }

    @Provides
    public HomeContract.IView provideHomeView() {
        return homeView;
    }

    @Provides
    public NearContract.IView provideNearView() {
        return nearView;
    }

    @Provides
    public SortContract.IView provideSortView() {
        return sortView;
    }

    @Provides
    public SelfContract.IView provideSelfView() {
        return selfView;
    }

    @Provides
    public HomeContract.IPresenter provideHomePresenter() {
        return new HomePresenter(homeView);
    }

    @Provides
    public NearContract.IPresenter provideNearPresenter() {
        return new NearPresenter(nearView);
    }

    @Provides
    public SortContract.IPresenter provideSortPresenter() {
        return new SortPresenter(sortView);
    }

    @Provides
    public SelfContract.IPresenter provideSelfPresenter() {
        return new SelfPresenter(selfView);
    }
}
