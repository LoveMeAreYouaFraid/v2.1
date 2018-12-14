package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.nautilus.ywlfair.R;

/**
 * Created by Administrator on 2016/6/17.
 */
public class MyIsPayLayout extends LinearLayout {

    private View tvSeeMore;

    private View ailIsPay;

    private View wxIsPay;

    private View layoutWx;

    private View layoutAil;

    public String Pay = "ALI";


    public MyIsPayLayout(Context context) {
        super(context);
    }

    public MyIsPayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyIsPayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View v = LayoutInflater.from(getContext()).inflate(R.layout.is_pay_item, this);

        ailIsPay = v.findViewById(R.id.ail_is_pay);

        layoutAil = v.findViewById(R.id.layout_ail);

        tvSeeMore = v.findViewById(R.id.tv_see_more);

        wxIsPay = v.findViewById(R.id.wx_is_pay);

        layoutWx = v.findViewById(R.id.layout_wx);

        tvSeeMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSeeMore.setVisibility(GONE);
                layoutWx.setVisibility(VISIBLE);
            }
        });

        layoutAil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ailIsPay.setVisibility(VISIBLE);
                wxIsPay.setVisibility(INVISIBLE);
                setPay("ALI");
            }
        });
        layoutWx.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ailIsPay.setVisibility(INVISIBLE);
                wxIsPay.setVisibility(VISIBLE);
                setPay("WX");
            }
        });

    }

    public String getPay() {
        return Pay;
    }

    public void setPay(String pay) {
        Pay = pay;
    }
}
