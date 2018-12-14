package com.nautilus.ywlfair.module.booth;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.BoothActInfo;
import com.nautilus.ywlfair.entity.request.PutBoothRequest;
import com.nautilus.ywlfair.entity.response.GetBoothDetailResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/3/8.
 */
public class CancelBoothActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private TextView activeName, boothType, boothFee, dateView, timeView,
            addressView, needKnow, boothRound, cancelType;

    private GetBoothDetailResponse response;

    private ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cancel_booth_activity);

        mContext = this;

        response = (GetBoothDetailResponse) getIntent().getSerializableExtra(Constant.KEY.BOOTH);

        initViews();

        setValue();
    }

    private void initViews() {
        activeName = (TextView) findViewById(R.id.tv_active_name);

        boothType = (TextView) findViewById(R.id.tv_booth_type);

        boothFee = (TextView) findViewById(R.id.tv_booth_price);

        boothRound = (TextView) findViewById(R.id.tv_booth_round);

        dateView = (TextView) findViewById(R.id.tv_active_date);

        timeView = (TextView) findViewById(R.id.tv_active_time);

        addressView = (TextView) findViewById(R.id.tv_address);

        needKnow = (TextView) findViewById(R.id.tv_need_know);

        icon = (ImageView) findViewById(R.id.iv_icon);

        cancelType = (TextView) findViewById(R.id.tv_cancel_type);

        findViewById(R.id.tv_confim_btn).setOnClickListener(this);

        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);
    }

    private void setValue() {

        GetBoothDetailResponse.BoothDetailInfo boothDetailInfo = response.getResult().getBoothDetailInfo();

        if(boothDetailInfo.getScore() != 0){
            cancelType.setText("退摊主积分");

            boothFee.setText(boothDetailInfo.getScore() + "");
        }else{
            boothFee.setText("￥" + boothDetailInfo.getPrice());
        }

        if (!TextUtils.isEmpty(boothDetailInfo.getRoundNo())) {
            boothRound.setVisibility(View.VISIBLE);

            boothRound.setText("活动场次：" + boothDetailInfo.getRoundNo());
        } else {
            boothRound.setVisibility(View.GONE);
        }
        BoothActInfo boothActInfo = response.getResult().getBoothActInfo();

        activeName.setText(boothActInfo.getName());

        boothType.setText("摊位类型：" + boothActInfo.getBoothType());

        dateView.setText("活动日期：" + TimeUtil.getDateFormat(boothActInfo.getStartdate(), boothActInfo.getEnddate()));

        timeView.setText("活动时间：" + TimeUtil.getTimeFormat(boothActInfo.getStartdate(), boothActInfo.getEnddate()));

        addressView.setText("活动地址：" + boothActInfo.getAddress());

        ImageLoader.getInstance().displayImage(boothActInfo.getPosterMain(), icon, ImageLoadUtils.createNoRoundedOptions());

        needKnow.setText(response.getResult().getShowTextInfo().getBackBoothNeedKnowText());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.tv_confim_btn:

                showDeleteConfirm();

                break;
        }
    }

    private void cancelBooth() {

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        String orderId = response.getResult().getBoothDetailInfo().getOrderId();

        PutBoothRequest request = new PutBoothRequest(userId, orderId, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在取消...");
            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if (response != null) {
                    ToastUtil.showShortToast("退订申请已提交");

                    setResult(RESULT_OK);

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

    private void showDeleteConfirm() {
        final Dialog dialog = new Dialog(mContext, R.style.dialog);

        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_confirm, null);

        dialog.setContentView(view);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        int paddingPx = BaseInfoUtil.dip2px(MyApplication.getInstance(), 20);
        window.getDecorView().setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView titleTextView = (TextView) view.findViewById(R.id.tv_title);
        titleTextView.setText("提示");

        View dividerView = view.findViewById(R.id.view_divider);
        dividerView.setVisibility(View.VISIBLE);

        TextView contentTextView = (TextView) view.findViewById(R.id.tv_content);
        contentTextView.setVisibility(View.VISIBLE);
        contentTextView.setText("是否确认退订摊位");

        TextView cancelTextView = (TextView) view.findViewById(R.id.tv_left);
        cancelTextView.setText("取消");

        TextView okTextView = (TextView) view.findViewById(R.id.tv_right);
        okTextView.setText("确定");

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

                cancelBooth();
            }
        });
    }
}
