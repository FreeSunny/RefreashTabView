package com.example.refreashtabview.wight;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * Created by hzsunyj on 2018/1/18.
 */

public class LinkMovement implements View.OnTouchListener {

    public static LinkMovement getInstance() {
        if (sInstance == null) {
            sInstance = new LinkMovement();
        }
        return sInstance;
    }

    private static LinkMovement sInstance;

    private CheckForLongPress longPress;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!(v instanceof TextView)) {
            return false;
        }
        TextView widget = (TextView) v;

        //widget.getText() is SpannedString
        Spannable buffer = Spannable.Factory.getInstance().newSpannable(widget.getText());

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);

            if (links.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    removeLongClick(v);
                    links[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer, buffer.getSpanStart(links[0]), buffer.getSpanEnd(links[0]));
                    checkForLongClick(v, 0);

                }
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        } else if (action == MotionEvent.ACTION_CANCEL) {
            removeLongClick(v);
        }

        return false;
    }

    private void removeLongClick(View v) {
        if (longPress != null) {
            v.removeCallbacks(longPress);
            longPress = null;
        }
    }


    private void checkForLongClick(View target, int delayOffset) {
        if (!target.isLongClickable()) {
            return;
        }
        if (longPress == null) {
            longPress = new CheckForLongPress(target);
        }
        target.postDelayed(longPress, ViewConfiguration.getLongPressTimeout() - delayOffset);
    }

    private final class CheckForLongPress implements Runnable {

        private View target;

        public CheckForLongPress(View target) {
            this.target = target;
        }

        @Override
        public void run() {
            target.performLongClick();
        }

        //        @Override
        //        public void run() {
        //            View v = target;
        //            boolean consumed = v.performLongClick();
        //            while (!consumed) {
        //                v = (View) v.getParent();
        //                if (v == null) {
        //                    break;
        //                }
        //                consumed = v.performLongClick();
        //            }
        //        }
    }
}
