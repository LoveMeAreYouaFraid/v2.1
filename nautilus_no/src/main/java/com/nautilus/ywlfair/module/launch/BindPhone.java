package com.nautilus.ywlfair.module.launch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.GetSendCodeRequest;
import com.nautilus.ywlfair.entity.request.PostBindPhoneRequest;
import com.nautilus.ywlfair.entity.request.PostVerifyCodeRequest;
import com.nautilus.ywlfair.entity.response.PutUserInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.wallet.SetPayPasswordActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

public class BindPhone extends BaseActivity implements OnClickListener {
    private EditText phoneInputView, codeInputView;

    private Context mContext;

    private String phoneNum;

    private TimeCount time;

    private TextView sendCode;

    private LinearLayout chooseregion;

    private TextView confirmBtn;

    private TextView warmText;

    private TextView title;

    private int sendType = 1;

    public enum Mode {
        BIND, CHANGE_BIND, VERIFY, PAY_PASSWORD
    }

    private Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bind_phone);

        mContext = this;

        mode = (Mode) getIntent().getSerializableExtra(Constant.KEY.MODE);

        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象

        initViews();
    }

    private void initViews() {

        title = (TextView) findViewById(R.id.tv_title);

        chooseregion = (LinearLayout) findViewById(R.id.choose_region);
        chooseregion.setOnClickListener(this);

        warmText = (TextView) findViewById(R.id.tv_warm_text);

        findViewById(R.id.img_back).setOnClickListener(this);

        confirmBtn = (TextView) findViewById(R.id.tv_ok);
        confirmBtn.setOnClickListener(this);

        sendCode = (TextView) findViewById(R.id.tv_code);
        sendCode.setOnClickListener(this);

        phoneInputView = (EditText) findViewById(R.id.ed_pnone);

        codeInputView = (EditText) findViewById(R.id.ed_code);

        if (mode == Mode.BIND) {
            title.setText("绑定手机");

            warmText.setText("请输入要绑定的手机号码");

        } else if (mode == Mode.CHANGE_BIND) {
            title.setText("更换绑定");

            warmText.setText("请输入要更换的手机号码");
        } else if (mode == Mode.PAY_PASSWORD) {
            title.setText("设置支付密码");

            confirmBtn.setText("下一步");

            sendType = 4;

            warmText.setVisibility(View.GONE);

            chooseregion.setVisibility(View.GONE);

            confirmBtn.setBackgroundResource(R.drawable.bg_confirm_btn_selector);

            phoneInputView.setHint("请输入绑定手机号码");

            findViewById(R.id.tv_pay_warm).setVisibility(View.VISIBLE);

        } else {
            title.setText("验证手机");

            confirmBtn.setText("完成验证");

            warmText.setText("请输入接收信息的手机号码");

            sendType = 3;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_ok:

                if (mode == Mode.VERIFY) {
                    verifyCode();
                } else {
                    bindPhone();
                }

                break;

            case R.id.tv_code:

                phoneNum = phoneInputView.getText().toString().trim();

                if (!StringUtils.isMobileNumber(phoneNum)) {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendCode(phoneNum);

                break;
            case R.id.choose_region:
                Toast.makeText(this, "暂时支持中国地区，请等待后续更新", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void sendCode(String phone) {

        GetSendCodeRequest request = new GetSendCodeRequest(phone, sendType,
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            time.start();
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

                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void bindPhone() {
        UserInfo userInfo = GetUserInfoUtil.getUserInfo();

        phoneNum = phoneInputView.getText().toString().trim();

        if (!StringUtils.isMobileNumber(phoneNum)) {
            ToastUtil.showLongToast("请输入正确的手机号码");
            return;
        }

        String codeNum = codeInputView.getText().toString().trim();

        if (TextUtils.isEmpty(codeNum)) {
            ToastUtil.showLongToast("请输入验证码");
            return;
        }

        PostBindPhoneRequest request = new PostBindPhoneRequest(userInfo.getUserId() + "", phoneNum, codeNum,
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

                            GetUserInfoUtil.getUserInfo().setBindPhone(phoneNum);

                            if (mode == Mode.PAY_PASSWORD) {
                                Intent setPayPsdIntent = new Intent(mContext, SetPayPasswordActivity.class);

                                setPayPsdIntent.putExtra(Constant.KEY.TYPE, 0);

                                startActivity(setPayPsdIntent);

                            } else {
                                Toast.makeText(MyApplication.getInstance(), "绑定成功",
                                        Toast.LENGTH_SHORT).show();

                                GetUserInfoUtil.getUserInfo().setBindPhone(phoneNum);

                                setResult(RESULT_OK);
                            }

                            finish();
                        } else {
                            ToastUtil.showShortToast("操作失败");
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

    private void verifyCode() {

        phoneNum = phoneInputView.getText().toString().trim();

        if (!StringUtils.isMobileNumber(phoneNum)) {
            ToastUtil.showLongToast("请输入正确的手机号码");
            return;
        }

        String codeNum = codeInputView.getText().toString().trim();

        if (TextUtils.isEmpty(codeNum)) {
            ToastUtil.showLongToast("请输入验证码");
            return;
        }

        int userType = 0;

        PostVerifyCodeRequest request = new PostVerifyCodeRequest(phoneNum, codeNum, userType,
                new ResponseListener<PutUserInfoResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在验证...");
                    }

                    @Override
                    public void onCacheResponse(PutUserInfoResponse response) {

                    }

                    @Override
                    public void onResponse(PutUserInfoResponse response) {
                        if (response != null) {
                            Intent intent = new Intent();

                            intent.putExtra(Constant.KEY.PHONE, phoneNum);

                            setResult(RESULT_OK, intent);

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


    class TimeCount extends CountDownTimer {// 计时器

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            sendCode.setText("重新验证");
            sendCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            sendCode.setClickable(false);
            sendCode.setText(millisUntilFinished / 1000 + "秒");
        }

    }


}
