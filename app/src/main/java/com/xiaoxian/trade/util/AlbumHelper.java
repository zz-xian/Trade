package com.xiaoxian.trade.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 帮助类
 */

public class AlbumHelper {
    Context mContext;
    ContentResolver mContentResolver;

    HashMap<String, String> thumbList = new HashMap<>();//缩略图列表

    List<HashMap<String, String>> albumList = new ArrayList<>();

    HashMap<String, ImageBucket> bucketList = new HashMap<>();

    private static AlbumHelper helper;

    final String TAG = getClass().getSimpleName();

    private AlbumHelper() {
    }

    public static AlbumHelper getHelper() {
        if (helper == null) {
            helper = new AlbumHelper();
        }
        return helper;
    }

    public void init(Context context) {
        if (this.mContext == null) {
            this.mContext = context;
            mContentResolver = context.getContentResolver();
        }
    }

    //得到缩略图
    private void getThumb() {
        //所要查询字段
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID
                , MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = mContentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
                , projection, null, null, null);
        getThumbColumnData(cursor);
    }

    //从数据库得到缩略图
    private void getThumbColumnData(Cursor cursor) {
        if (cursor.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;

            //得到索引
            int _idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
            int image_idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

            do {
                //得到索引对应值
                _id = cursor.getInt(_idColumn);
                image_id = cursor.getInt(image_idColumn);
                image_path = cursor.getString(dataColumn);

                thumbList.put("" + image_id, image_path);
            } while (cursor.moveToNext());
        }
    }

    //得到原图
    void getAlbum() {
        String[] projection = { MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM
                , MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ALBUM_KEY
                , MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.NUMBER_OF_SONGS };
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getAlbumColumnData(cursor);
    }

    //从本地数据库得到原图
    private void getAlbumColumnData(Cursor cursor) {
        if (cursor.moveToFirst()) {
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artist;
            int numOfSongs;

            int _idColumn = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int albumArtColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int albumKeyColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int numOfSongsColumn = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);

            do {
                _id = cursor.getInt(_idColumn);
                album = cursor.getString(albumColumn);
                albumArt = cursor.getString(albumArtColumn);
                albumKey = cursor.getString(albumKeyColumn);
                artist = cursor.getString(artistColumn);
                numOfSongs = cursor.getInt(numOfSongsColumn);

                Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt + "albumKey: " + albumKey
                        + " artist: " + artist + " numOfSongs: " + numOfSongs + "------");
                HashMap<String, String> hash = new HashMap<>();
                hash.put("_id", _id + "");
                hash.put("album", album);
                hash.put("albumArt", albumArt);
                hash.put("albumKey", albumKey);
                hash.put("artist", artist);
                hash.put("numOfSongs", numOfSongs + "");
                albumList.add(hash);
            } while (cursor.moveToNext());
        }
    }

    //是否已创建图片集
    boolean hasBuildImageBucketList = false;

    //得到图片集
    void buildImageBucketList() {
        //清空以免重复加载
        bucketList.clear();
        long startTime = System.currentTimeMillis();

        //构造缩略图索引
        getThumb();

        //构造相册索引
        String columns[] = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID
                , MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME
                , MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        if (cursor.moveToFirst()) {
            //获取指定列索引
            int photoIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int bucketIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            int picasaIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);
            int photoPathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int photoNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int photoSizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int photoTitleIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int bucketNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            //获取图片总数
            int totalNum = cursor.getCount();

            do {
                String _id = cursor.getString(photoIDIndex);
                String bucketId = cursor.getString(bucketIDIndex);
                String picasaId = cursor.getString(picasaIDIndex);
                String path = cursor.getString(photoPathIndex);
                String name = cursor.getString(photoNameIndex);
                String size = cursor.getString(photoSizeIndex);
                String title = cursor.getString(photoTitleIndex);
                String bucketName = cursor.getString(bucketNameIndex);

                Log.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: " + picasaId + " path:" + path + " name:" + name
                        + " size: " + size + " title: " + title + " bucket: " + bucketName + "------");

                ImageBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new ImageBucket();
                    bucketList.put(bucketId, bucket);

                    bucket.bucketName = bucketName;
                    bucket.imageList = new ArrayList<>();
                }
                bucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.thumbPath = thumbList.get(_id);

                bucket.imageList.add(imageItem);
            } while (cursor.moveToNext());
        }

        //Iterator--遍历并选择序列中对象
        //entrySet()--返回一个Map.Entry实例化后的对象集
        Iterator<Map.Entry<String, ImageBucket>> it = bucketList.entrySet().iterator();
        while (it.hasNext()) {
            //首次调用Iterator的next()时，返回序列首个元素
            Map.Entry<String, ImageBucket> entry = it.next();
            ImageBucket bucket = entry.getValue();
            Log.d(TAG, entry.getKey() + ", " + bucket.bucketName + ", "+ bucket.count + " ------ ");
            for (int i = 0; i < bucket.imageList.size(); ++i) {
                ImageItem item = bucket.imageList.get(i);
                Log.d(TAG, "------ " + item.imageId + ", " + item.imagePath+ ", " + item.thumbPath);
            }
        }
        hasBuildImageBucketList = true;
        long endTime = System.currentTimeMillis();
        Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
    }

    //得到图片集
    public List<ImageBucket> getImageBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImageBucketList)) {
            buildImageBucketList();
        }
        List<ImageBucket> tempList = new ArrayList<>();
        Iterator<Map.Entry<String, ImageBucket>> it = bucketList.entrySet().iterator();
        //hasNext()检查序列中是否还有元素
        while (it.hasNext()) {
            Map.Entry<String, ImageBucket> entry = it.next();
            tempList.add(entry.getValue());
        }
        return tempList;
    }

    //得到原始图像路径
    String getOriginalImagePath(String image_id) {
        String path = null;
        Log.i(TAG, "---(^o^)---" + image_id);
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , projection, MediaStore.Images.Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        return path;
    }
}
