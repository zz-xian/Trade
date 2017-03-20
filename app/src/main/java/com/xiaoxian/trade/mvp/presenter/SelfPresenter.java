package com.xiaoxian.trade.mvp.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoxian.trade.mvp.contract.SelfContract;

public class SelfPresenter implements SelfContract.IPresenter {
    private SelfContract.IView mView;

    public SelfPresenter(SelfContract.IView view) {
        this.mView = view;
    }

    @Override
    public void loadPicture(Context context, String url) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    //将图片呈现在ImageView
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        mView.showPicture(bitmap);
                    }
                });
    }
}
