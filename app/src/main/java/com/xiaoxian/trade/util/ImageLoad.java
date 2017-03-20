package com.xiaoxian.trade.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;

import com.xiaoxian.trade.mvp.view.activity.PublishActivity;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 自定义轻量级图片缓存加载（也可使用网上的开源库）
 */

public class ImageLoad extends Activity {
    public Handler handler = new Handler();

    //SoftReference(软引用)：实现内存敏感的高速缓存
    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<>();

    //将图片存储到imageCache
    public void putBitmapToCache(String path, Bitmap bitmap) {
        //暂未缓存就存入HashMap
        if (!TextUtils.isEmpty(path) && bitmap != null) {
            imageCache.put(path, new SoftReference<>(bitmap));
        }
    }

    //显示图片
    public void displayBitmap(final ImageView imageView, final String thumbPath, final String sourcePath, final ImageCallback callback) {
        if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
            return;
        }

        final String path;
        final boolean isThumbPath;
        if (!TextUtils.isEmpty(thumbPath)) {
            path = thumbPath;
            isThumbPath = true;
        } else if (!TextUtils.isEmpty(sourcePath)) {
            path = sourcePath;
            isThumbPath = false;
        } else {
            return;
        }

        //先试着从缓存得到图片，path作为图片的key
        if (imageCache.containsKey(path)) {
            SoftReference<Bitmap> reference = imageCache.get(path);
            Bitmap bmp = reference.get();
            if (bmp != null) {
                if (callback != null) {
                    //回调图片显示
                    callback.imageLoad(imageView, bmp, sourcePath);
                }
                imageView.setImageBitmap(bmp);
                return;
            }
        }
        imageView.setImageBitmap(null);

        //缓存没有则启动线程
        new Thread() {
            Bitmap thumb;
            public void run() {
                try {
                    if (isThumbPath) {
                        //加载缩略地址对应缩略图
                        thumb = BitmapFactory.decodeFile(thumbPath);
                        if (thumb == null) {
                            //找不到缩略图，就从原图地址寻找并压缩为缩略图
                            thumb = BitmapUtil.revisionImageSize256(sourcePath);
                        }
                    } else {
                        thumb = BitmapUtil.revisionImageSize256(sourcePath);
                    }
                } catch (Exception e) {

                }
                //若缩略图没有加载成功，则显示默认
                if (thumb == null) {
                    thumb = PublishActivity.bitmap;
                }
                //将缩略图放进缓存，path作为key
                putBitmapToCache(path, thumb);
                if (callback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //回调图片显示
                            callback.imageLoad(imageView, thumb, sourcePath);
                        }
                    });
                }
            }
        }.start();
    }

    public interface ImageCallback {
        void imageLoad(ImageView imageView, Bitmap bitmap, Object... params);
    }
}
