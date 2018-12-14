package com.nautilus.ywlfair.module.active;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.AppConfigDialog;
import com.nautilus.ywlfair.entity.bean.BoothApplicationInfo;
import com.nautilus.ywlfair.entity.request.GetActivityBoothApplicationConfigRequest;
import com.nautilus.ywlfair.entity.response.GetActivityBoothApplicationConfigResponse;
import com.nautilus.ywlfair.module.BaseActivity;

/**
 * 活动报名第一页
 * Created by Administrator on 2016/3/3.
 */


public class ActivitySignUpActivity1 extends BaseActivity implements View.OnClickListener, AppConfigDialog.LoginInputListener {
    private TextView appTitle, appTitleRight, ywlAmbassador, time, address, precautions, name, BoothPrice;
    private Context mContext;
    public static BoothApplicationInfo booth;
    public static String actid = "Caching" + GetUserInfoUtil.getUserInfo().getUserId() + "";
    private ImageView enterRemind;
    private TextView tvRoundno;

    private EditText nameEdit, phoneEdit, wxEdit;

    private GetActivityBoothApplicationConfigResponse boothConfig;

    private JsonUtil<BoothApplicationInfo> jsonUtil;

    public static ActivitySignUpActivity1 instance = null;

    private String roundId;

    private String actId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup_activity1);

        jsonUtil = new JsonUtil();

        instance = this;

        mContext = this;

        roundId = getIntent().getStringExtra(Constant.KEY.ROUND_ID);

        actId = getIntent().getStringExtra(Constant.KEY.ITEM_ID);

        View back = findViewById(R.id.img_back);
        back.setOnClickListener(this);
        appTitle = (TextView) findViewById(R.id.tv_title);
        appTitleRight = (TextView) findViewById(R.id.tv_right_btn);

        BoothPrice = (TextView) findViewById(R.id.booth_price);
        time = (TextView) findViewById(R.id.tv_start_end_time);
        name = (TextView) findViewById(R.id.tv_name);
        address = (TextView) findViewById(R.id.tv_activity_address);
        precautions = (TextView) findViewById(R.id.tv_precautions);
        nameEdit = (EditText) findViewById(R.id.ed_activity_one_name);
        phoneEdit = (EditText) findViewById(R.id.ed_activity_one_phone);
        wxEdit = (EditText) findViewById(R.id.ed_activity_one_wx);
        enterRemind = (ImageView) findViewById(R.id.enter_remind);
        ywlAmbassador = (TextView) findViewById(R.id.ywl_ambassador);
        tvRoundno = (TextView) findViewById(R.id.tv_roundno);

        ywlAmbassador.setTextIsSelectable(true);//设置可复制


        appTitleRight.setText("下一步");

        appTitleRight.setVisibility(View.VISIBLE);

        appTitleRight.setOnClickListener(this);

        appTitle.setText("活动报名");

        booth = jsonUtil.json2Bean(PreferencesUtil.getString(actid), BoothApplicationInfo.class.getName());

        booth.setRoundId(roundId);

        booth.setActivityId(actId);

        if (booth.getName().equals("")) {

        } else {
            nameEdit.setText(booth.getName());
            phoneEdit.setText(booth.getPhone());
            wxEdit.setText(booth.getWeixin());
        }

        getData();

    }

    @Override
    protected void onPause() {
        super.onPause();
        setData();
        PreferencesUtil.putString(actid, jsonUtil.bean2Json(booth));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:

                finish();
                break;
            case R.id.tv_right_btn:
                if (TextUtils.isEmpty(nameEdit.getText().toString())) {
                    ToastUtil.showShortToast("姓名不能为空");
                    return;
                }

                if (!StringUtils.isMobileNumber(phoneEdit.getText().toString())) {
                    ToastUtil.showShortToast("请输入正确的手机号");
                    return;
                }

                if (TextUtils.isEmpty(wxEdit.getText())) {
                    ToastUtil.showShortToast("微信号码不能为空");
                    return;
                }

                booth.setUserId(GetUserInfoUtil.getUserInfo().getUserId() + "");

                Bundle bundle = new Bundle();

                bundle.putSerializable(Constant.KEY.BOOTH_CONFIG, boothConfig);

                Intent intent = new Intent(mContext, ActivitySignUpActivity2.class);

                intent.putExtras(bundle);

                startActivity(intent);

                break;


        }

    }

    private void setData() {
        booth.setPhone(phoneEdit.getText().toString());

        booth.setName(nameEdit.getText().toString());

        booth.setWeixin(wxEdit.getText().toString());
    }

    private void getData() {
        GetActivityBoothApplicationConfigRequest request = new GetActivityBoothApplicationConfigRequest(
                actId, roundId, new ResponseListener<GetActivityBoothApplicationConfigResponse>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(GetActivityBoothApplicationConfigResponse response) {
            }

            @Override
            public void onResponse(GetActivityBoothApplicationConfigResponse response) {

                if (response == null || response.getResult().getActivityInfo() == null ||
                        response.getResult().getActivityBoothConfig() == null ||
                        response.getResult().getActivitySysConfig() == null) {

                    AppConfigDialog.getInstance().show(getFragmentManager(), null);

                    return;
                }

                boothConfig = response;

                time.setText("活动时间：" + TimeUtil.getYearMonthAndDay(Long.valueOf(response.getResult().getActivityInfo().getStartdate())) + "-" +
                        TimeUtil.getYearMonthAndDay(Long.valueOf(response.getResult().getActivityInfo().getEnddate())));
                address.setText("活动地址：" + response.getResult().getActivityBoothConfig().getAddrMsg());
                precautions.setText("注意事项：" + response.getResult().getActivityBoothConfig().getAttentionMsg());
                name.setText(response.getResult().getActivityInfo().getName());
                ywlAmbassador.setText("联系方式：" + response.getResult().getActivityBoothConfig().getWeixinMsg());
                BoothPrice.setText("摊位价格：" + response.getResult().getActivityBoothConfig().getFeeMsg());
                tvRoundno.setText("活动场次：" + response.getResult().getActivityBoothConfig().getRoundNo());

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                AppConfigDialog.getInstance().show(getFragmentManager(), null);
            }

            @Override
            public void onFinish() {

            }

        });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onLoginInputComplete(int type) {

        getData();

    }
}
