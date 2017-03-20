package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.MyCollectContract;
import com.xiaoxian.trade.mvp.model.Collect;
import com.xiaoxian.trade.mvp.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyCollectPresenter implements MyCollectContract.IPresenter{
    private MyCollectContract.IView mView;

    public MyCollectPresenter(MyCollectContract.IView view) {
        this.mView = view;
    }

    @Override
    public boolean queryMyCollect() {
        final User currentUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<Collect> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", currentUser.getObjectId());
        query.findObjects(new FindListener<Collect>() {
            @Override
            public void done(final List<Collect> list, BmobException e) {
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
                                    mView.loadMyCollect(userList, list);
                                }
                            }
                        }
                    });
                } else {
                    mView.queryError(e.getLocalizedMessage());
                }
            }
        });
        return false;
    }

    @Override
    public void cancelMyCollect(String collectId) {
        Collect collect = new Collect();
        collect.setObjectId(collectId);
        collect.delete(new UpdateListener() {
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
