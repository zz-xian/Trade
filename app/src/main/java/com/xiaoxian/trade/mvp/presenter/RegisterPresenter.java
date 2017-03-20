package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.RegisterContract;
import com.xiaoxian.trade.mvp.model.User;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterPresenter implements RegisterContract.IPresenter {
    private RegisterContract.IView mView;

    public RegisterPresenter(RegisterContract.IView view) {
        this.mView = view;
    }

    @Override
    public void sendSMSCode(String phone) {
        BmobSMS.requestSMSCode(phone, "FoundPass", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    mView.getSMSCodeSuccess();
                } else {
                    mView.getSMSCodeError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void verifySMSCode(String phone, String check) {
        BmobSMS.verifySmsCode(phone, check, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.verifyCorrect();
                } else {
                    mView.verifyError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void register(String phone, String name, String password) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.setMobilePhoneNumber(phone);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    mView.registerSuccess();
                } else {
                    mView.registerError(e.getLocalizedMessage());
                }
            }
        });
    }
}
