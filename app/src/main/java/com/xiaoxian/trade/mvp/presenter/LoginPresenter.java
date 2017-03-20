package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.LoginContract;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.util.FormatUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginPresenter implements LoginContract.IPresenter {
    private LoginContract.IView mView;

    public LoginPresenter(LoginContract.IView view) {
        this.mView = view;
    }

    @Override
    public void login(String name, String password) {
        User user = new User();
        if (FormatUtil.isMobileNO(name)) {
            if (FormatUtil.isCorrectPassword(password)) {
                user.setMobilePhoneNumber(name);
                user.setPassword(password);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            mView.loginSuccess();
                        } else {
                            mView.loginError("登录失败");
                        }
                    }
                });
            }else {
                mView.loginError("密码含有非法字符");
            }
        }else {
            if (FormatUtil.isCorrectPassword(password)) {
                user.setUsername(name);
                user.setPassword(password);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            mView.loginSuccess();
                        } else {
                            mView.loginError("登录失败");
                        }
                    }
                });
            }else {
                mView.loginError("密码含有非法字符");
            }
        }
    }
}
