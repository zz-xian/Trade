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

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerMainActivityComponent;
import com.xiaoxian.trade.di.module.MainActivityModule;
import com.xiaoxian.trade.mvp.contract.HomeContract;
import com.xiaoxian.trade.mvp.model.Banner;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.activity.DetailActivity;
import com.xiaoxian.trade.mvp.view.activity.SearchActivity;
import com.xiaoxian.trade.mvp.view.adapter.BannerHolder;
import com.xiaoxian.trade.mvp.view.adapter.CustomAdapter;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;
import com.xiaoxian.trade.widget.CustomListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements HomeContract.IView {
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.banner)
    ConvenientBanner banner;
    @BindView(R.id.weather)
    ImageView weather;
    @BindView(R.id.custom_list)
    CustomListView customList;
    @Inject
    HomeContract.IPresenter presenter;

    private View mRootView;
    private Context mContext;
    private Unbinder unbinder;

    private CustomAdapter adapter;

    private List<Goods> goodsList;
    private List<Banner> bannerList = new ArrayList<>();

    private Boolean isFirstCreate = true;

    public static final String TAG = "HomeFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_home, container, false);
            isFirstCreate = false;
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
        presenter.loadBanner(bannerList);
        presenter.loadGoods();
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        banner.startTurning(4000);
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止自动翻页
        banner.stopTurning();
    }

    /**
     * 获取该Fragment对象状态改变，可视状态加载列表
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && !isFirstCreate){
            presenter.loadGoods();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_search, R.id.weather})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                UIUtil.nextPage(mContext, SearchActivity.class);
                break;
            case R.id.weather:
                ToastUtil.showToast(mContext, "尚未开放功能入口");
                //后续添加相应功能
                break;
        }
    }

    @Override
    public void showBanner(List<Banner> bannerList) {
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerHolder();
            }
        }, bannerList)
                .setPageIndicator(new int[]{R.mipmap.banner_white, R.mipmap.banner_blue})//设置翻页指示器
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);//设置翻页指示器位置
    }

    @Override
    public void loadGoodsSuccess(List<Goods> goodsList) {
        this.goodsList = goodsList;
        presenter.parseGoodsUser(goodsList);
    }

    @Override
    public void loadGoodsError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(mContext, "加载商品列表失败");
    }

    @Override
    public void parseUser(final List<User> userList) {
        Log.i("User", userList.size() + "");
        Log.i("Goods", goodsList.size() + "");
        adapter = new CustomAdapter(mContext, userList, goodsList);
        customList.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            customList.expandGroup(i);
        }
        customList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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
        customList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY", "Goods");
                bundle.putSerializable("User",userList.get(groupPosition));
                bundle.putSerializable("Goods",goodsList.get(groupPosition));
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
    }
}
