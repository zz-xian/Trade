package com.xiaoxian.trade.mvp.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * PagerAdapter与ViewPager配合使用，是ViewPager的适配器
 * 功能：实现控件滑动效果（如广告栏）
 * 必须重写4个方法：
 * 1、instantiateItem(ViewGroup,int)
 * 2、destroyItem(ViewGroup,int,Object)
 * 3、getCount()
 * 4、isViewFromObject(View,Object)
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> viewList;

    public ViewPagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    //为给定位置创建相应View，创建完成需将View自行添加到container中
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    //为给定位置移除相应View
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public int getCount() {
        if (viewList == null) {
            return 0;
        }
        return viewList.size();
    }

    //确认View与实例对象是否相互对应，ViewPager内部用于获取View对应的ItemInfo
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
