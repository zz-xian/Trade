package com.xiaoxian.trade.mvp.view.activity.galley;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.mvp.view.adapter.AlbumAdapter;
import com.xiaoxian.trade.util.AlbumHelper;
import com.xiaoxian.trade.util.BitmapUtil;
import com.xiaoxian.trade.util.ImageBucket;
import com.xiaoxian.trade.util.ImageItem;
import com.xiaoxian.trade.util.PublicUtil;
import com.xiaoxian.trade.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 进入相册显示所有图片界面
 */

public class AlbumActivity extends BaseActivity {
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.album)
    Button album;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.grid)
    GridView grid;//显示所有图片列表控件
    @BindView(R.id.text)
    TextView text;//提示用户没有图片控件

    private Intent intent;
    private AlbumHelper helper;
    private AlbumAdapter adapter;
    private ArrayList<ImageItem> imageList;

    public static List<ImageBucket> contentList;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.plugin_album);
        ButterKnife.bind(this);
        imageList = new ArrayList<>();
        PublicUtil.activityList.add(this);
        isShowFinish();
    }

    @Override
    public void initData() {
        this.intent = getIntent();
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        contentList = helper.getImageBucketList(false);
        for (int i = 0; i < contentList.size(); i++) {
            imageList.addAll(contentList.get(i).imageList);
        }
        adapter = new AlbumAdapter(this, imageList, BitmapUtil.tempBitmap);
        grid.setEmptyView(text);
        grid.setAdapter(adapter);
        finish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");

        adapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ToggleButton view, int position, boolean isChecked, Button choose) {
                if (BitmapUtil.tempBitmap.size() >= PublicUtil.num) {
                    view.setChecked(false);
                    choose.setVisibility(View.GONE);
                    if (!removeItem(imageList.get(position))) {
                        ToastUtil.showToast(AlbumActivity.this, R.string.str_album_more);
                    }
                    return;
                }
                if (isChecked) {
                    choose.setVisibility(View.VISIBLE);
                    BitmapUtil.tempBitmap.add(imageList.get(position));
                    finish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
                } else {
                    choose.setVisibility(View.GONE);
                    BitmapUtil.tempBitmap.remove(imageList.get(position));
                    finish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
                }
                isShowFinish();
            }
        });
    }

    private boolean removeItem(ImageItem imageItem) {
        if (BitmapUtil.tempBitmap.contains(imageItem)) {
            BitmapUtil.tempBitmap.remove(imageItem);
            finish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
            return true;
        }
        return false;
    }

    //控制预览和完成按钮状态
    public void isShowFinish() {
        if (BitmapUtil.tempBitmap.size() > 0) {
            finish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
            finish.setPressed(true);
            finish.setClickable(true);
            finish.setTextColor(Color.WHITE);
        } else {
            finish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
            finish.setPressed(false);
            finish.setClickable(false);
            finish.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    @OnClick({R.id.cancel, R.id.album, R.id.finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.album:
                intent.setClass(this, FolderActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.finish:
                finish();
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onRestart() {
        isShowFinish();
        super.onRestart();
    }
}
