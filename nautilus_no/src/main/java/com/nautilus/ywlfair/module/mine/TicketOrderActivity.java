package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.ShowPayPasswordDialog;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.PayMethodResultListener;
import com.nautilus.ywlfair.component.PayMethodService;
import com.nautilus.ywlfair.dialog.OnUserSelectListener;
import com.nautilus.ywlfair.dialog.PayMethodMenuDialog;
import com.nautilus.ywlfair.dialog.ShowPasswordErrorDialog;
import com.nautilus.ywlfair.entity.bean.BoothInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.bean.TicketListItem;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.GetAccountStatusRequest;
import com.nautilus.ywlfair.entity.request.GetPayAuthorizationRequest;
import com.nautilus.ywlfair.entity.request.PostCreateOrderRequest;
import com.nautilus.ywlfair.entity.response.GetAccountStatusResponse;
import com.nautilus.ywlfair.entity.response.PostUserOrderResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.active.CompleteOrderActivity;
import com.nautilus.ywlfair.module.booth.MyBoothActivity;
import com.nautilus.ywlfair.module.launch.BindPhone;
import com.nautilus.ywlfair.module.launch.register.RegisterActivity;
import com.nautilus.ywlfair.module.mine.wallet.PayPasswordStatusActivity;
import com.nautilus.ywlfair.widget.NewPayMethodSelectLayout;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.RippleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 创建订单界面
 */
