package com.nautilus.ywlfair.module.booth;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.BoothActInfo;
import com.nautilus.ywlfair.entity.bean.ShowTextInfo;
import com.nautilus.ywlfair.entity.bean.event.EventBoothHandle;
import com.nautilus.ywlfair.entity.request.GetMyBoothDetailRequest;
import com.nautilus.ywlfair.entity.response.GetBoothDetailResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/3/8.
 */
public class MyBoothDetailActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private ImageView icon;

    private ImageView typeTag;

    private String[] statusSet = new String[]{"待付款", "待开始", "退订中", "已退订", "退订失败", "已摆摊"};

    private String[] boothTypes = {"普通摊位", "食品摊位"};

    private int[] boothTypeTag = new int[]{R.drawable.ic_normal_stall, R.drawable.ic_food_stall};

    private TextView activeName, boothType, boothFee, dateView, timeView,
            addressView, boothStatus, boothNo, orderNo, buyDate, needKnow,
            actuallyPaid, boothRound,stallType, boothCategory, stallPrice, scoreView;

    private String orderId;

    private GetBoothDetailResponse mResponse;

    private View cancelBoothApply;

    private View connectManager;

    private View scoreContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_booth_detail_activity);

        if (!MyApplication.getInstance().isLogin()) {
            return;
        }

        mContext = this;

        orderId = getIntent().getStringExtra(Constant.KEY.ORDER_ID);

        initViews();

        getData();
    }

    private void initViews() {
        icon = (ImageView) findViewById(R.id.iv_icon);

        icon.setOnClickListener(this);

        activeName = (TextView) findViewById(R.id.tv_active_name);

        scoreView = (TextView) findViewById(R.id.tv_score_total);

        scoreContainer = findViewById(R.id.ll_stall_score);

        boothCategory = (TextView) findViewById(R.id.tv_booth_category);

        stallPrice = (TextView) findViewById(R.id.tv_stall_total);

        typeTag = (ImageView) findViewById(R.id.iv_type_tag);

        stallType = (TextView) findViewById(R.id.tv_stall_type);

        boothType = (TextView) findViewById(R.id.tv_booth_type);

        boothFee = (TextView) findViewById(R.id.tv_booth_fee);

        boothRound = (TextView) findViewById(R.id.tv_booth_round);

        dateView = (TextView) findViewById(R.id.tv_active_date);

        timeView = (TextView) findViewById(R.id.tv_active_time);

        addressView = (TextView) findViewById(R.id.tv_address);

        boothStatus = (TextView) findViewById(R.id.tv_booth_status);

        boothNo = (TextView) findViewById(R.id.tv_booth_no);

        orderNo = (TextView) findViewById(R.id.tv_order_no);

        buyDate = (TextView) findViewById(R.id.tv_buy_date);

        needKnow = (TextView) findViewById(R.id.tv_need_know);

        actuallyPaid = (TextView) findViewById(R.id.tv_booth_price);

        cancelBoothApply = findViewById(R.id.tv_cancel_booth);

        cancelBoothApply.setOnClickListener(this);

        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);

        connectManager = findViewById(R.id.ll_connect_manager);
        connectManager.setOnClickListener(this);

    }

    private void setValue() {

        BoothActInfo boothActInfo = mResponse.getResult().getBoothActInfo();

        if (boothActInfo != null) {
            activeName.setText(boothActInfo.getName());

            boothType.setText("摊位类型：" + boothActInfo.getBoothType());

            dateView.setText("活动日期：" + TimeUtil.getDateFormat(boothActInfo.getStartdate(), boothActInfo.getEnddate()));

            timeView.setText("摊主入场时间：" + TimeUtil.getTimeFormat(boothActInfo.getApproachStartTime(), boothActInfo.getApproachEndTime()));

            addressView.setText("活动地址：" + boothActInfo.getAddress());

            boothFee.setText("摊位费：" + StringUtils.getMoneyFormat(boothActInfo.getPrice()));

            ImageLoader.getInstance().displayImage(boothActInfo.getPosterMain(), icon, ImageLoadUtils.createNoRoundedOptions());

        }

        GetBoothDetailResponse.BoothDetailInfo boothDetailInfo = mResponse.getResult().getBoothDetailInfo();

        if (boothDetailInfo != null) {

            boothStatus.setText("摊位状态：" + statusSet[boothDetailInfo.getStatus()]);

            switch (boothDetailInfo.getStatus()) {
                case 1:
                    if (!TextUtils.isEmpty(boothDetailInfo.getBoothNo())) {
                        boothNo.setText(boothDetailInfo.getBoothNo());
                    } else {
                        boothNo.setText("正在分配摊位号");
                    }
                    break;

                case 2:
                    boothNo.setText("退订中");
                    break;

                case 3:
                    boothNo.setText("已退订");
                    break;

                case 4:
                    boothNo.setText(boothDetailInfo.getBoothNo());
                    break;

                case 5:
                    boothNo.setText(boothDetailInfo.getBoothNo());
                    break;
            }

            if (!TextUtils.isEmpty(boothDetailInfo.getRoundNo())) {

                boothRound.setText("活动场次：" + boothDetailInfo.getRoundNo());

                boothRound.setVisibility(View.VISIBLE);
            } else {
                boothRound.setVisibility(View.GONE);
            }

            orderNo.setText("订单编号：" + boothDetailInfo.getOrderId());

            buyDate.setText("购买时间：" + TimeUtil.getYearMonthAndDayWithHour(boothDetailInfo.getPayTime()));

            actuallyPaid.setText("￥" + boothDetailInfo.getPrice());

            if(boothDetailInfo.getBoothCategory() > 0){
                typeTag.setImageResource(boothTypeTag[boothDetailInfo.getBoothCategory() - 1]);

                boothCategory.setText("摊位类型：" + boothTypes[boothDetailInfo.getBoothCategory() - 1]);
            }

            stallType.setText("摊位：" + boothDetailInfo.getBoothType());

            stallPrice.setText("￥" + StringUtils.getMoneyFormat(boothActInfo.getPrice()));

            if(boothDetailInfo.getScore() != 0){
                scoreContainer.setVisibility(View.VISIBLE);

                scoreView.setText("-￥" + StringUtils.getMoneyFormat(boothActInfo.getPrice()));
            }

            if (boothDetailInfo.getStatus() == 1 && !TextUtils.isEmpty(boothDetailInfo.getBoothNo())) {
                cancelBoothApply.setVisibility(View.VISIBLE);
            } else {
                cancelBoothApply.setVisibility(View.GONE);
            }
        }

        ShowTextInfo showTextInfo = mResponse.getResult().getShowTextInfo();

        if (showTextInfo != null) {
            needKnow.setText(showTextInfo.getBoothNeedKnowText());

            connectManager.setVisibility(View.VISIBLE);
        }

    }

    private void getData() {

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetMyBoothDetailRequest request = new GetMyBoothDetailRequest(userId, orderId, new ResponseListener<GetBoothDetailResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(GetBoothDetailResponse response) {
            }

            @Override
            public void onResponse(GetBoothDetailResponse response) {
                if (response == null || response.getResult() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }

                mResponse = response;

                setValue();
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

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel_booth:

                Intent intent = new Intent(mContext, CancelBoothActivity.class);

                intent.putExtra(Constant.KEY.BOOTH, mResponse);

                startActivityForResult(intent, Constant.REQUEST_CODE.CANCEL_BOOTH);

                break;

            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.iv_icon:

                Intent activeDetailIntent = new Intent(mContext,
                        ActiveWebViewActivity.class);

                activeDetailIntent.putExtra(Constant.KEY.ITEM_ID, mResponse.getResult().getBoothActInfo().getActId() + "");

                mContext.startActivity(activeDetailIntent);

                break;

            case R.id.ll_connect_manager:

                showConnectManagerDialog();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE.CANCEL_BOOTH) {

                EventBus.getDefault().post(new EventBoothHandle(0, 2));

                mResponse.getResult().getBoothDetailInfo().setStatus(2);

                setValue();
            }
        }
    }

    private void showConnectManagerDialog() {

        final Dialog dialog = new Dialog(this, R.style.dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final TextView showText, confirm, cancel;

        dialog.setContentView(R.layout.dialog_confirm);

        showText = (TextView) dialog.findViewById(R.id.tv_title);

        confirm = (TextView) dialog.findViewById(R.id.tv_left);

        cancel = (TextView) dialog.findViewById(R.id.tv_right);

        showText.setTextSize(16);

        confirm.setTextSize(14);

        cancel.setTextSize(14);

        showText.setText(mResponse.getResult().getShowTextInfo().getBoothManagerText());

        confirm.setText("确定");

        cancel.setText("取消");
        cancel.setVisibility(View.GONE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }
}
