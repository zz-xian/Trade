package com.xiaoxian.trade.mvp.view.activity.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.util.FormatUtil;
import com.xiaoxian.trade.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyPassActivity extends BaseActivity {
    @BindView(R.id.modify_pass_back)
    ImageView modifyPassBack;
    @BindView(R.id.et_input_old_pass)
    EditText etInputOldPass;
    @BindView(R.id.et_input_new_pass)
    EditText etInputNewPass;
    @BindView(R.id.confirm)
    Button confirm;

    public static final String TAG = "ModifyPassActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify_pass);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.modify_pass_back, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modify_pass_back:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.confirm:
                if (etInputOldPass.getText().toString().equals("") || etInputNewPass.getText().toString().equals("")) {
                    ToastUtil.showToast(this, "旧密码或新密码不能为空");
                } else if (!FormatUtil.isCorrectPassword(etInputNewPass.getText().toString())) {
                    ToastUtil.showToast(this, "密码应为6-18位，例如：字母 + 数字 + 下划线");
                } else {
                    confirmModify();
                }
                break;
        }
    }

    private void confirmModify() {
        BmobUser.updateCurrentUserPassword(etInputOldPass.getText().toString(), etInputNewPass.getText().toString(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUtil.showToast(ModifyPassActivity.this, "密码修改成功");
                }else{
                    Log.e(TAG, e.getLocalizedMessage());
                    ToastUtil.showToast(ModifyPassActivity.this, "密码修改失败，请检查旧密码是否输入正确！");
                }
            }
        });
    }
}
