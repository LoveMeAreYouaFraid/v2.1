package com.nautilus.ywlfair.module.launch.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.request.GetSendCodeRequest;
import com.nautilus.ywlfair.entity.request.PostValidInvitationRequest;
import com.nautilus.ywlfair.entity.request.PostVerifyCodeRequest;
import com.nautilus.ywlfair.entity.response.PutUserInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.launch.ModifyPassword;
import com.nautilus.ywlfair.module.mine.wallet.SetPayPasswordActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegisterActivity extends BaseActivity implements OnClickListener {


    @BindView(R.id.ed_invitation_code)
    EditText edInvitationCode;

    private Context mContext;

    private int type = 0;//0注册 4 找回支付密码 2 找回登录密码

    private TextView sendCode, zhuce;

    private View confirmBtn;

    private EditText confirmCodeEt, phoneNumEt;

    private TimeCount time;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.register_first_step_activity);

        ButterKnife.bind(this);

        type = getIntent().getIntExtra(Constant.KEY.TYPE, 0);

        title = getIntent().getStringExtra(Constant.KEY.NAME);

        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象

        initViews();

        zhuce.setText(title);

        if (type == 0) {
            edInvitationCode.setVisibility(View.VISIBLE);
        } else {

            confirmCodeEt.setVisibility(View.VISIBLE);

            confirmBtn.setVisibility(View.VISIBLE);

            sendCode.setText("发送验证码");

            if (type == 4) {

                String bindPhone = GetUserInfoUtil.getUserInfo().getBindPhone();

                phoneNumEt.setText(StringUtils.getProtectedPhone(bindPhone));

                phoneNumEt.setEnabled(false);

                zhuce.setText(getIntent().getStringExtra(Constant.KEY.NAME));

                confirmBtn.setBackgroundResource(R.drawable.bg_confirm_btn_selector);

                findViewById(R.id.tv_warm_text).setVisibility(View.VISIBLE);

            } else if (type == 2) {
                phoneNumEt.setHint("请输入手机号");

            }
        }
    }

    private void initViews() {

        findViewById(R.id.img_back).setOnClickListener(this);

        zhuce = (TextView) findViewById(R.id.zhuce);

        confirmBtn = findViewById(R.id.tv_user_agreement);

        confirmBtn.setOnClickListener(this);

        confirmCodeEt = (EditText) findViewById(R.id.et_code);

        phoneNumEt = (EditText) findViewById(R.id.phone_deitext);

        sendCode = (TextView) findViewById(R.id.tv_send_phone);

        sendCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:

                finish();

                break;

            case R.id.tv_send_phone:

                String account = phoneNumEt.getText().toString();

                if(type == 4){
                    account = GetUserInfoUtil.getUserInfo().getBindPhone();
                }

                if (StringUtils.isMobileNumber(account)) {

                    mySendCode(account, type);

                } else {
                    ToastUtil.showShortToast("请输入正确的手机号码");
                }

                break;

            case R.id.tv_user_agreement:

                String myCode = confirmCodeEt.getText().toString();

                String myPhone = phoneNumEt.getText().toString();

                if(type == 4){
                    myPhone = GetUserInfoUtil.getUserInfo().getBindPhone();
                }

                if (StringUtils.isMobileNumber(myPhone)) {
                    if (TextUtils.isEmpty(myCode)) {
                        ToastUtil.showShortToast("验证码不能为空");
                    } else {

                        if (!TextUtils.isEmpty(edInvitationCode.getText().toString())) {
                            checkInvitationCode(edInvitationCode.getText().toString());
                        }else{
                            verifyCode(myPhone, myCode);
                        }

                    }
                } else {
                    ToastUtil.showShortToast("手机号码不正确");
                }

                break;
        }
    }

    class TimeCount extends CountDownTimer {// 计时器

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            sendCode.setText("发送验证码");
            sendCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            sendCode.setClickable(false);
            sendCode.setText(millisUntilFinished / 1000 + "秒");
        }

    }

    private void mySendCode(String phone, int type) {

        GetSendCodeRequest request = new GetSendCodeRequest(phone, type,
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
                            ToastUtil.showShortToast(response.getMessage());

                        } else {
                            ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void verifyCode(String phone, String code) {
        PostVerifyCodeRequest request = new PostVerifyCodeRequest(phone, code, 0,
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
                            if (type == 2) {

                                Intent ok = new Intent(mContext,
                                        ModifyPassword.class);

                                ok.putExtra("phone", phoneNumEt.getText().toString());

                                startActivity(ok);

                            } else if (type == 0) {
                                Intent ok = new Intent(mContext,RegisterSecondStepActivity.class);

                                ok.putExtra("msg", "1");

                                ok.putExtra(Constant.KEY.PHONE, phoneNumEt.getText().toString());

                                if (!TextUtils.isEmpty(edInvitationCode.getText().toString())) {
                                    ok.putExtra(Constant.KEY.CODE, edInvitationCode.getText().toString());
                                }

                                startActivity(ok);

                                finish();
                            } else {
                                Intent setPayPsdIntent = new Intent(mContext, SetPayPasswordActivity.class);

                                setPayPsdIntent.putExtra(Constant.KEY.TYPE, 0);

                                startActivity(setPayPsdIntent);

                                finish();
                            }
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
                        ProgressDialog.getInstance().cancel();
                    }
                });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void checkInvitationCode(String invitationCode){
        PostValidInvitationRequest request = new PostValidInvitationRequest(invitationCode, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext,"验证邀请码...");
            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if (response != null){

                    String myCode = confirmCodeEt.getText().toString();

                    String myPhone = phoneNumEt.getText().toString();

                    verifyCode(myPhone, myCode);
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
                ProgressDialog.getInstance().cancel();
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

}
