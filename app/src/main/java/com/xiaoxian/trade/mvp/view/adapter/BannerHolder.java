package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.mvp.model.Banner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerHolder implements Holder<Banner> {
    @BindView(R.id.iv_banner)
    ImageView ivBanner;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner2, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, Banner data) {
        Glide.with(context)
                .load(data.getFile().getUrl())
                .into(ivBanner);
    }
}
