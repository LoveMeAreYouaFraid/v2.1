package com.nautilus.ywlfair.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.bean.ActiveStatusInfo;
import com.nautilus.ywlfair.module.webview.BuyStallWebViewActivity;
import com.nautilus.ywlfair.module.active.ActivitySignUpActivity1;
import com.nautilus.ywlfair.module.booth.BoothDepositActivity;

/**
 * Created by Administrator on 2015/12/29.
 */
public class ShowGetStallMenu implements View.OnClickListener {

    private Dialog goodsMenuDialog;

    private static ShowGetStallMenu instance;

    private Context mContext;

    private TextView payDepositBtn, joinActiveBtn, buyBoothBtn, butBoothStatusText;

    private ActiveStatusInfo activeStatusInfo;

    private String roundId;

    public static ShowGetStallMenu getInstance() {
        if (instance == null) {

            instance = new ShowGetStallMenu();
        }

        return instance;
    }

    public void initMenuDialog(Context context, ActiveStatusInfo activeStatusInfo, String roundId) {

        this.activeStatusInfo = activeStatusInfo;

        if (context == this.mContext && goodsMenuDialog != null) {

            if (!goodsMenuDialog.isShowing()) {

                setStatus();

                goodsMenuDialog.show();

            }
            return;

        }

        mContext = context;

        this.roundId = roundId;

        View view = View.inflate(mContext, R.layout.get_stall_dialog, null);

        goodsMenuDialog = new Dialog(mContext, R.style.dialog);

        goodsMenuDialog.setCanceledOnTouchOutside(true);

        goodsMenuDialog.setContentView(view);

        goodsMenuDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Window window = goodsMenuDialog.getWindow();

        /* 设置位置 */
        window.setGravity(Gravity.BOTTOM);

        /* 设置动画 */
        window.setWindowAnimations(R.style.umeng_socialize_shareboard_animation);

        initViews(view);

        goodsMenuDialog.show();

    }

    private void initViews(View rootView) {
        buyBoothBtn = (TextView) rootView.findViewById(R.id.btn_buy_booth);
        buyBoothBtn.setOnClickListener(this);

        payDepositBtn = (TextView) rootView.findViewById(R.id.btn_pay_deposit);
        payDepositBtn.setOnClickListener(this);

        joinActiveBtn = (TextView) rootView.findViewById(R.id.btn_join_active);
        joinActiveBtn.setOnClickListener(this);

        butBoothStatusText = (TextView) rootView.findViewById(R.id.tv_status_text);

        setStatus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay_deposit:

                mContext.startActivity(new Intent(mContext, BoothDepositActivity.class));

                goodsMenuDialog.dismiss();

                break;

            case R.id.btn_join_active:

                Intent intent = new Intent(mContext, ActivitySignUpActivity1.class);

                intent.putExtra(Constant.KEY.ITEM_ID, activeStatusInfo.getActId());

                intent.putExtra(Constant.KEY.ROUND_ID, roundId);

                mContext.startActivity(intent);

                goodsMenuDialog.dismiss();

                break;

            case R.id.btn_buy_booth:

                    postVendorIntent();

                break;
        }
    }

    private void setStatus() {


            if (activeStatusInfo.getIsDeposit() == 0) {

                payDepositBtn.setEnabled(true);
            } else {
                payDepositBtn.setEnabled(false);

                payDepositBtn.setText("您已缴纳摊位押金");
            }


        if (activeStatusInfo.getIsDeposit() == 1) {
            if (activeStatusInfo.getIsBoothApply() == 0) {
                joinActiveBtn.setEnabled(true);

                joinActiveBtn.setText("报名本次活动");

            } else if (activeStatusInfo.getIsBoothApply() == 1) {
                joinActiveBtn.setEnabled(false);

                joinActiveBtn.setText("报名审核中");
            } else {
                joinActiveBtn.setEnabled(false);

                joinActiveBtn.setText("您已报名成功");
            }
        } else {
            joinActiveBtn.setEnabled(false);
        }

        if (activeStatusInfo.getIsDeposit() == 1 && activeStatusInfo.getIsBoothApply() == 2) {
            if (activeStatusInfo.getIsBoothBuy() == 0) {

                buyBoothBtn.setEnabled(true);

                buyBoothBtn.setTag(0);

            } else if (activeStatusInfo.getIsBoothBuy() == 1) {
                buyBoothBtn.setEnabled(true);

                buyBoothBtn.setText("查看摊位");

                buyBoothBtn.setTag(1);

                butBoothStatusText.setText("您可以查看本次活动摊位");

            } else {
                buyBoothBtn.setEnabled(false);

                buyBoothBtn.setText("摊位已售罄");

                butBoothStatusText.setText("本活动摊位已售完");

            }
        } else {

            if (activeStatusInfo.getIsBoothBuy() == -1) {
                buyBoothBtn.setText("摊位已售罄");

                butBoothStatusText.setText("本活动摊位已售完");
            }
            buyBoothBtn.setEnabled(false);
        }

        if (activeStatusInfo.getIsBoothTimeLimit() == 1) {

            if (activeStatusInfo.getIsBoothBuy() == 1) {
                buyBoothBtn.setEnabled(true);

                buyBoothBtn.setTag(1);

                buyBoothBtn.setText("查看摊位");

                butBoothStatusText.setText("您可以查看本次活动摊位");

            } else {
                buyBoothBtn.setEnabled(false);

                buyBoothBtn.setText("停止售摊");

                butBoothStatusText.setText("停止售卖摊位");
            }

            if (activeStatusInfo.getIsBoothApply() == 2) {

                joinActiveBtn.setText("您已报名成功");


            } else {
                joinActiveBtn.setText("报名已截止");
            }

            payDepositBtn.setEnabled(false);

            joinActiveBtn.setEnabled(false);

        }

    }

    private void postVendorIntent() {

        Intent buyStallIntent = new Intent(mContext,
                BuyStallWebViewActivity.class);

        mContext.startActivity(buyStallIntent);

        goodsMenuDialog.dismiss();

    }

}
