package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.LoginActivityModule;
import com.xiaoxian.trade.mvp.view.activity.LoginActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = LoginActivityModule.class)
public interface LoginActivityComponent {
    void inject(LoginActivity loginActivity);
}

