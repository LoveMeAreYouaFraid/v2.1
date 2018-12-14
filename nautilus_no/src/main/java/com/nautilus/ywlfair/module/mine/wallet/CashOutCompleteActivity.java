package com.nautilus.ywlfair.module.mine.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.module.BaseActivity;

/**
 * Created by Administrator on 2016/6/17.
 */
public class CashOutCompleteActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cash_out_complete_activity);

        String numMoney = getIntent().getStringExtra(Constant.KEY.NUMBER);

        TextView numMoneyView = (TextView) findViewById(R.id.tv_money_num);

        numMoneyView.setText("提现金额：" + numMoney);

        findViewById(R.id.tv_top_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
