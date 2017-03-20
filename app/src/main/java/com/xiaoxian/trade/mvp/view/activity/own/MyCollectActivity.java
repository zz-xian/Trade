package com.xiaoxian.trade.mvp.view.activity.own;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerMyCollectActivityComponent;
import com.xiaoxian.trade.di.module.MyCollectActivityModule;
import com.xiaoxian.trade.mvp.contract.MyCollectContract;
import com.xiaoxian.trade.mvp.model.Collect;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.activity.DetailActivity;
import com.xiaoxian.trade.mvp.view.adapter.MyCollectAdapter;
import com.xiaoxian.trade.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 后续加入下拉刷新、上拉加载功能
 */

public class MyCollectActivity extends BaseActivity implements MyCollectContract.IView {
    @BindView(R.id.my_collect_back)
    ImageView myCollectBack;
    @BindView(R.id.my_collect_manager)
    TextView myCollectManager;
    @BindView(R.id.my_collect_list)
    ListView myCollectList;
    @BindView(R.id.manager_layout)
    RelativeLayout managerLayout;
    @BindView(R.id.all_check)
    CheckBox allCheck;
    @BindView(R.id.cancel_text)
    TextView cancelText;
    @BindView(R.id.my_collect_error)
    LinearLayout myCollectError;
    @Inject
    MyCollectContract.IPresenter presenter;

    private MyCollectAdapter adapter;
    private List<Collect> collectList = new ArrayList<>();

    public static final String TAG = "MyCollectActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_collect);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerMyCollectActivityComponent.builder()
                .appComponent(appComponent)
                .myCollectActivityModule(new MyCollectActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        presenter.queryMyCollect();
        if (collectList == null || collectList.size() == 0) {
            myCollectManager.setVisibility(View.GONE);
            managerLayout.setVisibility(View.GONE);
            myCollectList.setVisibility(View.GONE);
        }
        allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //记录选中数量
                int size = 0;
                if(isChecked){
                    //设置全选
                    for (int i = 0; i < adapter.getCount(); i++) {
                        adapter.checkMap.put(i, true);
                    }
                }else {
                    //全部不选
                    for (int i = 0; i < adapter.getCount(); i++) {
                        //判断每项是否选中，是则计数+1
                        if(adapter.checkMap.get(i)){
                            size += 1;
                        }
                    }
                    //判断列表选中数量是否等于列表总数，是则全部不选
                    if (size == adapter.getCount()) {
                        //全选没有选中，则将每项设为不选
                        for (int i = 0; i < adapter.getCount(); i++) {
                            adapter.checkMap.put(i, false);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.my_collect_back, R.id.my_collect_manager, R.id.cancel_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_collect_back:
                if (adapter.isVisible()) {
                    myCollectManager.setText(R.string.str_manager);
                    managerLayout.setVisibility(View.GONE);
                    resetState();//重置CheckBox状态
                    adapter.setVisible(false);
                } else {
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }
                break;
            case R.id.my_collect_manager:
                if (!adapter.isVisible()) {
                    myCollectManager.setText(R.string.str_cancel);
                    managerLayout.setVisibility(View.VISIBLE);
                    adapter.setVisible(true);
                    adapter.notifyDataSetChanged();
                }else{
                    myCollectManager.setText(R.string.str_manager);
                    managerLayout.setVisibility(View.GONE);
                    adapter.setVisible(false);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.cancel_text:
                List<Collect> list = new ArrayList<>();
                Map<Integer, Boolean> map = new HashMap<>();
                for (int i = 0; i < collectList.size(); i++) {
                    if (adapter.checkMap.get(i)) {
                        list.add(collectList.get(i));
                        map.put(i, adapter.checkMap.get(i));
                    }
                }

                if (list.size() != 0 && collectList.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        presenter.cancelMyCollect(list.get(i).getObjectId());
                        adapter.removeData(list.get(i));
                        map.remove(i);
                    }
                    list.clear();
                    resetState();
                } else if (collectList.size() == 0) {
                    ToastUtil.showToast(this, "没有商品可以取消");
                } else if (list.size() == 0) {
                    ToastUtil.showToast(this, "至少选中一个商品");
                }
                break;
        }
    }

    private void resetState() {
        for (int i = 0; i < collectList.size(); i++) {
            adapter.checkMap.put(i, false);
        }
        allCheck.setChecked(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadMyCollect(final List<User> userList, final List<Collect> collectList) {
        this.collectList = collectList;
        adapter = new MyCollectAdapter(this, userList, collectList);
        myCollectList.setAdapter(adapter);
        myCollectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isVisible()) {
                    MyCollectAdapter.ViewHolder viewHolder = (MyCollectAdapter.ViewHolder) view.getTag();
                    //每次点击item都对CheckBox状态进行改变
                    adapter.checkMap.put(position, viewHolder.checkBox.isChecked());
                } else {
                    Intent intent = new Intent(MyCollectActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY", "Collect");
                    bundle.putSerializable("User", userList.get(position));
                    bundle.putSerializable("Collect", collectList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        if (collectList == null || collectList.size() == 0) {
            myCollectManager.setVisibility(View.GONE);
            managerLayout.setVisibility(View.GONE);
            myCollectList.setVisibility(View.GONE);
            myCollectError.setVisibility(View.VISIBLE);
        } else {
            myCollectManager.setVisibility(View.VISIBLE);
            myCollectList.setVisibility(View.VISIBLE);
            myCollectError.setVisibility(View.GONE);
        }
    }

    @Override
    public void queryError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "查询收藏失败");
    }

    @Override
    public void cancelSuccess() {
        ToastUtil.showToast(this, "取消收藏成功");
    }

    @Override
    public void cancelError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "取消收藏失败");
    }

    public void getCheckData() {
        //记录选中数量
        int num = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (collectList != null && adapter.checkMap.get(i)) {
                num++;
            }
        }
        if(num == collectList.size()){
            allCheck.setChecked(true);
        } else {
            allCheck.setChecked(false);
        }
    }
}
