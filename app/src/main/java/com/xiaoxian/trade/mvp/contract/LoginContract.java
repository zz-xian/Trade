package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;

public interface LoginContract {
    interface IView extends BaseView {
        void loginSuccess();

        void loginError(String str);
    }

    interface IPresenter extends BasePresenter {
        void login(String name, String password);
    }
}
