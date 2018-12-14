package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.ActivitySysConfig;

import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
public class MyGoodsEditText extends EditText {

    private List<ActivitySysConfig> type;

    public MyGoodsEditText(Context context) {
        super(context);
        this.addTextChangedListener(new MyTextWatcher());
    }

    public MyGoodsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addTextChangedListener(new MyTextWatcher());
    }

    public MyGoodsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.addTextChangedListener(new MyTextWatcher());
    }

    public void setlist(List<ActivitySysConfig> type) {
        this.type = type;
    }

    private class MyTextWatcher implements TextWatcher {
        long lastChangeTime = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            myhandl.sendEmptyMessage(Integer.valueOf(TimeUtil.getMinAndSecond(System.currentTimeMillis())));

        }

        @Override
        public void afterTextChanged(Editable s) {

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastChangeTime > 1000) {
                LogUtil.e("test", "1");

                lastChangeTime = currentTime;
//                tagAdapter.setSelectedList(0);
            }
        }
    }

    private Handler myhandl = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
}
