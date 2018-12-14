package com.nautilus.ywlfair.module.vendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.GetVendorInfo;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.GetVendorInfoRequest;
import com.nautilus.ywlfair.entity.response.GetVendorInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowImagesPagerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class VendorInfoActivity extends BaseActivity implements
        OnClickListener {

    private Context mContext;

    private UserInfo currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vendor_info_activity);

        mContext = this;

        currentUser = GetUserInfoUtil.getUserInfo();

        View topBarBack = findViewById(R.id.tv_top_bar_back);
        topBarBack.setOnClickListener(this);

        getData();

    }

    private void initViews(final GetVendorInfo vendorInfo) {

        TextView name = (TextView) findViewById(R.id.tv_name);
        name.setText(vendorInfo.getName());

        TextView phone = (TextView) findViewById(R.id.tv_phone);
        phone.setText(vendorInfo.getPhone());

        TextView address = (TextView) findViewById(R.id.tv_address);
        address.setText(vendorInfo.getAddress());

        TextView brandName = (TextView) findViewById(R.id.tv_brand_name);
        brandName.setText(vendorInfo.getProductBrand());

        ImageView brandLogo = (ImageView) findViewById(R.id.iv_brand_logo);
        ImageLoadUtils.setItemImageView(brandLogo,
                vendorInfo.getProductLogUrl(), R.drawable.default_image,
                ImageScaleType.EXACTLY, false);

        brandLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showBigImage(vendorInfo.getProductLogUrl());
            }
        });

        TextView shopAddress = (TextView) findViewById(R.id.tv_shop_address);
        shopAddress.setText(vendorInfo.getRealShopCity() + vendorInfo.getRealShopAddress());

        TextView productType = (TextView) findViewById(R.id.tv_product_type);
        productType.setText(vendorInfo.getProductKindName());

        TextView productFrom = (TextView) findViewById(R.id.tv_product_from);
        String[] froms = getResources().getStringArray(R.array.product_from);
        int from = vendorInfo.getProductFrom() - 1;

        if (from < froms.length) {
            productFrom.setText(froms[from]);
        }

        TextView priceSection = (TextView) findViewById(R.id.tv_price_section);
        priceSection.setText(vendorInfo.getProductPrice().replace(",", " - "));

        showPictures(vendorInfo.getProductPicUrls());

        TextView onLineShop = (TextView) findViewById(R.id.tv_online_shop);
        onLineShop.setText(vendorInfo.getOnlineShopType() + " " + vendorInfo.getOnlineShopName() + " " + vendorInfo.getOnLineShopAddress());

        TextView idCardView = (TextView) findViewById(R.id.tv_id_card);
        idCardView.setText(vendorInfo.getIdCard());

        if(!TextUtils.isEmpty(vendorInfo.getIdCard())){
            TextView isAuth = (TextView) findViewById(R.id.tv_is_auth);

            isAuth.setText("已认证");
        }

    }

    private void showBigImage(String imagePath) {
        List<PicInfo> list = new ArrayList<>();

        PicInfo picInfo = new PicInfo();

        picInfo.setImgUrl(imagePath);

        picInfo.setThumbnailUrl(imagePath);

        list.add(picInfo);

        Intent intent = new Intent(mContext,
                ShowImagesPagerActivity.class);

        intent.putExtra(Constant.KEY.PICINFO_LIST,
                (ArrayList<PicInfo>) list);
        intent.putExtra(Constant.KEY.POSITION, 0);

        mContext.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;
        }

    }

    private void getData() {
        GetVendorInfoRequest request = new GetVendorInfoRequest(currentUser.getUserId() + "",
                new ResponseListener<GetVendorInfoResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "加载中...");
                    }

                    @Override
                    public void onCacheResponse(GetVendorInfoResponse response) {
                        if (response != null && response.getResult().getVendor() != null) {

                        }
                    }

                    @Override
                    public void onResponse(GetVendorInfoResponse response) {
                        if (response == null || response.getResult().getVendor() == null) {
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                            return;
                        }

                        initViews(response.getResult().getVendor());
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

    private void showPictures(final List<PicInfo> picInfoList) {

        TableRow row1 = (TableRow) findViewById(R.id.tr_row1);

        TableRow row2 = (TableRow) findViewById(R.id.tr_row2);

        AutoAdjustHeightImageView[] imageViews = new AutoAdjustHeightImageView[8];

        imageViews[0] = (AutoAdjustHeightImageView) findViewById(R.id.iv_multi_pics_0);

        imageViews[1] = (AutoAdjustHeightImageView) findViewById(R.id.iv_multi_pics_1);

        imageViews[2] = (AutoAdjustHeightImageView) findViewById(R.id.iv_multi_pics_2);

        imageViews[3] = (AutoAdjustHeightImageView) findViewById(R.id.iv_multi_pics_3);

        imageViews[4] = (AutoAdjustHeightImageView) findViewById(R.id.iv_multi_pics_4);

        imageViews[5] = (AutoAdjustHeightImageView) findViewById(R.id.iv_multi_pics_5);

        imageViews[6] = (AutoAdjustHeightImageView) findViewById(R.id.iv_multi_pics_6);

        imageViews[7] = (AutoAdjustHeightImageView) findViewById(R.id.iv_multi_pics_7);

        if (picInfoList != null) {
            if (picInfoList.size() > 3) {
                row1.setVisibility(View.VISIBLE);

                row2.setVisibility(View.VISIBLE);
            } else if (picInfoList.size() > 0 && picInfoList.size() <= 3) {
                row1.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < picInfoList.size(); i++) {

                if (i >= imageViews.length) {
                    continue;
                }
                ImageLoader.getInstance().displayImage(
                        picInfoList.get(i).getThumbnailUrl(), imageViews[i], ImageLoadUtils.createNoRoundedOptions());
                imageViews[i].setTag(i);

                imageViews[i].setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext,
                                ShowImagesPagerActivity.class);

                        intent.putExtra(Constant.KEY.PICINFO_LIST,
                                (ArrayList<PicInfo>) picInfoList);
                        intent.putExtra(Constant.KEY.POSITION,
                                (Integer) v.getTag());

                        mContext.startActivity(intent);
                    }
                });

            }
        }

    }
}
