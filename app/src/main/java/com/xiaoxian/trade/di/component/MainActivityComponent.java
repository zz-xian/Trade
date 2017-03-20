package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.MainActivityModule;
import com.xiaoxian.trade.mvp.view.fragment.HomeFragment;
import com.xiaoxian.trade.mvp.view.fragment.NearFragment;
import com.xiaoxian.trade.mvp.view.fragment.SelfFragment;
import com.xiaoxian.trade.mvp.view.fragment.SortFragment;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(HomeFragment homeFragment);
    void inject(NearFragment nearFragment);
    void inject(SortFragment sortFragment);
    void inject(SelfFragment selfFragment);
}
