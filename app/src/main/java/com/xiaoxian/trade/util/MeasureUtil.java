package com.xiaoxian.trade.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * 屏幕测量
 */

public class MeasureUtil {
    public static float getTextViewCenterX(TextView tv){
        float x = (tv.getLeft() + tv.getRight()) / 2;
        return x;
    }

    public static float getTextViewCenterY(TextView tv){
        float y = (tv.getTop() + tv.getBottom()) / 2;
        return y;
    }

    public static int getScreenWidth(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getBase3Width(int screenWidth){
        return (screenWidth - 2 * 5) / 3;
    }

    public static int getBase4Width(int screenWidth){
        return (screenWidth - 3 * 5) / 4;
    }

    public static int getBase5Width(int screenWidth){
        return (screenWidth - 4 * 5) / 5;
    }
}
