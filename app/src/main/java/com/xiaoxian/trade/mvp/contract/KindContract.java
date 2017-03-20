package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Kind;
import com.xiaoxian.trade.mvp.model.SubKind;

import java.util.List;

public interface KindContract {
    interface IView extends BaseView {
        void showKind(List<Kind> kindList);

        void showSubKind(List<SubKind> subKindList);

        void loadError(String str);
    }

    interface IPresenter extends BasePresenter {
        void loadKind();

        void loadSubKind(Kind kind);
    }
}
