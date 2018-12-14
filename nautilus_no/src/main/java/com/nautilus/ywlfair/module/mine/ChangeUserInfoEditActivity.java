package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.PutUserInfoRequest;
import com.nautilus.ywlfair.entity.response.PutUserInfoResponse;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

public class ChangeUserInfoEditActivity extends BaseActivity implements OnClickListener {

    private Context mContext;

    public enum Mode {
        NAME, SIGN, PHONE
    }

    int countChinese;
    int abc;
    int num;
    private String oldInputString = "";

    private TextView topBarBack;

    private String nickyName;

    private EditText inputView;

    private TextView tipView;

    private Mode mode;

    private String defaultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_user_info_edit_activity);

        mContext = this;

        mode = (Mode) getIntent().getSerializableExtra(Constant.KEY.MODE);

        defaultText = getIntent().getStringExtra(Constant.KEY.DEFAULT_TEXT);

        topBarBack = (TextView) findViewById(R.id.tv_top_bar_back);
        topBarBack.setOnClickListener(this);

        inputView = (EditText) findViewById(R.id.et_input);

        tipView = (TextView) findViewById(R.id.tv_tip);

        inputView.setText(defaultText);

        View confirm = findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(this);

        initViews();

    }

    private void initViews() {
        if (mode == Mode.NAME) {

            inputView.setHint("不超过18个字符");

            topBarBack.setText("更改昵称");

            tipView.setText("好的昵称可以让你的朋友更容易记住你！");

            inputView.addTextChangedListener(
                    new TextWatcher() {

                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            nickyName = inputView.getText().toString();

                            countChinese = StringUtils.countChinese(nickyName);

                            abc = StringUtils.countLetter(nickyName);

                            num = StringUtils.countNumber(nickyName);


                            if (num + abc + countChinese > 18) {
                                inputView.setText(oldInputString);

                                inputView.setSelection(oldInputString.length());

                                ToastUtil.showShortToast("昵称长度超过限制");
                            } else {
                                oldInputString = nickyName;
                            }


                        }
                    }

            );

        } else if (mode == Mode.SIGN) {

            inputView.setHint("不超过30个字");

            topBarBack.setText("更改签名");

            tipView.setText("留下你的心情吧！");

            inputView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String string = inputView.getText().toString();

                    if (string.length() > 30) {
                        ToastUtil.showShortToast("签名不能超过30个字");

                        string = string.substring(0, 30);

                        inputView.setText(string);

                        inputView.setSelection(string.length());
                    }
                }
            });
        }else if(mode == Mode.PHONE){
            inputView.setHint("请输入手机号码");

            topBarBack.setText("更改号码");

            tipView.setText("支付成功后相关信息将发送至此号码！");

            inputView.setInputType(InputType.TYPE_CLASS_PHONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.tv_confirm:

                if(mode == Mode.PHONE){

                    String phone = inputView.getText().toString().trim();

                    if(!StringUtils.isMobileNumber(phone)){
                        ToastUtil.showShortToast("请输入正确的手机号码");
                        return;
                    }

                    Intent intent = new Intent();

                    intent.putExtra(Constant.KEY.PHONE, phone);

                    setResult(RESULT_OK, intent);

                    finish();
                }else{
                    confirmChange();
                }

                break;
        }
    }

    private void confirmChange() {

        UserInfo userInfo = GetUserInfoUtil.getUserInfo();
        if (mode == Mode.NAME) {

            String nickName = inputView.getText().toString();

            if (TextUtils.isEmpty(nickName) || defaultText.equals(nickName)) {
                Toast.makeText(MyApplication.getInstance(), "请输入新的昵称",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            changeInfo(userInfo.getUserId() + "", userInfo.getAvatarPath(), nickName,
                    userInfo.getSex() + "", userInfo.getCity(), userInfo.getSignature());
        } else if (mode == Mode.SIGN) {
            String sign = inputView.getText().toString();

            if (TextUtils.isEmpty(sign) || defaultText.equals(sign)) {
                Toast.makeText(MyApplication.getInstance(), "请输入新的个性签名",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            changeInfo(userInfo.getUserId() + "", userInfo.getAvatarPath(), userInfo.getNickname(),
                    userInfo.getSex() + "", userInfo.getCity(), sign);
        }

    }

    private void changeInfo(String userId, final String avatarPath, final String nickName, final String sex, final String city, final String signature) {

        PutUserInfoRequest request = new PutUserInfoRequest(userId, avatarPath, nickName, sex, city, signature,
                new ResponseListener<PutUserInfoResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "提交中...");
                    }

                    @Override
                    public void onCacheResponse(PutUserInfoResponse response) {

                    }

                    @Override
                    public void onResponse(PutUserInfoResponse response) {
                        if (response == null || response.getResult().getUserInfo() == null) {
                            ToastUtil.showShortToast("操作失败,请检查网络连接");
                            return;
                        }
                        Toast.makeText(MyApplication.getInstance(), "修改成功",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        if (mode == Mode.NAME) {
                            intent.putExtra(Constant.KEY.NAME, inputView.getText()
                                    .toString());
                        } else if (mode == Mode.SIGN) {
                            GetUserInfoUtil.getUserInfo().setSignature(inputView.getText().toString());
                        }
                        setResult(RESULT_OK, intent);

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

}
