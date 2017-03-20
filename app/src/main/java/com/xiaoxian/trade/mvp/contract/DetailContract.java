package com.xiaoxian.trade.mvp.contract;

import com.xiaoxian.trade.base.BasePresenter;
import com.xiaoxian.trade.base.BaseView;
import com.xiaoxian.trade.mvp.model.Cart;
import com.xiaoxian.trade.mvp.model.Collect;
import com.xiaoxian.trade.mvp.model.Goods;

import java.util.List;

public interface DetailContract {
    interface IView extends BaseView {
        void queryCollectSuccess(List<Collect> collectList);

        void queryCollectError(String str);

        void queryCartSuccess(List<Cart> cartList);

        void queryCartError(String str);

        void saveCollectSuccess();

        void saveCollectError(String str);

        void saveCartSuccess();

        void saveCartError(String str);

        void deleteCollectSuccess();

        void deleteCollectError(String str);

        void deleteCartSuccess();

        void deleteCartError(String str);
    }

    interface IPresenter extends BasePresenter {
        void queryCollect(String userId);

        void queryCart(String userId);

        void saveCollect(String userId, Goods goods);

        void saveCart(String userId, Goods goods);

        void deleteCollect(String collectId);

        void deleteCart(String cartId);
    }
}
