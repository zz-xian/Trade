package com.xiaoxian.trade.mvp.view.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.mvp.model.Feedback;
import com.xiaoxian.trade.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.feedback_back)
    ImageView feedbackBack;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.submit)
    Button submit;

    private static String msg;

    public static final String TAG = "FeedBackActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.feedback_back, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback_back:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.submit:
                String content = etContent.getText().toString();
                String contacts = etContact.getText().toString();
                if (!TextUtils.isEmpty(contacts)) {
                    if (!TextUtils.isEmpty(content)) {
                        if (content.equals(msg)) {
                            ToastUtil.showToast(this, "请勿重复提交反馈");
                        } else {
                            msg = content;
                            //发送反馈信息
                            saveMessageWithContacts(content, contacts);
                            ToastUtil.showToast(this, "反馈信息发送成功");
                        }
                    } else {
                        ToastUtil.showToast(this, "反馈内容不能为空");
                    }
                } else {
                    if(!TextUtils.isEmpty(content)){
                        if(content.equals(msg)){
                            ToastUtil.showToast(this, "请勿重复提交反馈");
                        }else {
                            msg = content;
                            //发送反馈信息
                            saveMessageWithoutContacts(content);
                            ToastUtil.showToast(this, "反馈信息发送成功");
                        }
                    }else {
                        ToastUtil.showToast(this, "反馈内容不能为空");
                    }
                }
                break;
        }
    }

    //保存反馈信息到服务器（有联系方式）
    private void saveMessageWithContacts(String content, String contacts) {
        Feedback feedback = new Feedback();
        feedback.setContent(content);
        feedback.setContacts(contacts);
        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "保存反馈信息成功");
                } else {
                    Log.e(TAG, "保存反馈信息失败：" + e.getLocalizedMessage());
                }
            }
        });
    }

    //保存反馈信息到服务器（无联系方式）
    private void saveMessageWithoutContacts(String content) {
        Feedback feedback = new Feedback();
        feedback.setContent(content);
        feedback.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "保存反馈信息成功");
                } else {
                    Log.e(TAG, "保存反馈信息失败：" + e.getLocalizedMessage());
                }
            }
        });
    }
}
