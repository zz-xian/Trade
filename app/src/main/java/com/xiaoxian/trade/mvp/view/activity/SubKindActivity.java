package com.xiaoxian.trade.mvp.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerKindComponent;
import com.xiaoxian.trade.di.module.KindModule;
import com.xiaoxian.trade.mvp.contract.KindContract;
import com.xiaoxian.trade.mvp.model.Kind;
import com.xiaoxian.trade.mvp.model.SubKind;
import com.xiaoxian.trade.mvp.view.adapter.SubKindAdapter;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubKindActivity extends BaseActivity implements KindContract.IView {
    @BindView(R.id.sub_kind_back)
    ImageView subKindBack;
    @BindView(R.id.sub_kind_text)
    TextView subKindText;
    @BindView(R.id.lv_sub_kind)
    ListView lvSubKind;
    @Inject
    KindContract.IPresenter presenter;

    private Kind kind;
    private SubKindAdapter adapter;

    public static final String TAG = "SubKindActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sub_kind);
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
        kind = (Kind) getIntent().getSerializableExtra("Kind");
        subKindText.setText(kind.getKind());
        presenter.loadSubKind(kind);
    }

    @OnClick(R.id.sub_kind_back)
    public void onClick() {
        finish();
    }

    @Override
    public void showKind(List<Kind> kindList) {

    }

    @Override
    public void showSubKind(final List<SubKind> subKindList) {
        adapter = new SubKindAdapter(this, subKindList);
        lvSubKind.setAdapter(adapter);
        lvSubKind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("Result", "SubKind");
                bundle.putString("Key", subKindList.get(position).getSubKind());
                UIUtil.nextPage(SubKindActivity.this, ResultActivity.class, bundle);
            }
        });
    }

    @Override
    public void loadError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "加载二级分类失败");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
