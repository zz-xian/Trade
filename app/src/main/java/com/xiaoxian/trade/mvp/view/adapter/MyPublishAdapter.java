package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.activity.own.MyPublishActivity;
import com.xiaoxian.trade.widget.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

public class MyPublishAdapter extends BaseAdapter{
    private Context mContext;
    private List<Goods> goodsList;
    private LayoutInflater inflater;
    private MyPublishActivity activity;

    //控制CheckBox选中状况
    public Map<Integer, Boolean> checkMap;
    //是否处于管理状态
    private boolean isVisible = false;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public MyPublishAdapter(Context context, List<Goods> goodsList) {
        this.mContext = context;
        this.goodsList = goodsList;
        activity = (MyPublishActivity) context;
        inflater = LayoutInflater.from(context);
        checkMap = new HashMap<>();
        initDate();
    }

    //初始化选中状态
    private void initDate() {
        for (int i = 0; i < goodsList.size(); i++) {
            checkMap.put(i, false);
        }
    }

    public void removeData(Goods goods) {
        goodsList.remove(goods);
    }

    @Override
    public int getCount() {
        return goodsList != null ? goodsList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public CheckBox checkBox;
        CircleImageView userHead;
        TextView userName, price, desc;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_common, parent, false);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.single_check);
            viewHolder.userHead = (CircleImageView) convertView.findViewById(R.id.user_head);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.goods_price);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.goods_desc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isVisible) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(checkMap.get(position));
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkMap.put(position, true);
                    } else {
                        checkMap.put(position, false);
                    }
                    //当每次选中发生变化时调用进行判断
                    activity.getCheckData();
                }
            });
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.checkBox.setChecked(false);
        }

        User user = BmobUser.getCurrentUser(User.class);
        final CircleImageView civ = viewHolder.userHead;
        if (user.getAvatar() != null) {
            Glide.with(mContext)
                    .load(user.getAvatar().getUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() { //实现图文混排
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                            civ.setImageDrawable(drawable);
                            civ.postInvalidate();//刷新
                        }
                    });
        } else {
            viewHolder.userHead.setImageResource(R.mipmap.ic_head);
        }
        viewHolder.userName.setText(user.getUsername());
        viewHolder.price.setText("￥" + goodsList.get(position).getPrice());
        viewHolder.desc.setText(goodsList.get(position).getDesc());
        return convertView;
    }
}
