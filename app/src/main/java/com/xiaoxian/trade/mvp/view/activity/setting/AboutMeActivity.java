package com.xiaoxian.trade.mvp.view.activity.setting;

import android.os.Bundle;
import android.widget.ImageView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutMeActivity extends BaseActivity {
    @BindView(R.id.about_me_back)
    ImageView aboutMeBack;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.about_me_back)
    public void onClick() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
