package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.MyCollectActivityModule;
import com.xiaoxian.trade.mvp.view.activity.own.MyCollectActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = MyCollectActivityModule.class)
public interface MyCollectActivityComponent {
    void inject(MyCollectActivity myCollectActivity);
}
