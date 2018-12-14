package com.nautilus.ywlfair.module.mine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.ShareUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.entity.bean.SignInfo;
import com.nautilus.ywlfair.entity.request.GetSignInfoRequest;
import com.nautilus.ywlfair.entity.request.PostUserSignRequest;
import com.nautilus.ywlfair.entity.response.PostUserSignResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

public class MySignActivity extends BaseActivity implements OnClickListener {

    private Context mContext;

    private SignInfo mySignInfo;

    private String itemId;

    private TextView confirm;

    private LatLng activityGps;

    private TextView signRule, signedAddressView;

    private String checkLocation;

    private String userId;

    private TextView msgNum, msg;

    private ActiveShareInfo activeShareInfo;

    private LatLng userGps;

    private View bgLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.my_signin);

        activeShareInfo = (ActiveShareInfo) getIntent().getSerializableExtra(Constant.KEY.SHARE_INFO);

        if (activeShareInfo == null) {
            activeShareInfo = new ActiveShareInfo();
        }

        activeShareInfo.setTitle("鹦鹉螺市集签到");

        getView();

        getSignInfo();

    }


    /**
     * 提交签到
     */
    private void confirmSign() {

        String userGps = MyApplication.getInstance().getLatitude() + "" + "," + MyApplication.getInstance().getLongitude() + "";

        PostUserSignRequest request = new PostUserSignRequest(userId, mySignInfo.getSignDesc(),
                MyApplication.getInstance().getLocationDescription(), userGps, itemId,
                new ResponseListener<PostUserSignResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在提交签到...");
                    }

                    @Override
                    public void onCacheResponse(PostUserSignResponse response) {
                    }

                    @Override
                    public void onResponse(PostUserSignResponse response) {
                        if (response == null || response.getResult().getSignInfo() == null) {
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                            return;
                        }

                        ToastUtil.showShortToast("签到成功");

                        getSignInfo();
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
     * 获取签到信息  初始化显示
     */
    private void getSignInfo() {

        GetSignInfoRequest request = new GetSignInfoRequest(userId, itemId, new ResponseListener<PostUserSignResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(PostUserSignResponse response) {

            }

            @Override
            public void onResponse(PostUserSignResponse response) {
                if (response == null || response.getResult().getSignInfo() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }

                mySignInfo = response.getResult().getSignInfo();

                activityGps = StringUtils.gps(mySignInfo.getActivityInfo().getAddrMap());// 活动经纬度

                msgNum.setText("您已累计签到" + mySignInfo.getSignNum() + "" + "次");

                signedAddressView.setText("活动地点：" + mySignInfo.getActivityInfo().getAddress());

                signRule.setText(mySignInfo.getSignRule());

                checkLocation = TextUtils.isEmpty(mySignInfo.getSignCheckLocation()) ? "0" : mySignInfo.getSignCheckLocation();

                LogUtil.e("签到距离", checkLocation);

                msg.setVisibility(View.VISIBLE);

                //活动状态，0正在进行；1已结束;2即将开始
                //"activityStatus": 0,
                if (mySignInfo.getHasSigned() == 0) {//是否签到
                    confirm.setText("我要\n签到");
                    switch (mySignInfo.getActivityInfo().getActivityStatus()) {
                        case 0:
                            if (DistanceUtil.getDistance(userGps, activityGps) > Double.valueOf(checkLocation)) {
                                bgLayout.setBackgroundResource(R.drawable.no_sign);
                                msg.setText("请您靠近活动现场");
                                confirm.setEnabled(false);
                            } else {
                                confirm.setEnabled(true);
                            }
                            msg.setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            msg.setText("活动已结束");
                            confirm.setEnabled(false);
                            bgLayout.setBackgroundResource(R.drawable.no_sign);
                            break;
                        case 2:
                            msg.setText("活动尚未开始");
                            confirm.setEnabled(false);
                            bgLayout.setBackgroundResource(R.drawable.no_sign);
                            break;
                    }
                } else {
                    confirm.setText("您已\n签到");
                    msg.setVisibility(View.INVISIBLE);
                    confirm.setEnabled(false);
                    bgLayout.setBackgroundResource(R.drawable.sign_has);

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

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    private void getView() {

        userGps = new LatLng(MyApplication.getInstance().getLatitude(), MyApplication.getInstance().getLongitude());// 用户当前经纬度

        userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        mContext = this;

        msgNum = (TextView) findViewById(R.id.sign_msg);

        msg = (TextView) findViewById(R.id.sign_msg1);

        confirm = (TextView) findViewById(R.id.bt_confirm);

        signedAddressView = (TextView) findViewById(R.id.tv_address);

        signRule = (TextView) findViewById(R.id.tv_sign_rule);

        itemId = getIntent().getStringExtra(Constant.KEY.ITEM_ID);

        View topBarBack = findViewById(R.id.iv_top_bar_back);
        topBarBack.setOnClickListener(this);

        bgLayout = findViewById(R.id.bg_layout);

        confirm.setOnClickListener(this);

        confirm.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                if (activityGps == null) {
                    ToastUtil.showShortToast("活动地址未配置,无法签到");
                    return;
                }

                initShareMenuDialog();

                shareMenuDialog.show();

                break;

            case R.id.iv_top_bar_back:
                finish();
                break;

            case R.id.share_to_weixin:
                // 分享到微信好友
                confirmSign();
                ShareUtil
                        .shareToWechat(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl());
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_to_weixin_circle:
                // 分享到微信朋友圈
                confirmSign();
                ShareUtil
                        .shareToWxCircle(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl());
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_to_qq:
                // 分享到QQ
                confirmSign();
                ShareUtil
                        .shareToQQ(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl());
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_to_sina:
                // 分享到新浪微博
                confirmSign();
                ShareUtil
                        .shareToSina(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl(), "");
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;
            case R.id.share_to_qq_zone:
                // 分享到QQ空间
                confirmSign();
                ShareUtil
                        .shareToQZone(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl(), "");
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();
                break;

            case R.id.share_to_douban:
                // 分享到豆瓣
                ShareUtil
                        .shareToDouban(
                                (Activity) mContext,
                                activeShareInfo.getTitle(),
                                activeShareInfo.getActDesc(),
                                activeShareInfo.getActImgUrl(),
                                activeShareInfo.getContentUrl());
                if (shareMenuDialog.isShowing())
                    shareMenuDialog.cancel();

                confirmSign();

                break;

            case R.id.share_cancel:
                // 取消分享
                shareMenuDialog.cancel();
                break;

        }
    }

    /**
     * @return void 返回类型
     * @Title: 分享dialog
     */
    private Dialog shareMenuDialog;

    public void initShareMenuDialog() {

        if (shareMenuDialog != null) {
            return;
        }
        View view = View.inflate(this, R.layout.share, null);
        /* 设置透明度 */
        // view.getBackground().setAlpha(100);
        shareMenuDialog = new Dialog(this, R.style.share_dialog);
        /* 设置视图 */
        shareMenuDialog.setContentView(view);
        /* 设置宽 高 */
        shareMenuDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        Window window = shareMenuDialog.getWindow();
        /* 设置位置 */
        window.setGravity(Gravity.BOTTOM);
        /* 设置动画 */
        window.setWindowAnimations(R.style.umeng_socialize_shareboard_animation);
        /* 拿到Dialog里面的图标 并添加监听 */
        RelativeLayout toWeixin = (RelativeLayout) view
                .findViewById(R.id.share_to_weixin);
        RelativeLayout toQqZone = (RelativeLayout) view
                .findViewById(R.id.share_to_qq_zone);
        RelativeLayout toSina = (RelativeLayout) view
                .findViewById(R.id.share_to_sina);
        RelativeLayout toWeixinCircle = (RelativeLayout) view
                .findViewById(R.id.share_to_weixin_circle);
        RelativeLayout toQQ = (RelativeLayout) view
                .findViewById(R.id.share_to_qq);
        RelativeLayout toDouban = (RelativeLayout) view
                .findViewById(R.id.share_to_douban);
        TextView shareCancel = (TextView) view.findViewById(R.id.share_cancel);
        toWeixin.setOnClickListener(this);
        toSina.setOnClickListener(this);
        toWeixinCircle.setOnClickListener(this);
        toQQ.setOnClickListener(this);
        shareCancel.setOnClickListener(this);
        toQqZone.setOnClickListener(this);
        toDouban.setOnClickListener(this);

    }
}
