package com.xiaoxian.trade.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoxian.trade.R;

public class ToastUtil {
    private static TextView tvToast;
    protected static Toast toast = null;

    public static void showToast(Context context, String msg){
        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);

            View view = LayoutInflater.from(context).inflate(R.layout.item_toast, null);
            tvToast = (TextView) view.findViewById(R.id.tvToast);
            toast.setView(view);
            tvToast.setText(msg);
            toast.show();
        } else {
            tvToast.setText(msg);
            toast.show();
        }
    }

    public static void showToast(Context context, int resId){
        showToast(context, context.getString(resId));
    }
}
