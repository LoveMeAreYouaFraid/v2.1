package com.nautilus.ywlfair.module.vendor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.ShowPictureAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.SingleChoiceDialog;
import com.nautilus.ywlfair.entity.bean.AppConfig;
import com.nautilus.ywlfair.entity.bean.VendorInfo;
import com.nautilus.ywlfair.entity.bean.event.EventActiveStatus;
import com.nautilus.ywlfair.entity.bean.event.EventVendorGoodsInfo;
import com.nautilus.ywlfair.entity.request.PostCreateVendorRequest;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.webview.WebViewActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.TextViewParser;
import com.nautilus.ywlfair.widget.WrapContentHeightGridView;
import com.nautilus.ywlfair.widget.city.CityPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class RegistrationStall extends BaseActivity implements OnClickListener, SingleChoiceDialog.ItemChoiceListener {

    private Context mContext;

    private String[] verifyTypes = new String[]{"个人", "公司"};

    private String[] billTypes = new String[]{"不能开具发票", "能开普通发票", "能开增值税普通发票", "能开增值税专用发票"};

    private String[] careerTypes = new String[]{"全职", "兼职"};

    private TextView verifyType, billType, careerType;

    private EditText nameView;

    private TextView sectionView;

    private EditText phoneView, idCardEdit;

    private TextView userAgreement;

    public static VendorInfo vendorInfo;

    private TextView idCardStatus;

    private CheckBox agreeCheck;

    private JsonUtil<VendorInfo> jsonUtil;

    private int userId;

    private int changeType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration_stall);

        mContext = this;

        EventBus.getDefault().register(this);

        jsonUtil = new JsonUtil<>();

        userId = GetUserInfoUtil.getUserInfo().getUserId();

        String cacheString = PreferencesUtil.getString(Constant.PRE_KEY.VENDOR_CACHE + userId);

        if(TextUtils.isEmpty(cacheString)){
            vendorInfo = new VendorInfo();

            vendorInfo.setAuthType(-1);

            vendorInfo.setInvoiceType(-1);

            vendorInfo.setJobType(-1);

        }else {
            vendorInfo = jsonUtil.json2Bean(cacheString, VendorInfo.class.getName());

        }

        initViews();
    }

    private void initViews() {
        View backImage = findViewById(R.id.im_back);

        findViewById(R.id.tv_top_bar_right).setOnClickListener(this);

        backImage.setOnClickListener(this);

        View sectionSelect = findViewById(R.id.ll_section_select);
        sectionSelect.setOnClickListener(this);

        findViewById(R.id.ll_goods_info).setOnClickListener(this);

        findViewById(R.id.ll_brand_info).setOnClickListener(this);

        findViewById(R.id.ll_identity_verify).setOnClickListener(this);

        findViewById(R.id.ll_shop_info).setOnClickListener(this);

        findViewById(R.id.ll_verify_type).setOnClickListener(this);

        findViewById(R.id.ll_bill_type).setOnClickListener(this);

        findViewById(R.id.ll_career_type).setOnClickListener(this);

        idCardStatus = (TextView) findViewById(R.id.tv_id_verify_status);

        nameView = (EditText) findViewById(R.id.et_name);

        phoneView = (EditText) findViewById(R.id.et_phone);

        idCardEdit = (EditText) findViewById(R.id.et_id_card);

        sectionView = (TextView) findViewById(R.id.tv_section);

        userAgreement = (TextView) findViewById(R.id.tv_user_agreement);
        setUserAgreement();

        agreeCheck = (CheckBox) findViewById(R.id.cb_agree);

        verifyType = (TextView) findViewById(R.id.tv_verify_type);

        billType = (TextView) findViewById(R.id.tv_bill_type);

        careerType = (TextView) findViewById(R.id.tv_career_type);

        setDefaultInfo();
    }

    private void setDefaultInfo() {

        if (vendorInfo == null) {
            return;
        }

        if (vendorInfo.getAuthType() != -1) {
            verifyType.setText(verifyTypes[vendorInfo.getAuthType()]);

            if (vendorInfo.getAuthType() == 1) {
                verifyType.setText(vendorInfo.getCompany());
            }
        }

        if (vendorInfo.getInvoiceType() != -1) {
            billType.setText(billTypes[vendorInfo.getInvoiceType()]);
        }

        if (vendorInfo.getJobType() != -1) {
            careerType.setText(careerTypes[vendorInfo.getJobType()]);
        }

        cityCode = vendorInfo.getAddress();

        nameView.setText(vendorInfo.getName());

        phoneView.setText(vendorInfo.getPhone());

        sectionView.setText(vendorInfo.getAddressString());

        if (!TextUtils.isEmpty(vendorInfo.getIdCard())) {
            idCardEdit.setText(vendorInfo.getIdCard());
        }

        if (TextUtils.isEmpty(vendorInfo.getIdCardUrlString())) {

            idCardStatus.setText("未上传");
        } else {
            idCardStatus.setText("已上传");
        }

        if (!TextUtils.isEmpty(vendorInfo.getProductKind())) {
            showGoodsInfo();
        }

        if (!TextUtils.isEmpty(vendorInfo.getProductBrand())) {
            showBrandInfo();
        }

        if (vendorInfo.getHasShop() == 1) {
            showShopInfo();
        }

    }

    private void putInputMessageToVendor() {

        vendorInfo.setName(nameView.getText().toString());

        vendorInfo.setPhone(phoneView.getText().toString());

        vendorInfo.setAddress(cityCode);

        vendorInfo.setAddressString(sectionView.getText().toString());

        vendorInfo.setIdCard(idCardEdit.getText().toString().trim());

    }

    @Override
    public void onBackPressed() {

        putInputMessageToVendor();

        PreferencesUtil.putString(Constant.PRE_KEY.VENDOR_CACHE + userId, jsonUtil.bean2Json(vendorInfo));

        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:

                onBackPressed();

                break;

            case R.id.ll_section_select:
                showCityPickDialog();
                break;

            case R.id.ll_identity_verify:

                Intent intent = new Intent(mContext, IdentityVerifyActivity.class);

                startActivityForResult(intent, Constant.REQUEST_CODE.UP_LOAD_IDENTITY);

                break;

            case R.id.ll_goods_info:

                Intent goodsInfoIntent = new Intent(mContext, GoodsInfoActivity.class);

                goodsInfoIntent.putExtra(Constant.KEY.MODE, GoodsInfoActivity.Mode.GOODS);

                startActivity(goodsInfoIntent);

                break;

            case R.id.ll_brand_info:

                if (vendorInfo == null) {
                    return;
                }

                Intent brandInfoIntent = new Intent(mContext, GoodsInfoActivity.class);

                brandInfoIntent.putExtra(Constant.KEY.MODE, GoodsInfoActivity.Mode.BRAND);

                startActivity(brandInfoIntent);

                break;

            case R.id.ll_shop_info:

                Intent shopIntent = new Intent(mContext, ShopInfoActivity.class);

                startActivity(shopIntent);

                break;

            case R.id.tv_top_bar_right:

                createVendor();

                break;

            case R.id.ll_verify_type:
                changeType = 0;

                SingleChoiceDialog.getInstance().showDialog(mContext, verifyTypes);
                break;

            case R.id.ll_bill_type:
                changeType = 1;

                SingleChoiceDialog.getInstance().showDialog(mContext, billTypes);

                break;

            case R.id.ll_career_type:
                changeType = 2;

                SingleChoiceDialog.getInstance().showDialog(mContext, careerTypes);

                break;

        }

    }

    private void createVendor() {

        String verifyString = verifyType.getText().toString();

        if (TextUtils.isEmpty(verifyString)) {
            ToastUtil.showLongToast("请选择认证类型");
            return;
        }

        String name = nameView.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showLongToast("请填写真实姓名");
            return;
        }

        String phone = phoneView.getText().toString();

        if (!StringUtils.isMobileNumber(phone)) {
            ToastUtil.showLongToast("请填写正确的手机号码");
            return;
        }

        if (TextUtils.isEmpty(cityCode)) {
            ToastUtil.showLongToast("请选择所在地区");
            return;
        }

        String idCard = idCardEdit.getText().toString().trim();

        if (TextUtils.isEmpty(idCard)) {
            ToastUtil.showLongToast("请填写身份证号码");
            return;
        }

        if (TextUtils.isEmpty(vendorInfo.getIdCardUrlString())) {
            ToastUtil.showLongToast("请先上传身份证照片");
            return;
        }

        String billString = billType.getText().toString();

        if (TextUtils.isEmpty(billString)) {
            ToastUtil.showLongToast("请选择开具发票类型");
            return;
        }

        String careerString = careerType.getText().toString();

        if (TextUtils.isEmpty(careerString)) {
            ToastUtil.showLongToast("请选择职业类型");
            return;
        }

        if (TextUtils.isEmpty(vendorInfo.getProductKind())) {
            ToastUtil.showLongToast("请填写货品信息");
            return;
        }

        if (!agreeCheck.isChecked()) {
            ToastUtil.showLongToast("请确认阅读协议");
            return;
        }

        vendorInfo.setUserId(GetUserInfoUtil.getUserInfo().getUserId());

        putInputMessageToVendor();

        PreferencesUtil.putString(Constant.PRE_KEY.VENDOR_CACHE + userId, jsonUtil.bean2Json(vendorInfo));

        String realShopAddress = vendorInfo.getRealShopAddress();

        if (!TextUtils.isEmpty(realShopAddress)) {
            realShopAddress = realShopAddress.substring(realShopAddress.indexOf("temp") + 4);

            vendorInfo.setRealShopAddress(realShopAddress);
        }

        postCreateVendorApply();

    }

    private void postCreateVendorApply() {

        int method = GetUserInfoUtil.getUserInfo().getApplyVendorStatus() == -1 ?
                Request.Method.PUT : Request.Method.POST;

        PostCreateVendorRequest request = new PostCreateVendorRequest(method, false, vendorInfo, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "正在提交...");
            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if (response != null) {
                    ToastUtil.showShortToast("摊主申请提交成功，等待审核！");

                    setResult(RESULT_OK);

                    EventBus.getDefault().post(new EventActiveStatus(0, 1, null));

                    finish();
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
    public void onItemChoice(int position) {
        switch (changeType) {
            case 0:

                vendorInfo.setAuthType(position);

                if (position == 1) {
                    Intent intent = new Intent(mContext, EditViewActivity.class);

                    startActivityForResult(intent, Constant.REQUEST_CODE.EDIT_COMMENT);

                } else {
                    verifyType.setText(verifyTypes[position]);
                }

                break;

            case 1:
                billType.setText(billTypes[position]);

                vendorInfo.setInvoiceType(position);

                break;

            case 2:

                vendorInfo.setJobType(position);

                careerType.setText(careerTypes[position]);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case Constant.REQUEST_CODE.UP_LOAD_IDENTITY:
                    idCardStatus.setText("已上传");
                    break;

                case Constant.REQUEST_CODE.EDIT_COMMENT:
                    String company = data.getStringExtra(Constant.KEY.COMPANY);

                    verifyType.setText(company);

                    vendorInfo.setCompany(company);

                    break;

            }
        }
    }

    private void showGoodsInfo() {

        findViewById(R.id.ll_goods_info_container).setVisibility(View.VISIBLE);

        TextView categoryView = (TextView) findViewById(R.id.tv_goods_category);
        categoryView.setText("货品品类：" + vendorInfo.getProductKindName());

        String[] from = getResources().getStringArray(R.array.product_from);

        TextView goodsFromView = (TextView) findViewById(R.id.tv_goods_from);
        goodsFromView.setText("货品风格：" + from[vendorInfo.getProductFrom() - 1]);


        TextView priceRangeView = (TextView) findViewById(R.id.tv_goods_price_range);

        String priceRange = vendorInfo.getProductPrice();

        if (TextUtils.isEmpty(priceRange)) {
            priceRangeView.setVisibility(View.GONE);
        } else {
            priceRangeView.setVisibility(View.VISIBLE);

            priceRangeView.setText("价格区间：" + priceRange.replace(",", "~"));
        }

        WrapContentHeightGridView mGridView = (WrapContentHeightGridView) findViewById(R.id.ll_goods_pic_container);

        mGridView.setAdapter(new ShowPictureAdapter(this, vendorInfo.getmList()));

    }

    private void showBrandInfo() {
        findViewById(R.id.ll_brand_info_container).setVisibility(View.VISIBLE);

        TextView categoryView = (TextView) findViewById(R.id.tv_brand_name);
        categoryView.setText("品牌名称：" + vendorInfo.getProductBrand());

        WrapContentHeightGridView mGridView = (WrapContentHeightGridView) findViewById(R.id.ll_brand_pic_container);

        mGridView.setAdapter(new ShowPictureAdapter(this, vendorInfo.getBrandPicList()));
    }


    private String cityCode;

    private void showCityPickDialog() {
        View view = View.inflate(mContext, R.layout.dlg_city_pick_view, null);

        final Dialog pickDialog = new Dialog(mContext, R.style.share_dialog);

        pickDialog.setContentView(view);

        Window window = pickDialog.getWindow();
        /* 设置位置 */
        window.setGravity(Gravity.BOTTOM);
        /* 设置动画 */
        window.setWindowAnimations(R.style.umeng_socialize_shareboard_animation);

        final CityPicker cityPicker = (CityPicker) view.findViewById(R.id.city_picker);

        View confirm = view.findViewById(R.id.tv_confirm_city);
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sectionView.setText(cityPicker.getCity_string());

                cityCode = cityPicker.getProvinceCode() + ","
                        + cityPicker.getCity_code_string();
                pickDialog.cancel();
            }
        });

        pickDialog.show();
    }


    private void setUserAgreement() {
        TextViewParser textViewParser = new TextViewParser();
        textViewParser.append("阅读并接受",
                BaseInfoUtil.dip2px(16), mContext.getResources().getColor(R
                        .color.dark_gray));

        textViewParser.append("《鹦鹉螺摊主入驻须知》",
                BaseInfoUtil.dip2px(16),
                mContext.getResources().getColor(R.color.blue),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = null;
//                                AppConfig.getInstance().getAccessConfig().getkNautilusFairBoothOwnerJoinProtocol();

                        if (!TextUtils.isEmpty(url))
                            WebViewActivity.startWebViewActivity(mContext, "0", url, "0", 0, 0);
                    }
                });

        textViewParser.append("及",
                BaseInfoUtil.dip2px(16), mContext.getResources().getColor(R
                        .color.dark_gray));

        textViewParser.append("《鹦鹉螺禁售商品管理细则》",
                BaseInfoUtil.dip2px(16),
                mContext.getResources().getColor(R.color.blue),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = AppConfig.getInstance().getAccessConfig().getkNautilusFairForbidSellProtocol();

                        if (!TextUtils.isEmpty(url))
                            WebViewActivity.startWebViewActivity(mContext, "0", url, "0", 0, 0);
                    }
                });
        textViewParser.parse(userAgreement);
    }

    @Subscribe
    public void onEventMainThread(EventVendorGoodsInfo eventVendorGoodsInfo) {
        switch (eventVendorGoodsInfo.getType()) {
            case 0:
                showGoodsInfo();
                break;

            case 1:
                showBrandInfo();
                break;

            case 2:
                vendorInfo.setHasShop(1);

                showShopInfo();
                break;
        }
    }

    private void showShopInfo() {
        findViewById(R.id.ll_shop_info_container).setVisibility(View.VISIBLE);

        TextView onLineShop = (TextView) findViewById(R.id.tv_online_shop);

        String onLineShopString = vendorInfo.getOnlineShopType() + " " +
                vendorInfo.getOnlineShopName() + " " + vendorInfo.getOnLineShopAddress();

        if (TextUtils.isEmpty(onLineShopString.trim())) {
            onLineShop.setVisibility(View.GONE);
        } else {
            onLineShop.setText(onLineShopString);

            onLineShop.setVisibility(View.VISIBLE);
        }

        TextView realShop = (TextView) findViewById(R.id.tv_real_shop);

        String realAddress = vendorInfo.getRealShopAddress().replace("temp", "");

        if (!TextUtils.isEmpty(realAddress)) {
            realShop.setText(realAddress);

            realShop.setVisibility(View.VISIBLE);
        } else {
            realShop.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
