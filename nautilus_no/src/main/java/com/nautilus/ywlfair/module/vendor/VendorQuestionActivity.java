package com.nautilus.ywlfair.module.vendor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.module.BaseActivity;

/**
 * Created by Administrator on 2016/6/27.
 */
public class VendorQuestionActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vendor_question_activity);

        findViewById(R.id.tv_top_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
