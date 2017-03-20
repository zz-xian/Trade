package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.List;

public interface NearContract {
    interface IView extends BaseView {
        void loadGoodsSuccess(List<Goods> goodsList);

        void loadGoodsError(String str);

        void parseUser(List<User> userList);
    }

    interface IPresenter extends BasePresenter {
        void loadGoods(String city);

        void parseGoodsUser(List<Goods> goodsList);
    }
}
