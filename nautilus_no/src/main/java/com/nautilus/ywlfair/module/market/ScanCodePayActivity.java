package com.nautilus.ywlfair.module.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.CashierInputFilter;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.PayMethodResultListener;
import com.nautilus.ywlfair.component.PayMethodService;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.GetUserInfoRequest;
import com.nautilus.ywlfair.entity.request.PostCreateOrderRequest;
import com.nautilus.ywlfair.entity.response.GetAccessTokenResponse;
import com.nautilus.ywlfair.entity.response.PostUserOrderResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.MyIsPayLayout;
import com.nautilus.ywlfair.widget.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.VENDOR_ID;


/**
 * 扫码支付页面
 * Created by Administrator on 2016/6/17.
 */
public class ScanCodePayActivity extends BaseActivity implements PayMethodResultListener {

    private Context mContext;

    private String vendorId;

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_stall_name)
    TextView tvStallName;
    @BindView(R.id.ed_money_num)
    EditText edMoneyNum;
    @BindView(R.id.tv_money_num)
    TextView tvMoneyNum;
    @BindView(R.id.my_is_pay_layout)
    MyIsPayLayout myIsPayLayout;
    @BindView(R.id.tv_payment)
    TextView tvPayment;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;

    private OrderInfo orderInfo;

    private boolean mIsPaying = false;//是否正在启动支付

    private UserInfo userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        vendorId = getIntent().getStringExtra(VENDOR_ID);

        setContentView(R.layout.scan_code_pay_activity);

        ButterKnife.bind(this);

        tvTitle.setText("扫码支付");

        Selection.setSelection(edMoneyNum.getText(), edMoneyNum.getText().length());

        GetUserInfo(vendorId);

        InputFilter[] filters = {new CashierInputFilter()};

        edMoneyNum.setFilters(filters);


        edMoneyNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                tvMoneyNum.setText("￥" + s.toString());
                tvPayment.setText(edMoneyNum.getText() + "元  确认订单");

            }
        });

    }


    @OnClick({R.id.img_back, R.id.tv_payment, R.id.tv_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_payment:
//                if (TextUtils.isEmpty(edMoneyNum.getText()) || Double.valueOf(edMoneyNum.getText() + "") < 1) {
//                    ToastUtil.showLongToast("支付金额不能少于1元哦");
//                    return;
//                }

                confirmOrderStatus();

                break;


            case R.id.tv_feedback:

                startActivity(new Intent(mContext, FeedBackActivity.class).putExtra(VENDOR_ID, vendorId));

                break;
        }
    }

    /**
     * 点击空白位置 隐藏软键盘
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    private void GetUserInfo(String vendorId) {

        GetUserInfoRequest request = new GetUserInfoRequest(vendorId, new ResponseListener<GetAccessTokenResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "获取数据中...");
            }

            @Override
            public void onCacheResponse(GetAccessTokenResponse response) {

            }

            @Override
            public void onResponse(GetAccessTokenResponse response) {
                if (response == null) {
                    ToastUtil.showShortToast("用户信息获取失败，请稍后重试");
                    return;
                }

                userInfo = response.getResult().getUserInfo();

                ImageLoadUtils.setRoundHeadView(ivHeader,
                        userInfo.getAvatar(), R.drawable.default_avatar, 120);

                tvStallName.setText(userInfo.getNickname());

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

    private void confirmOrderStatus() {

        if (mIsPaying) {
            return;
        }

        PostCreateOrderRequest request = new PostCreateOrderRequest(GetUserInfoUtil.getUserInfo().getUserId() + "", "5", "", edMoneyNum.getText().toString(), "1", "", myIsPayLayout.getPay(), "", "", "", ""
                , vendorId, "1", "", new ResponseListener<PostUserOrderResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "校验订单...");
            }

            @Override
            public void onCacheResponse(PostUserOrderResponse response) {

            }

            @Override
            public void onResponse(PostUserOrderResponse response) {

                if (response != null && response.getResult() != null && response.getResult().getOrderInfo() != null) {

                    mIsPaying = true;
                    orderInfo = response.getResult().getOrderInfo();

                    PayMethodService.getInstance().startPay(mContext,
                            orderInfo, ScanCodePayActivity.this);

                } else {
                    ToastUtil.showShortToast("创建订单失败");
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

    @Override
    public void payResult(boolean isSuccess, boolean isTimeOut) {
        mIsPaying = false;

        if (isSuccess) {
            PaySuccessActivity.startCompleteActivity(mContext, orderInfo, "1");

            finish();

        } else {

        }


    }

}
