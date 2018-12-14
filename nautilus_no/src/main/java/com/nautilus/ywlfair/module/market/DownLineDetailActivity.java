package com.nautilus.ywlfair.module.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.OfflineOrdersInfo;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.request.GetOrderStallRequest;
import com.nautilus.ywlfair.entity.response.GetStallOrderInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.wallet.BalanceDetailActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/25.
 */

public class DownLineDetailActivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.tv_key_price)
    TextView tvKeyPrice;
    @BindView(R.id.tv_value_price)
    TextView tvValuePrice;
    @BindView(R.id.tv_key)
    TextView tvKey;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.tv_butt_look_details)
    TextView tvButtLookDetails;

    private Context mContext;

    private OfflineOrdersInfo offlineOrdersInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_line_order_details_activity);
        ButterKnife.bind(this);
        mContext = this;
        tvTitle.setText("订单详情");
        getData(getIntent().getStringExtra(Constant.KEY.ORDER_ID));

        if(getIntent().hasExtra(Constant.KEY.IS_LIKE)){
            tvButtLookDetails.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.img_back, R.id.tv_butt_look_details})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_butt_look_details:

                Intent intent = new Intent(mContext, BalanceDetailActivity.class);

                intent.putExtra(Constant.KEY.ITEM_ID, offlineOrdersInfo.getBalanceDetailId());

                intent.putExtra(Constant.KEY.IS_LIKE, true);

                startActivity(intent);

                break;
        }
    }

    private void getData(String orderId) {
        GetOrderStallRequest request = new GetOrderStallRequest(orderId, new ResponseListener<GetStallOrderInfoResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "数据获取中.....");
            }

            @Override
            public void onCacheResponse(GetStallOrderInfoResponse response) {

            }

            @Override
            public void onResponse(GetStallOrderInfoResponse response) {
                if (response == null || response.getResult().getOrderInfo() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }

                offlineOrdersInfo = response.getResult().getOrderInfo();

                init();

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();

                    ToastUtil.showShortToast(response.getMessage());

                } else {

                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                }
            }

            @Override
            public void onFinish() {
                ProgressDialog.getInstance().cancel();

            }
        });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void init() {

        String paystrting = "";
        if (offlineOrdersInfo.getChannel().equals("ALI")) {
            paystrting = "支付宝";
        } else {
            paystrting = "微信";
        }
        tvKeyPrice.setText("订单金额");
        tvValuePrice.setText("￥" + StringUtils.getMoneyFormat(offlineOrdersInfo.getPrice()));
        tvKey.setText("订单类\n付款用户\n时间\n钱包入账\n服务费\n订单编号\n支付方式\n摊主积分奖励");
        tvValue.setText(offlineOrdersInfo.getOrderName() +
                "\n" + offlineOrdersInfo.getNickname() +
                "\n" + TimeUtil.getYearMonthAndDayWithHour(StringUtils.getLongValueFromString(offlineOrdersInfo.getOrderTime())) +
                "\n￥" + StringUtils.getMoneyFormat(offlineOrdersInfo.getIncomeAmount()) +
                "\n-￥" + StringUtils.getMoneyFormat(offlineOrdersInfo.getTradeCommission()) +
               "\n"+ offlineOrdersInfo.getOrderId() +
                "\n" + paystrting +
                "\n" + offlineOrdersInfo.getScore());

    }
}
