package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jungly.gridpasswordview.GridPasswordView;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.OnUserSelectListener;
import com.nautilus.ywlfair.dialog.ShowPasswordErrorDialog;
import com.nautilus.ywlfair.entity.request.GetPayAuthorizationRequest;
import com.nautilus.ywlfair.entity.request.PostSetPayPasswordRequest;
import com.nautilus.ywlfair.entity.request.PutPayPasswordRequest;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

/**
 * Created by Administrator on 2016/6/18.
 * <p/>
 * 如果包含type== 1 则为修改支付密码
 */
public class SetPayPasswordActivity extends BaseActivity implements View.OnClickListener, OnUserSelectListener {

    private Context mContext;

    private Button confirmButton;

    private TextView name;

    private TextView titleView;

    private GridPasswordView gridPasswordView;

    private boolean isFirstInput = true;

    private String firstPassword, secondPassword;

    private int type;//1 修改密码 0设置密码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_pay_password_activity);

        mContext = this;

        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);

        type = getIntent().getIntExtra(Constant.KEY.TYPE, 0);

        confirmButton = (Button) findViewById(R.id.bt_confirm);

        name = (TextView) findViewById(R.id.tv_name);

        titleView = (TextView) findViewById(R.id.tv_title);

        confirmButton.setOnClickListener(this);

        if (type == 1) {
            titleView.setText("修改支付密码");

            name.setText("输入旧的支付密码以验证身份");

        } else {

        }

        gridPasswordView = (GridPasswordView) findViewById(R.id.password_input);

        gridPasswordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                confirmButton.setEnabled(false);
            }

            @Override
            public void onInputFinish(String psw) {
                if (isFirstInput) {
                    firstPassword = psw;

                    confirmButton.setText("下一步");

                    confirmButton.setEnabled(true);

                } else {

                    secondPassword = psw;

                    confirmButton.setText("确定");

                    confirmButton.setEnabled(true);

                    if (type == 1) {

                        confirmButton.setEnabled(true);
                    } else {
                        if (secondPassword.equals(firstPassword)) {
                            confirmButton.setEnabled(true);

                        } else {
                            ToastUtil.showLongToast("两次输入密码不一致，请重新设置");

                            firstPassword = "";

                            secondPassword = "";

                            isFirstInput = true;

                            name.setText("设置6位数字支付密码");

                            gridPasswordView.clearPassword();

                            confirmButton.setEnabled(false);
                        }
                    }
                }
            }
        });

        showKeyBoard();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.bt_confirm:
                changeConfirmStatus();
                break;
        }
    }

    private void changeConfirmStatus() {
        if (isFirstInput) {
            isFirstInput = false;

            if (type == 1) {

                getPayAuthorization();//修改密码 先验证第一次输入的旧密码是否正确

            } else {

                name.setText("再次输入支付密码");
            }

            gridPasswordView.clearPassword();

            confirmButton.setEnabled(false);

        } else {
            if (type == 1) {
                putPayPassword();
            } else {
                setPayPassword();
            }
        }
    }

    private void setPayPassword() {
        int userId = GetUserInfoUtil.getUserInfo().getUserId();

        PostSetPayPasswordRequest request = new PostSetPayPasswordRequest(userId, secondPassword, new ResponseListener<InterfaceResponse>() {
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

                    MyApplication.getInstance().getCurrentUser().setHasPayPassword(1);

                    Intent intent = new Intent(mContext, PayPasswordStatusActivity.class);

                    startActivity(intent);

                    setResult(RESULT_OK);

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

                confirmButton.setVisibility(View.GONE);
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void putPayPassword() {
        int userId = GetUserInfoUtil.getUserInfo().getUserId();

        PutPayPasswordRequest request = new PutPayPasswordRequest(userId + "", firstPassword, secondPassword,
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
                            ToastUtil.showLongToast("密码修改成功");

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

                        confirmButton.setVisibility(View.GONE);
                    }
                });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void getPayAuthorization() {

        int userId = GetUserInfoUtil.getUserInfo().getUserId();

        GetPayAuthorizationRequest request = new GetPayAuthorizationRequest(userId, firstPassword,
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在验证...");
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            name.setText("请输入新支付密码");

                            showKeyBoard();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        firstPassword = "";

                        isFirstInput = true;

                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            if (response.getMessage().contains("冻结")) {
                                ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, response.getMessage(), 2);

                                return;
                            }
                            ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, response.getMessage(), 1);

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

    private void showKeyBoard() {
        gridPasswordView.postDelayed(new Runnable() {
            @Override
            public void run() {
                gridPasswordView.performClick();
            }
        }, 300);
    }

    @Override
    public void onSelect(boolean isConfirm) {
        if (isConfirm) {
            showKeyBoard();
        }
    }
}
