package com.xiaoxian.trade.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.xiaoxian.trade.R;

public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = "BaseActivity";

    private SystemBarTintManager mSystemBarTintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //安卓版本4.4+有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initView(savedInstanceState);
        initData();
        initBar();
    }

    public abstract void initView(Bundle savedInstanceState);

    public abstract void initData();

    public void initBar() {
        mSystemBarTintManager = new SystemBarTintManager(this);
        mSystemBarTintManager.setStatusBarTintEnabled(true);
        mSystemBarTintManager.setStatusBarTintResource(R.color.colorTop);
    }
}
