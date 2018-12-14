package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.ActiveListAdapter;

/**
 * Created by Administrator on 2016/6/18.
 */
public class SelectListTextView extends LinearLayout {
    private TextView textView1, textView2, textView3;

    public interface OnLikePressedListener {
        void payType(String type);
    }

    private OnLikePressedListener onLikePressedListener;

    public OnLikePressedListener getOnLikePressedListener() {
        return onLikePressedListener;
    }

    public void setOnLikePressedListener(OnLikePressedListener onLikePressedListener) {
        this.onLikePressedListener = onLikePressedListener;
    }


    public SelectListTextView(Context context) {
        super(context);

    }

    public SelectListTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectListTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.secelt_text_view_layout, this);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        final Drawable img = getResources().getDrawable(R.drawable.bg_pay_yes);

        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());//必须设置图片大小，否则不显示

        textView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearImg();
                onLikePressedListener.payType("4");
                textView3.setCompoundDrawables(null, null, img, null);
            }
        });
        textView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearImg();
                onLikePressedListener.payType("3");
                textView2.setCompoundDrawables(null, null, img, null);
            }
        });
        textView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearImg();
                onLikePressedListener.payType("2");
                textView1.setCompoundDrawables(null, null, img, null);
            }
        });
    }

    public void ClearImg() {
        textView1.setCompoundDrawables(null, null, null, null);
        textView2.setCompoundDrawables(null, null, null, null);
        textView3.setCompoundDrawables(null, null, null, null);
    }


}
