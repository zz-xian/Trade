package com.xiaoxian.trade.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放所有list以便最后退出一起关闭
 */

public class PublicUtil {
    public static int num = 9;
    public static List<Activity> activityList = new ArrayList<>();
}
