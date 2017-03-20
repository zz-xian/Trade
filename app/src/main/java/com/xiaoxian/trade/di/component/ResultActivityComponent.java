package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.ResultActivityModule;
import com.xiaoxian.trade.mvp.view.activity.ResultActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = ResultActivityModule.class)
public interface ResultActivityComponent {
    void inject(ResultActivity resultActivity);
}
