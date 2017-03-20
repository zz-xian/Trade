package com.xiaoxian.trade.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 图片压缩处理、保存
 */

public class BitmapUtil {

    //选择图片的临时列表
    public static ArrayList<ImageItem> tempBitmap = new ArrayList<>();

    /**
     * 上传服务器时，图片按照如下方法压缩 保存到临时文件夹（压缩后小于100KB，失真度不明显）
     */
    public static Bitmap revisionImageSize(String path) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));

        //不会返回实际bitmap对象，不分配内存空间但可得到一些解码边界信息即图片大小等
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bis, null, options);

        bis.close();

        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            //options.outWidth>>i等价options.outWidth/(2^i)
            //此处压缩到宽和高都<=1000
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                bis = new BufferedInputStream(new FileInputStream(new File(path)));
                //inSampleSize--图片大小为原图几分之一（即缩放值）
                //Math.pow(2.0D,i)=2^i
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(bis, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static Bitmap revisionImageSize256(String path) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bis, null, options);

        bis.close();

        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 256) && (options.outHeight >> i <= 256)) {
                bis = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(bis, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 图片保存
     */
    public static Uri saveBitmap(Bitmap bitmap) {
        File tempDir;
        File image;
        if (hasSD()) {
            tempDir = new File("/sdcard/trade/goods/");
        } else {
            tempDir = new File(Environment.getDataDirectory() + "/trade/goods/");
        }

        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        image = new File(tempDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);//compress()压缩的是存储大小
            fos.flush();
            fos.close();
            return Uri.fromFile(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 是否存在SD卡
     */
    public static boolean hasSD() {
        //存在则保存到SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
