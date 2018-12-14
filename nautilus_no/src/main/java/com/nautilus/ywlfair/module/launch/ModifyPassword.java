package com.nautilus.ywlfair.module.launch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.request.PostResetPasswordRequest;
import com.nautilus.ywlfair.entity.request.PutPasswordRequest;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

/**
 * 修改密码界面
 */
public class ModifyPassword extends BaseActivity implements OnClickListener {

    private EditText oldPwdInput, newPwdInput, secondPwdInput;

    private Context mContext;

    private Intent intent;

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.modify_password);

        mContext = this;

        intent = getIntent();

        findViewById(R.id.img_back).setOnClickListener(this);

        oldPwdInput = (EditText) findViewById(R.id.ed_old_psw);

        newPwdInput = (EditText) findViewById(R.id.ed_new_psw);

        secondPwdInput = (EditText) findViewById(R.id.et_second_new_psd);

        findViewById(R.id.bt_ok).setOnClickListener(this);

        if (intent.hasExtra("phone")) {

            findViewById(R.id.ll_old_password).setVisibility(View.GONE);

            type = 2;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.bt_ok:

                String oldPwd = oldPwdInput.getText().toString();

                String newPwd = newPwdInput.getText().toString();

                String secondPwd = secondPwdInput.getText().toString().trim();

                if (TextUtils.isEmpty(newPwd)) {
                    ToastUtil.showShortToast("请输入新密码");

                    return;
                }

                if (!newPwd.equals(secondPwd)) {
                    ToastUtil.showShortToast("两次密码输入不一致");

                    return;
                }

                if (type == 0) {

                    if (TextUtils.isEmpty(oldPwd)) {

                        ToastUtil.showShortToast("请填写当前密码");

                        return;
                    }

                    changePassword(oldPwd, newPwd);

                    break;

                } else if (type == 2) {

                    String string = newPwdInput.getText().toString();

                    if(!TextUtils.isEmpty(string)){

                        sendpsw(intent.getStringExtra("phone"), string);
                    }else {
                        ToastUtil.showLongToast("请输入新密码");
                    }

                }
        }

    }

    private void changePassword(String oldPwd, String newPwd) {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        PutPasswordRequest request = new PutPasswordRequest(userId, oldPwd, newPwd,
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
                            Toast.makeText(MyApplication.getInstance(), "密码修改成功", Toast.LENGTH_SHORT).show();

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


    private void sendpsw(String phone, String psw) {
        PostResetPasswordRequest postResetPasswordRequest = new PostResetPasswordRequest
                (phone, psw, new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在修改...");
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
                        Intent main = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(main);
                        ToastUtil.showLongToast("修改密码成功");
                        finish();

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();
                            ToastUtil.showLongToast(response.getMessage());

                        } else {
                            Toast.makeText(MyApplication.getInstance(), "获取数据失败，请您稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFinish() {
                        ProgressDialog.getInstance().cancel();

                    }
                });
        postResetPasswordRequest.setShouldCache(false);
        VolleyUtil.addToRequestQueue(postResetPasswordRequest);
    }

}
