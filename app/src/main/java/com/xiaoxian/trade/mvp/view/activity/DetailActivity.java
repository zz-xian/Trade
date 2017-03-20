package com.xiaoxian.trade.mvp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.lzp.floatingactionbuttonplus.FabTagLayout;
import com.lzp.floatingactionbuttonplus.FloatingActionButtonPlus;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerDetailActivityComponent;
import com.xiaoxian.trade.di.module.DetailActivityModule;
import com.xiaoxian.trade.mvp.contract.DetailContract;
import com.xiaoxian.trade.mvp.model.Cart;
import com.xiaoxian.trade.mvp.model.Collect;
import com.xiaoxian.trade.mvp.model.Goods;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.adapter.ImageHolder;
import com.xiaoxian.trade.util.ToastUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity implements DetailContract.IView{
    @BindView(R.id.detail_back)
    ImageView detailBack;
    @BindView(R.id.goods_info)
    ScrollView goodsInfo;
    @BindView(R.id.goods_img)
    ConvenientBanner goodsImg;
    @BindView(R.id.goods_title)
    TextView goodsTitle;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.goods_mount)
    TextView goodsMount;
    @BindView(R.id.goods_condition)
    TextView goodsCondition;
    @BindView(R.id.goods_location)
    TextView goodsLocation;
    @BindView(R.id.goods_desc)
    TextView goodsDesc;
    @BindView(R.id.fab)
    FloatingActionButtonPlus fab;
    @Inject
    DetailContract.IPresenter presenter;

    private User user;
    private Goods goods;
    private Cart cart;
    private Collect collect;

    private String key;
    private String phone;

    //是否收藏
    private boolean isCollect = false;
    //收藏按钮是否点击
    private boolean isClickCollect = false;

    //是否求购
    private boolean isCart = false;
    //求购按钮是否点击
    private boolean isClickCart = false;

    public static final String TAG = "DetailActivity";

    final User currentUser = User.getCurrentUser(User.class);

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());
        initEvent();
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerDetailActivityComponent.builder()
                .appComponent(appComponent)
                .detailActivityModule(new DetailActivityModule(this))
                .build()
                .inject(this);
    }

    private void initEvent() {
        fab.setOnItemClickListener(new FloatingActionButtonPlus.OnItemClickListener() {
            @Override
            public void onItemClick(FabTagLayout tagView, int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        startActivity(intent);
                        break;
                    case 1:
                        isClickCollect = true;
                        if (currentUser == null) {
                            ToastUtil.showToast(DetailActivity.this, R.string.str_no_login);
                        } else {
                            presenter.queryCollect(currentUser.getObjectId());
                        }
                        break;
                    case 2:
                        isClickCart = true;
                        if (currentUser == null) {
                            ToastUtil.showToast(DetailActivity.this, R.string.str_no_login);
                        } else {
                            presenter.queryCart(currentUser.getObjectId());
                        }
                        break;
                }
            }
        });
    }

    private boolean collectDataQuery(List<Collect> collectList) {
        int i;
        for (i = 0; i < collectList.size(); i++) {
            if (collectList.get(i).getGoodsId().equals(goods.getObjectId())) {
                collect = collectList.get(i);
                return true;
            }
        }
        if (i == collectList.size() - 1) {
            return false;
        }
        return false;
    }

    private boolean cartDataQuery(List<Cart> cartList) {
        int i;
        for (i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getGoodsId().equals(goods.getObjectId())) {
                cart = cartList.get(i);
                return true;
            }
        }
        if (i == cartList.size() - 1) {
            return false;
        }
        return false;
    }

    @Override
    public void initData() {
        user = (User) getIntent().getSerializableExtra("User");

        key = getIntent().getStringExtra("KEY");
        switch (key) {
            case "Goods":
                goods = (Goods) getIntent().getSerializableExtra("Goods");
                goodsTitle.setText(goods.getTitle());
                goodsPrice.setText(goods.getPrice());
                goodsMount.setText(goods.getMount());
                goodsCondition.setText(goods.getCondition());
                goodsLocation.setText(goods.getLocation());
                goodsDesc.setText(goods.getDesc());
                phone = goods.getPhone();
                setLoopView(goods.getImages());
                break;
            case "Collect":
                Collect collect = (Collect) getIntent().getSerializableExtra("Collect");
                goodsTitle.setText(collect.getTitle());
                goodsPrice.setText(collect.getPrice());
                goodsMount.setText(collect.getMount());
                goodsCondition.setText(collect.getCondition());
                goodsLocation.setText(collect.getLocation());
                goodsDesc.setText(collect.getDesc());
                phone = collect.getPhone();
                setLoopView(collect.getImages());
                break;
            case "Cart":
                Cart cart = (Cart) getIntent().getSerializableExtra("Cart");
                goodsTitle.setText(cart.getTitle());
                goodsPrice.setText(cart.getPrice());
                goodsMount.setText(cart.getMount());
                goodsCondition.setText(cart.getCondition());
                goodsLocation.setText(cart.getLocation());
                goodsDesc.setText(cart.getDesc());
                phone = cart.getPhone();
                setLoopView(cart.getImages());
                break;
        }
    }

    //给商品轮播图加载图片
    private void setLoopView(List<String> list) {
        goodsImg.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ImageHolder();
            }
        }, list)
                .setPageIndicator(new int[]{R.mipmap.index_white, R.mipmap.index_red})//设置翻页指示器
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);//设置翻页指示器位置
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        goodsImg.startTurning(4000);
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止自动翻页
        goodsImg.stopTurning();
    }

    @OnClick({R.id.detail_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_back:
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
        }
    }

    @Override
    public void queryCollectSuccess(List<Collect> collectList) {
        //Collect表为空
        if (collectList == null || collectList.size() == 0) {
            if (isClickCollect) {
                presenter.saveCollect(user.getObjectId(), goods);
                isClickCollect = false;
            } else {
                isCollect = false;
            }
        }else {
            if (isClickCollect) {
                isCollect = collectDataQuery(collectList);
                if (isCollect) {
                    presenter.deleteCollect(collect.getObjectId());
                } else {
                    presenter.saveCollect(user.getObjectId(), goods);
                }
            } else {
                isCollect = collectDataQuery(collectList);
            }
        }
    }

    @Override
    public void queryCollectError(String str) {
        Log.e(TAG, str);
    }

    @Override
    public void queryCartSuccess(List<Cart> cartList) {
        //Cart表为空
        if (cartList == null || cartList.size() == 0) {
            if (isClickCart) {
                presenter.saveCart(user.getObjectId(), goods);
                isClickCart = false;
            } else {
                isCart = false;
            }
        }else {
            if (isClickCart) {
                isCart = cartDataQuery(cartList);
                if (isCart) {
                    presenter.deleteCart(cart.getObjectId());
                } else {
                    presenter.saveCart(user.getObjectId(), goods);
                }
            } else {
                isCart = cartDataQuery(cartList);
            }
        }
    }

    @Override
    public void queryCartError(String str) {
        Log.e(TAG, str);
    }

    @Override
    public void saveCollectSuccess() {
        isCollect = true;
        ToastUtil.showToast(this, "收藏成功");
    }

    @Override
    public void saveCollectError(String str) {
        isCollect = false;
        Log.e(TAG, str);
        ToastUtil.showToast(this, "收藏失败");
    }

    @Override
    public void saveCartSuccess() {
        isCart = true;
        ToastUtil.showToast(this, "求购成功");
    }

    @Override
    public void saveCartError(String str) {
        isCart = false;
        Log.e(TAG, str);
        ToastUtil.showToast(this, "求购失败");
    }

    @Override
    public void deleteCollectSuccess() {
        isCollect = false;
        ToastUtil.showToast(this, "已取消收藏哦");
    }

    @Override
    public void deleteCollectError(String str) {
        isCollect = true;
        Log.e(TAG, str);
        ToastUtil.showToast(this, "取消收藏失败");
    }

    @Override
    public void deleteCartSuccess() {
        isCart = false;
        ToastUtil.showToast(this, "已取消求购哦");
    }

    @Override
    public void deleteCartError(String str) {
        isCart = true;
        Log.e(TAG, str);
        ToastUtil.showToast(this, "取消求购失败");
    }
}
