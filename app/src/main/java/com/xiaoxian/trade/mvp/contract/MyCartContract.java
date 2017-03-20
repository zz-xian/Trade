package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Cart;
import com.xiaoxian.trade.mvp.model.User;

import java.util.List;

public interface MyCartContract {
    interface IView extends BaseView {
        void loadMyCart(List<User> userList, List<Cart> cartList);

        void queryError(String str);

        void cancelSuccess();

        void cancelError(String str);
    }

    interface IPresenter extends BasePresenter {
        //查找是否存在收藏商品
        boolean queryMyCart();
        //取消收藏商品
        void cancelMyCart(String cartId);
    }
}
