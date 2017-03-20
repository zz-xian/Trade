package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Collect;
import com.xiaoxian.trade.mvp.model.User;

import java.util.List;

public interface MyCollectContract {
    interface IView extends BaseView {
        void loadMyCollect(List<User> userList, List<Collect> collectList);

        void queryError(String str);

        void cancelSuccess();

        void cancelError(String str);
    }

    interface IPresenter extends BasePresenter {
        //查找是否存在收藏商品
        boolean queryMyCollect();
        //取消收藏商品
        void cancelMyCollect(String collectId);
    }
}
