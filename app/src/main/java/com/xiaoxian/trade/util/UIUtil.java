package com.xiaoxian.trade.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Map;

/**
 * UI相关处理
 */

public class UIUtil {
    public static void nextPage(Context context, Class<? extends Activity> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public static void nextPage(Context context, Class<? extends Activity> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * Map.Entry是Map一个内部接口，Entry<K,V>
     */
    public static void nextPage(Context context, Class<? extends Activity> cls, Map.Entry<String, String> entry) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(entry.getKey(), entry.getValue());
        context.startActivity(intent);
    }

    public static void nextPage(Context context, Class<? extends Activity> cls, Map.Entry<String, String> entry, int flag) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.addFlags(flag);
        intent.putExtra(entry.getKey(), entry.getValue());
        context.startActivity(intent);
    }

    /**
     * keySet()返回Map中key值集合，value=map.get(key)
     */
    public static void nextPage(Context context, Class<? extends Activity> cls, Map<String, String> map) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        for (String key : map.keySet()) {
            intent.putExtra(key, map.get(key));
        }
        context.startActivity(intent);
    }
}
