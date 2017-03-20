package com.xiaoxian.trade.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaoxian.trade.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    private Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initAnim();
        initData();
    }

    private void initAnim() {
        //换用资源动画，因为Glide.animate(Animation animation)已经弃用
        animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        ivSplash.startAnimation(animation);

        //缩放动画监听
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                //结束启动界面
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void initData() {
        Glide.with(this)
                .load(R.mipmap.ic_splash)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .animate(R.anim.scale)
                .into(ivSplash);
    }
}
