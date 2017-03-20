package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.ForgetContract;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPresenter implements ForgetContract.IPresenter {
    private ForgetContract.IView mView;

    public ForgetPresenter(ForgetContract.IView view) {
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
    public void modifyPassword(String check, String password) {
        BmobUser.resetPasswordBySMSCode(check, password, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.foundSuccess();
                } else {
                    mView.foundError(e.getLocalizedMessage());
                }
            }
        });
    }
}
