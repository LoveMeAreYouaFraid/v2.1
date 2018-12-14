package com.nautilus.ywlfair.module.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.cache.CacheUserInfo;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.PushManager;
import com.nautilus.ywlfair.common.utils.ShareUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.UserMainEventItem;
import com.nautilus.ywlfair.entity.request.GetAccessTokenRequest;
import com.nautilus.ywlfair.entity.response.GetAccessTokenResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.launch.register.RegisterActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


public class LoginActivity extends BaseActivity implements OnClickListener {

    private Activity mContext;

    public static LoginActivity instance = null;

    private UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.login");

    private EditText nameInput;

    private EditText passwordInput;

    private TextView forgetPassword, register;

    private Button confirmBtn;

    private String userName;

    private String password;

    private int thirdPartyFlag;

    public enum Mode {
        ACTIVE, PASSIVE
    }

    private Mode mode;

    public static void startLoginActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(MyApplication.getInstance(),
                    LoginActivity.class);
            intent.putExtra(Constant.KEY.MODE, Mode.PASSIVE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getInstance().startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        mContext = this;

        instance = this;

        register = (TextView) findViewById(R.id.tv_register);

        register.setOnClickListener(this);

        mode = (Mode) getIntent().getSerializableExtra(Constant.KEY.MODE);

        View topBarBack = findViewById(R.id.tv_top_bar_back);
        topBarBack.setOnClickListener(this);

        View qqLogin = findViewById(R.id.qq_login);
        qqLogin.setOnClickListener(this);

        View sinaLogin = findViewById(R.id.sina_login);
        sinaLogin.setOnClickListener(this);

        View weixinLogin = findViewById(R.id.weixin_login);
        weixinLogin.setOnClickListener(this);

        confirmBtn = (Button) findViewById(R.id.bt_confirm);
        confirmBtn.setOnClickListener(this);

        nameInput = (EditText) findViewById(R.id.ed_usenname);

        passwordInput = (EditText) findViewById(R.id.et_second_pwd);

        forgetPassword = (TextView) findViewById(R.id.tv_forget_pwd);
        forgetPassword.setOnClickListener(this);

        setSavedNameAndPwd();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                Intent id = new Intent(this, RegisterActivity.class);

                id.putExtra(Constant.KEY.TYPE, 0);

                id.putExtra(Constant.KEY.NAME, "注册");

                startActivity(id);

                break;

            case R.id.bt_confirm:

                userName = nameInput.getText().toString().trim();

                if (!StringUtils.isEmail(userName)
                        && !StringUtils.isMobileNumber(userName)) {
                    nameInput.setError("用户名不对");
                    return;
                }

                thirdPartyFlag = 0;

                password = passwordInput.getText().toString().trim();

                if (TextUtils.isEmpty(password) || password.length() < 6) {

                    ToastUtil.showShortToast("请输入6位以上密码");

                    return;
                }

                ProgressDialog.getInstance().show(mContext, "正在登录...");

                userLogin(userName, password, "");

                break;

            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.tv_forget_pwd:
                Intent phone = new Intent(this, RegisterActivity.class);

                phone.putExtra(Constant.KEY.TYPE,2);

                phone.putExtra(Constant.KEY.NAME, "找回密码");

                startActivity(phone);

                break;

            case R.id.qq_login:
                thirdPartyFlag = 1;

                ProgressDialog.getInstance().show(mContext, "正在登录...");

                ShareUtil.setQQssoHandler(mContext);

                sinaLogin(SHARE_MEDIA.QQ);
                break;

            case R.id.sina_login:
                ProgressDialog.getInstance().show(mContext, "正在登录...");

                mController.getConfig().setSsoHandler(new SinaSsoHandler());

                thirdPartyFlag = 2;

                sinaLogin(SHARE_MEDIA.SINA);
                break;

            case R.id.weixin_login:
                ProgressDialog.getInstance().show(mContext, "正在登录...");

                thirdPartyFlag = 4;

                ShareUtil.setWeixinSsoHandler(mContext);

                sinaLogin(SHARE_MEDIA.WEIXIN);
                break;

        }

    }

    private void setSavedNameAndPwd() {
        userName = PreferencesUtil.getString(Constant.PRE_KEY.USER_NAME);

        password = PreferencesUtil.getString(Constant.PRE_KEY.PASSWORD);

        if (!TextUtils.isEmpty(userName)) {
            nameInput.setText(userName);
        }
        if (!TextUtils.isEmpty(password)) {
            passwordInput.setText(password);
        }
    }

    private void userLogin(String account, String pwd, String nickyName) {
        GetAccessTokenRequest request = new GetAccessTokenRequest(account, pwd, thirdPartyFlag, nickyName,
                new ResponseListener<GetAccessTokenResponse>() {
                    @Override
                    public void onStart() {
//                        ProgressDialog.getInstance().show(mContext, "正在登录...");
                    }

                    @Override
                    public void onCacheResponse(GetAccessTokenResponse response) {
                    }

                    @Override
                    public void onResponse(GetAccessTokenResponse response) {
                        if (response == null || response.getResult().getUserInfo() == null) {
                            ToastUtil.showShortToast("登录失败,请检查网络连接");
                            return;
                        }

                        TalkingDataAppCpa.init(getApplicationContext(),Constant.TALKING_ID, Common.getAppMetaData(mContext,"UMENG_CHANNEL"));
                        TalkingDataAppCpa.onLogin(response.getResult().getUserInfo().getUserId() + "");

                        MyApplication.getInstance().setCurrentUser(response.getResult().getUserInfo());

                        MyApplication.getInstance().setLogin(true);

                        MyApplication.getInstance().setAccessToken(response.getResult().getAccessToken());

                        PushManager.bindPushAccount();

                        PreferencesUtil.putString(Constant.PRE_KEY.USER_NAME,
                                userName);

                        PreferencesUtil.putString(Constant.PRE_KEY.PASSWORD,
                                password);

                        PreferencesUtil.putString(Constant.PRE_KEY.ACCESSTOKEN,
                                response.getResult().getAccessToken());

                        CacheUserInfo cacheUserInfo = new CacheUserInfo();
                        cacheUserInfo.setUserInfo(response.getResult().getUserInfo());

                        MyApplication.getInstance().setUserType(0);

                        if (mode == Mode.ACTIVE) {
                            UserMainEventItem item = new UserMainEventItem();

                            item.setType(3);

                            EventBus.getDefault().post(item);
                        }

                        //登陆成功 发送广播
                        Intent mIntent = new Intent(Constant.BROADCAST.LOGIN_BROAD);

                        sendBroadcast(mIntent);

                        finish();
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

    /**
     * 第三方登录
     * <p/>
     * 1.授权 2.获取信息
     */
    private void sinaLogin(final SHARE_MEDIA type) {
        mController.doOauthVerify(mContext, type, new UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                ProgressDialog.getInstance().cancel();

                ToastUtil.showLongToast(e.getMessage() + "");
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {

                if (value != null) {
                    // 授权成功
                    Log.e("bundle", value.toString());

                    String uid = value.getString("uid");

                    if (type == SHARE_MEDIA.WEIXIN) {
                        uid = value.getString("unionid");
                    }

                    getInformation(platform, uid);

                } else {
                    ToastUtil.showShortToast("授权失败");

                    ProgressDialog.getInstance().cancel();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                ToastUtil.showShortToast("取消授权");

                ProgressDialog.getInstance().cancel();
            }

            @Override
            public void onStart(SHARE_MEDIA platform) {
            }
        });
    }

    private void getInformation(final SHARE_MEDIA type, final String uid) {

        mController.getPlatformInfo(mContext, type, new UMDataListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                String userId = uid;

                if (status == 200 && info != null) {

                    if (type == SHARE_MEDIA.WEIXIN) {
                        userId = info.get("unionid").toString();
                    }

                    String nickName;

                    if (type == SHARE_MEDIA.WEIXIN) {
                        nickName = info.get("nickname").toString();
                    } else {
                        nickName = info.get("screen_name").toString();
                    }

                    if (TextUtils.isEmpty(userId)) {
                        ToastUtil.showLongToast("获取第三方信息失败,请稍后重试");

                        ProgressDialog.getInstance().cancel();

                        return;
                    }

                    userLogin(userId, "", nickName);

                } else {
                    ToastUtil.showLongToast("第三方登录异常，请检查网络连接后重试");

                    ProgressDialog.getInstance().cancel();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public static void finishThis() {
        if (instance != null) {
            instance.finish();
        }
    }

}
