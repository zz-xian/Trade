package com.xiaoxian.trade.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerResultActivityComponent;
import com.xiaoxian.trade.di.module.ResultActivityModule;
import com.xiaoxian.trade.mvp.contract.ResultContract;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.adapter.CustomAdapter;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.widget.CustomListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends BaseActivity implements ResultContract.IView {
    @BindView(R.id.result_back)
    ImageView resultBack;
    @BindView(R.id.result_text)
    TextView resultText;
    @BindView(R.id.result_list)
    CustomListView resultList;
    @Inject
    ResultContract.IPresenter presenter;

    private String key;
    private String type;
    private List<Goods> goodsList;
    private CustomAdapter adapter;

    public static final String TAG = "ResultActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerResultActivityComponent.builder()
                .appComponent(appComponent)
                .resultActivityModule(new ResultActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        key = getIntent().getStringExtra("Key");
        type = getIntent().getStringExtra("Result");
        resultText.setText(key);
        if (type.equals("KeyWords")) {
            presenter.queryGoodsByKey(key);
        } else if (type.equals("Kind")) {
            presenter.queryGoodsByKind(key);
        } else if (type.equals("SubKind")) {
            presenter.queryGoodsBySubKind(key);
        }
    }

    @OnClick(R.id.result_back)
    public void onClick() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void showKeyResult(List<Goods> goodsList) {
        this.goodsList = goodsList;
        presenter.parseGoodsUser(goodsList);
    }

    @Override
    public void showSubKindResult(List<Goods> goodsList) {
        this.goodsList = goodsList;
        presenter.parseGoodsUser(goodsList);
    }

    @Override
    public void loadGoodsError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "加载失败");
    }

    @Override
    public void parseUser(final List<User> userList) {
        adapter = new CustomAdapter(this, userList, goodsList);
        resultList.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            //expandGroup()：在分组列表视图中，展开一组
            resultList.expandGroup(i);
        }
        resultList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY", "Goods");
                bundle.putSerializable("User", userList.get(groupPosition));
                bundle.putSerializable("Goods", goodsList.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        resultList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY", "Goods");
                bundle.putSerializable("User", userList.get(groupPosition));
                bundle.putSerializable("Goods", goodsList.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
    }
}
