package com.xiaoxian.trade.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.xiaoxian.trade.mvp.view.adapter.AllKindAdapter;
import com.xiaoxian.trade.mvp.view.adapter.SubKindAdapter;
import com.xiaoxian.trade.util.ToastUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllKindActivity extends BaseActivity implements KindContract.IView {
    @BindView(R.id.all_kind_back)
    ImageView allKindBack;
    @BindView(R.id.all_kind_finish)
    TextView allKindFinish;
    @BindView(R.id.lv_kind)
    ListView lvKind;
    @BindView(R.id.lv_sub_kind)
    ListView lvSubKind;
    @Inject
    KindContract.IPresenter presenter;

    private AllKindAdapter allKindAdapter;
    private SubKindAdapter subKindAdapter;

    private int kindOldClickIndex = 0;
    private int kindCurrentClickIndex = 0;

    private int subKindOldClickIndex = 0;
    private int subKindCurrentClickIndex = 0;

    private String kindSign, subKindSign;
    private static final int CHOOSE_KIND = 2;

    public static final String TAG = "AllKindActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_all_kind);
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

    @Override
    public void showKind(final List<Kind> kindList) {
        allKindAdapter = new AllKindAdapter(this, kindList);
        lvKind.setAdapter(allKindAdapter);
        //默认首个item状态
        lvKind.post(new Runnable() {
            @Override
            public void run() {
                /**
                 * getChildAt(index)只能获取当前屏幕显示item的view对象，其他不在显示view对象并不存在内存
                 * 注意判断获取item是否在屏幕中显示（取值范围 >= listView.getFirstVisiblePosition() && <= listView.getLastVisiblePosition()）
                 *
                 * getResources().getColor()在API23以上失效，换用ContextCompat.getColor(context,id)
                 */
                lvKind.getChildAt(kindCurrentClickIndex).setBackgroundColor(ContextCompat.getColor(AllKindActivity.this, R.color.colorWhite));
                kindSign = kindList.get(0).getKind();
                presenter.loadSubKind(kindList.get(0));
            }
        });
        lvKind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                kindCurrentClickIndex = position;
                if (kindCurrentClickIndex != kindOldClickIndex) {
                    parent.getChildAt(kindCurrentClickIndex).setBackgroundColor(ContextCompat.getColor(AllKindActivity.this, R.color.colorWhite));
                    parent.getChildAt(kindOldClickIndex).setBackgroundColor(ContextCompat.getColor(AllKindActivity.this, R.color.colorBackground));
                    kindOldClickIndex = position;
                } else {
                    parent.getChildAt(kindCurrentClickIndex).setBackgroundColor(ContextCompat.getColor(AllKindActivity.this, R.color.colorWhite));
                }
                kindSign = kindList.get(position).getKind();
                presenter.loadSubKind(kindList.get(position));
            }
        });
    }

    @Override
    public void showSubKind(final List<SubKind> subKindList) {
        subKindAdapter = new SubKindAdapter(this, subKindList);
        lvSubKind.setAdapter(subKindAdapter);
        lvSubKind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subKindCurrentClickIndex = position;
                if (subKindCurrentClickIndex != subKindOldClickIndex) {
                    parent.getChildAt(subKindCurrentClickIndex).setBackgroundColor(ContextCompat.getColor(AllKindActivity.this, R.color.colorTop));
                    parent.getChildAt(subKindOldClickIndex).setBackgroundColor(ContextCompat.getColor(AllKindActivity.this, R.color.colorWhite));
                    subKindOldClickIndex = position;
                } else {
                    parent.getChildAt(subKindCurrentClickIndex).setBackgroundColor(ContextCompat.getColor(AllKindActivity.this, R.color.colorTop));
                }
                subKindSign = subKindList.get(position).getSubKind();
            }
        });
    }

    @Override
    public void loadError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "加载分类失败");
    }

    @OnClick({R.id.all_kind_back, R.id.all_kind_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_kind_back:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            case R.id.all_kind_finish:
                if (kindSign != null && subKindSign != null) {
                    Intent intent = new Intent(this, PublishActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Kind", kindSign);
                    bundle.putString("SubKind", subKindSign);
                    intent.putExtras(bundle);
                    setResult(CHOOSE_KIND, intent);
                    finish();
                }else if (subKindSign == null){
                    ToastUtil.showToast(this, "请选择二级分类");
                }else {
                    ToastUtil.showToast(this, "请选择一级分类");
                }
                break;
        }
    }
}
