package com.nautilus.ywlfair.module.vendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.SingleChoiceDialog;
import com.nautilus.ywlfair.entity.bean.VendorInfo;
import com.nautilus.ywlfair.entity.bean.event.EventActiveStatus;
import com.nautilus.ywlfair.entity.request.PostCreateVendorRequest;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/3/28.
 */
public class VendorInfoCompletionActivity extends BaseActivity implements View.OnClickListener,SingleChoiceDialog.ItemChoiceListener {

    private Context mContext;

    private TextView verifyType, billType, careerType;

    private String[] verifyTypes = new String[]{"个人", "公司"};

    private String[] billTypes = new String[]{"不能开具发票", "能开普通发票", "能开增值税普通发票", "能开增值税专用发票"};

    private String[] careerTypes = new String[]{"全职", "兼职"};

    private int changeType = 0;

    private VendorInfo vendorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vendor_info_completion_activity);

        mContext = this;

        vendorInfo = new VendorInfo();

        findViewById(R.id.im_back).setOnClickListener(this);

        findViewById(R.id.ll_verify_type).setOnClickListener(this);

        findViewById(R.id.ll_bill_type).setOnClickListener(this);

        findViewById(R.id.ll_career_type).setOnClickListener(this);

        findViewById(R.id.tv_top_bar_right).setOnClickListener(this);

        verifyType = (TextView) findViewById(R.id.tv_verify_type);

        billType = (TextView) findViewById(R.id.tv_bill_type);

        careerType = (TextView) findViewById(R.id.tv_career_type);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.im_back:
                finish();
                break;

            case R.id.ll_verify_type:
                changeType = 0;

                SingleChoiceDialog.getInstance().showDialog(mContext, verifyTypes);
                break;

            case R.id.ll_bill_type:
                changeType = 1;

                SingleChoiceDialog.getInstance().showDialog(mContext, billTypes);

                break;

            case R.id.ll_career_type:
                changeType = 2;

                SingleChoiceDialog.getInstance().showDialog(mContext, careerTypes);

                break;

            case R.id.tv_top_bar_right:

                createVendor();

                break;
        }
    }

    private void createVendor() {

        String verifyString = verifyType.getText().toString();

        if(TextUtils.isEmpty(verifyString)){
            ToastUtil.showLongToast("请选择认证类型");
            return;
        }

        String billString = billType.getText().toString();

        if(TextUtils.isEmpty(billString)){
            ToastUtil.showLongToast("请选择开具发票类型");
            return;
        }

        String careerString = careerType.getText().toString();

        if(TextUtils.isEmpty(careerString)){
            ToastUtil.showLongToast("请选择职业类型");
            return;
        }

        postCreateVendorApply();

    }

    private void postCreateVendorApply(){

        vendorInfo.setUserId(GetUserInfoUtil.getUserInfo().getUserId());

        PostCreateVendorRequest request = new PostCreateVendorRequest(Request.Method.PUT, true, vendorInfo, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在提交...");
            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if(response != null){
                    ToastUtil.showShortToast("资料补填成功！");

                    setResult(RESULT_OK);

                    EventBus.getDefault().post(new EventActiveStatus(0, 1, null));

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

    @Override
    public void onItemChoice(int position) {
        switch (changeType){
            case 0:
                if(position == 1){
                    Intent intent = new Intent(mContext, EditViewActivity.class);

                    startActivityForResult(intent, Constant.REQUEST_CODE.EDIT_COMMENT);

                } else {
                    verifyType.setText(verifyTypes[position]);
                }

                vendorInfo.setAuthType(position);

                break;

            case 1:
                billType.setText(billTypes[position]);

                vendorInfo.setInvoiceType(position);
                break;

            case 2:

                careerType.setText(careerTypes[position]);

                vendorInfo.setJobType(position);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){

            String company = data.getStringExtra(Constant.KEY.COMPANY);

            verifyType.setText(company);
        }
    }
}
