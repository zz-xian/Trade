package com.xiaoxian.trade.mvp.presenter;

import android.util.Log;

import com.xiaoxian.trade.mvp.contract.PublishContract;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PublishPresenter implements PublishContract.IPresenter {
    private PublishContract.IView mView;

    public static final String TAG = "PublishPresenter";

    public PublishPresenter(PublishContract.IView view) {
        this.mView = view;
    }

    @Override
    public boolean publish(String title, String desc, List<String> images, String price, String kind, String subKind, String condition, String mount, String phone, String location, String city) {
        User user = BmobUser.getCurrentUser(User.class);
        Goods goods = new Goods();
        goods.setUserId(user.getObjectId());
        goods.setTitle(title);
        goods.setDesc(desc);
        goods.setImages(images);
        goods.setPrice(price);
        goods.setKind(kind);
        goods.setSubKind(subKind);
        goods.setCondition(condition);
        goods.setMount(mount);
        goods.setPhone(phone);
        goods.setLocation(location);
        goods.setCity(city);
        goods.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    mView.publishSuccess();
                } else {
                    mView.publishError(e.getLocalizedMessage());
                }
            }
        });
        return false;
    }

    @Override
    public boolean upload(final String[] imagePaths) {
        //批量上传
        BmobFile.uploadBatch(imagePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //files--上传后的数据，可以保存到表
                if (urls.size() == imagePaths.length) {//数量相等，代表文件全部上传完成
                    mView.uploadSuccess(urls);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                Log.e(TAG, curIndex + "    " + curPercent);
            }

            @Override
            public void onError(int statusCode, String errorMsg) {
                mView.uploadError(errorMsg);
            }
        });
        return true;
    }
}
