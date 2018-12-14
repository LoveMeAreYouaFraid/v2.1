package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.GridView;

/**
 * Created by dingying on 2015/2/28.
 */
public class LoadMoreGridView extends GridView implements AbsListView.OnScrollListener{

    private OnLastItemVisibleListener mOnLastItemVisibleListener;
    private boolean mLastItemVisible;

    public LoadMoreGridView(Context context, AttributeSet attrs) {
        super(context,attrs);
        setOnScrollListener(this);
    }
    public final void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        mOnLastItemVisibleListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == OnScrollListener.SCROLL_STATE_IDLE && null != mOnLastItemVisibleListener && mLastItemVisible) {
            mOnLastItemVisibleListener.onLastItemVisible();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mOnLastItemVisibleListener) {
            mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
        }
    }

    public interface OnLastItemVisibleListener {

        /**
         * Called when the user has scrolled to the end of the list
         */
        public void onLastItemVisible();
    }
}
