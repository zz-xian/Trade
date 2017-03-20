package com.xiaoxian.trade.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.util.ToastUtil;

public class CustomEditDialog extends Dialog implements View.OnClickListener{
    private View view;
    private Context context;

    private LinearLayout llEdit;
    private EditText etMsg;
    private Button btnNeg;
    private Button btnPos;

    public CustomEditDialog(Context context) {
        this(context, 0, null);
    }

    public CustomEditDialog(Context context, int theme, View contentView) {
        super(context, theme == 0 ? R.style.CustomDialog : theme);
        this.view = contentView;
        this.context = context;
        if (view == null) {
            view = View.inflate(context, R.layout.item_edit, null);
        }
        initView();
        initData();
    }

    private void initView() {
        this.setContentView(view);
        llEdit = (LinearLayout) view.findViewById(R.id.ll_edit);
        etMsg = (EditText) view.findViewById(R.id.et_msg);
        btnNeg = (Button) view.findViewById(R.id.btn_neg);
        btnPos = (Button) view.findViewById(R.id.btn_pos);
    }

    private void initData() {
        //设置背景是屏幕0.8
        llEdit.setLayoutParams(new FrameLayout.LayoutParams((int) (getMobileWidth(context) * 0.8), LinearLayout.LayoutParams.WRAP_CONTENT));
        btnNeg.setOnClickListener(this);
        btnPos.setOnClickListener(this);
    }

    public static int getMobileWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_neg:
//                if(onPosNegClickListener != null) {
//                    String value = etMsg.getHint().toString().trim();
//                    if(!value.isEmpty()) {
//                        onPosNegClickListener.negClickListener(value);
//                    }
//                }
                onPosNegClickListener.negClickListener();
                this.dismiss();
                break;
            case R.id.btn_pos:
                if(onPosNegClickListener != null) {
                    String value = etMsg.getText().toString().trim();
                    if (!value.isEmpty()) {
                        onPosNegClickListener.posClickListener(value);
                    } else {
                        ToastUtil.showToast(context, "输入不能为空");
                    }
                }
                this.dismiss();
                break;
        }
    }

    private OnPosNegClickListener onPosNegClickListener;

    public void setOnPosNegClickListener(OnPosNegClickListener onPosNegClickListener) {
        this.onPosNegClickListener = onPosNegClickListener;
    }

    public interface OnPosNegClickListener {
        void posClickListener(String value);
        void negClickListener();
    }
}
