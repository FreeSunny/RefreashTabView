package com.example.refreashtabview.wight;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by hzsunyj on 16/4/25.
 */
public class WrapperTextView extends TextView {

    public WrapperTextView(Context context) {
        super(context);
        init();
    }

    public WrapperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WrapperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("Wrapper", "action " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

}
