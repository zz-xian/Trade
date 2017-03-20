package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.KindModule;
import com.xiaoxian.trade.mvp.view.activity.AllKindActivity;
import com.xiaoxian.trade.mvp.view.activity.KindActivity;
import com.xiaoxian.trade.mvp.view.activity.SubKindActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = KindModule.class)
public interface KindComponent {
    void inject(AllKindActivity allKindActivity);
    void inject(KindActivity kindActivity);
    void inject(SubKindActivity subKindActivity);
}
