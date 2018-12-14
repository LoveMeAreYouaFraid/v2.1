package com.nautilus.ywlfair.module.launch.register;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.cache.CacheUserInfo;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.bean.UserMainEventItem;
import com.nautilus.ywlfair.entity.request.PostUserRegisterRequest;
import com.nautilus.ywlfair.entity.response.GetAccessTokenResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.webview.WebViewActivity;
import com.nautilus.ywlfair.module.launch.LoginActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import org.greenrobot.eventbus.EventBus;


public class RegisterSecondStepActivity extends BaseActivity implements OnClickListener {

    private CheckBox mCheckBox;

    private EditText nameInput;

    private EditText firstPassword, secondPassword;

    private int countChinese;

    private int abc;

    private int num;

    private String oldInputString = "";

    private String nickyName;

    private Context mContext;

    private String emailAddress;

    private String phoneNum;

    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_second_step_activity);

        mContext = this;

        findViewById(R.id.tv_user_agreement).setOnClickListener(this);

        findViewById(R.id.img_back).setOnClickListener(this);

        emailAddress = getIntent().getStringExtra(Constant.KEY.EMAIL);

        phoneNum = getIntent().getStringExtra(Constant.KEY.PHONE);

        mCheckBox = (CheckBox) findViewById(R.id.cb_agree);

        nameInput = (EditText) findViewById(R.id.ed_usenname);

        firstPassword = (EditText) findViewById(R.id.et_first_pwd);

        secondPassword = (EditText) findViewById(R.id.et_second_pwd);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.KEY.CODE))) {
            code = getIntent().getStringExtra(Constant.KEY.CODE);
        }

        findViewById(R.id.bt_confirm).setOnClickListener(this);

        nameInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                nickyName = nameInput.getText().toString();

                countChinese = StringUtils.countChinese(nickyName);

                abc = StringUtils.countLetter(nickyName);

                num = StringUtils.countNumber(nickyName);


                if (num + abc + countChinese > 18) {
                    nameInput.setText(oldInputString);

                    nameInput.setSelection(oldInputString.length());

                    ToastUtil.showShortToast("昵称长度超过限制");
                } else {
                    oldInputString = nickyName;
                }


            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_user_agreement:

                String url = null;
// = AppConfig.getInstance;().getAccessConfig().getkNautilusFairUserProtocolUrl();

                if (!TextUtils.isEmpty(url))
                    WebViewActivity.startWebViewActivity(mContext, "0", url, "0", 0, 0);

                break;

            case R.id.bt_confirm:

                if (!mCheckBox.isChecked()) {
                    Toast.makeText(mContext, "请先确认服务条款", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(nickyName)) {
                    ToastUtil.showShortToast("昵称不能为空");
                    return;
                }

                String password1 = firstPassword.getText().toString().trim();

                String password2 = secondPassword.getText().toString().trim();

                if (TextUtils.isEmpty(password1) || password1.length() < 6) {

                    ToastUtil.showShortToast("密码不能少于6位");

                    return;
                }

                if (!password1.equals(password2)) {

                    ToastUtil.showLongToast("两次密码输入不一致");

                    return;
                }

                userRegister(password1);

                break;
        }
    }

    private void userRegister(final String password) {
        String myInvitationCode = "";
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.KEY.CODE))) {
            myInvitationCode = getIntent().getStringExtra(Constant.KEY.CODE);
        }

        PostUserRegisterRequest registerRequest = new PostUserRegisterRequest(emailAddress == null ? "" : emailAddress, password, nickyName,
                phoneNum == null ? "" : phoneNum, 0, "", myInvitationCode, new ResponseListener<GetAccessTokenResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在提交...");
            }

            @Override
            public void onCacheResponse(GetAccessTokenResponse response) {

            }

            @Override
            public void onResponse(GetAccessTokenResponse response) {
                if (response != null & response.getResult().getUserInfo() != null) {

                    Toast.makeText(MyApplication.getInstance(), "注册成功",
                            Toast.LENGTH_SHORT).show();

                    UserInfo userInfo = response.getResult().getUserInfo();

                    TalkingDataAppCpa.onRegister(userInfo.getUserId() + "");

                    MyApplication.getInstance().setCurrentUser(userInfo);

                    MyApplication.getInstance().setLogin(true);

                    MyApplication.getInstance().setAccessToken(response.getResult().getAccessToken());

                    PreferencesUtil.putString(Constant.PRE_KEY.USER_NAME,
                            TextUtils.isEmpty(emailAddress) ? phoneNum : emailAddress);

                    PreferencesUtil.putString(Constant.PRE_KEY.PASSWORD,
                            password);

                    PreferencesUtil.putString(Constant.PRE_KEY.ACCESSTOKEN,
                            response.getResult().getAccessToken());

                    CacheUserInfo cacheUserInfo = new CacheUserInfo();
                    cacheUserInfo.setUserInfo(userInfo);

                    LoginActivity.finishThis();

                    UserMainEventItem item = new UserMainEventItem();

                    item.setType(3);

                    EventBus.getDefault().post(item);

                    finish();
                } else {
                    ToastUtil.showShortToast("操作失败,请稍候再试..");
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

        registerRequest.setShouldCache(false);

        VolleyUtil.addToRequestQueue(registerRequest);

    }


}
