package com.nautilus.ywlfair.module.active;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.entity.bean.event.EventActiveStatus;
import com.nautilus.ywlfair.entity.request.BoothSubscribeNoticeRequest;
import com.nautilus.ywlfair.entity.request.GetActivityInfoRequest;
import com.nautilus.ywlfair.entity.request.GetBoothStatusRequest;
import com.nautilus.ywlfair.entity.response.GetActivityInfoResponse;
import com.nautilus.ywlfair.module.BaiduMapActivity;
import com.nautilus.ywlfair.module.booth.BoothDepositActivity;
import com.nautilus.ywlfair.module.booth.DepositListActivity;
import com.nautilus.ywlfair.module.booth.MyBoothDetailActivity;
import com.nautilus.ywlfair.module.mine.TicketOrderActivity;
import com.nautilus.ywlfair.module.vendor.BuyStallActivity;
import com.nautilus.ywlfair.module.vendor.VendorVerifyActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.RippleView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


/**
 * Created by Administrator on 2015/11/30.
 */
public class ActiveDetailFragment extends Fragment implements View.OnClickListener, RippleView.OnEndClickListener {
    private static ActiveDetailFragment mInstance = null;

    private Context mContext;

    private DisplayImageOptions options = ImageLoadUtils
            .createNoRoundedOptions();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsRequesting = false;

    private TextView titleView;

    private TextView priceView;

    private TextView timeView;

    private TextView dateView;

    private TextView addressView;

    private TextView routeView;

    private RippleView buyTicket;

    private TextView activeIntro;

    private LinearLayout contentContainer;

    private TextView ticketPriceView;

    private View rootView;

    private NautilusItem mNautilusItem;

    private CheckBox checkBox;

    private TextView beVendorBtn, payDepositBtn, joinActiveBtn, buyBoothBtn, butBoothStatusText;

    public static ActiveDetailFragment getInstance(Bundle bundle) {

        mInstance = new ActiveDetailFragment();

        mInstance.setArguments(bundle);

        return mInstance;
    }

    @Override
    public void onCreate(Bundle icicle) {

        Bundle arguments = getArguments();
        if (arguments != null) {

        }

        mContext = getActivity();

        EventBus.getDefault().register(this);

        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_detail, null);

        initViews();

