package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.nautilus.ywlfair.R;

/**
 * Created by Administrator on 2015/11/24.
 */
public class NewPayMethodSelectLayout extends LinearLayout implements View.OnClickListener {

    private CheckBox child1, child2;

    private OnItemSelectListener listener;

    public NewPayMethodSelectLayout(Context context) {
        this(context, null);
    }

    public NewPayMethodSelectLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewPayMethodSelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.pay_channel_select2, this);

        View tv1 = findViewById(R.id.tv_ail);
        View tv2 = findViewById(R.id.tv_wx);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        child1 = (CheckBox) findViewById(R.id.ck_ali);
        child2 = (CheckBox) findViewById(R.id.ck_wx);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ail:
                listener.onItemSelect(0);
                child2.setChecked(false);
                child1.setChecked(true);
                break;
            case R.id.tv_wx:
                listener.onItemSelect(1);
                child1.setChecked(false);
                child2.setChecked(true);
                break;

        }

    }

    public interface OnItemSelectListener {
        void onItemSelect(int position);
    }

    public void setListener(OnItemSelectListener listener) {
        this.listener = listener;
    }
}
