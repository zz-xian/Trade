package com.xiaoxian.trade.di.component;

import android.content.Context;

import com.xiaoxian.trade.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
}
