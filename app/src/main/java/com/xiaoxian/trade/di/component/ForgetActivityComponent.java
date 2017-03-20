package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.ForgetActivityModule;
import com.xiaoxian.trade.mvp.view.activity.ForgetActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = ForgetActivityModule.class)
public interface ForgetActivityComponent {
    void inject(ForgetActivity forgetActivity);
}
