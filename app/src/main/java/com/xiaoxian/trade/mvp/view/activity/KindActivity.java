package com.xiaoxian.trade.mvp.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerKindComponent;
import com.xiaoxian.trade.di.module.KindModule;
import com.xiaoxian.trade.mvp.contract.KindContract;
import com.xiaoxian.trade.mvp.model.Kind;
import com.xiaoxian.trade.mvp.model.SubKind;
import com.xiaoxian.trade.mvp.view.adapter.KindAdapter;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KindActivity extends BaseActivity implements KindContract.IView {
    @BindView(R.id.kind_back)
    ImageView kindBack;
    @BindView(R.id.lv_kind)
    ListView lvKind;
    @Inject
    KindContract.IPresenter presenter;

    private KindAdapter adapter;

    public static final String TAG = "KindActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_kind);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerKindComponent.builder()
                .appComponent(appComponent)
                .kindModule(new KindModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        presenter.loadKind();
    }

    @OnClick(R.id.kind_back)
    public void onClick() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void showKind(final List<Kind> kindList) {
        adapter = new KindAdapter(this, kindList);
        lvKind.setAdapter(adapter);
        lvKind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                //若无实现接口，不起任何作用
                bundle.putSerializable("Kind", kindList.get(position));
                UIUtil.nextPage(KindActivity.this, SubKindActivity.class, bundle);
            }
        });
    }

    @Override
    public void showSubKind(List<SubKind> subKindList) {

    }

    @Override
    public void loadError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "加载一级分类失败");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
