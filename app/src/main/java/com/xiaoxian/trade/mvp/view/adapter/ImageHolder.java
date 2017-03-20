package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.xiaoxian.trade.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 构建适配器处理Banner的Item布局呈现效果
 */

public class ImageHolder implements Holder<String> {
    @BindView(R.id.item_banner_image)
    ImageView itemBannerImage;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner1, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        Glide.with(context)
                .load(data)
                .into(itemBannerImage);
    }
}
