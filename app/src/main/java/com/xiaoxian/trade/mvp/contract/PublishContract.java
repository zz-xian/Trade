package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;

import java.util.List;

public interface PublishContract {
    interface IView extends BaseView {
        void publishSuccess();

        void publishError(String str);

        void uploadSuccess(List<String> images);

        void uploadError(String str);
    }

    interface IPresenter extends BasePresenter {
        boolean publish(String title, String desc, List<String> images, String price, String kind, String subKind, String condition, String mount, String phone, String location, String city);

        boolean upload(String[] imagePaths);
    }
}
