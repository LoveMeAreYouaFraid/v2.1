package com.nautilus.ywlfair.module.vendor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.entity.bean.event.EventVendorGoodsInfo;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.city.CityPicker;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ShopInfoActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private EditText onLineShopNameView, onLineShopAddressView,realShopAddress;

    private TextView shopCity, platformView;

    private String realShopCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shop_info_activity);

        mContext = this;

        initViews();
    }

    private void initViews() {

        findViewById(R.id.im_back).setOnClickListener(this);

        findViewById(R.id.tv_top_bar_right).setOnClickListener(this);

        findViewById(R.id.ll_online_shop).setOnClickListener(this);

        findViewById(R.id.ll_choose_city).setOnClickListener(this);

        onLineShopNameView = (EditText) findViewById(R.id.et_online_shop_name);

        onLineShopAddressView = (EditText) findViewById(R.id.et_shop_url);

        realShopAddress = (EditText) findViewById(R.id.et_shop_address_detail);

        shopCity = (TextView) findViewById(R.id.tv_shop_city);

        platformView = (TextView) findViewById(R.id.tv_platform);

        setDefaultInfo();
    }

    private void setDefaultInfo(){

        platformView.setText(RegistrationStall.vendorInfo.getOnlineShopType());

        String shopName = RegistrationStall.vendorInfo.getOnlineShopName();
        if(!TextUtils.isEmpty(shopName)){
            onLineShopNameView.setText(shopName);
        }

        String shopPath = RegistrationStall.vendorInfo.getOnLineShopAddress();
        if(!TextUtils.isEmpty(shopPath)){
            onLineShopAddressView.setText(shopPath);
        }

        String address = RegistrationStall.vendorInfo.getRealShopAddress();

        if(address != null){
            String[] strings = address.split("temp");

            if(strings.length > 0)
                shopCity.setText(strings[0]);

            if(strings.length > 1)
                realShopAddress.setText(strings[1]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;

            case R.id.tv_top_bar_right:

                confirmShopInfo();

                break;

            case R.id.ll_online_shop:

                showGenderPopMenu();

                break;

            case R.id.ll_choose_city:

                showCityPickDialog();
                break;

        }
    }

    private void confirmShopInfo(){

        String onLineShopName = onLineShopNameView.getText().toString();

        String onLineShopAddress = onLineShopAddressView.getText()
                .toString();

        String onLineShopType = platformView.getText().toString();

        if(TextUtils.isEmpty(onLineShopName) && TextUtils.isEmpty(shopCity.getText().toString())){

            ToastUtil.showLongToast("请填写店铺信息再提交!");

            return;
        }

        RegistrationStall.vendorInfo.setOnlineShopType(onLineShopType);

        RegistrationStall.vendorInfo.setOnlineShopName(onLineShopName);

        RegistrationStall.vendorInfo.setOnLineShopAddress(onLineShopAddress);

        RegistrationStall.vendorInfo.setRealShopAddress(shopCity.getText().toString() + "temp" + realShopAddress.getText().toString());

        RegistrationStall.vendorInfo.setRealShopCity(realShopCity);

        EventBus.getDefault().post(new EventVendorGoodsInfo(2));

        finish();
    }

    private void showCityPickDialog() {
        View view = View.inflate(mContext, R.layout.dlg_city_pick_view, null);

        final Dialog pickDialog = new Dialog(mContext, R.style.share_dialog);

        pickDialog.setContentView(view);

        Window window = pickDialog.getWindow();
        /* 设置位置 */
        window.setGravity(Gravity.BOTTOM);
        /* 设置动画 */
        window.setWindowAnimations(R.style.umeng_socialize_shareboard_animation);

        final CityPicker cityPicker = (CityPicker) view
                .findViewById(R.id.city_picker);

        View confirm = view.findViewById(R.id.tv_confirm_city);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shopCity.setText(cityPicker.getCity_string());

                realShopCity = cityPicker.getProvinceCode() + ","
                        + cityPicker.getCity_code_string();

                pickDialog.cancel();
            }
        });

        pickDialog.show();
    }

    private String platformText;

    /**
     * 显示类型选择dialog
     */
    private void showGenderPopMenu() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,
                AlertDialog.THEME_HOLO_LIGHT);

        final String[] from;

            builder.setTitle("请选择平台");

            from = getResources().getStringArray(R.array.store_type);

            platformText = from[0];

        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        builder.setSingleChoiceItems(from, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            platformText = from[which];


                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    platformView.setText(platformText);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }

}
