package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.MyPublishContract;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyPublishPresenter implements MyPublishContract.IPresenter {
    private MyPublishContract.IView mView;

    public MyPublishPresenter(MyPublishContract.IView view) {
        this.mView = view;
    }

    @Override
    public boolean queryMyPublish() {
        final User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", user.getObjectId());
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e == null) {
                    mView.loadMyPublish(user, list);
                } else {
                    mView.queryError(e.getLocalizedMessage());
                }
            }
        });
        return false;
    }

    @Override
    public void deleteMyPublish(String goodsId) {
        Goods goods = new Goods();
        goods.setObjectId(goodsId);
        goods.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.deleteSuccess();
                } else {
                    mView.deleteError(e.getLocalizedMessage());
                }
            }
        });
    }
}
