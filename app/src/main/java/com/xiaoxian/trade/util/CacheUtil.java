package com.xiaoxian.trade.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户信息缓存工具类
 */

public class CacheUtil {
    private final static String KEY = "history_record";
    private final static String NAME = "search_history";

    public static String getString(Context context, String key, String value) {
        //MODE_PRIVATE：指定数据只能被当前进行读写
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, value);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();//存入指定key对应数据
    }

    /**
     * 设置缓存
     */
    public static void setCache(Context context, String value) {
        setString(context, KEY, value);
    }

    /**
     * 获取缓存
     */
    public static String getCache(Context context){
        return getString(context, KEY, null);
    }

    /**
     * 清空指定缓存信息
     */
    public static void ClearCache(Context context){
        setString(context, KEY, null);
    }
}
