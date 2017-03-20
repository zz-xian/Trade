package com.xiaoxian.trade.mvp.view.activity.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutAppActivity extends BaseActivity {
    @BindView(R.id.about_app_back)
    ImageView aboutAppBack;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_app);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        tvVersion.setText("Version " + getVersion());
    }

    private String getVersion() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getString(R.string.str_version);
        }
    }

    @OnClick(R.id.about_app_back)
    public void onClick() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
