package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.DetailActivityModule;
import com.xiaoxian.trade.mvp.view.activity.DetailActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = DetailActivityModule.class)
public interface DetailActivityComponent {
    void inject(DetailActivity detailActivity);
}
