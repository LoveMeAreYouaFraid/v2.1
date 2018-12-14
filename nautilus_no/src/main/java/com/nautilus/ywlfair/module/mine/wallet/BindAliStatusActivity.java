package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.WalletInfo;
import com.nautilus.ywlfair.entity.request.GetWalletInfoRequest;
import com.nautilus.ywlfair.entity.response.GetWalletInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/17.
 */
public class BindAliStatusActivity extends BaseActivity {

    private int bindStatus;

    private int aliType;

    private Context mContext;

    private String aliAccount;

    @BindView(R.id.iv_icon)
    ImageView imageView;

    @BindView(R.id.tv_status)
    TextView statusTextView;

    @BindView(R.id.tv_account)
    TextView accountTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bind_ali_pay_status_activity);

        mContext = this;

        ButterKnife.bind(this);

        getData();

    }

    private void iniViews() {

        findViewById(R.id.tv_top_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (bindStatus == 1) {
            statusTextView.setText("等待审核（预计1-7个工作日）");

            imageView.setImageResource(R.drawable.ic_is_binding);
        } else if (bindStatus == 2) {
            statusTextView.setText("已绑定");

            imageView.setImageResource(R.drawable.ic_has_bind);
        }

        String accountType;

        if (aliType == 1) {
            accountType = "个人支付宝：";
        } else {
            accountType = "企业支付宝：";
        }

        if (StringUtils.isMobileNumber(aliAccount)) {
            accountTextView.setText(accountType + StringUtils.getProtectedPhone(aliAccount));

        } else if (StringUtils.isEmail(aliAccount)) {
            accountTextView.setText(accountType + StringUtils.getProtectedEmail(aliAccount));
        }else{
            accountTextView.setText(accountType + aliAccount);
        }
    }

    private void getData() {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetWalletInfoRequest request = new GetWalletInfoRequest(userId, new ResponseListener<GetWalletInfoResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(GetWalletInfoResponse response) {

            }

            @Override
            public void onResponse(GetWalletInfoResponse response) {
                if (response != null && response.getResult().getWallet() != null) {

                    aliAccount = response.getResult().getWallet().getAliPayInfo().getAccount() + "";

                    aliType = response.getResult().getWallet().getAliPayInfo().getAliPayType();

                    bindStatus = response.getResult().getWallet().getAliPayBindStatus();

                    iniViews();
                } else {
                    ToastUtil.showLongToast("请稍后重试");
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
}