        return rootView;

    }

    private void initViews() {

        titleView = (TextView) rootView.findViewById(R.id.tv_active_name);

        priceView = (TextView) rootView.findViewById(R.id.tv_active_price);

        timeView = (TextView) rootView.findViewById(R.id.tv_time);

        dateView = (TextView) rootView.findViewById(R.id.tv_date);

        addressView = (TextView) rootView.findViewById(R.id.tv_address);
        addressView.setOnClickListener(this);

        routeView = (TextView) rootView.findViewById(R.id.tv_route);

        activeIntro = (TextView) rootView.findViewById(R.id.tv_active_introduce);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        contentContainer = (LinearLayout) rootView
                .findViewById(R.id.ll_detail_container);

        ticketPriceView = (TextView) rootView.findViewById(R.id.tv_ticket_price);

        buyTicket = (RippleView) rootView.findViewById(R.id.bt_buy_ticket);
        buyTicket.setOnEndClickListener(buyTicket.getId(), this);

        beVendorBtn = (TextView) rootView.findViewById(R.id.btn_be_vendor);
        beVendorBtn.setOnClickListener(this);

        payDepositBtn = (TextView) rootView.findViewById(R.id.btn_pay_deposit);
        payDepositBtn.setOnClickListener(this);

        joinActiveBtn = (TextView) rootView.findViewById(R.id.btn_join_active);
        joinActiveBtn.setOnClickListener(this);

        buyBoothBtn = (TextView) rootView.findViewById(R.id.btn_buy_booth);
        buyBoothBtn.setOnClickListener(this);

        rootView.findViewById(R.id.tv_check_booth).setOnClickListener(this);

        checkBox = (CheckBox) rootView.findViewById(R.id.ck_booth_notice);

        butBoothStatusText = (TextView) rootView.findViewById(R.id.tv_status_text);

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                    }
                });

    }

    private void setValue() {

        if (mNautilusItem.getType() == 1) {
            View bar = rootView.findViewById(R.id.ll_control_bar);
            bar.setVisibility(View.GONE);

            addressView.setVisibility(View.GONE);

            routeView.setVisibility(View.GONE);

            priceView.setVisibility(View.GONE);

            View recruitView = rootView.findViewById(R.id.ll_recruit);
            recruitView.setVisibility(View.GONE);
        }

        if (mNautilusItem.getActivityStatus() == 1) {
            buyTicket.setBackgroundColor(getResources().getColor(R.color.content_color));

            buyTicket.setEnabled(false);
        }

        if (mNautilusItem.getTicketInfoList().size() > 0) {
            double temp = mNautilusItem.getTicketInfoList().get(0).getPrice();

            if (temp == 0) {
                priceView.setText("门票: 免门票");

                ticketPriceView.setText("免门票");

                buyTicket.setBackgroundColor(getResources().getColor(R.color.content_color));

                buyTicket.setEnabled(false);
            } else {
                priceView.setText("门票: ￥" + StringUtils.getMoneyFormat(temp));
                ticketPriceView.setText("门票: ￥" + StringUtils.getMoneyFormat(temp));
            }
        }

        titleView.setText(mNautilusItem.getName());

        String startTime = mNautilusItem.getStartTime();

        String endTime = mNautilusItem.getEndTime();

        dateView.setText("日期: " + TimeUtil.getDateFormat(Long.valueOf(startTime), Long.valueOf(endTime)));

        timeView.setText("时间: " + TimeUtil.getTimeFormat(Long.valueOf(startTime), Long.valueOf(endTime)));

        addressView.setText("地址: " + mNautilusItem.getAddress());

        if (TextUtils.isEmpty(mNautilusItem.getTrafficInfo())) {
            routeView.setVisibility(View.GONE);
        }
        routeView.setText("路线: " + mNautilusItem.getTrafficInfo());

        activeIntro.setText(mNautilusItem.getIntroduction());

        List<PicInfo> picList = mNautilusItem.getPicInfoList();

        if (picList != null) {

            contentContainer.removeAllViews();

            for (int i = 0; i < picList.size(); i++) {
                AutoAdjustHeightImageView imageView = new AutoAdjustHeightImageView(
                        getActivity(), null);

                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                imageView.setAspectRatio(1.5);

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                imageView.setPadding(0, BaseInfoUtil.dip2px(10), 0, 0);

                ImageLoader.getInstance()
                        .displayImage(
                                mNautilusItem.getPicInfoList().get(i)
                                        .getImgUrl(), imageView, options);

                contentContainer.addView(imageView);
            }
        }

        setStatus();
    }

    private void setStatus() {

        if (MyApplication.getInstance().isLogin()) {

            if (mNautilusItem.getIsVendor() == 1) {
                beVendorBtn.setEnabled(false);

                beVendorBtn.setText("摊主申请审核中");

            } else if (mNautilusItem.getIsVendor() == 2) {
                beVendorBtn.setEnabled(false);

                beVendorBtn.setText("您已是摊主");
            } else {
                beVendorBtn.setEnabled(true);

                beVendorBtn.setText("申请成为摊主");
            }

            if (mNautilusItem.getIsVendor() == 2) {
                if (mNautilusItem.getIsDeposit() == 0) {
                    payDepositBtn.setEnabled(true);
                } else {
                    payDepositBtn.setEnabled(false);

                    payDepositBtn.setText("您已缴纳摊位押金");
                }
            } else {
                payDepositBtn.setEnabled(false);
            }


            if (mNautilusItem.getIsDeposit() == 1) {
                if (mNautilusItem.getIsBoothApply() == 0) {
                    joinActiveBtn.setEnabled(true);

                    joinActiveBtn.setText("报名本次活动");

                } else if (mNautilusItem.getIsBoothApply() == 1) {
                    joinActiveBtn.setEnabled(false);

                    joinActiveBtn.setText("报名审核中");
                } else {
                    joinActiveBtn.setEnabled(false);

                    joinActiveBtn.setText("您已报名成功");
                }
            } else {
                joinActiveBtn.setEnabled(false);
            }

            if (mNautilusItem.getIsDeposit() == 1 && mNautilusItem.getIsDeposit() == 1 && mNautilusItem.getIsBoothApply() == 2) {
                if (mNautilusItem.getIsBoothBuy() == 0) {

                    buyBoothBtn.setEnabled(true);

                    buyBoothBtn.setTag(0);

                } else if (mNautilusItem.getIsBoothBuy() == 1) {
                    buyBoothBtn.setEnabled(true);

                    buyBoothBtn.setText("查看摊位");

                    buyBoothBtn.setTag(1);

                    butBoothStatusText.setText("您可以查看本次活动摊位");

                } else {
                    buyBoothBtn.setEnabled(false);

                    buyBoothBtn.setText("摊位已售罄");

                    checkBox.setVisibility(View.VISIBLE);

                    butBoothStatusText.setText("本活动摊位已售完");

                    setBoothNotification();
                }
            } else {

                if (mNautilusItem.getIsBoothBuy() == -1) {
                    buyBoothBtn.setText("摊位已售罄");

                    butBoothStatusText.setText("本活动摊位已售完");
                }
                buyBoothBtn.setEnabled(false);
            }

            if (mNautilusItem.getIsBoothTimeLimit() == 1) {

                if (mNautilusItem.getIsBoothBuy() == 1) {
                    buyBoothBtn.setEnabled(true);

                    buyBoothBtn.setTag(1);

                    buyBoothBtn.setText("查看摊位");

                    butBoothStatusText.setText("您可以查看本次活动摊位");

                    beVendorBtn.setEnabled(false);

                } else {
                    buyBoothBtn.setEnabled(false);

                    buyBoothBtn.setText("停止售摊");

                    butBoothStatusText.setText("停止售卖摊位");
                }

                if (mNautilusItem.getIsBoothApply() == 2) {

                    joinActiveBtn.setText("您已报名成功");



                } else {
                    joinActiveBtn.setText("报名已截止");
                }

                payDepositBtn.setEnabled(false);

                joinActiveBtn.setEnabled(false);

            }
        } else {
            beVendorBtn.setEnabled(true);

            buyBoothBtn.setEnabled(false);

            joinActiveBtn.setEnabled(false);

            payDepositBtn.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_check_booth:

                postVendorIntent();

                break;

            case R.id.btn_be_vendor:

                if(LoginWarmUtil.getInstance().checkLoginStatus(mContext)){
                    Intent intent2 = new Intent(getActivity(),
                            VendorVerifyActivity.class);

                    intent2.putExtra(Constant.KEY.VENDOR_STATUS, mNautilusItem.getIsVendor() == 1 ? true : false);

                    startActivity(intent2);
                }



                break;

            case R.id.btn_pay_deposit:

                startActivity(new Intent(mContext, BoothDepositActivity.class));

                break;

            case R.id.btn_join_active:

                startActivity(new Intent(mContext, ActivitySignUpActivity1.class).putExtra(Constant.KEY.ITEM_ID, mNautilusItem.getActId()));

                break;

            case R.id.btn_buy_booth:

                if ((int) v.getTag() == 1) {
                    postMyBoothDetailIntent();
                } else {
                    postVendorIntent();
                }

                break;

            case R.id.tv_address:

                if (TextUtils.isEmpty(mNautilusItem.getAddrMap())) {
                    ToastUtil.showShortToast("没有活动位置信息");
                    return;
                }
                Intent mapIntent = new Intent(getActivity().getApplicationContext(),
                        BaiduMapActivity.class);

                mapIntent.putExtra(Constant.KEY.NAUTILUSITEM, mNautilusItem);

                startActivity(mapIntent);
                break;

        }
    }

    private void postMyBoothDetailIntent() {
        Intent intent = new Intent(mContext, MyBoothDetailActivity.class);

        intent.putExtra(Constant.KEY.ORDER_ID, mNautilusItem.getBoothBuyOrder());

        startActivity(intent);
    }

    private void getBoothStatus() {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetBoothStatusRequest request = new GetBoothStatusRequest(userId, new ResponseListener<InterfaceResponse>() {
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
                    postVendorIntent();
                } else {
                    ToastUtil.showLongToast("验证摊主信息出错");
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();

                    if (response.getStatus() == -20) {
                        showPayDepositAlert();
                    } else {
                        Toast.makeText(MyApplication.getInstance(), response.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

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

    private void postVendorIntent() {

        Intent buyStallIntent = new Intent(getActivity(),
                BuyStallActivity.class);

        buyStallIntent.putExtra(Constant.KEY.NAUTILUSITEM,
                mNautilusItem);

        startActivity(buyStallIntent);

    }

    @Subscribe
    public void onEventMainThread(EventActiveStatus eventActiveStatus) {

        switch (eventActiveStatus.getType()) {

            case -2:

                getData();

                break;

            case -1:

                this.mNautilusItem = eventActiveStatus.getNautilusItem();

                break;
            case 0:

                mNautilusItem.setIsVendor(eventActiveStatus.getStatus());

                break;

            case 1:

                mNautilusItem.setIsDeposit(eventActiveStatus.getStatus());

                break;

            case 2:

                mNautilusItem.setIsBoothApply(eventActiveStatus.getStatus());

                break;

            case 3:

                getData();

                break;
        }

        setValue();

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void mViewClick(int id) {
        switch (id) {
            case R.id.bt_buy_ticket:
                if (mNautilusItem == null)
                    return;

                Intent intent = new Intent(getActivity(),
                        TicketOrderActivity.class);

                intent.putExtra(
                        Constant.KEY.MODE,
                        TicketOrderActivity.Mode.TICKET);

                intent.putExtra(Constant.KEY.NAUTILUSITEM, mNautilusItem);

                startActivity(intent);

                break;
        }

    }

    private void setBoothNotification() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                changeBoothNoticeStatus(isChecked ? Request.Method.POST : Request.Method.PUT, 2);
            }
        });
    }

    private void changeBoothNoticeStatus(final int method, int type) {

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        BoothSubscribeNoticeRequest request = new BoothSubscribeNoticeRequest(method, userId, mNautilusItem.getActId(),
                type, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if (response != null) {
                    if (method == Request.Method.POST) {
                        ToastUtil.showShortToast("已设置购摊提醒");
                    } else {
                        ToastUtil.showShortToast("已取消购摊提醒");
                    }
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

    private void showPayDepositAlert() {
        final Dialog dialog = new Dialog(mContext, R.style.dialog);

        View view = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_confirm, null);

        dialog.setContentView(view);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        int paddingPx = BaseInfoUtil.dip2px(MyApplication.getInstance(), 20);
        window.getDecorView().setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView titleTextView = (TextView) view.findViewById(R.id.tv_title);
        titleTextView.setText("提示");

        View dividerView = view.findViewById(R.id.view_divider);
        dividerView.setVisibility(View.VISIBLE);

        TextView contentTextView = (TextView) view.findViewById(R.id.tv_content);
        contentTextView.setVisibility(View.VISIBLE);
        contentTextView.setText("您需要缴纳摊位押金才能购买此摊位，现在就去缴纳摊位押金吗？");

        TextView cancelTextView = (TextView) view.findViewById(R.id.tv_left);
        cancelTextView.setText("残忍拒绝");

        TextView okTextView = (TextView) view.findViewById(R.id.tv_right);
        okTextView.setText("同意");

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

                startActivity(new Intent(mContext, DepositListActivity.class));
            }
        });
    }

    private void getData() {

        if (mIsRequesting || mNautilusItem == null) {
            return;
        }

        GetActivityInfoRequest request = new GetActivityInfoRequest(mNautilusItem.getActId(), new ResponseListener<GetActivityInfoResponse>() {
            @Override
            public void onStart() {
                mSwipeRefreshLayout.setRefreshing(true);

                mIsRequesting = true;
            }

            @Override
            public void onCacheResponse(GetActivityInfoResponse response) {
            }

            @Override
            public void onResponse(GetActivityInfoResponse response) {

                if (response == null || response.getResult().getNautilusItem() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }

                mNautilusItem = response.getResult().getNautilusItem();

                setValue();

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
                mSwipeRefreshLayout.setRefreshing(false);

                mIsRequesting = false;
            }
        });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);

    }
}
