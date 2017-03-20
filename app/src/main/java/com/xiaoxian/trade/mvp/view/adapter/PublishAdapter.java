package com.xiaoxian.trade.mvp.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.util.BitmapUtil;
import com.xiaoxian.trade.util.MeasureUtil;

public class PublishAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;

    private OnPublishItemMoveListener listener;

    //自定义ItemMove回调接口
    public interface OnPublishItemMoveListener {
        void onItemMove(View v,int position);
    }

    //定义监听器
    public void setOnPublishItemMoveListener(OnPublishItemMoveListener listener) {
        this.listener = listener;
    }

    public PublishAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (BitmapUtil.tempBitmap.size() == 9) {
            return 9;
        }
        return (BitmapUtil.tempBitmap.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        public ImageView move;
        public ImageView image;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_publish, parent, false);
            holder = new ViewHolder();
            //定义每个item大小
            int width = MeasureUtil.getBase5Width(MeasureUtil.getScreenWidth(mContext));
            convertView.setLayoutParams(new AbsListView.LayoutParams(width, width));
            holder.move = (ImageView) convertView.findViewById(R.id.item_move);
            holder.image = (ImageView) convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemMove(v, position);
            }
        });
        //临时列表里面add图片可视
        if (position == BitmapUtil.tempBitmap.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_add_bg));
            holder.move.setVisibility(View.INVISIBLE);
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else { //临时列表外面加载缩略图
            holder.image.setImageBitmap(BitmapUtil.tempBitmap.get(position).getBitmap());
            holder.move.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
