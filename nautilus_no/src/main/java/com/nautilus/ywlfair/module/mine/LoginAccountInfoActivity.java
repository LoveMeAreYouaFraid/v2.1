package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.cache.CacheUserInfo;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.PushManager;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.bean.UserMainEventItem;
import com.nautilus.ywlfair.entity.request.DeleteAccessTokenRequest;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.launch.BindPhone;
import com.nautilus.ywlfair.module.launch.ModifyPassword;
import com.nautilus.ywlfair.module.main.MainActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;


public class LoginAccountInfoActivity extends BaseActivity implements OnClickListener {

    private Context mContext;

    private String[] signType = {"", "QQ绑定登录", "微博绑定登录", "微信绑定登录", "微信绑定登录"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_account_information);

        UserInfo userInfo = GetUserInfoUtil.getUserInfo();

        mContext = this;

        findViewById(R.id.img_back).setOnClickListener(this);

        findViewById(R.id.bt_out).setOnClickListener(this);

        findViewById(R.id.user_handle_phone).setOnClickListener(this);

        LinearLayout changePwd = (LinearLayout) findViewById(R.id.ll_change_pwd);
        changePwd.setOnClickListener(this);
        TextView loginAccount = (TextView) findViewById(R.id.tv_login_account);
        loginAccount.setText(PreferencesUtil
                .getString(Constant.PRE_KEY.USER_NAME));

        bundlePhone = (TextView) findViewById(R.id.tv_bundle_phone);

        showIsBindPhone();


        if (userInfo.getThirdPartyFlag() != 0) {
            changePwd.setVisibility(View.GONE);

            loginAccount.setText(signType[userInfo.getThirdPartyFlag()]);
        } else {

            if (!TextUtils.isEmpty(userInfo.getEmail())) {
                loginAccount.setText(userInfo.getEmail());
            } else {
                loginAccount.setText(userInfo.getPhone());
            }

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;
            case R.id.bt_out:

                PushManager.unBindPushAccount(-1, "");

                loginOut();

                break;

            case R.id.ll_change_pwd:
                Intent intent = new Intent(this, ModifyPassword.class);
                startActivity(intent);
                break;

            case R.id.user_handle_phone:
                Intent bindIntent = new Intent(this, BindPhone.class);

                if (isBindPhone) {
                    bindIntent.putExtra(Constant.KEY.MODE, BindPhone.Mode.CHANGE_BIND);

                    startActivityForResult(bindIntent, Constant.REQUEST_CODE.CHANGE_PHONE);

                } else {
                    bindIntent.putExtra(Constant.KEY.MODE, BindPhone.Mode.BIND);

                    startActivityForResult(bindIntent, Constant.REQUEST_CODE.BIND_PHONE);
                }

                break;
        }

    }

    private void loginOut() {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        DeleteAccessTokenRequest request = new DeleteAccessTokenRequest(userId, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在退出...");
            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if (response == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                MyApplication.getInstance().setCurrentUser(null);

                MyApplication.getInstance().setLogin(false);

                PushManager.bindPushAccount();

                CacheUserInfo cacheUserInfo = new CacheUserInfo();
                cacheUserInfo.clearData();

                ToastUtil.showShortToast("注销登录成功");

                MyApplication.getInstance().setUserType(0);

                Intent mIntent = new Intent(Constant.BROADCAST.EXCHANGE_USER);

                mContext.sendBroadcast(mIntent);

                UserMainEventItem item = new UserMainEventItem();

                item.setType(3);

                EventBus.getDefault().post(item);

                Intent intent = new Intent(mContext, MainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
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

    private boolean isBindPhone;

    private void showIsBindPhone() {

        if (!StringUtils.isMobileNumber(GetUserInfoUtil.getUserInfo()
                .getBindPhone())) {

            isBindPhone = false;

        } else {
            bundlePhone.setText(GetUserInfoUtil.getUserInfo().getBindPhone() + "");

            isBindPhone = true;

        }
    }

    private TextView bundlePhone;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE.BIND_PHONE ||
                    requestCode == Constant.REQUEST_CODE.CHANGE_PHONE) {

                showIsBindPhone();
            }
        }
    }


}
