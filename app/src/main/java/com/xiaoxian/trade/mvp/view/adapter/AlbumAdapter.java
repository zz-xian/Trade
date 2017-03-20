package com.xiaoxian.trade.mvp.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.util.ImageLoad;
import com.xiaoxian.trade.util.ImageItem;
import com.xiaoxian.trade.util.MeasureUtil;

import java.util.ArrayList;

/**
 * 所有图片的适配器
 */

public class AlbumAdapter extends BaseAdapter {
    private Context mContext;
    private DisplayMetrics dm;
    private ArrayList<ImageItem> imageList;
    private ArrayList<ImageItem> selectList;

    ImageLoad imageLoad;

    final String TAG = getClass().getSimpleName();

    public AlbumAdapter(Context context, ArrayList<ImageItem> imageList, ArrayList<ImageItem> selectList) {
        this.mContext = context;
        this.imageList = imageList;
        this.selectList = selectList;
        imageLoad = new ImageLoad();
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //存放列表项控件
    class ViewHolder {
        public Button choose;
        public ImageView select;
        public ToggleButton toggle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.plugin_select_photo, parent, false);
            viewHolder.choose = (Button) convertView.findViewById(R.id.choose);
            viewHolder.select = (ImageView) convertView.findViewById(R.id.select);
            viewHolder.toggle = (ToggleButton) convertView.findViewById(R.id.toggle);

            int width = MeasureUtil.getBase4Width(MeasureUtil.getScreenWidth(mContext));
            convertView.setLayoutParams(new AbsListView.LayoutParams(width,width));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String path;
        if (imageList != null && imageList.size() > position) {
            path = imageList.get(position).imagePath;
        } else {
            path = "camera_default";
        }
        if (path.contains("camera_default")) {
            viewHolder.select.setImageResource(R.mipmap.ic_no_pic);
        } else {
            final ImageItem item = imageList.get(position);
            viewHolder.select.setTag(item.imagePath);
            imageLoad.displayBitmap(viewHolder.select, item.thumbPath, item.imagePath, callback);
        }
        viewHolder.choose.setTag(position);
        viewHolder.toggle.setTag(position);
        viewHolder.toggle.setOnClickListener(new ToggleClickListener(viewHolder.choose));
        if (selectList.contains(imageList.get(position))) {
            viewHolder.toggle.setChecked(true);
            viewHolder.choose.setVisibility(View.VISIBLE);
        } else {
            viewHolder.toggle.setChecked(false);
            viewHolder.choose.setVisibility(View.GONE);
        }
        return convertView;
    }

    ImageLoad.ImageCallback callback = new ImageLoad.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                //判断此处url是否对应imageView.getTag()
                //若将判断去掉，可能经常出现图片显示错位问题！！
                if (url != null && url.equals(imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "Callback:Bitmap is not match");
                }
            } else {
                Log.e(TAG, "Callback:Bitmap is null");
            }
        }
    };

    private class ToggleClickListener implements View.OnClickListener {
        Button button;
        public ToggleClickListener(Button button){
            this.button = button;
        }
        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (imageList != null && mOnItemClickListener != null && position < imageList.size()) {
                    mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(), button);
                }
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ToggleButton view, int position, boolean isChecked, Button button);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
