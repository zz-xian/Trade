package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.MyCartActivityModule;
import com.xiaoxian.trade.mvp.view.activity.own.MyCartActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = MyCartActivityModule.class)
public interface MyCartActivityComponent {
    void inject(MyCartActivity myCartActivity);
}
