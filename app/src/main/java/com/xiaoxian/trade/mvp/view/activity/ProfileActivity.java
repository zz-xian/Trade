package com.xiaoxian.trade.mvp.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoxian.trade.R;
import com.xiaoxian.trade.app.App;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.di.component.AppComponent;
import com.xiaoxian.trade.di.component.DaggerProfileActivityComponent;
import com.xiaoxian.trade.di.module.ProfileActivityModule;
import com.xiaoxian.trade.mvp.contract.ProfileContract;
import com.xiaoxian.trade.mvp.model.User;
import com.xiaoxian.trade.util.FormatUtil;
import com.xiaoxian.trade.util.ToastUtil;
import com.xiaoxian.trade.widget.CircleImageView;
import com.xiaoxian.trade.widget.CustomEditDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;

public class ProfileActivity extends BaseActivity implements ProfileContract.IView{
    @BindView(R.id.profile_back)
    ImageView profileBack;
    @BindView(R.id.user_head_layout)
    LinearLayout userHeadLayout;
    @BindView(R.id.user_head)
    CircleImageView userHead;
    @BindView(R.id.user_name_layout)
    LinearLayout userNameLayout;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.user_sex_layout)
    LinearLayout userSexLayout;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R.id.user_age_layout)
    LinearLayout userAgeLayout;
    @BindView(R.id.tv_user_age)
    TextView tvUserAge;
    @BindView(R.id.user_signature_layout)
    LinearLayout userSignatureLayout;
    @BindView(R.id.tv_user_signature)
    TextView tvUserSignature;
    @Inject
    ProfileContract.IPresenter presenter;

    private TextView tvCamera;
    private TextView tvGallery;

    private Uri imageUri;
    private AlertDialog dialog;
    private CustomEditDialog customEditDialog;

    private static final int TAKE_PHOTO = 1;
    private static final int SELECT_PHOTO = 2;
    private static final int CROP_PHOTO = 3;

    private static final String CHANGE_NAME = "CHANGE_NAME";
    private static final String CHANGE_AGE = "CHANGE_AGE";
    private static final String CHANGE_SIGN = "CHANGE_SIGN";

    private static String path = "/sdcard/trade/image/";

    public static final String TAG = "ProfileActivity";

    final User currentUser = User.getCurrentUser(User.class);

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initComponent(((App) getApplication()).getAppComponent());
    }

    protected void initComponent(AppComponent appComponent) {
        DaggerProfileActivityComponent.builder()
                .appComponent(appComponent)
                .profileActivityModule(new ProfileActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        refreshHead(currentUser.getAvatar());
        if (FormatUtil.isNullOrEmpty(currentUser.getUsername())) {
            tvUserName.setText("未设置");
        } else {
            tvUserName.setText(currentUser.getUsername());
        }
        if (currentUser.getSex() == null || currentUser.getSex().equals("")) {
            tvUserSex.setText("未设置");
        } else {
            tvUserSex.setText(currentUser.getSex() == true ? "男" : "女");
        }
        if (currentUser.getAge() == null || currentUser.getAge().equals("")) {
            tvUserAge.setText("未设置");
        } else {
            tvUserAge.setText(currentUser.getAge().toString());
        }
        if (FormatUtil.isNullOrEmpty(currentUser.getSignature())) {
            tvUserSignature.setText("未设置");
        } else {
            tvUserSignature.setText(currentUser.getSignature());
        }
    }

    private void refreshHead(BmobFile file) {
        if (file == null) {
            userHead.setImageResource(R.mipmap.ic_default);
        } else {
            Glide.with(this)
                    .load(currentUser.getAvatar().getUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        //将图片呈现在ImageView
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                            userHead.setImageDrawable(drawable);
                        }
                    });
        }
    }

    @OnClick({R.id.profile_back, R.id.user_head_layout, R.id.user_name_layout, R.id.user_sex_layout,
            R.id.user_age_layout, R.id.user_signature_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_back:
                finish();
                break;
            case R.id.user_head_layout:
                showHeadDialog();
                break;
            case R.id.user_name_layout:
                showEditDialog(CHANGE_NAME);
                break;
            case R.id.user_sex_layout:
                showSexDialog();
                break;
            case R.id.user_age_layout:
                showEditDialog(CHANGE_AGE);
                break;
            case R.id.user_signature_layout:
                showEditDialog(CHANGE_SIGN);
                break;
        }
    }

    private void showHeadDialog() {
        View view = View.inflate(this, R.layout.item_dialog, null);
        tvCamera = (TextView) view.findViewById(R.id.tv_camera);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File temp = new File(Environment.getExternalStorageDirectory(), "head.png");
                try {
                    if (temp.exists()) {
                        temp.delete();
                    }
                    temp.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(temp);
                Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(i1, TAKE_PHOTO);
                dialog.dismiss();
            }
        });
        tvGallery = (TextView) view.findViewById(R.id.tv_gallery);
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Intent.ACTION_PICK, null);
                i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(i2, SELECT_PHOTO);
                dialog.dismiss();
            }
        });
        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(view);
        dialog.show();
    }

    private void showEditDialog(String str) {
        customEditDialog = new CustomEditDialog(this);
        customEditDialog.show();
        switch (str) {
            case CHANGE_NAME:
                customEditDialog.setOnPosNegClickListener(new CustomEditDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(final String value) {
                        presenter.updateName(currentUser.getObjectId(), value);
                    }

                    @Override
                    public void negClickListener() {
                        ToastUtil.showToast(ProfileActivity.this, "取消修改");
                    }
                });
                break;
            case CHANGE_AGE:
                customEditDialog.setOnPosNegClickListener(new CustomEditDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(final String value) {
                        presenter.updateAge(currentUser.getObjectId(), value);
                    }

                    @Override
                    public void negClickListener() {
                        ToastUtil.showToast(ProfileActivity.this, "取消修改");
                    }
                });
                break;
            case CHANGE_SIGN:
                customEditDialog.setOnPosNegClickListener(new CustomEditDialog.OnPosNegClickListener() {
                    @Override
                    public void posClickListener(final String value) {
                        presenter.updateSign(currentUser.getObjectId(), value);
                    }

                    @Override
                    public void negClickListener() {
                        ToastUtil.showToast(ProfileActivity.this, "取消修改");
                    }
                });
                break;
        }
    }

    private void showSexDialog() {
        String[] sexArray = new String[]{ "男", "女" };
        new AlertDialog.Builder(this)
                .setTitle("性别")
                .setSingleChoiceItems(sexArray, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.updateSex(currentUser.getObjectId(), which);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    cropPhoto(imageUri);
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());
                }
                break;
            case CROP_PHOTO:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = extras.getParcelable("data");
                    if (bitmap != null) {
                        presenter.uploadHead(currentUser.getObjectId(), path);
                        saveToSD(bitmap);
                        userHead.setImageBitmap(bitmap);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //调用系统裁剪
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //aspectX、aspectY：宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //outputX、outputY：裁剪宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PHOTO);
    }

    //保存到SD卡
    private void saveToSD(Bitmap bitmap) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        FileOutputStream fos = null;
        new File(path).mkdirs();
        String fileName = path + "head.png";
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateHeadSuccess(BmobFile bmobFile) {
        refreshHead(bmobFile);
        ToastUtil.showToast(this, "头像更新成功");
    }

    @Override
    public void updateHeadError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "头像更新失败");
    }

    @Override
    public void uploadHeadError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "头像上传失败");
    }

    @Override
    public void updateNameSuccess(String value) {
        tvUserName.setText(value);
        ToastUtil.showToast(this, "修改成功");
    }

    @Override
    public void updateNameError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "修改失败");
    }

    @Override
    public void updateAgeSuccess(String value) {
        tvUserAge.setText(value);
        ToastUtil.showToast(this, "修改成功");
    }

    @Override
    public void updateAgeError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "修改失败");
    }

    @Override
    public void updateSignSuccess(String value) {
        tvUserSignature.setText(value);
        ToastUtil.showToast(this, "修改成功");
    }

    @Override
    public void updateSignError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "修改失败");
    }

    @Override
    public void updateSexSuccess(Boolean sex) {
        tvUserSex.setText(sex == true ? "男" : "女");
        ToastUtil.showToast(this, "修改成功");
    }

    @Override
    public void updateSexError(String str) {
        Log.e(TAG, str);
        ToastUtil.showToast(this, "修改失败");
    }
}
