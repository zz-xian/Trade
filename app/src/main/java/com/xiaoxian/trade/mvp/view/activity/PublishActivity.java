package com.xiaoxian.trade.mvp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerPublishActivityComponent;
import com.xiaoxian.trade.di.module.PublishActivityModule;
import com.xiaoxian.trade.mvp.contract.PublishContract;
import com.xiaoxian.trade.mvp.view.activity.galley.AlbumActivity;
import com.xiaoxian.trade.mvp.view.adapter.PublishAdapter;
import com.xiaoxian.trade.util.BitmapUtil;
import com.xiaoxian.trade.util.FormatUtil;
import com.xiaoxian.trade.util.ImageItem;
import com.xiaoxian.trade.util.ToastUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublishActivity extends BaseActivity implements PublishContract.IView {
    @BindView(R.id.goods_send)
    TextView goodsSend;
    @BindView(R.id.goods_title)
    EditText goodsTitle;
    @BindView(R.id.goods_desc)
    EditText goodsDesc;
    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.goods_price)
    EditText goodsPrice;
    @BindView(R.id.goods_kind)
    RelativeLayout goodsKind;
    @BindView(R.id.goods_kind_text)
    TextView goodsKindText;
    @BindView(R.id.goods_kind_next)
    ImageView goodsKindNext;
    @BindView(R.id.goods_condition)
    EditText goodsCondition;
    @BindView(R.id.goods_mount)
    EditText goodsMount;
    @BindView(R.id.goods_phone)
    EditText goodsPhone;
    @BindView(R.id.goods_location)
    TextView goodsLocation;
    @Inject
    PublishContract.IPresenter presenter;

    private View parentView;
    private PopupWindow pw = null;
    private LinearLayout popupWindow;

    private PublishAdapter adapter;

    private String[] imagePaths;
    private ProgressDialog dialog;

    private String kind, subKind;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_KIND = 2;

    private String mCurrentLocation;
    private String mCurrentCity;

    public static Bitmap bitmap;

    public static final String TAG = "PublishActivity";

    // 定位相关
    LocationClient mClient;
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
            mCurrentLocation = location.getAddrStr();
            mCurrentCity = location.getCity();
            goodsLocation.setText(mCurrentLocation);

            goodsLocation.postInvalidate();//用于非UI线程
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_add_bg);
        parentView = getLayoutInflater().inflate(R.layout.activity_publish, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        pw = new PopupWindow(this);
        initComponent(((App) getApplication()).getAppComponent());
        initLocation();
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerPublishActivityComponent.builder()
                .appComponent(appComponent)
                .publishActivityModule(new PublishActivityModule(this))
                .build()
                .inject(this);
    }

    public void initLocation() {
        mClient = new LocationClient(this);
        mClient.registerLocationListener(mListener);
        LocationClientOption option = new LocationClientOption();//设置定位方式
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");//设置坐标类型
        option.setIsNeedAddress(true);//开启位置信息包括city
        mClient.setLocOption(option);
        mClient.start();
    }

    @Override
    public void initData() {
        View view = getLayoutInflater().inflate(R.layout.item_popup, null);
        pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setFocusable(true);
        //设置点击屏幕其他地方消失
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable(this.getResources(), (Bitmap) null));
        pw.setContentView(view);

        popupWindow = (LinearLayout) view.findViewById(R.id.popup_window);

        RelativeLayout rootView = (RelativeLayout) view.findViewById(R.id.rootView);

        Button popupWindowCamera = (Button) view.findViewById(R.id.popup_window_camera);
        Button popupWindowPhoto = (Button) view.findViewById(R.id.popup_window_photo);
        Button popupWindowCancel = (Button) view.findViewById(R.id.popup_window_cancel);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                popupWindow.clearAnimation();
            }
        });
        popupWindowCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                pw.dismiss();
                popupWindow.clearAnimation();
            }
        });
        popupWindowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishActivity.this, AlbumActivity.class);
                startActivity(intent);
                //overridePendingTransition()只能在startActivity()或finish()之后调用
                overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
                pw.dismiss();
                popupWindow.clearAnimation();
            }
        });
        popupWindowCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                popupWindow.clearAnimation();
            }
        });

        adapter = new PublishAdapter(this);
        adapter.setOnPublishItemMoveListener(new PublishAdapter.OnPublishItemMoveListener() {
            @Override
            public void onItemMove(View v, int position) {
                BitmapUtil.tempBitmap.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        gridView.setAdapter(adapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                if (position == BitmapUtil.tempBitmap.size()) {
                    popupWindow.startAnimation(AnimationUtils.loadAnimation(PublishActivity.this, R.anim.translate_in));
                    pw.showAtLocation(parentView, Gravity.CENTER, 0, 0);
                } else {
                    //后续增加查看图片功能
                    ToastUtil.showToast(PublishActivity.this, "图片地址：" + BitmapUtil.tempBitmap.get(position).imagePath);
                }
            }
        });
    }

    @OnClick({R.id.goods_kind, R.id.goods_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goods_kind:
                processChooseKind();
                break;
            case R.id.goods_send:
                processPublish();
                break;
        }
    }

    public void processChooseKind() {
        Intent intent = new Intent(this, AllKindActivity.class);
        startActivityForResult(intent, CHOOSE_KIND);
    }

    public void processPublish() {
        if (FormatUtil.isNullOrEmpty(goodsTitle.getText().toString())) {
            ToastUtil.showToast(PublishActivity.this, "商品主题不能为空");
            return;
        }
        if (FormatUtil.isNullOrEmpty(goodsDesc.getText().toString())) {
            ToastUtil.showToast(PublishActivity.this, "商品描述不能为空");
            return;
        }
        if (FormatUtil.isNullOrEmpty(goodsCondition.getText().toString())
                || FormatUtil.isNullOrEmpty(goodsPrice.getText().toString())
                || FormatUtil.isNullOrEmpty(goodsMount.getText().toString())
                || FormatUtil.isNullOrEmpty(goodsPhone.getText().toString())) {
            ToastUtil.showToast(PublishActivity.this, "商品信息尚未填写完整");
            return;
        }
        if (!FormatUtil.checkPhone(goodsPhone.getText().toString())) {
            ToastUtil.showToast(PublishActivity.this, "手机号码格式错误");
            return;
        }
        imagePaths = new String[BitmapUtil.tempBitmap.size()];
        for (int i = 0; i < BitmapUtil.tempBitmap.size(); i++) {
            imagePaths[i] = BitmapUtil.tempBitmap.get(i).getImagePath();
        }
        dialog = new ProgressDialog(this);
        dialog.setTitle("正在发布");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        presenter.upload(imagePaths);
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void publishSuccess() {
        dialog.dismiss();
        ToastUtil.showToast(this, "发布成功");
        finish();
    }

    @Override
    public void publishError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "发布失败");
    }

    @Override
    public void uploadSuccess(List<String> images) {
        ToastUtil.showToast(this, "上传成功");
        //图片上传成功后，加上商品信息发布
        presenter.publish(goodsTitle.getText().toString(), goodsDesc.getText().toString()
                , images, goodsPrice.getText().toString(), kind, subKind, goodsCondition.getText().toString()
                , goodsMount.getText().toString(), goodsPhone.getText().toString(), mCurrentLocation, mCurrentCity);
    }

    @Override
    public void uploadError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "上传失败");
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        adapter.notifyDataSetChanged();
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (data == null) {
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bmp = bundle.getParcelable("data");
                        Uri uri = BitmapUtil.saveBitmap(bmp);
                        ImageItem item = new ImageItem();
                        item.setBitmap(bmp);
                        item.setImagePath(uri.getPath());
                        BitmapUtil.tempBitmap.add(item);
                    }
                }
                break;
            case CHOOSE_KIND:
                if (data == null) {
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        kind = bundle.getString("Kind");
                        subKind = bundle.getString("SubKind");
                        goodsKindText.setText(kind + " - " + subKind);
                    }
                }
                break;
        }
        adapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mClient.unRegisterLocationListener(mListener);
        mClient.stop();
        bitmap.recycle();
        super.onDestroy();
    }
}
