package com.nautilus.ywlfair.module.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.nautilus.ywlfair.dialog.DetermineCancelDialog;
import com.nautilus.ywlfair.entity.request.PostInvitationInfoRequest;
import com.nautilus.ywlfair.entity.response.PostInvitationInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.launch.BindPhone;
import com.nautilus.ywlfair.widget.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/28.
 */

public class InvitationCodeActivity extends BaseActivity implements DetermineCancelDialog.DialogListener {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.ed_code)
    EditText edCode;
    @BindView(R.id.tv_determine)
    TextView tvDetermine;
    @BindView(R.id.tv_user_txt)
    TextView tvUserTxt;
    @BindView(R.id.layout_ail)
    LinearLayout layoutAil;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.invitation_code_activity);

        ButterKnife.bind(this);

        tvTitle.setText("请输入邀请码");

        mContext = this;

        if (!TextUtils.isEmpty(GetUserInfoUtil.getUserInfo().getHasInputInvitationCode())) {

            if (GetUserInfoUtil.getUserInfo().getHasInputInvitationCode().equals("1")) {

                layoutAil.setVisibility(View.GONE);

                findViewById(R.id.layout_user_txt).setVisibility(View.VISIBLE);

                tvUserTxt.setText(GetUserInfoUtil.getUserInfo().getInvitationVendorNickname());

            }
        }
    }

    @OnClick({R.id.img_back, R.id.ed_code, R.id.tv_determine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ed_code:

                break;
            case R.id.tv_determine:

                if (TextUtils.isEmpty(edCode.getText() + "")) {
                    ToastUtil.showShortToast("邀请码不能为空");
                    return;
                }

                if (TextUtils.isEmpty(GetUserInfoUtil.getUserInfo().getBindPhone())) {

                    DetermineCancelDialog.getInstance().showDialog(mContext, "输入邀请码前，请先绑定手机号码 ", "立即绑定", "取消");
                } else {

                    PostInvitationInfo(edCode.getText() + "");
                }

                break;
        }
    }

    @Override
    public void onItemChoice(int position) {
        switch (position) {
            case 0:
                Intent bindIntent = new Intent(this, BindPhone.class);
                bindIntent.putExtra(Constant.KEY.MODE, BindPhone.Mode.BIND);
                startActivityForResult(bindIntent, Constant.REQUEST_CODE.BIND_PHONE);
//
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE.BIND_PHONE) {
                ToastUtil.showShortToast("绑定成功");
            }

        }
    }

    private void PostInvitationInfo(String invitationCode) {
        PostInvitationInfoRequest request = new PostInvitationInfoRequest(GetUserInfoUtil.getUserInfo().getUserId() + "",
                invitationCode, new ResponseListener<PostInvitationInfoResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在提交...");
            }

            @Override
            public void onCacheResponse(PostInvitationInfoResponse response) {

            }

            @Override
            public void onResponse(PostInvitationInfoResponse response) {
                if(response != null){

                    ToastUtil.showLongToast("邀请成功");

                    String nickName = response.getResult().getInvitationVendorNickname();

                    MyApplication.getInstance().getCurrentUser().setHasInputInvitationCode("1");

                    MyApplication.getInstance().getCurrentUser().setInvitationVendorNickname(nickName);

                    findViewById(R.id.layout_user_txt).setVisibility(View.VISIBLE);

                    layoutAil.setVisibility(View.GONE);

                    tvUserTxt.setVisibility(View.VISIBLE);

                    tvUserTxt.setText(nickName);

                    setResult(RESULT_OK);
                }

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();

                    ToastUtil.showLongToast(response.getMessage());

                } else {
                    ToastUtil.showLongToast("获取数据失败，请您稍后重试");
                }


            }

            @Override
            public void onFinish() {
                ProgressDialog.getInstance().cancel();
            }
        }

        );
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }
}