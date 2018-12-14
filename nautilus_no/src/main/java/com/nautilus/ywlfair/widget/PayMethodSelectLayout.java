package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class PayMethodSelectLayout extends LinearLayout implements View.OnClickListener {

    private CheckBox child1, child2;

    private OnItemSelectListener listener;

    public PayMethodSelectLayout(Context context) {
        this(context, null);
    }

    public PayMethodSelectLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayMethodSelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.pay_channel_select, this);

        child1 = (CheckBox) findViewById(R.id.ck_ali);

        child2 = (CheckBox) findViewById(R.id.ck_wx);

        findViewById(R.id.rl_first_position).setOnClickListener(this);

        findViewById(R.id.rl_second_position).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_first_position:
                listener.onItemSelect(0);
                child2.setChecked(false);
                child1.setChecked(true);
                break;

            case R.id.rl_second_position:
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
