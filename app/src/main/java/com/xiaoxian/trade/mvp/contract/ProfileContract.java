package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;

import cn.bmob.v3.datatype.BmobFile;

public interface ProfileContract {
    interface IView extends BaseView {
        void updateHeadSuccess(BmobFile bmobFile);

        void updateHeadError(String str);

        void uploadHeadError(String str);

        void updateNameSuccess(String value);

        void updateNameError(String str);

        void updateAgeSuccess(String value);

        void updateAgeError(String str);

        void updateSignSuccess(String value);

        void updateSignError(String str);

        void updateSexSuccess(Boolean sex);

        void updateSexError(String str);
    }

    interface IPresenter extends BasePresenter {
        void uploadHead(String userId, String path);

        void updateName(String userId, String value);

        void updateAge(String userId, String value);

        void updateSign(String userId, String value);

        void updateSex(String userId, int which);
    }
}
