package com.xiaoxian.trade.mvp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerMainActivityComponent;
import com.xiaoxian.trade.di.module.MainActivityModule;
import com.xiaoxian.trade.mvp.contract.SortContract;
import com.xiaoxian.trade.mvp.model.Icon;
import com.xiaoxian.trade.mvp.model.Sort;
import com.xiaoxian.trade.mvp.view.activity.KindActivity;
import com.xiaoxian.trade.mvp.view.activity.MainActivity;
import com.xiaoxian.trade.mvp.view.activity.ResultActivity;
import com.xiaoxian.trade.mvp.view.activity.SearchActivity;
import com.xiaoxian.trade.mvp.view.adapter.GridViewAdapter;
import com.xiaoxian.trade.mvp.view.adapter.SortAdapter;
import com.xiaoxian.trade.mvp.view.adapter.ViewPagerAdapter;
import com.xiaoxian.trade.util.UIUtil;
import com.xiaoxian.trade.widget.DrawableTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SortFragment extends Fragment implements SortContract.IView {
    @BindView(R.id.tv_search)
    DrawableTextView tvSearch;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;
    @BindView(R.id.sort_list)
    RecyclerView sortList;
    @BindView(R.id.all_text)
    TextView allText;
    @Inject
    SortContract.IPresenter presenter;

    private View mRootView;
    private Context mContext;

    private Unbinder unbinder;

    private SortAdapter sortAdapter;

    private String[] titles = {"手机", "相机", "电脑/配件", "3C数码",
            "交通工具", "家用电器", "家居用品", "文娱用品",
            "门票/服务", "服装鞋包", "珠宝首饰", "化妆用品"};

    private List<Icon> mIcons;
    private List<View> mPagerList;
    private LayoutInflater inflater;

    //总的页数
    private int pageCount;
    //页数下标
    private int curIndex = 0;
    //每页显示个数
    private int pageSize = 8;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_sort, container, false);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        initComponent(((App) mContext.getApplicationContext()).getAppComponent());
        initData();
        initViewPager();
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
        final Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        ((MainActivity) mContext).setSupportActionBar(toolbar);
        ((MainActivity) mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitle("");
        presenter.loadSort();

        mIcons = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            //动态获取资源ID：资源名称、资源类型、包名
            int imageId = getResources().getIdentifier("ic_category_" + i, "mipmap", mContext.getPackageName());
            mIcons.add(new Icon(imageId, titles[i]));
        }
    }

    public void initViewPager() {
        inflater = LayoutInflater.from(mContext);
        //总的页数=总数/每页数量，取整
        pageCount = (int) Math.ceil(mIcons.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都inflate出一个实例
            GridView gridView = (GridView) inflater.inflate(R.layout.layout_grid_view, viewpager, false);
            gridView.setAdapter(new GridViewAdapter(mContext, mIcons, i, pageSize));
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    Bundle bundle = new Bundle();
                    bundle.putString("Result", "Kind");
                    bundle.putString("Key", mIcons.get(pos).getName());
                    UIUtil.nextPage(mContext, ResultActivity.class, bundle);
                }
            });
        }
        viewpager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置圆点
        setOvalLayout();
    }

    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            llPoint.addView(inflater.inflate(R.layout.item_point, null));
        }
        //默认显示第一页
        llPoint.getChildAt(0).findViewById(R.id.v_point).setBackgroundResource(R.drawable.point_select);
        //注意setOnPageChangeListener()已过期
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //取消圆点选中
                llPoint.getChildAt(curIndex).findViewById(R.id.v_point).setBackgroundResource(R.drawable.point_normal);
                //圆点选中
                llPoint.getChildAt(position).findViewById(R.id.v_point).setBackgroundResource(R.drawable.point_select);
                curIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showSort(List<Sort> sorts) {
        sortAdapter = new SortAdapter(mContext, sorts);
        sortList.setLayoutManager(new LinearLayoutManager(mContext));
        sortList.setAdapter(sortAdapter);
        sortAdapter.setSortAdapterOnClickListener(new SortAdapter.SortAdapterOnClickListener() {
            @Override
            public void onClick(int item, int pos) {
                Bundle bundle = new Bundle();
                switch (item) {
                    case 0:
                        if (pos == 1) {
                            bundle.putString("Result", "SubKind");
                            bundle.putString("Key", "iPhone");
                            UIUtil.nextPage(mContext, ResultActivity.class, bundle);
                        } else if (pos == 2) {
                            bundle.putString("Result", "SubKind");
                            bundle.putString("Key", "三星");
                            UIUtil.nextPage(mContext, ResultActivity.class, bundle);
                        } else if (pos == 3) {
                            bundle.putString("Result", "SubKind");
                            bundle.putString("Key", "华为");
                            UIUtil.nextPage(mContext, ResultActivity.class, bundle);
                        }
                        break;
                    case 1:
                        if (pos == 1) {
                            bundle.putString("Result", "SubKind");
                            bundle.putString("Key", "佳能");
                            UIUtil.nextPage(mContext, ResultActivity.class, bundle);
                        } else if (pos == 2) {
                            bundle.putString("Result", "SubKind");
                            bundle.putString("Key", "尼康");
                            UIUtil.nextPage(mContext, ResultActivity.class, bundle);
                        } else if (pos == 3) {
                            bundle.putString("Result", "SubKind");
                            bundle.putString("Key", "拍立得");
                            UIUtil.nextPage(mContext, ResultActivity.class, bundle);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void showSortError(String str) {
        Log.e("显示失败!", str);
    }

    @OnClick({R.id.tv_search, R.id.all_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                UIUtil.nextPage(mContext, SearchActivity.class);
                break;
            case R.id.all_text:
                UIUtil.nextPage(mContext, KindActivity.class);
                break;
        }
    }
}
