package com.xiaoxian.trade.mvp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerForgetActivityComponent;
import com.xiaoxian.trade.di.module.ForgetActivityModule;
import com.xiaoxian.trade.mvp.contract.ForgetContract;
import com.xiaoxian.trade.util.FormatUtil;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetActivity extends BaseActivity implements ForgetContract.IView {
    @BindView(R.id.forget_back)
    ImageView forgetBack;
    @BindView(R.id.forget_input_phone)
    TextView forgetInputPhone;
    @BindView(R.id.forget_input_verify)
    TextView forgetInputVerify;
    @BindView(R.id.forget_set_password)
    TextView forgetSetPassword;
    @BindView(R.id.forget_edit_1)
    CardView forgetEdit1;
    @BindView(R.id.forget_edit_phone)
    EditText forgetEditPhone;
    @BindView(R.id.forget_edit_2)
    CardView forgetEdit2;
    @BindView(R.id.forget_edit_verify)
    EditText forgetEditVerify;
    @BindView(R.id.forget_edit_3)
    CardView forgetEdit3;
    @BindView(R.id.forget_edit_pass)
    EditText forgetEditPass;
    @BindView(R.id.forget_edit_4)
    CardView forgetEdit4;
    @BindView(R.id.forget_confirm_pass)
    EditText forgetConfirmPass;
    @BindView(R.id.forget_finish)
    Button forgetFinish;
    @Inject
    ForgetContract.IPresenter presenter;

    private String phone;
    private String workMode = "InputPhone";

    public static final String TAG = "ForgetActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerForgetActivityComponent.builder()
                .appComponent(appComponent)
                .forgetActivityModule(new ForgetActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        if (workMode.equals("InputPhone")){
            processInputPhone();
        }else if (workMode.equals("InputVerify")){
            processInputVerify();
        }else if (workMode.equals("SetPassword")){
            processSetPassword();
        }
    }

    @OnClick({R.id.forget_back, R.id.forget_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_back:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.forget_finish:
                if (workMode.equals("InputPhone")) {
                    phone = forgetEditPhone.getText().toString();
                    if (FormatUtil.checkPhone(phone)) {
                        presenter.sendSMSCode(phone);
                    } else {
                        ToastUtil.showToast(this, "手机号格式错误");
                    }
                } else if (workMode.equals("InputVerify")) {
                    if (!FormatUtil.isNullOrEmpty(forgetEditVerify.getText().toString())) {
                        presenter.verifySMSCode(phone, forgetEditVerify.getText().toString());
                    } else {
                        ToastUtil.showToast(this, "验证码错误");
                    }
                } else if (workMode.equals("SetPassword")) {
                    if (forgetEditPass.getText().toString().equals(forgetConfirmPass.getText().toString())) {
                        presenter.modifyPassword(forgetEditVerify.getText().toString(), forgetEditPass.getText().toString());
                    } else {
                        ToastUtil.showToast(this, "两次密码输入不一致！");
                    }
                }
                break;
        }
    }

    public void processInputPhone() {
        forgetInputPhone.setSelected(true);
        forgetInputVerify.setSelected(false);
        forgetSetPassword.setSelected(false);
        forgetEdit1.setVisibility(View.VISIBLE);
        forgetEdit2.setVisibility(View.GONE);
        forgetEdit3.setVisibility(View.GONE);
        forgetEdit4.setVisibility(View.GONE);
        forgetFinish.setText(R.string.str_get_verify);
    }

    public void processInputVerify() {
        forgetInputPhone.setSelected(false);
        forgetInputVerify.setSelected(true);
        forgetSetPassword.setSelected(false);
        forgetEdit1.setVisibility(View.GONE);
        forgetEdit2.setVisibility(View.VISIBLE);
        forgetEdit3.setVisibility(View.GONE);
        forgetEdit4.setVisibility(View.GONE);
        forgetFinish.setText(R.string.str_verify);
    }

    public void processSetPassword() {
        forgetInputPhone.setSelected(false);
        forgetInputVerify.setSelected(false);
        forgetSetPassword.setSelected(true);
        forgetEdit1.setVisibility(View.GONE);
        forgetEdit2.setVisibility(View.GONE);
        forgetEdit3.setVisibility(View.VISIBLE);
        forgetEdit4.setVisibility(View.VISIBLE);
        forgetFinish.setText(R.string.str_confirm);
    }

    @Override
    public void getSMSCodeSuccess() {
        ToastUtil.showToast(this, "发送成功");
        workMode = "InputVerify";
        processInputVerify();
    }

    @Override
    public void getSMSCodeError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "发送失败");
    }

    @Override
    public void verifyCorrect() {
        workMode = "SetPassword";
        processSetPassword();
    }

    @Override
    public void verifyError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "验证失败");
    }

    @Override
    public void foundSuccess() {
        ToastUtil.showToast(this, "密码找回成功，请登录");
        UIUtil.nextPage(this, LoginActivity.class);
        finish();
    }

    @Override
    public void foundError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "密码找回失败，请重试");
        processInputPhone();
    }
}
