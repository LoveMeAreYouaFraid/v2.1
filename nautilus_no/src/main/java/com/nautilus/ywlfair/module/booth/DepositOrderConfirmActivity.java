package com.nautilus.ywlfair.module.booth;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.PayMethodResultListener;
import com.nautilus.ywlfair.component.PayMethodService;
import com.nautilus.ywlfair.entity.bean.GetVendorInfo;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.PostCreateOrderRequest;
import com.nautilus.ywlfair.entity.response.PostUserOrderResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.active.CompleteOrderActivity;
import com.nautilus.ywlfair.widget.NewPayMethodSelectLayout;
import com.nautilus.ywlfair.widget.ProgressDialog;


/**
 * Created by Administrator on 2016/1/25.
 */
public class DepositOrderConfirmActivity extends BaseActivity implements View.OnClickListener, PayMethodResultListener {

    private Context mContext;

    private GetVendorInfo vendor;

    private String[] payChannels = new String[]{"ALI", "WX"};

    private NewPayMethodSelectLayout payMethodSelectLayout;

    private String channelType = "ALI";

    private boolean mIsPaying = false;//是否正在启动支付

    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_confirmation_deposit);

        mContext = this;

        vendor = (GetVendorInfo) getIntent().getSerializableExtra(Constant.KEY.DEPOSIT);

        initViews();

    }

    private void initViews() {

        UserInfo currentUser = GetUserInfoUtil.getUserInfo();

        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("确认订单");

        View back = findViewById(R.id.img_back);
        back.setOnClickListener(this);


        TextView userName = (TextView) findViewById(R.id.tv_user_name);
        userName.setText(currentUser.getNickname());

        TextView priceView = (TextView) findViewById(R.id.tv_price);
        priceView.setText("￥" + StringUtils.getMoneyFormat(vendor.getDeposit()));

        TextView payMoneyView = (TextView) findViewById(R.id.tv_pay_money);
        payMoneyView.setText("￥" +  StringUtils.getMoneyFormat(vendor.getDeposit()));

        payMethodSelectLayout = (NewPayMethodSelectLayout) findViewById(R.id.pay_method_select);
        payMethodSelectLayout.setListener(new NewPayMethodSelectLayout.OnItemSelectListener() {
            @Override
            public void onItemSelect(int position) {
                channelType = payChannels[position];
            }
        });

        View payBtn = findViewById(R.id.button);
        payBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.button:
                confirmOrder();
                break;
        }
    }

    private void confirmOrder() {

        if (mIsPaying) {
            return;
        }

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        PostCreateOrderRequest request = new PostCreateOrderRequest(userId, "4", "", vendor.getDeposit()+"", 1+"",
                vendor.getPhone(), channelType, "", "", "", "", "", "", "", new ResponseListener<PostUserOrderResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在提交...");
            }

            @Override
            public void onCacheResponse(PostUserOrderResponse response) {

            }

            @Override
            public void onResponse(PostUserOrderResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getOrderInfo() != null) {

                    mIsPaying = true;

                    orderInfo = response.getResult().getOrderInfo();

                    PayMethodService.getInstance().startPay(mContext,
                            orderInfo, DepositOrderConfirmActivity.this);

                } else {
                    ToastUtil.showShortToast("创建订单失败");
                }

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();

                    Toast.makeText(MyApplication.getInstance(), response.getMessage(),
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MyApplication.getInstance(), "获取数据失败，请您稍后重试", Toast.LENGTH_SHORT).show();
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


    @Override
    public void payResult(boolean isSuccess, boolean isTimeOut) {
        mIsPaying = false;

        if (isSuccess) {
            CompleteOrderActivity.startCompleteActivity(mContext, orderInfo);
        }

        finish();
    }
}
