package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.MyPublishActivityModule;
import com.xiaoxian.trade.mvp.view.activity.own.MyPublishActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = MyPublishActivityModule.class)
public interface MyPublishActivityComponent {
    void inject(MyPublishActivity myPublishActivity);
}
