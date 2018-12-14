package com.nautilus.ywlfair.module.vendor;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.event.EventDeposit;
import com.nautilus.ywlfair.entity.request.PostReturnDepositRequest;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Administrator on 2016/1/25.
 */
public class ReturnDepositActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private EditText phoneEdit, accountEdit, realNameEdit;

    private TextView chooseChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.submit_application);

        mContext = this;

        initViews();
    }

    private void initViews() {

        View back = findViewById(R.id.img_back);
        back.setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("提交申请");

        TextView rightBtn = (TextView) findViewById(R.id.tv_right_btn);
        rightBtn.setVisibility(View.VISIBLE);

        rightBtn.setText("完成");

        rightBtn.setOnClickListener(this);

        phoneEdit = (EditText) findViewById(R.id.et_phone);

        realNameEdit = (EditText) findViewById(R.id.et_real_name);

        accountEdit = (EditText) findViewById(R.id.et_account);

        chooseChannel = (TextView) findViewById(R.id.tv_choose_channel);
        chooseChannel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.tv_choose_channel:
                showChannelDialog();
                break;

            case R.id.tv_right_btn:
                applyReturnDeposit();
                break;
        }
    }

    private void applyReturnDeposit() {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        String depositId = phoneEdit.getText().toString().trim();

        if (!StringUtils.isMobileNumber(depositId)) {
            ToastUtil.showShortToast("请输入正确的手机号码");
            return;
        }
        PostReturnDepositRequest returnDepositRequest = new PostReturnDepositRequest(userId, depositId,
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
                            ToastUtil.showLongToast("申请提交成功,3-5个工作日退还到您的账户");

                            EventBus.getDefault().post(new EventDeposit(2));

                            finish();
                        } else {
                            ToastUtil.showShortToast("操作失败,请稍后再试!");
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

        returnDepositRequest.setShouldCache(false);

        VolleyUtil.addToRequestQueue(returnDepositRequest);
    }

    private String payChannel = "ALI";

    private void showChannelDialog() {

        final Dialog dialog = new Dialog(this, R.style.dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final CheckBox CkMain, CkLady;

        final TextView determine, cancel, title;

        View main, lady;

        dialog.setContentView(R.layout.dlg_pay_channel);

        title = (TextView) findViewById(R.id.tv_title);
        CkMain = (CheckBox) dialog.findViewById(R.id.ck_man);

        CkLady = (CheckBox) dialog.findViewById(R.id.ck_lady);

        title.setText("选择支付方式");

        main = dialog.findViewById(R.id.main);

        lady = dialog.findViewById(R.id.lady);

        determine = (TextView) dialog.findViewById(R.id.tv_determine);

        cancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        if (chooseChannel.getText().toString().equals("支付宝")) {
            CkLady.setChecked(false);
            CkMain.setChecked(true);
        }
        if (chooseChannel.getText().toString().equals("微信")) {
            CkLady.setChecked(true);
            CkMain.setChecked(false);
        }

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CkMain.setChecked(true);
                CkLady.setChecked(false);
            }
        });
        lady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CkMain.setChecked(false);
                CkLady.setChecked(true);
            }
        });
        determine.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (CkMain.isChecked() == true) {
                    payChannel = "ALI";

                    chooseChannel.setText("支付宝");

                    realNameEdit.setVisibility(View.VISIBLE);
                } else {
                    payChannel = "WX";

                    chooseChannel.setText("微信");

                    realNameEdit.setVisibility(View.GONE);
                }

                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
