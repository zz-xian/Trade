package com.xiaoxian.trade.mvp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerMainActivityComponent;
import com.xiaoxian.trade.di.module.MainActivityModule;
import com.xiaoxian.trade.mvp.contract.NearContract;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.activity.DetailActivity;
import com.xiaoxian.trade.mvp.view.activity.SearchActivity;
import com.xiaoxian.trade.mvp.view.adapter.CustomAdapter;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;
import com.xiaoxian.trade.widget.CustomListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NearFragment extends Fragment implements NearContract.IView {
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.clv_goods)
    CustomListView clvGoods;
    @Inject
    NearContract.IPresenter presenter;

    private View mRootView;
    private Context mContext;
    private Unbinder unbinder;
    private CustomAdapter adapter;

    private List<Goods> goodsList;
    private String mCurrentCity = "广州";

    public static final String TAG = "NearFragment";

    // 定位相关
    LocationClient mLocationClient;
    public MyLocationListener mListener = new MyLocationListener();

    //定位SDK监听函数
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有相关人员进行跟进");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("网络定位失败，务必检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("网络定位失败，确保不是处于飞行模式");
            }
            tvAddress.setText(location.getAddrStr());
            tvAddress.postInvalidate();
            mCurrentCity = location.getCity();
            presenter.loadGoods(mCurrentCity);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_near, container, false);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        initComponent(((App) mContext.getApplicationContext()).getAppComponent());
        initData();
        return mRootView;
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder()
                .appComponent(appComponent)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    public void initData() {
        clvGoods.setFocusable(false);
        initLocation();
    }

    public void initLocation() {
        //定位初始化
        mLocationClient = new LocationClient(mContext);
        mLocationClient.registerLocationListener(mListener);
        LocationClientOption option = new LocationClientOption();//设置定位方式
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");//设置坐标类型
        option.setIsNeedAddress(true);//开启位置信息包括city
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 获取该Fragment对象状态改变，可视状态加载列表
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            Log.i("Refresh", "刷新数据");
            presenter.loadGoods(mCurrentCity);
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        mLocationClient.unRegisterLocationListener(mListener);
        mLocationClient.stop();
        super.onDestroy();
    }

    @Override
    public void loadGoodsSuccess(List<Goods> goodsList) {
        this.goodsList = goodsList;
        presenter.parseGoodsUser(goodsList);
    }

    @Override
    public void loadGoodsError(String str) {
        Log.i(TAG, str);
        ToastUtil.showToast(mContext, "加载失败");
    }

    @Override
    public void parseUser(final List<User> userList) {
        adapter = new CustomAdapter(mContext, userList, goodsList);
        clvGoods.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            clvGoods.expandGroup(i);
        }
        clvGoods.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY", "Goods");
                bundle.putSerializable("User", userList.get(groupPosition));
                bundle.putSerializable("Goods", goodsList.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        clvGoods.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(mContext, DetailActivity.class);
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

    @OnClick(R.id.iv_search)
    public void onClick() {
        UIUtil.nextPage(mContext, SearchActivity.class);
    }
}
