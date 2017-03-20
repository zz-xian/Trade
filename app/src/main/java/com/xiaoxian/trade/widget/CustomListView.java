package com.xiaoxian.trade.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class CustomListView extends ExpandableListView {
    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 宽、高和各自方向上对应模式合成的一个值
     * int类型32位二进制位，31-30表示模式，0-29表示宽、高实际值
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //makeMeasureSpec(specSize,specMode)：将大小、模式合成
        //AT_MOST：表示父控件给子view一个最大的特定值，子view不能超过该值大小
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
