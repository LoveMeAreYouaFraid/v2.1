package com.nautilus.ywlfair.module.mine.level;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.request.GetQrCodeRequest;
import com.nautilus.ywlfair.entity.response.GetQrCodeResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.wallet.MoneyDetailActivity;
import com.nautilus.ywlfair.module.webview.UseHelpActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/23.
 */
public class GetMoneyCodeActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    @BindView(R.id.tv_check_record)
    View checkRecord;

    @BindView(R.id.tv_top_bar_back)
    View backView;

    @BindView(R.id.iv_top_bar_right)
    View rightView;

    @BindView(R.id.iv_qr_code)
    AutoAdjustHeightImageView imageView;

    @BindView(R.id.iv_header)
    ImageView headerView;

    @BindView(R.id.tv_user_name)
    TextView userNameView;

    @BindView(R.id.tv_default)
    TextView defaultView;

    private GetQrCodeResponse.OfflinePayQrCode offlinePayQrCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.get_money_code_activity);

        ButterKnife.bind(this);

        mContext = this;

        backView.setOnClickListener(this);

        rightView.setOnClickListener(this);

        checkRecord.setOnClickListener(this);

        if (!TextUtils.isEmpty(GetUserInfoUtil.getUserInfo().getAvatar())) {

            ImageLoadUtils.setRoundHeadView(headerView,
                    GetUserInfoUtil.getUserInfo().getAvatar(), R.drawable.default_avatar, 120);

        }

        userNameView.setText(GetUserInfoUtil.getUserInfo().getNickname());

        String htmlString = "使用<font color='#EC6432'>支付宝</font>或者<font color='#EC6432'>微信</font>扫码向我付款";

        defaultView.setText(Html.fromHtml(htmlString));

        getData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.iv_top_bar_right:
                Intent helpIntent = new Intent(mContext, UseHelpActivity.class);

                helpIntent.putExtra(Constant.KEY.URL, offlinePayQrCode.getOfflinePayHelpUrl());

                startActivity(helpIntent);

                break;

            case R.id.tv_check_record:
                Intent intent = new Intent(mContext, MoneyDetailActivity.class);

                intent.putExtra(Constant.KEY.TYPE, 1);

                startActivity(intent);

                break;
        }
    }

    private void getData(){

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetQrCodeRequest request = new GetQrCodeRequest(userId,new ResponseListener<GetQrCodeResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "载入中...");
            }

            @Override
            public void onCacheResponse(GetQrCodeResponse response) {

            }

            @Override
            public void onResponse(GetQrCodeResponse response) {
                if(response != null && response.getResult().getOfflinePayQrCode() != null){
                    offlinePayQrCode = response.getResult().getOfflinePayQrCode();

                    ImageLoader.getInstance().displayImage(offlinePayQrCode.getQrCodeUrl(), imageView, ImageLoadUtils.createNoRoundedOptions());

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
}
