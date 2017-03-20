package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.List;

public interface ResultContract {
    interface IView extends BaseView {
        void showKeyResult(List<Goods> goodsList);

        void showSubKindResult(List<Goods> goodsList);

        void loadGoodsError(String str);

        void parseUser(List<User> userList);
    }

    interface IPresenter extends BasePresenter {
        //关键字查询
        void queryGoodsByKey(String key);

        void queryGoodsByKind(String kind);

        void queryGoodsBySubKind(String subKind);
        //找出商品对应User
        void parseGoodsUser(List<Goods> goodsList);
    }
}
