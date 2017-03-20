package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.util.TimeUtil;
import com.xiaoxian.trade.widget.CircleImageView;

import java.util.List;

public class CustomAdapter implements ExpandableListAdapter {
    private Context mContext;
    private List<User> userList;
    private List<Goods> goodsList;

    public CustomAdapter(Context mContext, List<User> userList, List<Goods> goodsList) {
        this.mContext = mContext;
        this.userList = userList;
        this.goodsList = goodsList;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        //获取分组个数
        return goodsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //获取指定分组中子选项个数
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        //获取指定分组数据
        return goodsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //获取指定分组中指定子选项数据
        return goodsList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        //获取指定分组ID, 这个ID必须唯一
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        //获取子选项ID, 这个ID必须唯一
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        //分组和子选项是否持有稳定ID, 即底层数据改变会否影响到它们
        return true;
    }

    private static class ParentHolder {
        CircleImageView mCircleImageView;
        TextView mUserName, mPrice, mDesc;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //获取显示指定分组视图
        ParentHolder parentHolder;
        if (convertView == null) {
            parentHolder = new ParentHolder();
            LayoutInflater parentInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = parentInflater.inflate(R.layout.item_parent, null);
            parentHolder.mCircleImageView = (CircleImageView) convertView.findViewById(R.id.user_head);
            parentHolder.mUserName = (TextView) convertView.findViewById(R.id.user_name);
            parentHolder.mPrice = (TextView) convertView.findViewById(R.id.goods_price);
            parentHolder.mDesc = (TextView) convertView.findViewById(R.id.goods_desc);
            convertView.setHorizontalScrollBarEnabled(true);
            convertView.setTag(parentHolder);
        } else {
            parentHolder = (ParentHolder) convertView.getTag();
        }
        final CircleImageView civ = parentHolder.mCircleImageView;
        if (userList.get(groupPosition).getAvatar() != null) {
            Glide.with(mContext)
                    .load(userList.get(groupPosition).getAvatar().getUrl())
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
            parentHolder.mCircleImageView.setImageResource(R.mipmap.ic_head);
        }
        parentHolder.mUserName.setText(userList.get(groupPosition).getUsername());
        parentHolder.mPrice.setText("￥" + goodsList.get(groupPosition).getPrice());
        parentHolder.mDesc.setText(goodsList.get(groupPosition).getDesc());
        return convertView;
    }

    private static class ChildHolder {
        static RecyclerView mHorizontalList;
        static TextView mLocation, mTime;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //获取显示指定分组中指定子选项视图
        ChildHolder childHolder;
        if (goodsList.get(groupPosition).getImages() == null){
            return new View(mContext);
        }
        if (convertView == null) {
            childHolder = new ChildHolder();
            LayoutInflater childInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInflater.inflate(R.layout.item_child, parent, false);
            childHolder.mHorizontalList = (RecyclerView) convertView.findViewById(R.id.goods_image);
            childHolder.mLocation = (TextView)convertView.findViewById(R.id.location_text);
            childHolder.mTime = (TextView)convertView.findViewById(R.id.time_text);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        childHolder.mHorizontalList.setLayoutManager(manager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mContext, goodsList.get(groupPosition).getImages());
        childHolder.mHorizontalList.setAdapter(adapter);
        childHolder.mLocation.setText(goodsList.get(groupPosition).getLocation());
        childHolder.mTime.setText(TimeUtil.convertBefore(goodsList.get(groupPosition).getCreatedAt()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //指定位置上子元素是否可选
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
