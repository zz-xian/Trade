package com.xiaoxian.trade.di.component;

import com.xiaoxian.trade.di.UserScope;
import com.xiaoxian.trade.di.module.ProfileActivityModule;
import com.xiaoxian.trade.mvp.view.activity.ProfileActivity;

import dagger.Component;

@UserScope
@Component(dependencies = AppComponent.class, modules = ProfileActivityModule.class)
public interface ProfileActivityComponent {
    void inject(ProfileActivity profileActivity);
}
