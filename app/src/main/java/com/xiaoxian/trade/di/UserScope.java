package com.xiaoxian.trade.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * UserScope作为Application scope的sub-scope，可以获取Application scope中的对象
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {

}
