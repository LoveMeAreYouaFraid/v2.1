package com.nautilus.ywlfair.module.booth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.GetVendorInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.GetVendorInfoRequest;
import com.nautilus.ywlfair.entity.response.GetVendorInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

/**
 * Created by lipeng on 2016/3/28.
 */
public class BoothDepositActivity extends BaseActivity implements View.OnClickListener {
    private TextView isPayDeposit, payDeposit, tvDoothDescription;

    private Context mContext;

    private GetVendorInfo vendor;

    private View layoutBooth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booth_deposit_activity);
        mContext = this;
        getView();
        getData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    private void getView() {
        View back = findViewById(R.id.img_back);
        TextView appTitle = (TextView) findViewById(R.id.tv_title);
        isPayDeposit = (TextView) findViewById(R.id.tv_is_pay_deposit);
        View isPayDepositLayout = findViewById(R.id.layout_is_pay_deposit);
        payDeposit = (TextView) findViewById(R.id.tv_pay_deposit);
        tvDoothDescription = (TextView) findViewById(R.id.tv_booth_description);
        layoutBooth = findViewById(R.id.layout_booth_deposit);

        isPayDepositLayout.setOnClickListener(this);
        payDeposit.setOnClickListener(this);
        appTitle.setText("摊位押金");
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_is_pay_deposit:
                startActivity(new Intent(mContext, DepositListActivity.class));
                break;
            case R.id.tv_pay_deposit:
                startActivity(new Intent(mContext, DepositOrderConfirmActivity.class).putExtra(Constant.KEY.DEPOSIT, vendor));
                break;
        }

    }

    private void getData() {

        UserInfo userInfo = GetUserInfoUtil.getUserInfo();

        if(userInfo == null){
            return;
        }

        String userId = userInfo.getUserId() + "";

        GetVendorInfoRequest request = new GetVendorInfoRequest(userId,
                new ResponseListener<GetVendorInfoResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "加载中...");
                    }

                    @Override
                    public void onCacheResponse(GetVendorInfoResponse response) {
                        if (response != null && response.getResult().getVendor() != null) {

                        }
                    }

                    @Override
                    public void onResponse(GetVendorInfoResponse response) {
                        if (response == null || response.getResult().getVendor() == null) {
                            return;
                        }
                        vendor = response.getResult().getVendor();
                        payDeposit.setText("缴纳押金 ￥" + StringUtils.getMoneyFormat(vendor.getDeposit()));
                        switch (vendor.getDepositFlag()) {
                            case 0:
                                layoutBooth.setVisibility(View.VISIBLE);
                                isPayDeposit.setText("未缴纳");
                                break;
                            case 1:
                                if (vendor.getDeposit() == 0) {
                                    isPayDeposit.setText("未缴纳");
                                } else {
                                    isPayDeposit.setText("￥" + StringUtils.getMoneyFormat(vendor.getDepositSurplus()));
                                    isPayDeposit.setTextColor(getResources().getColor(R.color.money_num));
                                    layoutBooth.setVisibility(View.GONE);
                                }
                                break;
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
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }
}
