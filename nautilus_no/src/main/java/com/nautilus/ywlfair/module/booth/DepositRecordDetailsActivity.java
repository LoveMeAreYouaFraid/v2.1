package com.nautilus.ywlfair.module.booth;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ListViewUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.DebitRecordsInfo;
import com.nautilus.ywlfair.entity.request.GetDepositRecordDetailsRequest;
import com.nautilus.ywlfair.entity.request.PostReturnDepositRequest;
import com.nautilus.ywlfair.entity.response.GetDebitRecordsResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.booth.adapter.MoneyBackAdapter;
import com.nautilus.ywlfair.widget.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 摊主交押金记录明细
 * Created by lipeng on 2016/3/29.
 */
public class DepositRecordDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvUsersDeposit, tvWay, tvCode, tvData, tvRefundButt, surplusMoney;

    private GetDebitRecordsResponse responses;

    private Context mContext;

    private String depositId;

    private List<DebitRecordsInfo> deductionsLogList; //历史扣款

    private List<DebitRecordsInfo> refundLogList;//退款详情

    private MoneyBackAdapter deductionsLogAdapter, refundLogAdapter;

    private ListView deductionsLogListView, refundLogListView;

    private View backMoneyView;

    private View msg;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.deposit_record_details_activity);

        depositId = getIntent().getStringExtra(Constant.REQUEST.KEY.ITEM_ID);

        mContext = this;

        deductionsLogList = new ArrayList<>();

        refundLogList = new ArrayList<>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        deductionsLogAdapter = new MoneyBackAdapter(mContext, deductionsLogList);

        refundLogAdapter = new MoneyBackAdapter(mContext, refundLogList);

        deductionsLogListView = (ListView) findViewById(R.id.deductions_log_list);

        refundLogListView = (ListView) findViewById(R.id.refund_log_list);

        deductionsLogListView.setAdapter(deductionsLogAdapter);

        refundLogListView.setAdapter(refundLogAdapter);

        backMoneyView = findViewById(R.id.back_money_view);

        msg = findViewById(R.id.msg);

        View back = findViewById(R.id.img_back);
        back.setOnClickListener(this);

        TextView appTitle = (TextView) findViewById(R.id.tv_title);
        appTitle.setText("押金详情");

        tvUsersDeposit = (TextView) findViewById(R.id.tv_users_deposit);

        tvWay = (TextView) findViewById(R.id.tv_way);

        tvCode = (TextView) findViewById(R.id.tv_code);

        tvData = (TextView) findViewById(R.id.tv_data);

        tvRefundButt = (TextView) findViewById(R.id.tv_refund_butt);

        surplusMoney = (TextView) findViewById(R.id.surplus_money);

        tvRefundButt.setOnClickListener(this);

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                    }
                });

        getData();
    }

    private void getView() {
        tvUsersDeposit.setText("￥ " + responses.getResult().getDepositInfo().getPrice());

        if (responses.getResult().getDepositInfo().getPayChannel().equals("WX")) {
            tvWay.setText("支付方式：" + "微信");
        } else {
            tvWay.setText("支付方式：" + "支付宝");
        }

        tvData.setText("支付时间：" + TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(responses.getResult().getDepositInfo().getPayTime())));

        tvCode.setText("订单编号：" + responses.getResult().getDepositInfo().getOrderId());

        surplusMoney.setText("￥" + responses.getResult().getDepositInfo().getSurplusAmount());//剩余押金

//        //交押金状态。0：可交押金；1:已交押金可申请退款；2：已申请退款；3:已锁定；4：退押失败；5：退押成功；6: 押金已扣完

        switch (responses.getResult().getDepositInfo().getDepositStatus()) {
            case 0:
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
                tvRefundButt.setVisibility(View.GONE);
                break;
        }


        if (responses.getResult().getDepositLogList().getDeductionsLog().size() == 0) {

            msg.setVisibility(View.VISIBLE);

        } else {
            deductionsLogList.clear();

            deductionsLogList.addAll(responses.getResult().getDepositLogList().getDeductionsLog());

            deductionsLogAdapter.notifyDataSetChanged();

            ListViewUtil.setListViewHeight(deductionsLogListView);
        }

        if (responses.getResult().getDepositLogList().getRefundLog().size() == 0) {
            backMoneyView.setVisibility(View.GONE);

        } else {
            backMoneyView.setVisibility(View.VISIBLE);

            refundLogList.clear();

            refundLogList.addAll(responses.getResult().getDepositLogList().getRefundLog());

            refundLogAdapter.notifyDataSetChanged();

            ListViewUtil.setListViewHeight(refundLogListView);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_refund_butt:
                depositBackDialog();
                break;
        }

    }


    private void getData() {

        GetDepositRecordDetailsRequest request = new GetDepositRecordDetailsRequest(
                GetUserInfoUtil.getUserInfo().getUserId() + "", depositId,
                new ResponseListener<GetDebitRecordsResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "加载中...");
                    }

                    @Override
                    public void onCacheResponse(GetDebitRecordsResponse response) {

                    }

                    @Override
                    public void onResponse(GetDebitRecordsResponse response) {
                        if (response.getResult().getDepositInfo() != null) {
                            responses = null;
                            responses = response;
                            getView();
                        } else {
                            Toast.makeText(MyApplication.getInstance(), "获取数据失败，请您稍后重试", Toast.LENGTH_SHORT).show();
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
                        mSwipeRefreshLayout.setRefreshing(false);
                        ProgressDialog.getInstance().cancel();
                    }
                });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    private void depositBackDialog() {

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
        titleTextView.setVisibility(View.GONE);

        View dividerView = view.findViewById(R.id.view_divider);
        dividerView.setVisibility(View.VISIBLE);

        TextView contentTextView = (TextView) view.findViewById(R.id.tv_content);
        contentTextView.setVisibility(View.VISIBLE);
        contentTextView.setText("确认申请退款剩余押金？");

        TextView ok = (TextView) view.findViewById(R.id.tv_left);
        ok.setText("确定");

        TextView no = (TextView) view.findViewById(R.id.tv_right);
        no.setText("取消");

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                depositBack();
                dialog.cancel();


            }
        });


    }

    private void depositBack() {

        PostReturnDepositRequest request = new PostReturnDepositRequest(GetUserInfoUtil.getUserInfo().getUserId() + "",
                depositId, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");

            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if (response != null) {
                    ToastUtil.showShortToast("退款申请已提交，等待审核！");
                } else {
                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");
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
                getData();
                ProgressDialog.getInstance().cancel();
            }
        });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

}
