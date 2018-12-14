package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.CashierInputFilter;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
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
import com.nautilus.ywlfair.entity.request.GetAccountStatusRequest;
import com.nautilus.ywlfair.entity.request.GetPayAuthorizationRequest;
import com.nautilus.ywlfair.entity.request.PostCashOutRequest;
import com.nautilus.ywlfair.entity.response.GetAccountStatusResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.launch.register.RegisterActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

/**
 * Created by Administrator on 2016/6/16.
 */
public class CashOutActivity extends BaseActivity implements View.OnClickListener,
        ShowPayPasswordDialog.PasswordInputListener, OnUserSelectListener {

    private Context mContext;

    private EditText numMoneyView;

    private WalletInfo walletInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cash_out_activity);

        mContext = this;

        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);

        findViewById(R.id.bt_confirm).setOnClickListener(this);

        InputFilter[] filters = {new CashierInputFilter()};

        numMoneyView = (EditText) findViewById(R.id.et_num_money);

        numMoneyView.setFilters(filters);

        walletInfo = (WalletInfo) getIntent().getSerializableExtra(Constant.KEY.WALLET);

        if (walletInfo != null) {
            TextView outAmountView = (TextView) findViewById(R.id.tv_out_amount);
            outAmountView.setText("￥" + StringUtils.getMoneyFormat(walletInfo.getWithdrawAmount()));

            TextView totalAmountView = (TextView) findViewById(R.id.tv_total_amount);

            totalAmountView.setText("￥" + StringUtils.getMoneyFormat(walletInfo.getAmount()));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.bt_confirm:

                String numMoney = numMoneyView.getText().toString().trim();

                if (TextUtils.isEmpty(numMoney)) {
                    ToastUtil.showLongToast("请输入提现金额");
                    return;
                }
                if (Double.valueOf(numMoney) > walletInfo.getWithdrawAmount()) {
                    ToastUtil.showLongToast("提现金额超出");
                    return;
                }

                checkAccountStatus();

                break;

        }
    }

    @Override
    public void onInputFinished(String password) {

        getPayAuthorization(password);

    }

    private void cashOut(final String amount, String password) {

        int userId = GetUserInfoUtil.getUserInfo().getUserId();

        PostCashOutRequest request = new PostCashOutRequest(userId, amount, password,
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在提交...");
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            Intent intent = new Intent(mContext, CashOutCompleteActivity.class);

                            intent.putExtra(Constant.KEY.NUMBER, amount);

                            startActivity(intent);

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

    private void getPayAuthorization(final String password) {

        int userId = GetUserInfoUtil.getUserInfo().getUserId();

        GetPayAuthorizationRequest request = new GetPayAuthorizationRequest(userId, password,
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext,"验证支付密码");
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            cashOut(numMoneyView.getText().toString().trim(), password);

                            ShowPayPasswordDialog.getInstance().disMissDialog();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        ProgressDialog.getInstance().cancel();

                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            if(response.getMessage().contains("冻结")){
                                ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, response.getMessage(),4);

                                return;
                            }

                            ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, response.getMessage(),3);

                        }

                    }

                    @Override
                    public void onFinish() {
                    }
                });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void checkAccountStatus(){
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetAccountStatusRequest request = new GetAccountStatusRequest(userId, new ResponseListener<GetAccountStatusResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext,"检查账户...");
            }

            @Override
            public void onCacheResponse(GetAccountStatusResponse response) {

            }

            @Override
            public void onResponse(GetAccountStatusResponse response) {
                if (response != null){

                    if(response.getResult().getPayAccountStatus() == 1) {
                        ShowPayPasswordDialog.getInstance().initMenuDialog(mContext);
                    }else if(response.getResult().getPayAccountStatus() == -1){

                        ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, response.getMessage(), 4);
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onFinish() {
                ProgressDialog.getInstance().cancel();
            }
        });

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onSelect(boolean isConfirm) {
        if(isConfirm){
            ShowPayPasswordDialog.getInstance().clearText();
        }else{
            Intent phone = new Intent(mContext, RegisterActivity.class);

            phone.putExtra(Constant.KEY.NAME,"找回支付密码");

            phone.putExtra(Constant.KEY.TYPE, 4);

            mContext.startActivity(phone);
        }
    }
}
