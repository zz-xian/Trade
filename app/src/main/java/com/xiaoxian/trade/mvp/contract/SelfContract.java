package com.xiaoxian.trade.mvp.contract;

import android.content.Context;
import android.graphics.Bitmap;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;

public interface SelfContract {
    interface IView extends BaseView {
        void showPicture(Bitmap bitmap);
    }

    interface IPresenter extends BasePresenter {
        void loadPicture(Context context, String url);
    }
}
