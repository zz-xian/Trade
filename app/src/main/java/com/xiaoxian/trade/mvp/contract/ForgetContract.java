package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;

public interface ForgetContract {
    interface IView extends BaseView {
        void getSMSCodeSuccess();

        void getSMSCodeError(String str);

        void verifyCorrect();

        void verifyError(String str);

        void foundSuccess();

        void foundError(String str);
    }

    interface IPresenter extends BasePresenter {
        void sendSMSCode(String phone);

        void verifySMSCode(String phone, String check);

        void modifyPassword(String phone, String password);
    }
}
