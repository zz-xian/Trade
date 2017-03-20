package com.xiaoxian.trade.mvp.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.mvp.view.activity.galley.AlbumActivity;
import com.xiaoxian.trade.mvp.view.activity.galley.PhotoActivity;
import com.xiaoxian.trade.util.ImageItem;
import com.xiaoxian.trade.util.ImageLoad;
import com.xiaoxian.trade.util.MeasureUtil;

import java.util.ArrayList;

public class FolderAdapter extends BaseAdapter {
    private Context mContext;
    private DisplayMetrics dm;

    ImageLoad imageLoad;

    final String TAG = getClass().getSimpleName();

    public FolderAdapter(Context context) {
        imageLoad = new ImageLoad();
        init(context);
    }

    public void init(Context context) {
        mContext = context;
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public int getCount() {
        return AlbumActivity.contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        public ImageView fileBg;
        public ImageView fileIv;
        public ImageView chooseBg;
        // 文件夹名
        public TextView folderName;
        // 文件夹中图片数量
        public TextView fileNum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.plugin_select_folder, null);
            viewHolder = new ViewHolder();
            viewHolder.fileBg = (ImageView) convertView.findViewById(R.id.file_bg);
            viewHolder.fileIv = (ImageView) convertView.findViewById(R.id.file_iv);
            viewHolder.chooseBg = (ImageView) convertView.findViewById(R.id.choose_bg);
            viewHolder.folderName = (TextView) convertView.findViewById(R.id.folder_name);
            viewHolder.fileNum = (TextView) convertView.findViewById(R.id.file_num);

            int width = MeasureUtil.getBase3Width(MeasureUtil.getScreenWidth(mContext));
            convertView.setLayoutParams(new AbsListView.LayoutParams(width, width));
            viewHolder.fileIv.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String path;
        if (AlbumActivity.contentList.get(position).imageList != null) {
            //封面图片路径
            path = AlbumActivity.contentList.get(position).imageList.get(0).imagePath;
            viewHolder.folderName.setText(AlbumActivity.contentList.get(position).bucketName);
            viewHolder.fileNum.setText("" + AlbumActivity.contentList.get(position).count);
        } else {
            path = "android_hybrid_camera_default";
        }
        if (path.contains("android_hybrid_camera_default")) {
            viewHolder.fileIv.setImageResource(R.mipmap.ic_no_pic);
        } else {
            final ImageItem item = AlbumActivity.contentList.get(position).imageList.get(0);
            viewHolder.fileIv.setTag(item.imagePath);
            imageLoad.displayBitmap(viewHolder.fileIv, item.thumbPath, item.imagePath, callback);
        }
        viewHolder.fileIv.setOnClickListener(new ImageViewClickListener(position, viewHolder.chooseBg));
        return convertView;
    }

    //为每个文件夹构建监听器
    private class ImageViewClickListener implements View.OnClickListener {
        private int position;
        private ImageView chooseBg;

        public ImageViewClickListener(int position,ImageView chooseBg) {
            this.position = position;
            this.chooseBg = chooseBg;
        }

        public void onClick(View view) {
            PhotoActivity.imageList = (ArrayList<ImageItem>) AlbumActivity.contentList.get(position).imageList;
            Intent intent = new Intent();
            String folderName = AlbumActivity.contentList.get(position).bucketName;
            intent.putExtra("folderName", folderName);
            intent.setClass(mContext, PhotoActivity.class);
            mContext.startActivity(intent);
            chooseBg.setVisibility(view.VISIBLE);
        }
    }

    ImageLoad.ImageCallback callback=new ImageLoad.ImageCallback() {
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
}
