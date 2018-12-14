package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.launch.register.RegisterActivity;

/**
 * Created by Administrator on 2016/6/18.
 */
public class PayPasswordStatusActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay_password_status_activity);

        findViewById(R.id.tv_top_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayPasswordStatusActivity.this, SetPayPasswordActivity.class);

                intent.putExtra(Constant.KEY.TYPE, 1);

                startActivity(intent);
            }
        });

        findViewById(R.id.tv_find_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phone = new Intent(PayPasswordStatusActivity.this, RegisterActivity.class);

                phone.putExtra(Constant.KEY.NAME,"找回支付密码");

                phone.putExtra(Constant.KEY.TYPE, 4);

                startActivity(phone);
            }
        });
    }
}
