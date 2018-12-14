package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.ShowPayPasswordDialog;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.OnUserSelectListener;
import com.nautilus.ywlfair.dialog.ShowPasswordErrorDialog;
import com.nautilus.ywlfair.entity.bean.WalletInfo;
import com.nautilus.ywlfair.entity.request.GetWalletInfoRequest;
import com.nautilus.ywlfair.entity.response.GetWalletInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.launch.BindPhone;
import com.nautilus.ywlfair.module.launch.register.RegisterActivity;
import com.nautilus.ywlfair.module.mine.level.GetMoneyCodeActivity;
import com.nautilus.ywlfair.module.webview.UseHelpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/16.
 */
public class MyWalletActivity extends BaseActivity implements View.OnClickListener, OnUserSelectListener {

    private Context mContext;

    private WalletInfo walletInfo;

    private boolean isCanSee = true;

    private boolean mIsRequesting = false;

    private int warmType;

    @BindView(R.id.tv_remain)
    TextView remainView;

    @BindView(R.id.tv_can_out)
    TextView outAmountView;

    @BindView(R.id.tv_is_see)
    ImageView isSeeView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.is_bind_ali)
    TextView isBindAliView;

    @BindView(R.id.is_set_pay)
    TextView isSetPayView;

    @BindView(R.id.tv_pay_psd)
    View setPayPsd;

    @BindView(R.id.tv_bind_pay)
    View bindPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_wallet_activity);

        ButterKnife.bind(this);

        mContext = this;

        isCanSee = PreferencesUtil.getBoolean(Constant.PRE_KEY.IS_CAN_SEE + GetUserInfoUtil.getUserInfo().getUserId(), true);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mIsRequesting) {
                    getData();
                }
            }
        });

        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);

        findViewById(R.id.tv_cash_out).setOnClickListener(this);

        findViewById(R.id.tv_money_detail).setOnClickListener(this);

        bindPay.setOnClickListener(this);

        setPayPsd.setOnClickListener(this);

        findViewById(R.id.iv_share).setOnClickListener(this);

        findViewById(R.id.tv_help).setOnClickListener(this);

        findViewById(R.id.ll_container).setOnClickListener(this);

        isSeeView.setOnClickListener(this);

    }

    private void setValue() {

        if (walletInfo == null) {
            return;
        }

        remainView.setText("￥" + StringUtils.getMoneyFormat(walletInfo.getAmount()));

        outAmountView.setText("可提现金额：￥" + StringUtils.getMoneyFormat(walletInfo.getWithdrawAmount()));

        if (walletInfo.getAliPayBindStatus() == 0) {
            isBindAliView.setText("未绑定");
        } else if (walletInfo.getAliPayBindStatus() == 1) {
            isBindAliView.setText("审核中");
        } else if (walletInfo.getAliPayBindStatus() == 2) {
            isBindAliView.setText("已绑定");
        } else if (walletInfo.getAliPayBindStatus() == 3) {
            isBindAliView.setText("审核失败");
        }

        if (walletInfo.getHasPayPassword() == 0) {
            isSetPayView.setText("未设置");
        } else {
            isSetPayView.setText("已设置");
        }
        exchangeCanSee();
    }

    @Override
    public void onClick(View v) {

        if (walletInfo == null) {
            return;
        }

        switch (v.getId()) {

            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.tv_cash_out:

                if (walletInfo.getAliPayBindStatus() == 0 || walletInfo.getAliPayBindStatus() == 1) {
                    warmType = 6;

                    ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, "提现前请先绑定支付宝账户", warmType);
                    return;
                }
                if (walletInfo.getAliPayBindStatus() == 1) {
                    ToastUtil.showLongToast("您绑定的支付宝账户正在审核中，请等待通过后再试");
                    return;
                }

                if (walletInfo.getHasPayPassword() == 0) {

                    warmType= 5;

                    ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, "您暂未设置支付密码，请先设置后重试", warmType);
                } else {
                    Intent cashOutIntent = new Intent(mContext, CashOutActivity.class);

                    cashOutIntent.putExtra(Constant.KEY.WALLET, walletInfo);

                    startActivity(cashOutIntent);
                }

                break;

            case R.id.tv_money_detail:
                Intent moneyDetailIntent = new Intent(mContext, MoneyDetailActivity.class);

                startActivity(moneyDetailIntent);
                break;

            case R.id.tv_bind_pay:
                Intent bindAliIntent = new Intent();

                if (walletInfo.getAliPayBindStatus() == 0 || walletInfo.getAliPayBindStatus() == 3) {
                    bindAliIntent.setClass(mContext, BindAliPayActivity.class);
                } else {
                    bindAliIntent.setClass(mContext, BindAliStatusActivity.class);

                    bindAliIntent.putExtra(Constant.REQUEST.KEY.ALI_PAY_TYPE, walletInfo.getAliPayInfo().getAliPayType());

                    bindAliIntent.putExtra(Constant.REQUEST.KEY.ACCOUNT, walletInfo.getAliPayInfo().getAccount());
                }

                startActivity(bindAliIntent);

                break;

            case R.id.tv_pay_psd:

                if (TextUtils.isEmpty(GetUserInfoUtil.getUserInfo().getBindPhone())) {

                    Intent intent = new Intent(mContext, BindPhone.class);

                    intent.putExtra(Constant.KEY.MODE, BindPhone.Mode.PAY_PASSWORD);

                    startActivity(intent);

                    return;
                }

                if (walletInfo != null && walletInfo.getHasPayPassword() == 0) {

                    Intent intent = new Intent(mContext, RegisterActivity.class);

                    intent.putExtra(Constant.KEY.TYPE, 4);

                    intent.putExtra(Constant.KEY.NAME, "设置支付密码");

                    startActivity(intent);

                } else {

                    Intent setPsdIntent = new Intent();

                    setPsdIntent.setClass(mContext, PayPasswordStatusActivity.class);

                    startActivityForResult(setPsdIntent, Constant.REQUEST_CODE.SET_PASSWORD);
                }

                break;

            case R.id.iv_share:

                Intent qrCodeIntent = new Intent(mContext, GetMoneyCodeActivity.class);

                startActivity(qrCodeIntent);

                break;

            case R.id.tv_help:

                Intent helpIntent = new Intent(mContext, UseHelpActivity.class);

                helpIntent.putExtra(Constant.KEY.URL, walletInfo.getWalletHelpUrl());

                startActivity(helpIntent);
                break;

            case R.id.tv_is_see:

                isCanSee = !isCanSee;

                PreferencesUtil.putBoolean(Constant.PRE_KEY.IS_CAN_SEE + GetUserInfoUtil.getUserInfo().getUserId(), isCanSee);

                exchangeCanSee();

                break;
        }
    }

    @Override
    protected void onResume() {

        getData();

        super.onResume();
    }

    private void exchangeCanSee() {

        if (isCanSee) {

            isSeeView.setImageResource(R.drawable.ic_visible);

            remainView.setText("￥" + StringUtils.getMoneyFormat(walletInfo.getAmount()));

            outAmountView.setVisibility(View.VISIBLE);
        } else {
            isSeeView.setImageResource(R.drawable.ic_disable);

            remainView.setText("****");

            outAmountView.setVisibility(View.GONE);
        }

    }

    private void getData() {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetWalletInfoRequest request = new GetWalletInfoRequest(userId, new ResponseListener<GetWalletInfoResponse>() {
            @Override
            public void onStart() {
                mIsRequesting = true;

                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onCacheResponse(GetWalletInfoResponse response) {

            }

            @Override
            public void onResponse(GetWalletInfoResponse response) {
                if (response != null && response.getResult().getWallet() != null) {
                    walletInfo = response.getResult().getWallet();

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
//                ProgressDialog.getInstance().cancel();
                mIsRequesting = false;

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onSelect(boolean isConfirm) {
        if (isConfirm) {

            if(warmType == 6){
                bindPay.performClick();
            }else if(warmType == 5){
                setPayPsd.performClick();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE.SET_PASSWORD) {
                if (walletInfo != null) {
                    walletInfo.setHasPayPassword(1);
                }
            }
        }
    }
}
