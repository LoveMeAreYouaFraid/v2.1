package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.nautilus.ywlfair.R;

/**
 * Copyright (©) 2014 feitan.net
 * <p/>
 * 可监听到滑动到列表底部事件的ListView，用于实现上拉（下滑）到底部自动加载更多等功能
 *
 * @author Dingying
 * @version 1.0, 6/13/14 14:30
 * @since 5/13/14
 */
public class LoadMoreListView extends ListView implements OnScrollListener {

    private OnLastItemVisibleListener mOnLastItemVisibleListener;

    private OnFirstItemVisibleListener mOnFirstItemVisibleListener;

    private boolean mLastItemVisible;

    private boolean mFirstItemVisible;

    private View footerView;

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);

        footerView = inflate(context, R.layout.list_view_footer, null);

        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public final void setFooter(boolean isShowFooter){

        if(isShowFooter){
            addFooterView(footerView);
        }else{
            removeFooterView(footerView);
        }

    }

    public final void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        mOnLastItemVisibleListener = listener;
    }

    public final void setOnFirstItemVisibleListener(OnFirstItemVisibleListener listener) {
        mOnFirstItemVisibleListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, final int state) {

        if (state == OnScrollListener.SCROLL_STATE_IDLE) {

            if (mOnLastItemVisibleListener != null && mLastItemVisible) {
                mOnLastItemVisibleListener.onLastItemVisible();
            }

            if (mOnFirstItemVisibleListener != null && mFirstItemVisible) {
                mOnFirstItemVisibleListener.onFirstItemVisible();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (null != mOnLastItemVisibleListener) {
            mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
        }

        if (null != mOnFirstItemVisibleListener) {
            mFirstItemVisible = firstVisibleItem == 0;
        }

    }

    public interface OnLastItemVisibleListener {

        /**
         * Called when the user has scrolled to the end of the list
         */
        public void onLastItemVisible();
    }

    public interface OnFirstItemVisibleListener {

        /**
         * Called when the user has scrolled to the start of the list
         */
        public void onFirstItemVisible();
    }

}