public class TicketOrderActivity extends BaseActivity implements OnClickListener,
        RippleView.OnEndClickListener, PayMethodResultListener, ShowPayPasswordDialog.PasswordInputListener,
        OnUserSelectListener, PayMethodMenuDialog.CheckPayMethodListener {

    private Context mContext;

    private RippleView confirmBtn;

    private double price;

    private EditText phoneView, reInputPhone;

    private BoothInfo boothInfo;

    private TextView venueView;

    private TextView stallSale;

    private boolean mIsRequesting = false;

    private OrderInfo orderInfo;

    private String[] payChannels = new String[]{"ALI", "WX"};

    private int[] boothTypeTag = new int[]{R.drawable.ic_normal_stall, R.drawable.ic_food_stall};

    public enum Mode {
        TICKET, STALL
    }

    private Mode mode;

    private String phoneNumber;

    private TicketListItem ticketListItem;

    private HomePagerActivityInfo homePagerActivityInfo;

    private boolean mIsPaying = false;//是否正在启动支付

    private int itemNum = 1;

    private int payType = 1; // 1 人民币支付  2  积分支付

    private double totalPrice;

    private int warmType;//弹出提示框的类型 5未设置支付密码 4账户被冻结  3 支付密码错误

    @BindView(R.id.tv_title)
    TextView titleView;

    @BindView(R.id.ll_phone_confirm)
    View phoneConfirm;

    @BindView(R.id.tv_pay_cover)
    View coverView;

    @BindView(R.id.tv_is_score)
    ImageView switchImage;

    @BindView(R.id.ll_use_score)
    View scoreContainView;

    @BindView(R.id.ll_stall_price)
    View stallPriceView;

    @BindView(R.id.ll_stall_score)
    View stallScore;

    @BindView(R.id.tv_score_total)
    TextView scoreView;

    @BindView(R.id.tv_need_pay)
    TextView needPay;

    @BindView(R.id.tv_visible_score)
    TextView visibleScoreView;

    @BindView(R.id.iv_pay_image)
    ImageView payImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ticket_order_activity);

        ButterKnife.bind(this);

        mContext = this;

        mode = (Mode) getIntent().getSerializableExtra(Constant.KEY.MODE);

        initViews();
    }

    private void initViews() {

        titleView.setText("订单确认");

        venueView = (TextView) findViewById(R.id.tv_venue);

        confirmBtn = (RippleView) findViewById(R.id.bt_confirm);

        phoneView = (EditText) findViewById(R.id.tv_phone_num);
        phoneView.clearFocus();

        reInputPhone = (EditText) findViewById(R.id.tv_phone_two);
        reInputPhone.clearFocus();

        String cachePhone = PreferencesUtil.getString(Constant.PRE_KEY.PHONE_NUMBER);

        if (TextUtils.isEmpty(cachePhone)) {
            UserInfo userInfo = GetUserInfoUtil.getUserInfo();

            if (userInfo != null && StringUtils.isMobileNumber(userInfo.getBindPhone())) {
                phoneView.setText(userInfo.getBindPhone());

                reInputPhone.setText(userInfo.getBindPhone());
            }
        } else {
            phoneView.setText(cachePhone);

            reInputPhone.setText(cachePhone);
        }

        confirmBtn.setOnEndClickListener(confirmBtn.getId(), this);

        NewPayMethodSelectLayout payMethodSelectLayout = (NewPayMethodSelectLayout) findViewById(R.id.pay_method_select);

        payMethodSelectLayout.setListener(new NewPayMethodSelectLayout.OnItemSelectListener() {
            @Override
            public void onItemSelect(int position) {
                channelType = payChannels[position];
            }
        });

        if (mode == Mode.TICKET) {

            homePagerActivityInfo = (HomePagerActivityInfo) getIntent().getSerializableExtra(Constant.KEY.NAUTILUSITEM);

            phoneConfirm.setVisibility(View.VISIBLE);

            itemNum = getIntent().getIntExtra(Constant.KEY.NUMBER, 1);

            if (homePagerActivityInfo == null) {
                ToastUtil.showLongToast("活动信息异常");

                return;
            }

            findViewById(R.id.ll_ticket_container).setVisibility(View.VISIBLE);

            String venueString = homePagerActivityInfo.getVenue();

            if (TextUtils.isEmpty(venueString)) {
                venueView.setVisibility(View.GONE);
            } else {
                venueView.setVisibility(View.VISIBLE);

                venueView.setText("场馆：" + venueString);
            }

            ticketListItem = (TicketListItem) getIntent().getSerializableExtra(Constant.KEY.TICKET);

            price = ticketListItem.getPrice();

            setTicketValue();

        } else if (mode == Mode.STALL) {

            boothInfo = (BoothInfo) getIntent().getSerializableExtra(
                    Constant.KEY.BOOTH);

            if (boothInfo == null) {
                ToastUtil.showLongToast("摊位信息异常");
                return;
            }

            price = boothInfo.getBoothDisCount();

            totalPrice = price * itemNum;

            scoreContainView.setVisibility(View.VISIBLE);

            coverView.setOnClickListener(this);

            stallPriceView.setVisibility(View.VISIBLE);

            visibleScoreView.setText("摊主积分（" + boothInfo.getVendorScore() + "可用）");

            switchImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (payType == 1) {

                        if (boothInfo.getVendorScore() >= boothInfo.getBoothScore()) {

                            switchImage.setSelected(true);

                            payType = 2;

                            totalPrice = 0;

                            coverView.setVisibility(View.VISIBLE);

                            stallScore.setVisibility(View.VISIBLE);

                            scoreView.setText("-￥" + StringUtils.getMoneyFormat(boothInfo.getBoothDisCount()));

                            needPay.setText("￥" + StringUtils.getMoneyFormat(totalPrice));

                            stallSale.setText("已优惠：" + (StringUtils.getMoneyFormat(boothInfo.getBoothDisCount() - totalPrice)));

                            visibleScoreView.setText("摊主积分（" + StringUtils.getScoreFormat(boothInfo.getVendorScore()) + "可用） -" +
                                    (int) boothInfo.getBoothScore());

                        } else {
                            ToastUtil.showLongToast("积分不足");
                        }

                    } else {
                        switchImage.setSelected(false);

                        payType = 1;

                        coverView.setVisibility(View.GONE);

                        scoreView.setText("0");

                        totalPrice = price * itemNum;

                        needPay.setText("￥" + StringUtils.getMoneyFormat(totalPrice));

                        stallScore.setVisibility(View.GONE);

                        stallSale.setText("已优惠：" + (StringUtils.getMoneyFormat(boothInfo.getBoothDisCount() - totalPrice)));

                        visibleScoreView.setText("摊主积分（" + boothInfo.getVendorScore() + "可用）");
                    }
                }
            });

            findViewById(R.id.ll_stall_container).setVisibility(View.VISIBLE);

            setStallValue();
        }

        needPay.setText("￥" + StringUtils.getMoneyFormat(totalPrice));

    }

    private void setTicketValue() {

        ImageView activeImage = (ImageView) findViewById(R.id.iv_active_image);
        ImageLoader.getInstance().displayImage(homePagerActivityInfo.getPosterMain(), activeImage, ImageLoadUtils.createNoRoundedOptions());

        TextView activeName = (TextView) findViewById(R.id.tv_active_name);
        activeName.setText(homePagerActivityInfo.getName());

        TextView totalNum = (TextView) findViewById(R.id.tv_total_num);
        totalNum.setText("共计" + itemNum + "张");

        TextView ticketPrice = (TextView) findViewById(R.id.tv_ticket_price);
        ticketPrice.setText("门票价格：￥" + StringUtils.getMoneyFormat(price));

        String startTime = homePagerActivityInfo.getStartTime();

        String endTime = homePagerActivityInfo.getEndTime();

        TextView date = (TextView) findViewById(R.id.tv_date);
        date.setText("活动日期：" + TimeUtil.getYearMonthAndDay(Long.valueOf(startTime)) + " ~ "
                + TimeUtil.getMonthAndDay(Long.valueOf(endTime)));

        TextView time = (TextView) findViewById(R.id.tv_time);
        time.setText("活动时间：" + TimeUtil.getHourAndMin(Long.valueOf(startTime)) + " ~ "
                + TimeUtil.getHourAndMin(Long.valueOf(endTime)));

        TextView address = (TextView) findViewById(R.id.tv_address);
        address.setText("活动地址：" + homePagerActivityInfo.getAddress());

        TextView totalPriceView = (TextView) findViewById(R.id.tv_total_price);
        totalPriceView.setText("合计：" + StringUtils.getMoneyFormat(price * itemNum));

    }

    private void setStallValue() {
        if (boothInfo == null) {
            return;
        }

        String venueString = boothInfo.getActVenue();

        if (TextUtils.isEmpty(venueString)) {
            venueView.setVisibility(View.GONE);
        } else {
            venueView.setVisibility(View.VISIBLE);

            venueView.setText("场馆：" + venueString);
        }

        ImageView typeTag = (ImageView) findViewById(R.id.iv_type_tag);
        typeTag.setImageResource(boothTypeTag[boothInfo.getBoothCategory() - 1]);

        TextView stallType = (TextView) findViewById(R.id.tv_stall_type);
        stallType.setText("摊位：" + boothInfo.getBoothType());

        TextView boothPrice = (TextView) findViewById(R.id.tv_booth_price);
        boothPrice.setText("摊位费：￥" + StringUtils.getMoneyFormat(boothInfo.getBoothDisCount()));

        TextView stallDesc = (TextView) findViewById(R.id.tv_stall_desc);
        stallDesc.setText("摊位说明：" + boothInfo.getBoothMsg());

        ImageView activeImage = (ImageView) findViewById(R.id.iv_stall_act_image);
        ImageLoader.getInstance().displayImage(boothInfo.getActImage(), activeImage, ImageLoadUtils.createNoRoundedOptions());

        TextView stallActiveName = (TextView) findViewById(R.id.tv_stall_active_name);
        stallActiveName.setText(boothInfo.getActName());

        TextView activeSite = (TextView) findViewById(R.id.tv_active_number);
        activeSite.setText("活动场次：" + boothInfo.getRoundNo());

        TextView stallDate = (TextView) findViewById(R.id.tv_stall_date);
        stallDate.setText("本场市集日期：" + TimeUtil.getYearMonthAndDay(Long.valueOf(boothInfo.getActStartTime())) + " ~ "
                + TimeUtil.getMonthAndDay(Long.valueOf(boothInfo.getActEndTime())));

        TextView stallTime = (TextView) findViewById(R.id.tv_stall_time);

        stallTime.setText("本场摊主入场时间：" + TimeUtil.getTimeFormat(boothInfo.getRoundapproachStartTime(), boothInfo.getRoundApproachEndTime()));

        TextView stallSite = (TextView) findViewById(R.id.tv_stall_site);
        stallSite.setText("活动场馆：" + boothInfo.getActVenue());

        TextView stallAddress = (TextView) findViewById(R.id.tv_address);
        stallAddress.setText("活动地点：" + boothInfo.getActAddress());

        TextView stallTotalPrice = (TextView) findViewById(R.id.tv_stall_total);
        stallTotalPrice.setText("￥" + StringUtils.getMoneyFormat(boothInfo.getBoothDisCount()));

        stallSale = (TextView) findViewById(R.id.tv_stall_sale);
        stallSale.setText("已优惠：" + (StringUtils.getMoneyFormat(boothInfo.getBoothDisCount() - totalPrice)));

    }

    @OnClick({R.id.ll_choose_pay, R.id.img_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.ll_choose_pay:
                PayMethodMenuDialog.getInstance(TicketOrderActivity.this).show(TicketOrderActivity.this.getFragmentManager(), null);
                break;
        }
    }

    @Override
    public void onCheckMethod(int checkPosition) {
        channelType = payChannels[checkPosition];

        if (checkPosition == 0) {
            payImageView.setImageResource(R.drawable.ic_ali_pay);
        } else {
            payImageView.setImageResource(R.drawable.ic_wx_pay);
        }
    }

    @Override
    public void onInputFinished(String password) {
        getPayAuthorization(password);
    }

    private void confirmOrder() {

        if (mIsRequesting || mIsPaying) {
            return;
        }

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        String activeId = "";

        String itemType = "";

        String skuId = "";

        String boothId = "";

        String orderId = "";

        if (mode == Mode.TICKET) {

            activeId = homePagerActivityInfo.getActId();

            itemType = "1";

            skuId = activeId + "," + ticketListItem.getTicketType();

            orderId = ticketListItem.getOrderId();

        } else if (mode == Mode.STALL) {

            activeId = boothInfo.getActId();

            itemType = "2";

            skuId = activeId + "," + boothInfo.getBoothType() + "," + boothInfo.getRoundId();

            boothId = boothInfo.getBoothId() + "";

        }

        PreferencesUtil.putString(Constant.PRE_KEY.PHONE_NUMBER, phoneNumber);

        PostCreateOrderRequest request = new PostCreateOrderRequest(userId, itemType, skuId, price + "", itemNum + "",
                phoneNumber, channelType, "", "", orderId, boothId, "", payType + "", "", new ResponseListener<PostUserOrderResponse>() {
            @Override
            public void onStart() {

                mIsRequesting = true;

                ProgressDialog.getInstance().show(mContext, "正在提交...");
            }

            @Override
            public void onCacheResponse(PostUserOrderResponse response) {

            }

            @Override
            public void onResponse(PostUserOrderResponse response) {
                if (response != null && response.getResult().getOrderInfo() != null) {

                    mIsPaying = true;

                    orderInfo = response.getResult().getOrderInfo();

                    if (mode == Mode.STALL && coverView.getVisibility() == View.VISIBLE) {

                        ShowPayPasswordDialog.getInstance().disMissDialog();

                        CompleteOrderActivity.startCompleteActivity(mContext, orderInfo);

                        finish();

                    } else {
                        PayMethodService.getInstance().startPay(mContext, orderInfo, TicketOrderActivity.this);
                    }

                } else {
                    ToastUtil.showShortToast("操作失败");
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

                mIsRequesting = false;

                ProgressDialog.getInstance().cancel();
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private String channelType = "ALI";

    @Override
    public void mViewClick(int id) {
        switch (id) {
            case R.id.bt_confirm:

                if (payType == 2) {// 积分支付

                    if (GetUserInfoUtil.getUserInfo().getHasPayPassword() == 1) {

                        checkAccountStatus();
                    } else {
                        warmType = 5;

                        ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, "您暂未设置支付密码，请先设置后重试", 5);
                    }
                } else {
                    phoneNumber = phoneView.getText().toString().trim();

                    String rePhoneNumber = reInputPhone.getText().toString().trim();

                    if ((mode == Mode.TICKET) && (TextUtils.isEmpty(phoneNumber) || !phoneNumber.equals(rePhoneNumber))) {

                        ToastUtil.showLongToast("两次输入的手机号不一致");

                        return;
                    }

                    confirmOrder();
                }

                break;
        }
    }

    private void getPayAuthorization(final String password) {

        int userId = GetUserInfoUtil.getUserInfo().getUserId();

        GetPayAuthorizationRequest request = new GetPayAuthorizationRequest(userId, password,
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "验证支付密码");
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            confirmOrder();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            if (response.getMessage().contains("冻结")) {

                                warmType = 4;

                                ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, response.getMessage(), 4);

                                return;
                            }

                            warmType = 3;

                            ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, response.getMessage(), 3);

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

    @Override
    public void payResult(boolean isSuccess, boolean isTimeOut) {
        mIsPaying = false;

        if (isSuccess) {
            CompleteOrderActivity.startCompleteActivity(mContext, orderInfo);

            finish();
        } else {

            if (!isTimeOut)
                processCancelIntent();

            finish();
        }
    }

    @Override
    public void onSelect(boolean isConfirm) {
        if (isConfirm) {

            if (warmType == 5) {
                processSetPassword();
            } else if (warmType == 3) {
                ShowPayPasswordDialog.getInstance().clearText();
            }

        } else {
            Intent phone = new Intent(mContext, RegisterActivity.class);

            phone.putExtra(Constant.KEY.NAME, "找回支付密码");

            phone.putExtra(Constant.KEY.TYPE, 4);

            mContext.startActivity(phone);
        }
    }

    private void processSetPassword() {
        if (TextUtils.isEmpty(GetUserInfoUtil.getUserInfo().getBindPhone())) {

            Intent intent = new Intent(mContext, BindPhone.class);

            intent.putExtra(Constant.KEY.MODE, BindPhone.Mode.PAY_PASSWORD);

            startActivity(intent);

            return;
        }

        Intent intent = new Intent(mContext, RegisterActivity.class);

        intent.putExtra(Constant.KEY.TYPE, 4);

        intent.putExtra(Constant.KEY.NAME, "设置支付密码");

        startActivity(intent);

    }

    private void processCancelIntent() {

        Intent intent = new Intent();

        if (mode == Mode.TICKET) {
            intent.setClass(mContext, MyTicketsListActivity.class);

        } else {
            intent.setClass(mContext, MyBoothActivity.class);
        }

        intent.putExtra(Constant.KEY.POSITION, 1);

        startActivity(intent);

    }

    private void checkAccountStatus() {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetAccountStatusRequest request = new GetAccountStatusRequest(userId, new ResponseListener<GetAccountStatusResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(GetAccountStatusResponse response) {

            }

            @Override
            public void onResponse(GetAccountStatusResponse response) {
                if (response != null) {

                    if (response.getResult().getPayAccountStatus() == 1) {
                        ShowPayPasswordDialog.getInstance().initMenuDialog(mContext);
                    } else if (response.getResult().getPayAccountStatus() == -1) {
                        warmType = 4;

                        ShowPasswordErrorDialog.getInstance().initMenuDialog(mContext, response.getMessage(), 4);
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onFinish() {

            }
        });

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    protected void onPause() {

        ShowPayPasswordDialog.getInstance().disMissDialog();

        super.onPause();
    }
}
