package com.xiaoxian.trade.mvp.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerLoginActivityComponent;
import com.xiaoxian.trade.di.module.LoginActivityModule;
import com.xiaoxian.trade.mvp.contract.LoginContract;
import com.xiaoxian.trade.util.FormatUtil;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;

import org.json.JSONObject;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends BaseActivity implements LoginContract.IView {
    @BindView(R.id.login_user_name)
    EditText loginUserName;
    @BindView(R.id.login_user_pass)
    EditText loginUserPass;
    @BindView(R.id.login_error)
    TextView forgetPass;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.weibo_login)
    ImageView weiboLogin;
    @Inject
    LoginContract.IPresenter presenter;

    private UMShareAPI mUmShareAPI;

    public static final String TAG = "LoginActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());

        mUmShareAPI = UMShareAPI.get(this);//初始化sdk
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerLoginActivityComponent.builder()
                .appComponent(appComponent)
                .loginActivityModule(new LoginActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.login_button, R.id.login_error, R.id.register, R.id.weibo_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                if (!FormatUtil.isNullOrEmpty(loginUserName.getText().toString()) && !FormatUtil.isNullOrEmpty(loginUserPass.getText().toString())) {
                    presenter.login(loginUserName.getText().toString(), loginUserPass.getText().toString());
                } else {
                    ToastUtil.showToast(this, "用户名和密码不能为空");
                }
                break;
            case R.id.login_error:
                UIUtil.nextPage(this, ForgetActivity.class);
                break;
            case R.id.register:
                UIUtil.nextPage(this, RegisterActivity.class);
                break;
            case R.id.weibo_login:
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mUmShareAPI.doOauthVerify(this, platform, umAuthListener);
                break;
        }
    }

    /**
     * 微博授权登录
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int i, Map<String, String> map) {
            if (map != null) {
                mUmShareAPI.getPlatformInfo(LoginActivity.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        ToastUtil.showToast(getApplicationContext(), "授权成功");
                        String uid = map.get("uid");
                        String expires_in = map.get("expires_in");
                        String access_token = map.get("access_token");

//                        final String name = map.get("screen_name");
//                        final String img_url = map.get("profile_image_url");

                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_WEIBO, access_token, expires_in, uid);
                        BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
                            @Override
                            public void done(JSONObject userAuth,BmobException e) {
                                if (e == null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("WorkMode", "Self");
                                    UIUtil.nextPage(LoginActivity.this, MainActivity.class, bundle);
                                    ToastUtil.showToast(getApplicationContext(), "登录成功");
//                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    intent.putExtra("name", name);
//                                    intent.putExtra("img_url", img_url);
//                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int i, Throwable throwable) {
            ToastUtil.showToast(getApplicationContext(), "授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int i) {
            ToastUtil.showToast(getApplicationContext(), "授权取消");
        }
    };

    @Override
    public void loginSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString("WorkMode", "Self");
        UIUtil.nextPage(this, MainActivity.class, bundle);
        ToastUtil.showToast(this, "登录成功");
        finish();
    }

    @Override
    public void loginError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, str);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
