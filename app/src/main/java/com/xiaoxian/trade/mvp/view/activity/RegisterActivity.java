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
import com.xiaoxian.trade.di.component.DaggerRegisterActivityComponent;
import com.xiaoxian.trade.di.module.RegisterActivityModule;
import com.xiaoxian.trade.mvp.contract.RegisterContract;
import com.xiaoxian.trade.util.FormatUtil;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements RegisterContract.IView{
    @BindView(R.id.register_back)
    ImageView registerBack;
    @BindView(R.id.register_input_phone)
    TextView registerInputPhone;
    @BindView(R.id.register_input_verify)
    TextView registerInputVerify;
    @BindView(R.id.register_set_password)
    TextView registerSetPassword;
    @BindView(R.id.register_edit_1)
    CardView registerEdit1;
    @BindView(R.id.register_edit_phone)
    EditText registerEditPhone;
    @BindView(R.id.register_edit_2)
    CardView registerEdit2;
    @BindView(R.id.register_edit_verify)
    EditText registerEditVerify;
    @BindView(R.id.register_edit_3)
    CardView registerEdit3;
    @BindView(R.id.register_edit_user)
    EditText registerEditUser;
    @BindView(R.id.register_edit_4)
    CardView registerEdit4;
    @BindView(R.id.register_edit_pass)
    EditText registerEditPass;
    @BindView(R.id.register_edit_5)
    CardView registerEdit5;
    @BindView(R.id.register_confirm_pass)
    EditText registerConfirmPass;
    @BindView(R.id.register_finish)
    Button registerFinish;
    @Inject
    RegisterContract.IPresenter presenter;

    private String phone;
    private int workMode = 1;

    public static final String TAG = "RegisterActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerRegisterActivityComponent.builder()
                .appComponent(appComponent)
                .registerActivityModule(new RegisterActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        switch (workMode){
            case 1:
                processInputPhone();
                break;
            case 2:
                processInputVerify();
                break;
            case 3:
                processSetPassword();
                break;
        }
    }

    @OnClick({R.id.register_back, R.id.register_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                if (workMode == 1){
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }else if (workMode == 2){
                    processInputPhone();
                    workMode = 1;
                }else if (workMode == 3){
                    processInputPhone();
                    workMode = 1;
                }
                break;
            case R.id.register_finish:
                if (workMode == 1){
                    workMode = 2;
                    phone = registerEditPhone.getText().toString();
                    if (FormatUtil.checkPhone(phone)){
                        presenter.sendSMSCode(phone);
                    }else{
                        ToastUtil.showToast(this, "手机号格式错误");
                    }
                }else if (workMode == 2){
                    if (!FormatUtil.isNullOrEmpty(registerEditVerify.getText().toString())) {
                        presenter.verifySMSCode(phone, registerEditVerify.getText().toString());
                    } else {
                        ToastUtil.showToast(this, "验证码错误");
                    }
                }else if (workMode == 3){
                    if (FormatUtil.isNullOrEmpty(registerEditUser.getText().toString())){
                        ToastUtil.showToast(this, "输入用户名");
                    }
                    if (registerEditPass.getText().toString().equals("") || registerConfirmPass.getText().toString().equals("")){
                        ToastUtil.showToast(this, "输入密码或再次确认密码");
                    }else if (!registerEditPass.getText().toString().equals(registerConfirmPass.getText().toString())){
                        ToastUtil.showToast(this, "两次密码输入不一致！");
                    }else if (!FormatUtil.isCorrectPassword(registerEditPass.getText().toString())){
                        ToastUtil.showToast(this, "密码应为6-18位，例如：字母 + 数字 + 下划线");
                    }else {
                        presenter.register(phone, registerEditUser.getText().toString(), registerEditPass.getText().toString());
                    }
                }
                break;
        }
    }

    public void processInputPhone() {
        registerInputPhone.setSelected(true);
        registerInputVerify.setSelected(false);
        registerSetPassword.setSelected(false);
        registerEdit1.setVisibility(View.VISIBLE);
        registerEdit2.setVisibility(View.GONE);
        registerEdit3.setVisibility(View.GONE);
        registerEdit4.setVisibility(View.GONE);
        registerEdit5.setVisibility(View.GONE);
        registerFinish.setText(R.string.str_get_verify);
    }

    public void processInputVerify(){
        registerInputPhone.setSelected(false);
        registerInputVerify.setSelected(true);
        registerSetPassword.setSelected(false);
        registerEdit1.setVisibility(View.GONE);
        registerEdit2.setVisibility(View.VISIBLE);
        registerEdit3.setVisibility(View.GONE);
        registerEdit4.setVisibility(View.GONE);
        registerEdit5.setVisibility(View.GONE);
        registerFinish.setText(R.string.str_verify);
    }

    public void processSetPassword(){
        registerInputPhone.setSelected(false);
        registerInputVerify.setSelected(false);
        registerSetPassword.setSelected(true);
        registerEdit1.setVisibility(View.GONE);
        registerEdit2.setVisibility(View.GONE);
        registerEdit3.setVisibility(View.VISIBLE);
        registerEdit4.setVisibility(View.VISIBLE);
        registerEdit5.setVisibility(View.VISIBLE);
        registerFinish.setText(R.string.str_register);
    }

    @Override
    public void getSMSCodeSuccess() {
        ToastUtil.showToast(this, "发送成功");
        processInputVerify();
    }

    @Override
    public void getSMSCodeError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "发送失败");
        processInputPhone();
    }

    @Override
    public void verifyCorrect() {
        workMode = 3;
        processSetPassword();
    }

    @Override
    public void verifyError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "验证失败");
    }

    @Override
    public void registerSuccess() {
        ToastUtil.showToast(this, "注册成功，立即登录...");
        UIUtil.nextPage(this, LoginActivity.class);
        finish();
    }

    @Override
    public void registerError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "注册失败");
        processInputPhone();
    }
}
