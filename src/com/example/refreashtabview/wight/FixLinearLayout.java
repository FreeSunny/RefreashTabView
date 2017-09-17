package com.example.refreashtabview.wight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by hzsunyj on 2017/9/17.
 */

public class FixLinearLayout extends LinearLayout {
    public FixLinearLayout(Context context) {
        super(context);
    }

    public FixLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 改写高度，内容多高，就多高
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
