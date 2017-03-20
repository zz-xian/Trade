package com.xiaoxian.trade.mvp.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.mvp.view.fragment.HomeFragment;
import com.xiaoxian.trade.mvp.view.fragment.NearFragment;
import com.xiaoxian.trade.mvp.view.fragment.SelfFragment;
import com.xiaoxian.trade.mvp.view.fragment.SortFragment;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tab_publish)
    TextView tabPublish;
    @BindView(R.id.tab_home)
    TextView tabHome;
    @BindView(R.id.tab_near)
    TextView tabNear;
    @BindView(R.id.tab_sort)
    TextView tabSort;
    @BindView(R.id.tab_person)
    TextView tabPerson;

    private List<Fragment> mFragments;
    private HomeFragment mHomeFragment;
    private NearFragment mNearFragment;
    private SortFragment mSortFragment;
    private SelfFragment mSelfFragment;

    private List<TextView> mTextViews;

    private String mWorkMode;
    private String name;
    private String img_url;

    private long exitTime = 0;

    private int mOldIndex = 0;
    private int mCurrentIndex = 0;

    private FragmentTransaction ft;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragments();
        initTextViews();
    }

    //初始化Fragment
    private void initFragments() {
        mHomeFragment = new HomeFragment();
        mNearFragment = new NearFragment();
        mSortFragment = new SortFragment();
        mSelfFragment = new SelfFragment();

        mFragments = new ArrayList<>();
        mFragments.add(mHomeFragment);
        mFragments.add(mNearFragment);
        mFragments.add(mSortFragment);
        mFragments.add(mSelfFragment);
        //默认加载前面两个Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mHomeFragment)
                .add(R.id.content, mNearFragment)
                .show(mHomeFragment)
                .hide(mNearFragment)
                .commit();
    }

    private void initTextViews() {
        mTextViews = new ArrayList<>();
        mTextViews.add(tabHome);
        mTextViews.add(tabNear);
        mTextViews.add(tabSort);
        mTextViews.add(tabPerson);
    }

    @Override
    public void initData() {
		if (BmobUser.getCurrentUser(User.class) == null) {
            UIUtil.nextPage(this, LoginActivity.class);
		}
        if (getIntent() != null && getIntent().getExtras() != null) {
            mWorkMode = getIntent().getExtras().getString("WorkMode");
            if (mWorkMode != null && mWorkMode.equals("Self")){
                showCurrentFragment(3);
            }
            else{
                tabHome.setSelected(true);
            }
//            name = getIntent().getExtras().getString("name");
//            img_url = getIntent().getExtras().getString("img_url");
//            Bundle bundle = new Bundle();
//            bundle.putString("name", name);
//            bundle.putString("img_url", img_url);
//            mFragments.get(3).setArguments(bundle);
//            getSupportFragmentManager().beginTransaction().add(R.id.content, mFragments.get(3)).commit();
        } else {
            tabHome.setSelected(true);
        }
    }

    //展示当前选中Fragment
    private void showCurrentFragment(int currentIndex) {
        if (currentIndex != mOldIndex) {
            ft = getSupportFragmentManager().beginTransaction();
            tabPublish.setSelected(false);
            mTextViews.get(mOldIndex).setSelected(false);
            mTextViews.get(currentIndex).setSelected(true);
            ft.hide(mFragments.get(mOldIndex));
            if (!mFragments.get(currentIndex).isAdded()) {
                ft.add(R.id.content, mFragments.get(currentIndex)).show(mFragments.get(currentIndex)).commit();
            } else {
                ft.show(mFragments.get(currentIndex)).commit();
            }
            mOldIndex = currentIndex;
        }
    }

    @OnClick({R.id.tab_publish, R.id.tab_home, R.id.tab_near, R.id.tab_sort, R.id.tab_person})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_publish:
                processPublish();
                break;
            case R.id.tab_home:
                mCurrentIndex = 0;
                break;
            case R.id.tab_near:
                mCurrentIndex = 1;
                break;
            case R.id.tab_sort:
                mCurrentIndex = 2;
                break;
            case R.id.tab_person:
                mCurrentIndex = 3;
                break;
        }
        if (mCurrentIndex != 4){
            showCurrentFragment(mCurrentIndex);
        }
    }

    public void processPublish(){
        User user = BmobUser.getCurrentUser(User.class);
        if (user == null){
            ToastUtil.showToast(this, R.string.str_no_login);
            UIUtil.nextPage(this, LoginActivity.class);
        }else {
            mCurrentIndex = 4;
            mTextViews.get(mOldIndex).setSelected(false);
            tabPublish.setSelected(true);
            mOldIndex = 0;
            UIUtil.nextPage(this, PublishActivity.class);
        }
    }

    @Override
    protected void onResume() {
        if (mCurrentIndex == 4){
            ft = getSupportFragmentManager().beginTransaction();
            mCurrentIndex = 3;
            tabPublish.setSelected(false);
            mTextViews.get(3).setSelected(true);
            if (!mFragments.get(3).isAdded()){
                ft.add(R.id.content,mFragments.get(3)).hide(mFragments.get(mOldIndex)).show(mFragments.get(3)).commit();
            }else{
                ft.hide(mFragments.get(mOldIndex)).show(mFragments.get(3)).commit();
            }
            mOldIndex = mCurrentIndex;
        }
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            /**
             * 两秒之内按返回键就会退出（时间差是否在预期值内）
             * 首次按BACK会弹Toast
             */
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showToast(this, "再按一次退出应用");
                exitTime = System.currentTimeMillis();
            } else {
                finish();//仅仅释放当前Activity，没有释放资源
                System.exit(0);//退出整个应用程序，释放内存
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
