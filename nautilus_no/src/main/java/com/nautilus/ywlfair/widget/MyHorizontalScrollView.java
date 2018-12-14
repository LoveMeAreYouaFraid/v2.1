package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.HorizontalScrollView;

/**
 * Created by Administrator on 2016/6/20.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

    private int scroll;

    private int radius;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;

        scrollTo(scroll, getScrollY());

    }
}
