package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.mvp.model.Icon;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private List<Icon> iconList;
    private LayoutInflater inflater;
    //页数下标(从0开始)
    private int curIndex;
    //每页显示个数
    private int pageSize;

    public GridViewAdapter(Context context, List<Icon> iconList, int curIndex, int pageSize) {
        inflater = LayoutInflater.from(context);
        this.iconList = iconList;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
    }

    /**
     * 先判断图标集大小是否足够铺满本页：iconList.size() > (curIndex+1)*pageSize,
     * 够则直接返回每页显示最大条目个数pageSize，不够则有几项返回几项(iconList.size() - curIndex * pageSize——最后一页显示剩余item)
     */
    @Override
    public int getCount() {
        return iconList.size() > (curIndex + 1) * pageSize ? pageSize : (iconList.size() - curIndex * pageSize);
    }

    @Override
    public Object getItem(int position) {
        return iconList.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_grid, parent, false);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_item);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //给View绑定显示数据时，计算正确position
        int pos = position + curIndex * pageSize;
        viewHolder.iv.setImageResource(iconList.get(pos).iconRes);
        viewHolder.tv.setText(iconList.get(pos).name);
        return convertView;
    }
}
