package com.example.refreashtabview.wight;

import android.content.Context;
import android.util.AttributeSet;

import com.example.refreashtabview.refreash.PullToRefreshListView;

/**
 * Created by hzsunyj on 16/5/6.
 */
public class WrapperPullRefreshListView extends PullToRefreshListView {
    public WrapperPullRefreshListView(Context context) {
        super(context);
    }

    public WrapperPullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapperPullRefreshListView(Context context, Mode mode) {
        super(context, mode);
    }

    //onInterceptTouchEvent(MotionEvent event) {
    // super.onInterceptTouchEvent(event);
    //}
    // onInterceptTouchEvent
}
