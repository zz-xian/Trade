package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.PublishActivityModule;
import com.xiaoxian.trade.mvp.view.activity.PublishActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = PublishActivityModule.class)
public interface PublishActivityComponent {
    void inject(PublishActivity publishActivity);
}
