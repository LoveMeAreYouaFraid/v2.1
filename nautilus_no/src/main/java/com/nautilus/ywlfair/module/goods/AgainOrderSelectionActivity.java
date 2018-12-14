package com.nautilus.ywlfair.module.goods;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.PayMethodResultListener;
import com.nautilus.ywlfair.component.PayMethodService;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.request.GetOrderDetailRequest;
import com.nautilus.ywlfair.entity.request.PutOrderStatusRequest;
import com.nautilus.ywlfair.entity.response.GetOrderDetailResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * 订单确认 付款页面
 * Created by Administrator on 2015/12/28.
 */
public class AgainOrderSelectionActivity extends BaseActivity implements OnClickListener, PayMethodResultListener {

    private OrderInfo orderInfo;

    private String orderId;

    private Context mContext;

    private ImageView CommodityImage;

    private TextView determine, cancelOrder;

    private TextView orderNoView;

    private boolean mIsPaying = false;//是否正在启动支付

    private TextView CommodityNumber,
            receiver,
            address,
            VendorNickname,
            phoneNum,
            commodityTitle,
            commodityClassification,
            commodityPrice,
            tvShipment,
            tvTotalPrice, payby, TransactionTime, ShipTime,
            BuyersMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product_order_details_incomplete);

        mContext = this;

        orderId = getIntent().getStringExtra(Constant.KEY.ORDER_ID);

        initViews();

        getData();
    }

    private void initViews() {
        View back = findViewById(R.id.back);

        back.setOnClickListener(this);

        orderNoView = (TextView) findViewById(R.id.tv_order_no);

        cancelOrder = (TextView) findViewById(R.id.cancel_order);

        BuyersMessage = (TextView) findViewById(R.id.buyers_message);

        payby = (TextView) findViewById(R.id.payby);

        tvShipment = (TextView) findViewById(R.id.tv_shipment);

        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);

        determine = (TextView) findViewById(R.id.tv_determine);//付款按钮

        CommodityImage = (ImageView) findViewById(R.id.iv_goods_image);

        VendorNickname = (TextView) findViewById(R.id.tv_vendor_nick_name);

        commodityTitle = (TextView) findViewById(R.id.tv_goods_name);

        commodityClassification = (TextView) findViewById(R.id.commodity_classification);

        commodityPrice = (TextView) findViewById(R.id.tv_goods_price);

        receiver = (TextView) findViewById(R.id.receiver);

        address = (TextView) findViewById(R.id.address);

        phoneNum = (TextView) findViewById(R.id.phone_num);

        TransactionTime = (TextView) findViewById(R.id.transaction_time);

        ShipTime = (TextView) findViewById(R.id.ship_time);

        CommodityNumber = (TextView) findViewById(R.id.tv_goods_number);

        determine.setOnClickListener(this);

        cancelOrder.setOnClickListener(this);
    }

    public void setValue() {

        if (orderInfo == null) {
            return;
        }

        if (orderInfo.getOrderStatus() == 0) {
            View warmView = findViewById(R.id.ll_warm_view);
            warmView.setVisibility(View.VISIBLE);
        }

        String payMethod = orderInfo.getChannel();

        if (payMethod.equals("WX")) {
            payby.setText("微信");
        } else {
            payby.setText("支付宝");
        }

        address.setText(orderInfo.getAddress());

        phoneNum.setText(orderInfo.getMobilePhone());

        receiver.setText("收货人：" + orderInfo.getConsignee());

        tvShipment.setText(StringUtils.getCourierFeeString(orderInfo.getCourierFee()));

        commodityTitle.setText(orderInfo.getGoodsName());

        commodityClassification.setText("卖家承诺" + orderInfo.getPostDays() + "天发货");

        BuyersMessage.setText("卖家留言：" + orderInfo.getUserMessage());

        CommodityNumber.setText("X" + orderInfo.getNum());

        commodityPrice.setText("￥：" + StringUtils.getMoneyFormat(orderInfo.getPrice() / orderInfo.getNum()));

        VendorNickname.setText("卖家姓名：" + orderInfo.getVendorNickname());

        ImageLoader.getInstance().displayImage(orderInfo.getSkuImgUrl(),
                CommodityImage, ImageLoadUtils.createNoRoundedOptions());

        tvTotalPrice.setText("￥" + StringUtils.getMoneyFormat(orderInfo.getPrice()));

        orderNoView.setText("订单编号：" + orderInfo.getOrderId());

        String payTime = orderInfo.getPayTime();

        if (orderInfo.getOrderStatus() == 1) {

            if (!TextUtils.isEmpty(payTime)) {
                TransactionTime.setText("交易时间：" + TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(orderInfo.getPayTime())));
                TransactionTime.setVisibility(View.VISIBLE);
            }

        } else if (orderInfo.getOrderStatus() >= 2) {
            String sendTime = orderInfo.getSendTime();

            if (!TextUtils.isEmpty(sendTime)) {
                ShipTime.setVisibility(View.VISIBLE);
                ShipTime.setText("发货时间：" + TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(orderInfo.getSendTime())));
            }

            if (!TextUtils.isEmpty(payTime)) {
                TransactionTime.setText("交易时间：" + TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(orderInfo.getPayTime())));
                TransactionTime.setVisibility(View.VISIBLE);
            }

        }
    }

    private void getData() {
        GetOrderDetailRequest request = new GetOrderDetailRequest(orderId, "3", new ResponseListener<GetOrderDetailResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(GetOrderDetailResponse response) {
                if(response != null && response.getResult().getOrderInfo() != null){
                    orderInfo = response.getResult().getOrderInfo();

                    setValue();
                }
            }

            @Override
            public void onResponse(GetOrderDetailResponse response) {
                if(response != null && response.getResult().getOrderInfo() != null){
                    orderInfo = response.getResult().getOrderInfo();

                    setValue();
                }else{
                    ToastUtil.showLongToast("获取数据失败，请检查网络后重试");
                }
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

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.tv_determine:

                mIsPaying = true;

                PayMethodService.getInstance().startPay(mContext, orderInfo, AgainOrderSelectionActivity.this);

                break;

            case R.id.cancel_order:

                showConfirmAlert();

                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    private void showConfirmAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage("您是否确认要取消该订单?");
        builder.setTitle("取消订单");
        builder.setPositiveButton("确认", new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeOrderStatus();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private void changeOrderStatus() {

        if (mIsPaying) {
            return;
        }

        PutOrderStatusRequest request = new PutOrderStatusRequest(orderInfo.getOrderId(), orderInfo.getOrderType(), "-1",
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "操作中...");
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {
                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {

                            Toast.makeText(MyApplication.getInstance(), "订单取消成功",
                                    Toast.LENGTH_SHORT).show();

                            finish();
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

        }
    }
}
