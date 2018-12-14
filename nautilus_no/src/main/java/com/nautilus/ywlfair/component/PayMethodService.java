package com.nautilus.ywlfair.component;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.request.GetOrderDetailRequest;
import com.nautilus.ywlfair.entity.request.PutOrderStatusRequest;
import com.nautilus.ywlfair.entity.response.GetOrderDetailResponse;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCReqParams;

/**
 * Created by Administrator on 2016/4/20.
 */
public class PayMethodService {
    private OrderInfo orderInfo;

    private Context mContext;

    private int type;//0 门票购买 1 摊位购买  2 商品购买

    private String payChannel;

    private String[] typeArray;

    private static PayMethodService instance;

    private TimeCount timeCount;

    private PayMethodResultListener payMethodResultListener;

    public static PayMethodService getInstance() {
        if (instance == null) {
            instance = new PayMethodService();
        }

        return instance;
    }

    // 支付结果返回入口
    BCCallback bcCallback = new BCCallback() {
        @Override
        public void done(final BCResult bcResult) {

            final BCPayResult bcPayResult = (BCPayResult) bcResult;

            final Activity activity = (Activity) mContext;

            // 如果想通过Toast通知用户结果，请使用如下方式，
            // 直接makeText有可能会造成java.lang.RuntimeException: Can't create handler
            // inside thread that has not called Looper.prepare()
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String result = bcPayResult.getResult();

                    if (result.equals(BCPayResult.RESULT_SUCCESS)) {

                        ProgressDialog.getInstance().show(mContext, "检查支付结果...", false);

                        checkOrderStatus();

                    } else if (result.equals(BCPayResult.RESULT_CANCEL)) {

                        ToastUtil.showLongToast("取消支付");

                        payFailedResult(false);

                    } else if (result.equals(BCPayResult.RESULT_FAIL)) {
                        if (bcPayResult.getDetailInfo().contains("时间戳")) {
                            ToastUtil.showLongToast("抱歉，支付失败，请校验手机时间是否正确");
                        } else {
                            ToastUtil.showLongToast("支付失败, 原因: " + bcPayResult.getErrMsg() + ", "
                                    + bcPayResult.getDetailInfo());
                        }

                        payFailedResult(false);

                    } else {

                        payFailedResult(false);
                    }
                }
            });
        }


    };


    public void startPay(Context context, OrderInfo mOrderInfo, PayMethodResultListener listener) {

        timeCount = new TimeCount(60000, 2000);

        mContext = context;

        this.payMethodResultListener = listener;

        orderInfo = mOrderInfo;

        payChannel = orderInfo.getChannel();

        typeArray = context.getResources().getStringArray(R.array.order_type);

        type = Integer.valueOf(orderInfo.getOrderType()) - 1;

        if (payChannel.equals("WX")) {

            // 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
            // 第二个参数需要换成你自己的微信AppID.
            BCPay.initWechatPay(mContext, "wxf61bb23ed791fe9e");
        }

        Map<String, String> mapOptional = new HashMap<>();

        mapOptional.put("type", orderInfo.getOrderTypeName());

        BCPay.PayParam payParam = new BCPay.PayParam();
        if (type < typeArray.length)
            payParam.billTitle = typeArray[type];

        payParam.billTotalFee = (int) (orderInfo.getPrice() * 100);

//        payParam.billTotalFee = 1;

        payParam.billNum = orderInfo.getOrderId().replace("-", "");

        payParam.billTimeout = 900;

        payParam.optional = mapOptional;

        if (payChannel.equals("WX")) {

            // 对于微信支付, 手机内存太小会有OutOfResourcesException造成的卡顿, 以致无法完成支付
            // 这个是微信自身存在的问题

            if (BCPay.isWXAppInstalledAndSupported()
                    && BCPay.isWXPaySupported()) {

                payParam.channelType = BCReqParams.BCChannelTypes.WX_APP;

                BCPay.getInstance(mContext).reqPaymentAsync(payParam, bcCallback);

            }
        } else if (payChannel.equals("ALI")) {

            payParam.channelType = BCReqParams.BCChannelTypes.ALI_APP;

            BCPay.getInstance(mContext).reqPaymentAsync(payParam, bcCallback);

        }
    }

    private void payFailedResult(boolean isTimeOut) {

        if (payMethodResultListener != null)
            payMethodResultListener.payResult(false, isTimeOut);

    }

    private boolean isCountStart = false;

    private void postTalkingData() {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        TalkingDataAppCpa.onPay(userId, orderInfo.getOrderId(), (int) (orderInfo.getPrice() * 100), "CNY");
    }

    private void checkOrderStatus() {

        GetOrderDetailRequest request = new GetOrderDetailRequest(orderInfo.getOrderId(),
                orderInfo.getOrderType(), new ResponseListener<GetOrderDetailResponse>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCacheResponse(GetOrderDetailResponse response) {
            }

            @Override
            public void onResponse(GetOrderDetailResponse response) {
                if (response != null && response.getResult().getOrderInfo() != null
                        && response.getResult().getOrderInfo().getOrderStatus() == 1) {

                    timeCount.cancel();

                    ProgressDialog.getInstance().cancel();

                    if (payMethodResultListener != null)
                        payMethodResultListener.payResult(true, false);

                    ToastUtil.showLongToast("支付成功");

                    postTalkingData();

                } else {
                    if (!isCountStart) {

                        timeCount.start();

                        isCountStart = true;
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (!isCountStart) {

                    timeCount.start();

                    isCountStart = true;
                }
            }

            @Override
            public void onFinish() {
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    class TimeCount extends CountDownTimer {

        int count = 0;

        long pastTime = 60000;

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {

            ProgressDialog.getInstance().cancel();

            changeOrderStatus();

            payFailedResult(true);

            ToastUtil.showLongToast("支付验证失败！");

        }

        public void onTick(long millisUntilFinished) {

            if (pastTime - millisUntilFinished > 1000 * Math.pow(2, count)) {

                count = count + 1;

                checkOrderStatus();

            }
        }
    }

    private void changeOrderStatus() {

        PutOrderStatusRequest request = new PutOrderStatusRequest(orderInfo.getOrderId(), orderInfo.getOrderType(), "-2",
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {
                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }

                    @Override
                    public void onFinish() {
                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }
}
