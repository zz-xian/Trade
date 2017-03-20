package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.RegisterActivityModule;
import com.xiaoxian.trade.mvp.view.activity.RegisterActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = RegisterActivityModule.class)
public interface RegisterActivityComponent {
    void inject(RegisterActivity registerActivity);
}
