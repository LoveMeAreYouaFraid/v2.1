package com.nautilus.ywlfair.module.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.ShowShareMenuUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.entity.request.GetInvitationInfoRequest;
import com.nautilus.ywlfair.entity.request.GetShareInvitationCodeInfoRequest;
import com.nautilus.ywlfair.entity.response.GetInvitationInfoResponse;
import com.nautilus.ywlfair.entity.response.GetShareInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/28.
 */

public class InviteFriendsActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.tv_invitation_code)
    TextView tvInvitationCode;
    @BindView(R.id.tv_friends)
    TextView tvFriends;
    @BindView(R.id.tv_old_user)
    TextView tvOldUser;

    @BindView(R.id.iv_ywl)
    AutoAdjustHeightImageView ivYwl;
    @BindView(R.id.iv_ywl_cover)
    AutoAdjustHeightImageView ivYwlCover;

    private Context mContext;

    private DisplayImageOptions options = ImageLoadUtils.createNoRoundedOptions();

    private ActiveShareInfo activeShareInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friends_activity);
        ButterKnife.bind(this);
        mContext = this;
        tvTitle.setText("邀请好友");
        activeShareInfo = new ActiveShareInfo();
        GetShareInvitationCodeInfo();
        GetInvitationInfo();
    }


    @OnClick({R.id.img_back, R.id.tv_friends, R.id.tv_old_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_friends:
                if (TextUtils.isEmpty(activeShareInfo.getActDesc()) || TextUtils.isEmpty(activeShareInfo.getContentUrl()) ||
                        TextUtils.isEmpty(activeShareInfo.getActImgUrl())) {
                    ToastUtil.showLongToast("请稍后重试");
                    return;
                }
                ShowShareMenuUtil.getInstance().initShareMenuDialog(this, activeShareInfo);
                ShowShareMenuUtil.setQQZone();
                ShowShareMenuUtil.setWeixinCircleGon();
                break;
            case R.id.tv_old_user:
                startActivity(new Intent(mContext, InvitationCodeActivity.class));
                break;
        }
    }

    private void GetShareInvitationCodeInfo() {
        GetShareInvitationCodeInfoRequest request = new GetShareInvitationCodeInfoRequest(GetUserInfoUtil.getUserInfo().getUserId() + "",
                new ResponseListener<GetShareInfoResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "Loding....");
                    }

                    @Override
                    public void onCacheResponse(GetShareInfoResponse response) {

                    }

                    @Override
                    public void onResponse(GetShareInfoResponse response) {
                        if (response == null || response.getResult() == null || response.getResult().getActiveShareInfo() == null) {
                            ToastUtil.showLongToast("请稍后重试");
                            return;
                        }
                        ActiveShareInfo info = response.getResult().getActiveShareInfo();
                        activeShareInfo.setTitle(info.getTitle());
                        activeShareInfo.setActDesc(info.getDesc());
                        activeShareInfo.setActImgUrl(info.getImageUrl());
                        activeShareInfo.setContentUrl(info.getUrl());

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

    private void GetInvitationInfo() {
        GetInvitationInfoRequest request = new GetInvitationInfoRequest(GetUserInfoUtil.getUserInfo().getUserId() + "",
                new ResponseListener<GetInvitationInfoResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "Loding....");
                    }

                    @Override
                    public void onCacheResponse(GetInvitationInfoResponse response) {


                    }

                    @Override
                    public void onResponse(GetInvitationInfoResponse response) {
                        if (response == null || response.getResult() == null || response.getResult().getInvitationInfo() == null) {
                            ToastUtil.showLongToast("获取数据失败，请您稍后重试");
                            return;
                        }

                        ImageLoader.getInstance().displayImage(response.getResult().getInvitationInfo().getInvitationDescImageUrl(),
                                ivYwl, options);
                        ImageLoader.getInstance().displayImage(response.getResult().getInvitationInfo().getJobDescImageUrl(),
                                ivYwlCover, options);
                        String code = "邀请码：" + "<font color='#f5703f'>" + response.getResult().getInvitationInfo().getInvitationCode() + "</font>";
                        tvInvitationCode.setText(Html.fromHtml(code));


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
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }
}
