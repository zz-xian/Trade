package com.xiaoxian.trade.mvp.presenter;

import com.xiaoxian.trade.mvp.contract.ProfileContract;
import com.xiaoxian.trade.mvp.model.User;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ProfilePresenter implements ProfileContract.IPresenter{
    private ProfileContract.IView mView;

    public ProfilePresenter(ProfileContract.IView view) {
        this.mView = view;
    }

    @Override
    public void uploadHead(final String userId, String path) {
        final BmobFile file = new BmobFile(new File(path + "head.png"));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    User user = new User();
                    user.setAvatar(file);
                    user.setObjectId(userId);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                mView.updateHeadSuccess(file);
                            } else {
                                mView.updateHeadError(e.getLocalizedMessage());
                            }
                        }
                    });
                } else {
                    mView.uploadHeadError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void updateName(String userId, final String value) {
        User user = new User();
        user.setUsername(value);
        user.setObjectId(userId);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.updateNameSuccess(value);
                } else {
                    mView.updateNameError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void updateAge(String userId, final String value) {
        User user = new User();
        user.setAge(Integer.parseInt(value));
        user.setObjectId(userId);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.updateAgeSuccess(value);
                } else {
                    mView.updateAgeError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void updateSign(String userId, final String value) {
        User user = new User();
        user.setSignature(value);
        user.setObjectId(userId);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.updateSignSuccess(value);
                } else {
                    mView.updateSignError(e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void updateSex(String userId, int which) {
        final User user = new User();
        if (which == 0) {
            user.setSex(true);
        } else {
            user.setSex(false);
        }
        user.setObjectId(userId);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mView.updateSexSuccess(user.getSex());
                } else {
                    mView.updateSexError(e.getLocalizedMessage());
                }
            }
        });
    }
}
