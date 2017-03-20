package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;

public interface RegisterContract {
    interface IView extends BaseView {
        void getSMSCodeSuccess();

        void getSMSCodeError(String str);

        void verifyCorrect();

        void verifyError(String str);

        void registerSuccess();

        void registerError(String str);
    }

    interface IPresenter extends BasePresenter {
        void sendSMSCode(String phone);

        void verifySMSCode(String phone, String check);

        void register(String phone, String name, String password);
    }
}
