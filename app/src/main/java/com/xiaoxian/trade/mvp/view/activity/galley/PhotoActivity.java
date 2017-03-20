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
import com.xiaoxian.trade.mvp.view.activity.PublishActivity;
import com.xiaoxian.trade.mvp.view.adapter.AlbumAdapter;
import com.xiaoxian.trade.util.BitmapUtil;
import com.xiaoxian.trade.util.ImageItem;
import com.xiaoxian.trade.util.PublicUtil;
import com.xiaoxian.trade.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 显示文件夹中所有图片
 */

public class PhotoActivity extends BaseActivity {
    @BindView(R.id.photo_cancel)
    Button photoCancel;
    @BindView(R.id.photo_title)
    TextView photoTitle;
    @BindView(R.id.photo_finish)
    Button photoFinish;
    @BindView(R.id.grid)
    GridView grid;

    private Intent intent;
    private AlbumAdapter adapter;
    public static ArrayList<ImageItem> imageList = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.plugin_photo);
        ButterKnife.bind(this);
        PublicUtil.activityList.add(this);
        isShowFinish();
    }

    @Override
    public void initData() {
        this.intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        if (folderName.length() > 10) {
            folderName = folderName.substring(0, 9) + "···";
        }
        photoTitle.setText(folderName);
        adapter = new AlbumAdapter(this, imageList, BitmapUtil.tempBitmap);
        grid.setAdapter(adapter);

        adapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            public void onItemClick(final ToggleButton view, int position, boolean isChecked, Button button) {
                if (BitmapUtil.tempBitmap.size() >= PublicUtil.num && isChecked) {
                    view.setChecked(false);
                    button.setVisibility(View.GONE);
                    ToastUtil.showToast(PhotoActivity.this, R.string.str_album_more);
                    return;
                }
                if (isChecked) {
                    button.setVisibility(View.VISIBLE);
                    BitmapUtil.tempBitmap.add(imageList.get(position));
                    photoFinish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
                } else {
                    button.setVisibility(View.GONE);
                    BitmapUtil.tempBitmap.remove(imageList.get(position));
                    photoFinish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
                }
                isShowFinish();
            }
        });
    }

    public void isShowFinish() {
        if (BitmapUtil.tempBitmap.size() > 0) {
            photoFinish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
            photoFinish.setPressed(true);
            photoFinish.setClickable(true);
            photoFinish.setTextColor(Color.WHITE);
        } else {
            photoFinish.setText("完成" + "(" + BitmapUtil.tempBitmap.size() + "/" + PublicUtil.num + ")");
            photoFinish.setPressed(false);
            photoFinish.setClickable(false);
            photoFinish.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    @OnClick({R.id.photo_cancel, R.id.photo_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo_cancel:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.photo_finish:
                photoFinish.setClickable(false);
                intent.setClass(this, PublishActivity.class);
                startActivity(intent);
                finish();
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
