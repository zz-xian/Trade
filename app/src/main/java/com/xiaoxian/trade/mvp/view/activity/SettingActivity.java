package com.xiaoxian.trade.mvp.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.mvp.view.activity.setting.AboutAppActivity;
import com.xiaoxian.trade.mvp.view.activity.setting.AboutMeActivity;
import com.xiaoxian.trade.mvp.view.activity.setting.FeedBackActivity;
import com.xiaoxian.trade.mvp.view.activity.setting.ModifyPassActivity;
import com.xiaoxian.trade.util.ShareUtil;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.setting_back)
    ImageView settingBack;
    @BindView(R.id.layout_about_me)
    RelativeLayout layoutAboutMe;
    @BindView(R.id.layout_about_app)
    RelativeLayout layoutAboutApp;
    @BindView(R.id.layout_modify_pass)
    RelativeLayout layoutModifyPass;
    @BindView(R.id.horizontal_view)
    View horizontalView;
    @BindView(R.id.layout_clear_cache)
    RelativeLayout layoutClearCache;
    @BindView(R.id.layout_feedback)
    RelativeLayout layoutFeedback;
    @BindView(R.id.layout_share)
    RelativeLayout layoutShare;
    @BindView(R.id.logout)
    Button logout;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        if (BmobUser.getCurrentUser() != null) {
            layoutModifyPass.setVisibility(View.VISIBLE);
            horizontalView.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        } else {
            layoutModifyPass.setVisibility(View.GONE);
            horizontalView.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.setting_back, R.id.layout_about_me, R.id.layout_about_app, R.id.layout_modify_pass,
            R.id.layout_clear_cache, R.id.layout_feedback, R.id.layout_share, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.layout_about_me:
                UIUtil.nextPage(this, AboutMeActivity.class);
                break;
            case R.id.layout_about_app:
                UIUtil.nextPage(this, AboutAppActivity.class);
                break;
            case R.id.layout_modify_pass:
                UIUtil.nextPage(this, ModifyPassActivity.class);
                break;
            case R.id.layout_clear_cache:
                BmobQuery.clearAllCachedResults();
                ToastUtil.showToast(this, "缓存清理成功");
                break;
            case R.id.layout_feedback:
                UIUtil.nextPage(this, FeedBackActivity.class);
                break;
            case R.id.layout_share:
                ShareUtil.shareLink(this, getString(R.string.str_share_title));
                break;
            case R.id.logout:
                BmobUser.logOut();
                UIUtil.nextPage(this, MainActivity.class);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
