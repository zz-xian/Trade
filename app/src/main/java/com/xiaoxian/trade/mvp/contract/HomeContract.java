package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Banner;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.List;

public interface HomeContract {
    interface IView extends BaseView {
        void showBanner(List<Banner> bannerList);

        void loadGoodsSuccess(List<Goods> goodsList);

        void loadGoodsError(String str);

        void parseUser(List<User> userList);
    }

    interface IPresenter extends BasePresenter {
        void loadBanner(List<Banner> bannerList);

        void loadGoods();

        void parseGoodsUser(List<Goods> goodsList);
    }
}
