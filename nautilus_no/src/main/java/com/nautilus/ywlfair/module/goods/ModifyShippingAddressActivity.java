package com.nautilus.ywlfair.module.goods;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ShippingAddressInfo;
import com.nautilus.ywlfair.entity.request.PostShippingAddressRequest;
import com.nautilus.ywlfair.entity.request.PutEditAddressRequest;
import com.nautilus.ywlfair.entity.response.GetShippingAddressList;
import com.nautilus.ywlfair.entity.response.PostShippingAddressResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.city.CityPicker;

/**
 * Created by Administrator on 2015/12/28.
 */
public class ModifyShippingAddressActivity extends BaseActivity implements View.OnClickListener {
    private TextView TVAddSave, Provinces;
    private View LayoutKey, LayoutValue;
    private EditText Name, telephone, postCode, Address;
    private Intent intent;
    private int intentKey;
    private String cityCode, provinceCode;
    private String cityString;
    private Context mContext;
    private GetShippingAddressList mlist;
    private String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.shipping_address);
        View back = findViewById(R.id.img_back);
        LayoutKey = findViewById(R.id.layout_key);
        LayoutValue = findViewById(R.id.layout_value);
        TVAddSave = (TextView) findViewById(R.id.tv_add_save);
        Name = (EditText) findViewById(R.id.ed_name);
        telephone = (EditText) findViewById(R.id.ed_phone);
        postCode = (EditText) findViewById(R.id.ed_zip_code);
        Provinces = (TextView) findViewById(R.id.tv_select_provinces);
        Address = (EditText) findViewById(R.id.ed_address);
        back.setOnClickListener(this);
        TVAddSave.setOnClickListener(this);
        Provinces.setOnClickListener(this);
        intent = getIntent();
        intentKey = Integer.valueOf(intent.getStringExtra("key"));//0增加，1修改,2查看

        Log.e("123", "key=" + intentKey + "");
        isInput(intentKey);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_add_save:
                addAddress(intentKey);
                break;
            case R.id.tv_select_provinces:
                showCityPickDialog();
                break;


        }
    }

    /**
     * //0增加，1修改,2查看
     * 初始化0，1，2情况下的界面
     *
     * @param intentKey
     */
    void isInput(int intentKey) {//设置不能输入
        if (intentKey == 2) {
            ShippingAddressInfo shippingAddressInfo = (ShippingAddressInfo) intent.getSerializableExtra("shipping");
            look(shippingAddressInfo);
            Name.setEnabled(false);
            telephone.setEnabled(false);
            postCode.setEnabled(false);
            Provinces.setEnabled(false);
            Address.setEnabled(false);
            TVAddSave.setVisibility(View.INVISIBLE);
            LayoutKey.setVisibility(View.GONE);
        } else if (intentKey == 1) {
            position = intent.getStringExtra("position");
            int a = Integer.valueOf(position);
            GetShippingAddressList mresponse = (GetShippingAddressList) intent.getSerializableExtra("shippingAddressInfo");
            ShippingAddressInfo shippingAddressInfo = mresponse.getResult().getShippingAddressInfoList().get(a);
            look(shippingAddressInfo);
            TVAddSave.setText("保存");
        } else if (intentKey == 0) {
            LayoutKey.setVisibility(View.GONE);
            TVAddSave.setText("保存");

        }

    }


    /**
     * 点击确定修改后的操作
     *
     * @param intentKey
     */
    void addAddress(int intentKey) {

        if (isinput()) {
            if (intentKey == 0) {
                ShippingAddressRequest();
            } else if (intentKey == 1) {
                position = intent.getStringExtra("position");
                int id = Integer.valueOf(position);
                /**
                 * 判断是否修改过
                 */
                GetShippingAddressList shippingAddressInfo = (GetShippingAddressList) intent.getSerializableExtra("shippingAddressInfo");
                if (shippingAddressInfo.getResult().getShippingAddressInfoList().get(id).getAddress().equals(Address.getText().toString()) &
                        shippingAddressInfo.getResult().getShippingAddressInfoList().get(id).getConsignee().equals(Name.getText().toString()) &
                        shippingAddressInfo.getResult().getShippingAddressInfoList().get(id).getPostCode().equals(postCode.getText().toString()) &
                        shippingAddressInfo.getResult().getShippingAddressInfoList().get(id).getProvinceCity().equals(Provinces.getText().toString()) &
                        shippingAddressInfo.getResult().getShippingAddressInfoList().get(id).getTelephone().equals(telephone.getText().toString())
                        ) {
                    Toast.makeText(mContext, "请确认类容是否修改", Toast.LENGTH_LONG).show();
                } else {
                    PutEditAddress(id, shippingAddressInfo);
                }
            }

        }

    }

    /**
     * 判断输入是否合法
     *
     * @return
     */
    boolean isinput() {
        if (TextUtils.isEmpty(Name.getText().toString())) {
            ToastUtil.showShortToast("姓名不能为空");
            return false;
        } else if (TextUtils.isEmpty(telephone.getText().toString())) {
            ToastUtil.showShortToast("手机号不能为空");
            return false;
        } else if (TextUtils.isEmpty(postCode.getText().toString())) {
            ToastUtil.showShortToast("邮政编码不能为空");
            return false;
        } else if (TextUtils.isEmpty(Provinces.getText().toString())) {
            ToastUtil.showShortToast("省市不能为空");
            return false;
        } else if (TextUtils.isEmpty(Address.getText().toString())) {
            ToastUtil.showShortToast("详细地址不能为空");
            return false;
        } else if (!StringUtils.isMobileNumber(telephone.getText().toString())) {
            ToastUtil.showShortToast("手机号码不正确");
            return false;
        } else if (!StringUtils.isFormatName(Name.getText().toString())) {
            ToastUtil.showShortToast("姓名不正确");
            return false;
        } else if (!StringUtils.isZipNO(postCode.getText().toString())) {
            ToastUtil.showShortToast("邮政编码不正确");
            return false;
        }
        return true;

    }

    /**
     * 选择省市Dialog
     */
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
//        provinceCode
//                cityCode
                cityString = cityPicker.getCity_string();

                provinceCode = cityPicker.getProvinceCode();
                cityCode = cityPicker.getCity_code_string();
                Log.e("123", "provinceCode=" + provinceCode + "cityCode=" + cityCode);
                Provinces.setText(cityString);
                // confirmChange();

                pickDialog.cancel();
            }
        });

        pickDialog.show();
    }


    /**
     * 添加收货地址
     */
    private void ShippingAddressRequest() {
        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());
        PostShippingAddressRequest postShippingAddressRequest = new PostShippingAddressRequest(userId,
                Name.getText().toString(),
                provinceCode,
                cityCode,
                telephone.getText().toString(),
                Address.getText().toString(),
                postCode.getText().toString(),
                new ResponseListener<PostShippingAddressResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在提交....");
                    }

                    @Override
                    public void onCacheResponse(PostShippingAddressResponse response) {

                    }

                    @Override
                    public void onResponse(PostShippingAddressResponse response) {
                        ProgressDialog.getInstance().cancel();
                        Toast.makeText(mContext, "添加成功", Toast.LENGTH_LONG).show();
                        finish();

                    }


                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
        postShippingAddressRequest.setShouldCache(false);

        VolleyUtil.addToRequestQueue(postShippingAddressRequest);
    }

    /**
     * 初始化界面数据
     */
    void look(ShippingAddressInfo shippingAddressInfo) {

        //ShippingAddressInfo shippingAddressInfo = (ShippingAddressInfo) intent.getSerializableExtra("shipping");
//        Name, telephone, postCode, Address;Provinces
        Log.e("provinceCity", shippingAddressInfo.getProvinceCity());
        Provinces.setText(shippingAddressInfo.getProvinceCity());
        Name.setText(shippingAddressInfo.getConsignee());
        telephone.setText(shippingAddressInfo.getTelephone());
        postCode.setText(shippingAddressInfo.getPostCode());
        Address.setText(shippingAddressInfo.getAddress());
    }

    /**
     * 修改收货地址
     *
     * @param
     */
    private void PutEditAddress(int id, GetShippingAddressList mresponse) {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        mresponse = (GetShippingAddressList) intent.getSerializableExtra("shippingAddressInfo");

        if (provinceCode == null) {

            provinceCode = mresponse.getResult().getShippingAddressInfoList().get(id).getProvinceCode();
            cityCode = mresponse.getResult().getShippingAddressInfoList().get(id).getPostCode();
        }
        PutEditAddressRequest putEditAddressRequest = new PutEditAddressRequest(userId, mresponse.getResult().getShippingAddressInfoList().get(id).getId(),
                Name.getText().toString(),
                provinceCode,
                cityCode,
                telephone.getText().toString(),
                Address.getText().toString(),
                postCode.getText().toString(), new ResponseListener<PostShippingAddressResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "数据提交中....");

            }

            @Override
            public void onCacheResponse(PostShippingAddressResponse response) {

            }

            @Override
            public void onResponse(PostShippingAddressResponse response) {

                if (response.getStatus() == 0) {
                    ProgressDialog.getInstance().cancel();
                    ToastUtil.showShortToast("修改成功");
                    finish();
                }

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onFinish() {

            }
        });
        putEditAddressRequest.setShouldCache(false);

        VolleyUtil.addToRequestQueue(putEditAddressRequest);
    }


}
