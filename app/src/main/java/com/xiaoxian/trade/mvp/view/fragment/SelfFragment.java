package com.xiaoxian.trade.mvp.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerMainActivityComponent;
import com.xiaoxian.trade.di.module.MainActivityModule;
import com.xiaoxian.trade.mvp.contract.SelfContract;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.activity.LoginActivity;
import com.xiaoxian.trade.mvp.view.activity.MainActivity;
import com.xiaoxian.trade.mvp.view.activity.ProfileActivity;
import com.xiaoxian.trade.mvp.view.activity.SettingActivity;
import com.xiaoxian.trade.mvp.view.activity.own.MyCartActivity;
import com.xiaoxian.trade.mvp.view.activity.own.MyCollectActivity;
import com.xiaoxian.trade.mvp.view.activity.own.MyPublishActivity;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;
import com.xiaoxian.trade.widget.CircleImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;

public class SelfFragment extends Fragment implements SelfContract.IView {
    @BindView(R.id.login_layout)
    LinearLayout loginLayout;
    @BindView(R.id.iv_login_head)
    CircleImageView ivLoginHead;
    @BindView(R.id.tv_login_name)
    TextView tvLoginName;
    @BindView(R.id.tv_login_signature)
    TextView tvLoginSignature;
    @BindView(R.id.no_login_layout)
    LinearLayout noLoginLayout;
    @BindView(R.id.no_login_btn)
    Button noLoginBtn;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.tv_cart)
    TextView tvCart;
    @BindView(R.id.setting_layout)
    RelativeLayout settingLayout;
    @Inject
    SelfContract.IPresenter presenter;

    private View mRootView;
    private Context mContext;
    private Unbinder unbinder;

    private final User currentUser = BmobUser.getCurrentUser(User.class);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_self, container, false);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        initComponent(((App) mContext.getApplicationContext()).getAppComponent());
        initData();
        return mRootView;
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder()
                .appComponent(appComponent)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    public void initData() {
        if (currentUser != null || getArguments() != null) {
            //已登录
            loginLayout.setVisibility(View.VISIBLE);
            noLoginLayout.setVisibility(View.GONE);
            if (currentUser.getAvatar() != null) {
                presenter.loadPicture(mContext, currentUser.getAvatar().getUrl());
            } else {
                ivLoginHead.setImageResource(R.mipmap.ic_default);
            }
            if (currentUser.getUsername() != null) {
                tvLoginName.setText(currentUser.getUsername());
            } else {
                tvLoginName.setText("未设置");
            }
            if (currentUser.getSignature() == null || currentUser.getSignature().equals("")) {
                tvLoginSignature.setText(R.string.str_signature);
            } else {
                tvLoginSignature.setText(currentUser.getSignature());
            }
        } else {
            //未登录
            loginLayout.setVisibility(View.GONE);
            noLoginLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showPicture(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        ivLoginHead.setImageDrawable(drawable);
    }

    @OnClick({R.id.login_layout, R.id.no_login_btn, R.id.tv_publish, R.id.tv_collect, R.id.tv_cart, R.id.setting_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_layout:
                UIUtil.nextPage(mContext, ProfileActivity.class);
                break;
            case R.id.no_login_btn:
                UIUtil.nextPage(mContext, LoginActivity.class);
                ((MainActivity) mContext).finish();
                break;
            case R.id.tv_publish:
                if (currentUser != null) {
                    UIUtil.nextPage(mContext, MyPublishActivity.class);
                } else {
                    ToastUtil.showToast(mContext, R.string.str_no_login);
                }
                break;
            case R.id.tv_collect:
                if (currentUser != null) {
                    UIUtil.nextPage(mContext, MyCollectActivity.class);
                } else {
                    ToastUtil.showToast(mContext, R.string.str_no_login);
                }
                break;
            case R.id.tv_cart:
                if (currentUser != null) {
                    UIUtil.nextPage(mContext, MyCartActivity.class);
                } else {
                    ToastUtil.showToast(mContext, R.string.str_no_login);
                }
                break;
            case R.id.setting_layout:
                UIUtil.nextPage(mContext, SettingActivity.class);
                break;
        }
    }
}
