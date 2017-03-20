package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.List;

public interface MyPublishContract {
    interface IView extends BaseView {
        void loadMyPublish(User user, List<Goods> goodsList);

        void queryError(String str);

        void deleteSuccess();

        void deleteError(String str);
    }

    interface IPresenter extends BasePresenter {
        //查找是否存在发布商品
        boolean queryMyPublish();
        //删除商品
        void deleteMyPublish(String goodsId);
    }
}
