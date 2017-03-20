package com.xiaoxian.trade.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 弹性滑动ScrollView
 */

public class SpringScrollView extends ScrollView {
    private View contentView; //ScrollView唯一内容控件
    private Rect originalRect = new Rect(); //用于记录正常布局位置

    private boolean isMoved = false; //记录是否移动布局
    private boolean canPullUp = false; //是否可以继续上拉
    private boolean canPullDown = false; //是否可以继续下拉

    private static final float MOVE_FACTOR = 0.5f; //移动因子（手指移动100px，View就只移动50px）
    private static final int ANIM_TIME = 300; //松开手指后，界面回到正常位置所需动画时间

    private float startY; //手指按下时的Y值, 用于在移动时计算移动距离，若按下时不能上拉和下拉，会在手指移动时更新为当前手指Y值

    public SpringScrollView(Context context) {
        super(context);
    }

    public SpringScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpringScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //当View及其子View从XML文件中加载完成后会被调用
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
    }

    //当前View需为子View分配大小和位置时调用
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (contentView == null) {
            return;
        }
        //ScrollView中唯一子控件的位置信息, 这个位置信息在整个控件生命周期中保持不变
        originalRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
    }

    //触摸事件中处理上拉和下拉逻辑
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (contentView == null) {
            return super.dispatchTouchEvent(ev);
        }
        //手指是否移动到当前ScrollView控件外
        boolean isTouchOutOfScrollView = ev.getY() >= this.getHeight() || ev.getY() <= 0;
        if (isTouchOutOfScrollView) {
            if (isMoved) //如果当前contentView已被移动, 首先把布局移到原位置, 然后消费这个事件
            {
                boundBack();
            }
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //判断是否可以上拉、下拉
                canPullUp = isCanPullUp();
                canPullDown = isCanPullDown();
                //记录按下时的Y值
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                boundBack();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动过程既未滚动到可以上拉的程度，也未滚动到可以下拉的程度
                if (!canPullUp && !canPullDown) {
                    startY = ev.getY();
                    canPullUp = isCanPullUp();
                    canPullDown = isCanPullDown();
                    break;
                }
                //计算手指移动距离
                float nowY = ev.getY();
                int deltaY = (int) (nowY - startY);

                //是否应该移动布局
                boolean shouldMove = (canPullUp && deltaY < 0) //可以上拉，并且手指向上移动
                        || (canPullDown && deltaY > 0) //可以下拉，并且手指向下移动
                        || (canPullUp && canPullDown); //既可上拉也可下拉（该情况出现在ScrollView包裹控件比ScrollView还小）
                if (shouldMove) {
                    //计算偏移量
                    int offset = (int) (deltaY * MOVE_FACTOR);
                    //随手指的移动而移动布局
                    contentView.layout(originalRect.left, originalRect.top + offset, originalRect.right, originalRect.bottom + offset);
                    //记录移动布局
                    isMoved = true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //将内容布局移到原位置（可在UP事件调用，或其他需要的地方调用-如手指移动到当前ScrollView外时）
    private void boundBack() {
        if (!isMoved) {
            return;
        }
        //开启动画
        TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(), originalRect.top);
        anim.setDuration(ANIM_TIME);
        contentView.startAnimation(anim);
        //设置回到正常布局位置
        contentView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
        //将标志位设回false
        isMoved = false;
        canPullUp = false;
        canPullDown = false;
    }

    //getScrollY()达到最大加上scrollView的高度就等于其内容高度
    //判断是否滚动到底部
    private boolean isCanPullUp() {
        return contentView.getHeight() <= getHeight() + getScrollY();
    }

    //判断是否滚动到顶部
    private boolean isCanPullDown() {
        return getScrollY() == 0 || contentView.getHeight() < getHeight() + getScrollY();
    }
}
