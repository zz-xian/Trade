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
import com.xiaoxian.trade.di.component.DaggerMyPublishActivityComponent;
import com.xiaoxian.trade.di.module.MyPublishActivityModule;
import com.xiaoxian.trade.mvp.contract.MyPublishContract;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.activity.DetailActivity;
import com.xiaoxian.trade.mvp.view.adapter.MyPublishAdapter;
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

public class MyPublishActivity extends BaseActivity implements MyPublishContract.IView{
    @BindView(R.id.my_publish_back)
    ImageView myPublishBack;
    @BindView(R.id.my_publish_manager)
    TextView myPublishManager;
    @BindView(R.id.my_publish_list)
    ListView myPublishList;
    @BindView(R.id.manager_layout)
    RelativeLayout managerLayout;
    @BindView(R.id.all_check)
    CheckBox allCheck;
    @BindView(R.id.delete_text)
    TextView deleteText;
    @BindView(R.id.my_publish_error)
    LinearLayout myPublishError;
    @Inject
    MyPublishContract.IPresenter presenter;

    private MyPublishAdapter adapter;
    private List<Goods> goodsList = new ArrayList<>();

    public static final String TAG = "MyPublishActivity";

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_publish);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerMyPublishActivityComponent.builder()
                .appComponent(appComponent)
                .myPublishActivityModule(new MyPublishActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        presenter.queryMyPublish();
        if (goodsList == null || goodsList.size() == 0) {
            myPublishManager.setVisibility(View.GONE);
            managerLayout.setVisibility(View.GONE);
            myPublishList.setVisibility(View.GONE);
        }
        allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    @OnClick({R.id.my_publish_back, R.id.my_publish_manager, R.id.delete_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_publish_back:
                if (adapter.isVisible()) {
                    myPublishManager.setText(R.string.str_manager);
                    managerLayout.setVisibility(View.GONE);
                    resetState();//重置CheckBox状态
                    adapter.setVisible(false);
                } else {
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }
                break;
            case R.id.my_publish_manager:
                if (!adapter.isVisible()) {
                    myPublishManager.setText(R.string.str_cancel);
                    managerLayout.setVisibility(View.VISIBLE);
                    adapter.setVisible(true);
                    adapter.notifyDataSetChanged();
                }else{
                    myPublishManager.setText(R.string.str_manager);
                    managerLayout.setVisibility(View.GONE);
                    adapter.setVisible(false);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.delete_text:
                List<Goods> list = new ArrayList<>();
                Map<Integer, Boolean> map = new HashMap<>();
                for (int i = 0; i < goodsList.size(); i++) {
                    if (adapter.checkMap.get(i)) {
                        list.add(goodsList.get(i));
                        map.put(i, adapter.checkMap.get(i));
                    }
                }

                if (list.size() != 0 && goodsList.size() != 0) {
                    for (int i = 0; i < list.size(); i++) {
                        presenter.deleteMyPublish(list.get(i).getObjectId());
                        adapter.removeData(list.get(i));
                        map.remove(i);
                    }
                    list.clear();
                    resetState();
                } else if (goodsList.size() == 0) {
                    ToastUtil.showToast(this, "没有商品可以删除");
                } else if (list.size() == 0) {
                    ToastUtil.showToast(this, "至少选中一个商品");
                }
                break;
        }
    }

    private void resetState() {
        for (int i = 0; i < goodsList.size(); i++) {
            adapter.checkMap.put(i, false);
        }
        allCheck.setChecked(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadMyPublish(final User user, final List<Goods> goodsList) {
        this.goodsList = goodsList;
        adapter = new MyPublishAdapter(this, goodsList);
        myPublishList.setAdapter(adapter);
        myPublishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isVisible()) {
                    MyPublishAdapter.ViewHolder viewHolder = (MyPublishAdapter.ViewHolder) view.getTag();
                    //每次点击item都对CheckBox状态进行改变
                    adapter.checkMap.put(position, viewHolder.checkBox.isChecked());
                } else {
                    Intent intent = new Intent(MyPublishActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY", "Goods");
                    bundle.putSerializable("User", user);
                    bundle.putSerializable("Goods", goodsList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        if (goodsList == null || goodsList.size() == 0) {
            myPublishManager.setVisibility(View.GONE);
            managerLayout.setVisibility(View.GONE);
            myPublishList.setVisibility(View.GONE);
            myPublishError.setVisibility(View.VISIBLE);
        } else {
            myPublishManager.setVisibility(View.VISIBLE);
            myPublishList.setVisibility(View.VISIBLE);
            myPublishError.setVisibility(View.GONE);
        }
    }

    @Override
    public void queryError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "查询失败");
    }

    @Override
    public void deleteSuccess() {
        ToastUtil.showToast(this, "删除成功");
    }

    @Override
    public void deleteError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "删除失败");
    }

    public void getCheckData() {
        //记录选中数量
        int num = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (goodsList != null && adapter.checkMap.get(i)) {
                num++;
            }
        }
        if(num == goodsList.size()){
            allCheck.setChecked(true);
        } else {
            allCheck.setChecked(false);
        }
    }
}
