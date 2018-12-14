package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.BalanceDetailInfo;
import com.nautilus.ywlfair.entity.request.GetBalanceDetailRequest;
import com.nautilus.ywlfair.entity.response.GetBalanceDetailResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.market.DownLineDetailActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/27.
 */
public class BalanceDetailActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.tv_type)
    TextView typeView;

    @BindView(R.id.tv_real_amount)
    TextView realAmountView;

    @BindView(R.id.tv_balance_type)
    TextView titleView;

    @BindView(R.id.tv_date)
    TextView dateView;

    @BindView(R.id.tv_order_price)
    TextView orderPriceView;

    @BindView(R.id.tv_other_fee)
    TextView otherFeeView;

    @BindView(R.id.tv_order_no)
    TextView orderNoView;

    @BindView(R.id.tv_remain_amount)
    TextView remainAmountView;

    @BindView(R.id.tv_note)
    TextView noteView;

    @BindView(R.id.ll_order)
    View orderView;

    @BindView(R.id.ll_fee)
    View feeView;

    @BindView(R.id.tv_check_detail)
    TextView checkDetailView;

    private Context mContext;

    private String balanceId;

    private BalanceDetailInfo balanceDetailInfo;

    private int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.balance_detail_activity);

        ButterKnife.bind(this);

        mContext = this;

        balanceId = getIntent().getStringExtra(Constant.KEY.ITEM_ID);

        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);

        checkDetailView.setOnClickListener(this);

        getData();
    }

    private void setValue(){

        if(balanceDetailInfo == null){
            return;
        }

        type = balanceDetailInfo.getOrderType();

        if(type == 1 || type == 2 || type == 5){//收入
            titleView.setText("入账金额");

            orderView.setVisibility(View.VISIBLE);

            orderPriceView.setText("￥" + StringUtils.getMoneyFormat(balanceDetailInfo.getOrderAmount()));

            feeView.setVisibility(View.VISIBLE);

            otherFeeView.setText("￥" + StringUtils.getMoneyFormat(balanceDetailInfo.getTradeCommission()));

            typeView.setText("收入");

        }else{// 支出
            titleView.setText("出账金额");

            typeView.setText("支出");
        }

        if(type == 5){

            checkDetailView.setText("查看关联记录");

            checkDetailView.setVisibility(View.VISIBLE);

        }else if(type == 1){
            checkDetailView.setText("订单详情");

            checkDetailView.setVisibility(View.VISIBLE);

        }

        if(getIntent().hasExtra(Constant.KEY.IS_LIKE)){
            checkDetailView.setVisibility(View.GONE);
        }

        dateView.setText(TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(balanceDetailInfo.getAddTime())));

        orderNoView.setText(balanceDetailInfo.getOrderId());

        remainAmountView.setText("￥" + StringUtils.getMoneyFormat(balanceDetailInfo.getWalletAmount()));

        noteView.setText(balanceDetailInfo.getOrderName());

        realAmountView.setText("￥" + StringUtils.getMoneyFormat(balanceDetailInfo.getAmount()));
    }

    private void getData(){
        GetBalanceDetailRequest request = new GetBalanceDetailRequest(balanceId, new ResponseListener<GetBalanceDetailResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext,"载入中...");
            }

            @Override
            public void onCacheResponse(GetBalanceDetailResponse response) {

            }

            @Override
            public void onResponse(GetBalanceDetailResponse response) {
                if(response != null){

                    balanceDetailInfo = response.getResult().getBalanceDetail();

                    setValue();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.tv_check_detail:

                Intent intent = new Intent();

                if(type == 5){

                    intent.setClass(mContext, BalanceDetailActivity.class);

                    intent.putExtra(Constant.KEY.ITEM_ID, balanceDetailInfo.getRelatedId() + "");

                }else if(type == 1){

                    intent.setClass(mContext, DownLineDetailActivity.class);

                    intent.putExtra(Constant.KEY.ORDER_ID, balanceDetailInfo.getOrderId());
                }

                intent.putExtra(Constant.KEY.IS_LIKE, true);

                startActivity(intent);

                break;
        }
    }
}
