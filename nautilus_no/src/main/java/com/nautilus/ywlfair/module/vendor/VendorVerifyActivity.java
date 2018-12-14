package com.nautilus.ywlfair.module.vendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.module.BaseActivity;

/**
 * Created by Administrator on 2016/3/28.
 */
public class VendorVerifyActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private boolean isApplying;

    private TextView beginVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vendor_verify_activity);

        mContext = this;

        findViewById(R.id.im_back).setOnClickListener(this);

        isApplying = GetUserInfoUtil.getUserInfo().getApplyVendorStatus() == 2 ? true : false;

        beginVerify = (TextView) findViewById(R.id.tv_begin_verify);

        beginVerify.setOnClickListener(this);

        findViewById(R.id.tv_vendor_question).setOnClickListener(this);

        setStatus();
    }

    private void setStatus(){

        if (isApplying) {

            beginVerify.setEnabled(false);

            beginVerify.setText("申请审核中");

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;

            case R.id.tv_begin_verify:

                Intent intent = new Intent(mContext,
                        RegistrationStall.class);

                startActivityForResult(intent, Constant.REQUEST_CODE.TO_BE_VENDOR);

                break;

            case R.id.tv_vendor_question:

                Intent questionIntent = new Intent(mContext, VendorQuestionActivity.class);

                startActivity(questionIntent);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            GetUserInfoUtil.getUserInfo().setApplyVendorStatus(2);

            isApplying = true;

            setStatus();
        }
    }
}
