package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.MyCartContract;
import com.xiaoxian.trade.mvp.model.Cart;
import com.xiaoxian.trade.mvp.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyCartPresenter implements MyCartContract.IPresenter {
    private MyCartContract.IView mView;

    public MyCartPresenter(MyCartContract.IView view) {
        this.mView = view;
    }

    @Override
    public boolean queryMyCart() {
        final User currentUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<Cart> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", currentUser.getObjectId());
        query.findObjects(new FindListener<Cart>() {
            @Override
            public void done(final List<Cart> list, BmobException e) {
                if (e == null) {
                    final List<User> userList = new ArrayList<>();
                    BmobQuery<User> query1 = new BmobQuery<>();
                    query1.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list1, BmobException e) {
                            if (e == null) {
                                for (int i = 0; i < list.size(); i++) {
                                    for (User user : list1) {
                                        if (user.getObjectId().endsWith(list.get(i).getUserId())) {
                                            userList.add(user);
                                        }
                                    }
                                }
                                if (userList.size() == list.size()) {
                                    mView.loadMyCart(userList, list);
                                }
                            }
                        }
                    });
                }else {
                    mView.queryError(e.getLocalizedMessage());
                }
            }
        });
        return false;
    }

    @Override
    public void cancelMyCart(String cartId) {
        Cart cart = new Cart();
        cart.setObjectId(cartId);
        cart.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.cancelSuccess();
                } else {
                    mView.cancelError(e.getLocalizedMessage());
                }
            }
        });
    }
}
