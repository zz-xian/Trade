package com.xiaoxian.trade.mvp.view.activity.galley;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.GridView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.mvp.view.adapter.FolderAdapter;
import com.xiaoxian.trade.util.PublicUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 显示图片文件夹
 */

public class FolderActivity extends BaseActivity {
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.gird)
    GridView gird;

    private FolderAdapter adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.plugin_folder);
        ButterKnife.bind(this);
        PublicUtil.activityList.add(this);
        adapter = new FolderAdapter(this);
        gird.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.cancel)
    public void onClick() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }
}
