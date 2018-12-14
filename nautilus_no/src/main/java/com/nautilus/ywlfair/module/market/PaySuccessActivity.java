package com.nautilus.ywlfair.module.market;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.OrderInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/17.
 */
public class PaySuccessActivity extends Activity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.img_stall)
    ImageView imgStall;
    @BindView(R.id.tv_stall_name)
    TextView tvStallName;
    @BindView(R.id.tv_preferential)
    TextView tvPreferential;
    @BindView(R.id.tv_actual_pay)
    TextView tvActualPay;
    @BindView(R.id.tv_pay_and_data)
    TextView tvPayAndData;
    @BindView(R.id.layout_head)
    LinearLayout layoutHead;

    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_pay_success_activity);
        orderInfo = (OrderInfo) getIntent().getSerializableExtra(Constant.KEY.ORDER);
        ButterKnife.bind(this);

        tvOrderNum.setText("订单编号：" + orderInfo.getOrderId());

        ImageLoadUtils.setRoundHeadView(imgStall,
                orderInfo.getVendorAvatar(), R.drawable.default_avatar, 120);

        tvStallName.setText(orderInfo.getVendorNickname());

        tvPreferential.setText("￥ " + StringUtils.getMoneyFormat(orderInfo.getPrice()));

        String StringPrice = "实际支付： " + "<font color ='#f5703f'>" + "￥" + StringUtils.getMoneyFormat(orderInfo.getPrice()) + "</font>";

        tvActualPay.setText(Html.fromHtml(StringPrice));//实际支付
        String channel;
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.KEY.TYPE))) {
            String type = getIntent().getStringExtra(Constant.KEY.TYPE);
            if (type.equals("1")) {
                tvTitle.setText("支付成功");
            } else if (type.equals("0")) {
                tvTitle.setText("订单详情");
            }
            layoutHead.setVisibility(View.GONE);
            findViewById(R.id.tv_remind).setVisibility(View.GONE);
            TextView tvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
            tvOrderNumber.setVisibility(View.VISIBLE);
            tvOrderNumber.setText("订单编号：" + orderInfo.getOrderId());


        } else {
            layoutHead.setVisibility(View.VISIBLE);

            tvTitle.setText("订单详情");
        }
        if (orderInfo.getChannel().equals("ALI")) {
            channel = "支付宝";
        } else {
            channel = "微信";
        }
        tvPayAndData.setText("支付方式：" + channel + "  |  " + TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(orderInfo.getOrderTime())));


    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

        }
    }

    public static void startCompleteActivity(Context context, OrderInfo orderInfo, String type) {

        Intent intent = new Intent(context, PaySuccessActivity.class);

        intent.putExtra(Constant.KEY.ORDER, orderInfo);

        intent.putExtra(Constant.KEY.TYPE, type);

        context.startActivity(intent);

    }
}
