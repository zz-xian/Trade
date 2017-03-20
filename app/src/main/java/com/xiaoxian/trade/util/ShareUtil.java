package com.xiaoxian.trade.util;

import android.content.Context;
import android.content.Intent;

/**
 * 分享工具类
 */

public class ShareUtil {

    public static void shareLink(Context context, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "二次源君：将我分享出去，会有更多人知道喔O(∩_∩)O");
        context.startActivity(Intent.createChooser(intent, title));
    }
}
