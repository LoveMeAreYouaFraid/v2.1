package com.nautilus.ywlfair.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.cache.CacheUserInfo;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.ExchangeUserDialog;
import com.nautilus.ywlfair.entity.bean.GetVendorInfo;
import com.nautilus.ywlfair.entity.bean.Statistics;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.bean.UserMainEventItem;
import com.nautilus.ywlfair.entity.request.GetUserStatisticRequest;
import com.nautilus.ywlfair.entity.request.GetVendorInfoRequest;
import com.nautilus.ywlfair.entity.response.GetUserStatisticResponse;
import com.nautilus.ywlfair.entity.response.GetVendorInfoResponse;
import com.nautilus.ywlfair.module.booth.BoothDepositActivity;
import com.nautilus.ywlfair.module.booth.MyBoothActivity;
import com.nautilus.ywlfair.module.launch.LoginActivity;
import com.nautilus.ywlfair.module.market.InviteFriendsActivity;
import com.nautilus.ywlfair.module.market.OrderStallActivity;
import com.nautilus.ywlfair.module.mine.AllMsgActivity;
import com.nautilus.ywlfair.module.mine.ChangeUserInfoEditActivity;
import com.nautilus.ywlfair.module.mine.MissGoActivityListActivity;
import com.nautilus.ywlfair.module.mine.MyCommentsActivity;
import com.nautilus.ywlfair.module.mine.MyDiyActivity;
import com.nautilus.ywlfair.module.mine.MyOrderFormActivity;
import com.nautilus.ywlfair.module.mine.MySignListActivity;
import com.nautilus.ywlfair.module.mine.MyTicketsListActivity;
import com.nautilus.ywlfair.module.mine.UserInfoActivity;
import com.nautilus.ywlfair.module.mine.level.VendorLevelActivity;
import com.nautilus.ywlfair.module.mine.wallet.MyWalletActivity;
import com.nautilus.ywlfair.module.vendor.VendorInfoActivity;
import com.nautilus.ywlfair.module.vendor.VendorInfoCompletionActivity;
import com.nautilus.ywlfair.module.vendor.VendorVerifyActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserCenterFragment extends Fragment implements OnClickListener {

    @BindView(R.id.tv_deposit_type)
    TextView tvDepositType;
    @BindView(R.id.tv_vendor_info_type)
    TextView tvVendorInfoType;

    private int[] norIcons = {R.drawable.level_nor_0, R.drawable.level_nor_1, R.drawable.level_nor_2, R.drawable.level_nor_3,
            R.drawable.level_nor_4, R.drawable.level_nor_5, R.drawable.level_nor_6};

    private int[] lightIcons = {R.drawable.level0, R.drawable.level1, R.drawable.level2, R.drawable.level3, R.drawable.level4,
            R.drawable.level5, R.drawable.level6};

    private ImageView userHeader;

    private TextView userName;

    private TextView userSign;

    private UserInfo currentUser;

    private ImageView imageButtMsg;

    private Context mContext;

    private View rootView;

    private View officialTag;

    private View normalUserView, vendorUserView;

    private TextView exchangeUser;

    private View myTicket, mySign, myWantJoin;

    private boolean isExchange = false;

    private boolean isRequestingFinish = true;

    private boolean isTimeOut = true;

    private GetVendorInfo getVendorInfo;

    @BindView(R.id.ll_level_container)
    LinearLayout levelContainer;

    @BindView(R.id.iv_pre_level)
    ImageView preImage;

    @BindView(R.id.iv_current_level)
    ImageView currentImage;

    @BindView(R.id.iv_next_level)
    ImageView nextImage;

    @BindView(R.id.tv_num_privilege)
    TextView numPrivilege;

    @BindView(R.id.iv_level_progress)
    ImageView progressView;

    private String[] vendorDepositType = {"待缴纳", "已缴纳", "", ""};//不要删

    private String[] VendorInfoType = {"未认证", "已认证", "", ""};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.user_center_fragment, null);
        }

        mContext = getActivity();

        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    private void initView() {

        initRootView();

        if (MyApplication.getInstance().isLogin()) {

            setValue();

            getData(GetUserInfoUtil.getUserInfo().getUserId() + "");

            getVendorInfo();
        }
    }


    /**
     * 初始化界面控件
     */
    public void initRootView() {

        exchangeUser = (TextView) rootView.findViewById(R.id.tv_exchange_user);

        normalUserView = rootView.findViewById(R.id.ll_normal_user);

        vendorUserView = rootView.findViewById(R.id.ll_vendor_user);

        rootView.findViewById(R.id.tv_invite_friends).setOnClickListener(this);

        View topBarRight = rootView.findViewById(R.id.img_user_info);

        imageButtMsg = (ImageView) rootView.findViewById(R.id.iv_message);

        View myArticle = rootView.findViewById(R.id.ll_my_article);

        View myComments = rootView.findViewById(R.id.ll_my_comments);

        View myOrder = rootView.findViewById(R.id.ll_my_order);

        userHeader = (ImageView) rootView.findViewById(R.id.iv_header);

        userName = (TextView) rootView.findViewById(R.id.tv_user_name);

        View loginNow = rootView.findViewById(R.id.tv_login_now);

        userSign = (TextView) rootView.findViewById(R.id.tv_user_sign);

        officialTag = rootView.findViewById(R.id.iv_official_tag);

        myTicket = rootView.findViewById(R.id.tv_my_ticket);

        mySign = rootView.findViewById(R.id.tv_my_sign);

        myWantJoin = rootView.findViewById(R.id.tv_want_join);

        userHeader.setOnClickListener(this);

        topBarRight.setOnClickListener(this);

        myTicket.setOnClickListener(this);

        mySign.setOnClickListener(this);

        myWantJoin.setOnClickListener(this);

        imageButtMsg.setOnClickListener(this);

        userSign.setOnClickListener(this);

        myArticle.setOnClickListener(this);

        myOrder.setOnClickListener(this);

        myComments.setOnClickListener(this);

        initVendorUserViews();

        if (MyApplication.getInstance().isLogin()) {

            officialTag.setVisibility(View.GONE);

            userSign.setVisibility(View.VISIBLE);

            exchangeUser.setVisibility(View.VISIBLE);

            exchangeUser.setText("切换至摊主身份");

            exchangeUser.setOnClickListener(this);

            userName.setVisibility(View.VISIBLE);

            loginNow.setVisibility(View.GONE);

            if (MyApplication.getInstance().getUserType() == 0) {
                showNormalUser();
            } else {
                showVendorUser();
            }

        } else {

            normalUserView.setVisibility(View.VISIBLE);

            vendorUserView.setVisibility(View.GONE);

            officialTag.setVisibility(View.GONE);

            userSign.setVisibility(View.GONE);

            exchangeUser.setVisibility(View.GONE);

            loginNow.setVisibility(View.VISIBLE);

            loginNow.setOnClickListener(this);

            userName.setVisibility(View.GONE);

            loginNow.setVisibility(View.VISIBLE);

            imageButtMsg.setImageResource(R.drawable.ic_none_message);

            userHeader.setImageResource(R.drawable.default_avatar);

            levelContainer.setVisibility(View.GONE);
        }


    }


    private void setValue() {

        currentUser = GetUserInfoUtil.getUserInfo();

        if (!TextUtils.isEmpty(currentUser.getAvatar())) {

            ImageLoadUtils.setRoundHeadView(userHeader,
                    currentUser.getAvatar(), R.drawable.default_avatar, 120);

        }

        userName.setText(currentUser.getNickname());

        userSign.setText(TextUtils.isEmpty(currentUser.getSignature()) ? "点击编辑签名内容"
                : currentUser.getSignature());

    }


    private void getData(String userId) {
        GetUserStatisticRequest request = new GetUserStatisticRequest(userId, new ResponseListener<GetUserStatisticResponse>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCacheResponse(GetUserStatisticResponse response) {
            }

            @Override
            public void onResponse(GetUserStatisticResponse response) {
                if (response != null && response.getResult().getStatistics() != null) {
                    MyApplication.getInstance().setMessageCount(response.getResult().getStatistics().getMessageCount());

                    setUnReadCount(response.getResult().getStatistics());
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
            }
        });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void getVendorInfo() {

        GetVendorInfoRequest request = new GetVendorInfoRequest(GetUserInfoUtil.getUserInfo().getUserId() + "",
                new ResponseListener<GetVendorInfoResponse>() {
                    @Override
                    public void onStart() {

                        if (isExchange) {
                            ExchangeUserDialog.getInstance().showDialog(getActivity().getFragmentManager());
                        }

                        isRequestingFinish = false;
                    }

                    @Override
                    public void onCacheResponse(GetVendorInfoResponse response) {
                    }

                    @Override
                    public void onResponse(GetVendorInfoResponse response) {

                        if (response != null && response.getResult().getUserInfo() != null) {

                            MyApplication.getInstance().setCurrentUser(response.getResult().getUserInfo());

                            setValue();

                            getVendorInfo = response.getResult().getVendor();

                            isRequestingFinish = true;

                            GetVendorInfo vendorInfo = response.getResult().getVendor();

                            if (vendorInfo != null &&
                                    vendorInfo.getVendorLevelInfo() != null) {
                                setLevelValue(vendorInfo.getVendorLevelInfo());

                                MyApplication.getInstance().getCurrentUser().setHasPayPassword(vendorInfo.getHasPayPassword());

                                tvDepositType.setText(vendorDepositType[getVendorInfo.getDepositFlag()]);

                                tvVendorInfoType.setText(VendorInfoType[getVendorInfo.getAuthType() + 1]);
                            }

                            CacheUserInfo cacheUserInfo = new CacheUserInfo();

                            cacheUserInfo.setUserInfo(MyApplication.getInstance().getCurrentUser());

                            if (isTimeOut && isExchange)
                                handler.sendEmptyMessage(1);

                        } else {
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            Toast.makeText(MyApplication.getInstance(), response.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                        }
                    }

                    @Override
                    public void onFinish() {
                    }
                });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void exchangeUser() {

        MyApplication.getInstance().setUserType(0);

        if (normalUserView.getVisibility() == View.VISIBLE) {

            if (currentUser.getApplyVendorStatus() == 3) {

                if (getVendorInfo != null && getVendorInfo.getAuthType() == -1 && isExchange) {

                    Intent intent = new Intent(mContext, VendorInfoCompletionActivity.class);

                    intent.putExtra(Constant.KEY.VENDOR_INFO, getVendorInfo);

                    startActivity(intent);

                } else {
                    showVendorUser();

                    MyApplication.getInstance().setUserType(1);

                    sendExchangeBroadCast();
                }

            } else {

                Intent intent = new Intent(mContext, VendorVerifyActivity.class);

                startActivity(intent);
            }

        } else {

            showNormalUser();

            MyApplication.getInstance().setUserType(0);

            sendExchangeBroadCast();

        }

        isExchange = false;
    }

    private void showVendorUser() {
        userSign.setVisibility(View.GONE);

        normalUserView.setVisibility(View.GONE);

        vendorUserView.setVisibility(View.VISIBLE);

        exchangeUser.setText("切换至普通用户");

        officialTag.setVisibility(View.VISIBLE);

        levelContainer.setVisibility(View.VISIBLE);

        levelContainer.setOnClickListener(this);
    }

    private void showNormalUser() {
        userSign.setVisibility(View.VISIBLE);

        normalUserView.setVisibility(View.VISIBLE);

        vendorUserView.setVisibility(View.GONE);

        officialTag.setVisibility(View.GONE);

        exchangeUser.setText("切换至摊主身份");

        levelContainer.setVisibility(View.GONE);
    }

    private void sendExchangeBroadCast() {
        Intent mIntent = new Intent(Constant.BROADCAST.EXCHANGE_USER);

        mContext.sendBroadcast(mIntent);
    }

    private void initVendorUserViews() {
        rootView.findViewById(R.id.tv_my_deposit).setOnClickListener(this);

        rootView.findViewById(R.id.ll_my_booth).setOnClickListener(this);

        rootView.findViewById(R.id.tv_vendor_info).setOnClickListener(this);

        rootView.findViewById(R.id.tv_vendor_order).setOnClickListener(this);

        rootView.findViewById(R.id.tv_vendor_wallet).setOnClickListener(this);
    }

    private void setLevelValue(GetVendorInfoResponse.VendorLevelInfo vendorLevelInfo) {
        if (vendorLevelInfo.getPrevLevel() > 0) {
            preImage.setImageResource(norIcons[vendorLevelInfo.getPrevLevel() - 1]);

            progressView.setImageResource(R.drawable.level_progress);
        } else {
            progressView.setImageResource(R.drawable.level_progress0);

            preImage.setImageResource(R.color.transparent);
        }

        if (vendorLevelInfo.getLevel() > 0){
            currentImage.setImageResource(lightIcons[vendorLevelInfo.getLevel() - 1]);

            if(vendorLevelInfo.getLevel() == lightIcons.length){
                progressView.setImageResource(R.drawable.level_progress1);
            }
        }

        if (vendorLevelInfo.getNextLevel() > 0) {

            nextImage.setImageResource(norIcons[vendorLevelInfo.getNextLevel() - 1]);

        }else{
            nextImage.setImageResource(R.color.transparent);
        }

        numPrivilege.setText(vendorLevelInfo.getPrivilegeCount() + "项特权");
    }

    @Override
    public void onResume() {

        initView();

        super.onResume();
    }

    @Override
    public void onClick(View v) {

        if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {
            return;
        }

        switch (v.getId()) {

            case R.id.img_user_info:
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE.CHECK_USER_INFO);
                break;
            case R.id.tv_vendor_info:

                Intent vendorInfoIntent = new Intent(mContext, VendorInfoActivity.class);

                startActivity(vendorInfoIntent);
                break;

            case R.id.ll_my_booth:
                Intent myBoothIntent = new Intent(mContext, MyBoothActivity.class);
                startActivity(myBoothIntent);
                break;

            case R.id.tv_my_ticket:
                startActivity(new Intent(mContext, MyTicketsListActivity.class));
                break;

            case R.id.tv_my_sign:

                startActivity(new Intent(mContext, MySignListActivity.class));

                break;

            case R.id.tv_want_join:
                Intent my_activity = new Intent(mContext, MissGoActivityListActivity.class);
                startActivity(my_activity);
                break;

            case R.id.ll_my_article:
                Intent articleIntent = new Intent(mContext, MyDiyActivity.class);
                startActivity(articleIntent);
                break;

            case R.id.ll_my_comments:
                Intent commentIntent = new Intent(mContext, MyCommentsActivity.class);
                startActivity(commentIntent);
                break;

            case R.id.ll_my_order:
                Intent orderIntent = new Intent(mContext, MyOrderFormActivity.class);
                startActivity(orderIntent);
                break;

            case R.id.tv_user_sign:
                Intent signIntent = new Intent(mContext, ChangeUserInfoEditActivity.class);

                signIntent.putExtra(Constant.KEY.MODE, ChangeUserInfoEditActivity.Mode.SIGN);

                signIntent.putExtra(Constant.KEY.DEFAULT_TEXT, currentUser.getSignature());

                startActivityForResult(signIntent, Constant.REQUEST_CODE.CHANGE_SIGN);
                break;

            case R.id.iv_header:

                if (MyApplication.getInstance().isLogin()) {
                    Intent headIntent = new Intent(mContext, UserInfoActivity.class);

                    startActivity(headIntent);
                } else {
                    processLoginActivity();
                }

                break;

            case R.id.tv_login_now:

                processLoginActivity();
                break;
            case R.id.iv_message:
                startActivity(new Intent(mContext, AllMsgActivity.class));

                break;

            case R.id.tv_exchange_user:

                isExchange = true;

                isTimeOut = false;

                getVendorInfo();

                Message message = handler.obtainMessage();

                message.what = 0;

                handler.sendMessageDelayed(message, 1500);

                break;

            case R.id.tv_my_deposit:

                mContext.startActivity(new Intent(mContext, BoothDepositActivity.class));

                break;

            case R.id.tv_vendor_order:
                Intent vendorOrderIntent = new Intent(mContext, OrderStallActivity.class);

                startActivity(vendorOrderIntent);
                break;

            case R.id.tv_vendor_wallet:
                Intent walletIntent = new Intent(mContext, MyWalletActivity.class);

                startActivity(walletIntent);
                break;

            case R.id.ll_level_container:

                Intent levelIntent = new Intent(mContext, VendorLevelActivity.class);

                startActivity(levelIntent);

                break;

            case R.id.tv_invite_friends:

                startActivity(new Intent(mContext, InviteFriendsActivity.class));

                break;
        }

    }

    private void processLoginActivity() {
        Intent loginIntent = new Intent();

        loginIntent.setClass(mContext, LoginActivity.class);

        loginIntent.putExtra(
                Constant.KEY.MODE,
                LoginActivity.Mode.ACTIVE);

        startActivity(loginIntent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                case 0:
                    isTimeOut = true;

                    if (isRequestingFinish) {
                        ExchangeUserDialog.getInstance().disMissDialog();

                        exchangeUser();
                    }

                    break;

                case 1:
                    ExchangeUserDialog.getInstance().disMissDialog();

                    exchangeUser();
                    break;
            }

        }
    };

    private void setUnReadCount(Statistics statistics) {

        if (!isAdded()) {
            return;
        }

        MyApplication.getInstance().setMessageCount(
                statistics.getMessageCount());

        if (statistics.getMessageCount() == 0) {
            imageButtMsg.setImageResource(R.drawable.ic_none_message);

        } else {
            imageButtMsg.setImageResource(R.drawable.ic_have_message);
        }


    }

    @Subscribe
    public void onEventMainThread(UserMainEventItem item) {

        switch (item.getType()) {
            case 1:
                ImageLoadUtils.setRoundHeadView(userHeader,
                        item.getAvatar(), R.drawable.default_avatar, 120);
                break;

            case 2:

                break;

            case 3:
                initView();
                break;
        }

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}