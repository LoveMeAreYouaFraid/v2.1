package com.nautilus.ywlfair.module.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.PayMethodResultListener;
import com.nautilus.ywlfair.component.PayMethodService;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.bean.ShippingAddressInfo;
import com.nautilus.ywlfair.entity.bean.SkuInfo;
import com.nautilus.ywlfair.entity.request.GetShippingAddressListRequest;
import com.nautilus.ywlfair.entity.request.PostCreateOrderRequest;
import com.nautilus.ywlfair.entity.response.GetShippingAddressList;
import com.nautilus.ywlfair.entity.response.PostUserOrderResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.active.CompleteOrderActivity;
import com.nautilus.ywlfair.widget.PayMethodSelectLayout;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.RippleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 点击商品购买后的也订单页面
 * 订单修改页
 * Created by Administrator on 2015/12/29.
 */
public class OrderSelectionActivity extends BaseActivity implements View.OnClickListener,
        RippleView.OnEndClickListener, PayMethodResultListener {

    private EditText buyersMessage;
    private Context mContext;
    private TextView
            receiver,
            address,
            VendorNickname,
            phoneNum,
            tvShipment,
            tvTotalPrice;
    private PayMethodSelectLayout payMethodSelectLayout;
    private String[] payChannels = new String[]{"ALI", "WX"};
    private String channelType = "ALI";
    private Intent intent;

    private ShippingAddressInfo shippingAddressInfo;
    private GoodsInfo goodsInfo;
    private RippleView determine;

    private SkuInfo skuInfo;

    private int goodsNum = 1;

    private EditText numEdit;

    private View limitError;

    private View receiveAddress;

    private TextView goodsNameView;

    private RippleView addAddress;

    private TextView skuValueView;

    private ImageView goodsImage;

    private TextView goodsPrice;

    private TextView goodsNumberView;

    private boolean mIsPaying = false;//是否正在启动支付

    private OrderInfo orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirm_order);

        mContext = this;

        intent = getIntent();

        goodsInfo = (GoodsInfo) getIntent().getSerializableExtra(Constant.KEY.GOODS_INFO);

        skuInfo = (SkuInfo) intent.getSerializableExtra(Constant.KEY.SKU_INFO);

        goodsNum = intent.getIntExtra(Constant.KEY.NUMBER, 1);

        if (goodsInfo == null || skuInfo == null) {
            ToastUtil.showLongToast("商品不存在");

            finish();

            return;
        }

        initViews();

        loadAddressList();

        setValue();
    }

    private void initViews() {
        View Subtract = findViewById(R.id.iv_subtract);
        View Add = findViewById(R.id.iv_add);
        View back = findViewById(R.id.back);

        Subtract.setOnClickListener(this);
        back.setOnClickListener(this);
        Add.setOnClickListener(this);

        numEdit = (EditText) findViewById(R.id.et_goods_num);
        numEdit.setText(goodsNum + "");

        addAddress = (RippleView) findViewById(R.id.tv_add_address);
        addAddress.setOnEndClickListener(addAddress.getId(), this);

        receiveAddress = findViewById(R.id.ll_receive_address);

        limitError = findViewById(R.id.limit_error);

        goodsNameView = (TextView) findViewById(R.id.tv_goods_name);

        goodsImage = (ImageView) findViewById(R.id.iv_goods_image);

        skuValueView = (TextView) findViewById(R.id.tv_sku_value);

        goodsPrice = (TextView) findViewById(R.id.tv_goods_price);

        goodsNumberView = (TextView) findViewById(R.id.tv_goods_number);

        buyersMessage = (EditText) findViewById(R.id.buyers_message);
        tvShipment = (TextView) findViewById(R.id.tv_shipment);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        determine = (RippleView) findViewById(R.id.tv_determine);

        VendorNickname = (TextView) findViewById(R.id.tv_vendor_nick_name);
        receiver = (TextView) findViewById(R.id.receiver);
        address = (TextView) findViewById(R.id.address);
        phoneNum = (TextView) findViewById(R.id.phone_num);

        View LookAddress = findViewById(R.id.layout_look_shipping_address);

        determine.setOnEndClickListener(determine.getId(), this);
        LookAddress.setOnClickListener(this);

        payMethodSelectLayout = (PayMethodSelectLayout) findViewById(R.id.pay_method_select);

        payMethodSelectLayout.setListener(new PayMethodSelectLayout.OnItemSelectListener() {
            @Override
            public void onItemSelect(int position) {
                channelType = payChannels[position];
            }
        });

        numEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (numEdit.getText().length() == 0) {
                    numEdit.setText("1");
                }
                if (TextUtils.isEmpty(numEdit.getText().toString())) {
                    goodsNum = 0;
                } else {
                    goodsNum = Integer.valueOf(numEdit.getText().toString());
                }

                if (goodsNum > skuInfo.getLeftNum()) {
                    numEdit.setTextColor(Color.RED);

                    limitError.setVisibility(View.VISIBLE);
                } else {
                    numEdit.setTextColor(getResources().getColor(R.color.normal_black));

                    limitError.setVisibility(View.GONE);
                }

                goodsNumberView.setText("X" + goodsNum);

                tvTotalPrice.setText(StringUtils.getMoneyFormat(goodsNum * skuInfo.getPrice() + goodsInfo.getCourierFee()));
            }
        });

    }

    private void setValue() {
        ImageLoader.getInstance().displayImage(skuInfo.getSkuAttrImageUrl(), goodsImage, ImageLoadUtils.createNoRoundedOptions());

        goodsNameView.setText(goodsInfo.getGoodsName());

        skuValueView.setText(skuInfo.getSkuAttrValue() + "");

        goodsPrice.setText("￥ " + skuInfo.getPrice());

        goodsNumberView.setText("X" + goodsNum);

        tvShipment.setText(StringUtils.getCourierFeeString(goodsInfo.getCourierFee()));

        VendorNickname.setText("卖家姓名：" + goodsInfo.getVendorNickname());

        tvTotalPrice.setText(StringUtils.getMoneyFormat(goodsNum * skuInfo.getPrice() + goodsInfo.getCourierFee()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_look_shipping_address:
                startActivityForResult(new Intent(mContext, ReceivingAddressListActivity.class), 1);
                break;
            case R.id.back:
                finish();
                break;

            case R.id.iv_add:

                if (TextUtils.isEmpty(numEdit.getText().toString())) {
                    goodsNum = 0;
                } else {
                    goodsNum = Integer.valueOf(numEdit.getText().toString());
                }

                if (goodsNum >= 9999) {

                    numEdit.setText("9999");
                } else {
                    goodsNum++;

                    numEdit.setText(goodsNum + "");
                }

                if (goodsNum > skuInfo.getLeftNum()) {
                    numEdit.setTextColor(Color.RED);

                    limitError.setVisibility(View.VISIBLE);
                }

                goodsNumberView.setText("X" + goodsNum);

                tvTotalPrice.setText(StringUtils.getMoneyFormat(goodsNum * skuInfo.getPrice() + goodsInfo.getCourierFee()));

                break;
            case R.id.iv_subtract:

                if (TextUtils.isEmpty(numEdit.getText().toString())) {
                    goodsNum = 1;
                } else {
                    goodsNum = Integer.valueOf(numEdit.getText().toString());
                }

                if (goodsNum == 1 || goodsNum == 0) {
                    numEdit.setText(goodsNum + "");
                } else {

                    goodsNum--;

                    numEdit.setText(goodsNum + "");
                }

                if (goodsNum <= skuInfo.getLeftNum()) {
                    numEdit.setTextColor(getResources().getColor(R.color.normal_black));

                    limitError.setVisibility(View.GONE);
                }

                goodsNumberView.setText("X" + goodsNum);

                tvTotalPrice.setText(StringUtils.getMoneyFormat(goodsNum * skuInfo.getPrice() + goodsInfo.getCourierFee()));

                break;


        }

    }


    /**
     * 显示收货地址
     */
    public void loadAddressList() {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        GetShippingAddressListRequest getShippingAddressListRequest = new GetShippingAddressListRequest(userId,
                new ResponseListener<GetShippingAddressList>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCacheResponse(GetShippingAddressList response) {

                    }

                    @Override
                    public void onResponse(GetShippingAddressList response) {


                        if (response != null && response.getResult() != null && response.getResult().getShippingAddressInfoList() != null) {
                            setAddressList(response.getResult().getShippingAddressInfoList());
                        } else {
                            ToastUtil.showLongToast("收货地址数据异常");
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            Toast.makeText(MyApplication.getInstance(), response.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            addAddress.setTag(1);
                            Toast.makeText(MyApplication.getInstance(), "获取数据失败，请您稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
        getShippingAddressListRequest.setShouldCache(true);

        VolleyUtil.addToRequestQueue(getShippingAddressListRequest);

    }

    private void confirmOrder() {

        if (mIsPaying) {
            return;
        }

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        String userMessage = buyersMessage.getText().toString();

        String phoneNumber = shippingAddressInfo.getTelephone();

        String addressId = shippingAddressInfo.getId();

        PostCreateOrderRequest request = new PostCreateOrderRequest(userId, "3", skuInfo.getSkuId() + "", skuInfo.getPrice()+"", goodsNum+"",
                phoneNumber, channelType, userMessage, addressId, "", "", "", "", "", new ResponseListener<PostUserOrderResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在提交...");
            }

            @Override
            public void onCacheResponse(PostUserOrderResponse response) {

            }

            @Override
            public void onResponse(PostUserOrderResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getOrderInfo() != null) {

                    orderInfo = response.getResult().getOrderInfo();

                    mIsPaying = true;

                    PayMethodService.getInstance().startPay(mContext, orderInfo, OrderSelectionActivity.this);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                addAddress.setVisibility(View.GONE);
                receiveAddress.setVisibility(View.VISIBLE);
                shippingAddressInfo = (ShippingAddressInfo) data.getSerializableExtra("shipping");

                setAddressValue();
            }

        }
        if (resultCode == 0) {
            addAddress.setVisibility(View.VISIBLE);
            receiveAddress.setVisibility(View.GONE);

        }

    }

    public void setAddressList(List<ShippingAddressInfo> addressList) {
        if (addressList.size() == 0) {
            addAddress.setVisibility(View.VISIBLE);

            receiveAddress.setVisibility(View.GONE);
        } else {

            shippingAddressInfo = addressList.get(0);

            setAddressValue();
        }

    }

    public void setAddressValue() {

        addAddress.setVisibility(View.GONE);

        receiveAddress.setVisibility(View.VISIBLE);

        address.setText("收货地址：" + shippingAddressInfo.getProvinceCity() + shippingAddressInfo.getAddress());

        phoneNum.setText(shippingAddressInfo.getTelephone());

        receiver.setText("收货人：" + shippingAddressInfo.getConsignee());
    }

    @Override
    public void mViewClick(int id) {
        switch (id) {
            case R.id.tv_determine:

                if (shippingAddressInfo == null) {
                    ToastUtil.showLongToast("请添加收货地址");
                    return;
                }

                confirmOrder();
                break;

            case R.id.tv_add_address:
                startActivityForResult(new Intent(mContext, ReceivingAddressListActivity.class), 1);

                break;

        }

    }

    @Override
    public void payResult(boolean isSuccess, boolean isTimeOut) {
        mIsPaying = false;

        if (isSuccess) {
            CompleteOrderActivity.startCompleteActivity(mContext, orderInfo);

        } else {

            if (!isTimeOut) {

                Intent intent = new Intent(mContext, AgainOrderSelectionActivity.class);

                intent.putExtra(Constant.KEY.ORDER_ID, orderInfo.getOrderId());

                mContext.startActivity(intent);
            }
        }

        finish();
    }

}
