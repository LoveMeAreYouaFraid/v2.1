package com.nautilus.ywlfair.module.vendor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.AddPictureStringAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.FileUtils;
import com.nautilus.ywlfair.common.utils.PictureUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.OkHttpMultipartUpLoad;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ActivitySysConfig;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;
import com.nautilus.ywlfair.entity.bean.PictureInfo;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;
import com.nautilus.ywlfair.entity.bean.event.EventProductType;
import com.nautilus.ywlfair.entity.bean.event.EventVendorGoodsInfo;
import com.nautilus.ywlfair.entity.request.GetGoodsClassRequest;
import com.nautilus.ywlfair.entity.response.GetGoodsClassResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.active.CategoryActivity;
import com.nautilus.ywlfair.module.active.GoodsStyle;
import com.nautilus.ywlfair.module.active.SmallPublicActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.nautilus.ywlfair.widget.WrapContentHeightGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/3/28.
 */
public class GoodsInfoActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private static final String CACHE_DIR_NAME = "choose_photo";

    private static final int PIC_NUM = 5;

    private ArrayList<UpLoadPicInfo> upLoadPicList;

    private List<GoodsClassInfo> goodsClassList;

    private TextView titleView;

    private ArrayList<String> mList;

    private ArrayList<String> mSelectPath;

    private WrapContentHeightGridView mGridView;

    private AddPictureStringAdapter mAdapter;

    private TextView productTypeView;

    private TextView productFromView;

    private TextView priceView;

    private EditText brandName;

    private View necessaryDot;

    public enum Mode {
        GOODS, BRAND
    }

    private Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.goods_info_activity);

        mContext = this;

        EventBus.getDefault().register(this);

        mode = (Mode) getIntent().getSerializableExtra(Constant.KEY.MODE);

        upLoadPicList = new ArrayList<>();

        mList = new ArrayList<>();

        mSelectPath = new ArrayList<>();

        getGoodsClass();

        initViews();
    }

    private void initViews() {

        findViewById(R.id.im_back).setOnClickListener(this);

        findViewById(R.id.tv_top_bar_right).setOnClickListener(this);

        necessaryDot = findViewById(R.id.tv_necessary);

        titleView = (TextView) findViewById(R.id.tv_title);

        mGridView = (WrapContentHeightGridView) findViewById(R.id.ll_goods_pic_container);

        mAdapter = new AddPictureStringAdapter(this, mList);

        mGridView.setAdapter(mAdapter);

        if (mode == Mode.GOODS) {

            titleView.setText("货品信息");

            findViewById(R.id.ll_goods_type).setVisibility(View.VISIBLE);

            findViewById(R.id.ll_select_category).setOnClickListener(this);

            findViewById(R.id.ll_product_from).setOnClickListener(this);

            findViewById(R.id.layout_price).setOnClickListener(this);

            priceView = (TextView) findViewById(R.id.tv_price);

            View selectCategory = findViewById(R.id.ll_select_category);
            selectCategory.setOnClickListener(this);

            productTypeView = (TextView) findViewById(R.id.tv_product_category);

            productFromView = (TextView) findViewById(R.id.tv_product_from);

            setGoodsDefaultInfo();

        } else {

            titleView.setText("品牌信息");

            findViewById(R.id.ll_brand_type).setVisibility(View.VISIBLE);

            brandName = (EditText) findViewById(R.id.et_brand_name);

            TextView picTitle = (TextView) findViewById(R.id.tv_pic_title);
            picTitle.setText("添加品牌Logo图片");

            TextView picCue = (TextView) findViewById(R.id.tv_pic_cue);
            picCue.setText("（请上传透明底的Logo图片）");

            necessaryDot.setVisibility(View.INVISIBLE);

            setBrandDefaultInfo();
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if (mList.size() != 0 && position != mList.size()) {

                    Intent intent = new Intent(mContext,
                            ShowPicturesPagerActivity.class);

                    ArrayList<Uri> uris = new ArrayList<>();
//                    uris.addAll(mList);

                    for (String string : mList) {
                        uris.add(Uri.parse(string));
                    }

                    intent.putExtra(Constant.KEY.URIS, uris);
                    intent.putExtra(Constant.KEY.POSITION, position);

                    startActivityForResult(intent,
                            Constant.REQUEST_CODE.SHOW_PICTURES);

                } else {
                    choosePicture();
                }

            }
        });
    }

    private void setGoodsDefaultInfo() {
        productTypes = RegistrationStall.vendorInfo.getProductKind();

        if (!TextUtils.isEmpty(productTypes)) {

            productKindName = RegistrationStall.vendorInfo.getProductKindName();

            productTypeView.setText(productKindName);

            String[] from = getResources().getStringArray(R.array.product_from);

            productFromTag = RegistrationStall.vendorInfo.getProductFrom();

            productFromView.setText(from[productFromTag - 1]);

            priceRange = RegistrationStall.vendorInfo.getProductPrice();

            if (!TextUtils.isEmpty(priceRange)) {
                priceView.setText(priceRange.replace(",", "~"));

            }

            mList.addAll(RegistrationStall.vendorInfo.getmList());

            mSelectPath.addAll(RegistrationStall.vendorInfo.getGoodsInfoSelectPath());

            mAdapter.notifyDataSetChanged();
        }

    }

    private void setBrandDefaultInfo() {

        String brand = RegistrationStall.vendorInfo.getProductBrand();

        if (!TextUtils.isEmpty(brand)) {
            brandName.setText(brand);

            mSelectPath.addAll(RegistrationStall.vendorInfo.getBrandLogoSelectPath());

            mList.addAll(RegistrationStall.vendorInfo.getBrandPicList());

            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;

            case R.id.tv_top_bar_right:

                if (mode == Mode.GOODS) {

                    String category = productTypeView.getText().toString();

                    if (TextUtils.isEmpty(category)) {
                        Toast.makeText(MyApplication.getInstance(), "请选择商品品类",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (productFromTag == 0) {
                        Toast.makeText(MyApplication.getInstance(), "请选择商品风格",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (mList.size() < 3) {
                        ToastUtil.showLongToast("最少上传3张货品图片");
                        return;
                    }

                    upLoadPicture(0);

                } else {

                    String brandNameString = brandName.getText().toString().trim();

                    if (TextUtils.isEmpty(brandNameString)) {
                        ToastUtil.showLongToast("品牌名不能为空");

                        return;
                    }

                    if (mList.size() < 1) {
                        ToastUtil.showLongToast("请添加品牌Logo图片");

                        return;
                    }

                    ProgressDialog.getInstance().show(mContext, "图片上传中...", false);

                    upLoadPicture(0);
                }

                break;

            case R.id.ll_select_category:
                if (goodsClassList == null || goodsClassList.size() == 0) {
                    Toast.makeText(MyApplication.getInstance(), "类型加载中...",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(mContext, CategoryActivity.class);

                intent.putExtra(Constant.KEY.GOODS_CATEGORY, exchangeCategory());

                intent.putExtra(Constant.KEY.TYPE, "vendor");

                intent.putExtra(Constant.KEY.DEFAULT_TEXT, productKindName);

                startActivityForResult(intent, Constant.REQUEST_CODE.GOODS_KIND);

                break;

            case R.id.ll_product_from:

                Intent styleIntent = new Intent(mContext, GoodsStyle.class);

                startActivityForResult(styleIntent, Constant.REQUEST_CODE.GOODS_STYLE);

                break;

            case R.id.layout_price:

                showPriceRangeDialog();

                break;

        }
    }

    private ArrayList<ActivitySysConfig> exchangeCategory() {
        ArrayList<ActivitySysConfig> list = new ArrayList<>();

        for (GoodsClassInfo goodsClassInfo : goodsClassList) {
            ActivitySysConfig activitySysConfig = new ActivitySysConfig();

            activitySysConfig.setName(goodsClassInfo.getClassName());

            activitySysConfig.setId(goodsClassInfo.getId() + "");

            list.add(activitySysConfig);
        }

        return list;
    }

    private void setGoodsInfoFinish() {

        RegistrationStall.vendorInfo.setProductKindName(productTypeView.getText().toString());

        RegistrationStall.vendorInfo.setGoodsInfoSelectPath(mSelectPath);

        RegistrationStall.vendorInfo.setProductKind(productTypes);

        RegistrationStall.vendorInfo.setProductFrom(productFromTag);

        RegistrationStall.vendorInfo.setProductPrice(priceRange);

        RegistrationStall.vendorInfo.setmList(mList);

        RegistrationStall.vendorInfo.setProductUrl(getPicturesUrl());

        EventBus.getDefault().post(new EventVendorGoodsInfo(0));

        finish();
    }

    private void setBrandInfoFinish() {
        RegistrationStall.vendorInfo.setProductBrand(brandName.getText().toString().trim());

        RegistrationStall.vendorInfo.setBrandPicList(mList);

        RegistrationStall.vendorInfo.setProductLogUrl(getPicturesUrl());

        RegistrationStall.vendorInfo.setBrandLogoSelectPath(mSelectPath);

        EventBus.getDefault().post(new EventVendorGoodsInfo(1));

        finish();
    }

    private void choosePicture() {
        Intent selectIntent = new Intent(mContext, MultiImageSelectorActivity.class);
        // whether show camera
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, PIC_NUM);

        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

        if (mSelectPath != null && mSelectPath.size() > 0) {

            selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }

        startActivityForResult(selectIntent, Constant.REQUEST_CODE.SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case Constant.REQUEST_CODE.SELECT_IMAGE:
                    addPicResult(data);
                    break;

                case Constant.REQUEST_CODE.EDIT_PRICE:
                    String string = data.getStringExtra("Price");

                    priceView.setText(string);

                    priceRange = string.replace("~", ",").replace("元", "");

                    break;

                case Constant.REQUEST_CODE.SHOW_PICTURES:

                    if (data == null || !data.hasExtra(Constant.KEY.URIS)) {
                        return;
                    }

                    @SuppressWarnings("unchecked")
                    ArrayList<Uri> uris = (ArrayList<Uri>) data
                            .getSerializableExtra(Constant.KEY.URIS);

                    deleteDefaultSelectPic(uris);

                    mAdapter.notifyDataSetChanged();

                    break;

                case Constant.REQUEST_CODE.GOODS_STYLE:
                    productFromText = data.getStringExtra(Constant.KEY.GOODS_STYLE);

                    productFromTag = Integer.valueOf(data.getStringExtra(Constant.KEY.GOODS_STYLE_TAG));

                    productFromView.setText(productFromText);
                    break;

                case Constant.REQUEST_CODE.GOODS_KIND:

                    productTypes = data.getStringExtra(Constant.KEY.PRODUCT_KIND_ID);

                    productKindName = data.getStringExtra(Constant.KEY.PRODUCT_KIND);

                    productTypeView.setText(productKindName);

                    break;
            }
        }
    }

    private void deleteDefaultSelectPic(ArrayList<Uri> uris) {


        if (mSelectPath == null) {
            return;
        }

        if (uris.size() == 0) {
            mList.clear();

            mSelectPath.clear();
        }

        for (int i = mList.size() - 1; i > -1; i--) {

            Uri uri = Uri.parse(mList.get(i));

            if (!uris.contains(uri)) {

                mList.remove(i);

                mSelectPath.remove(i);
            }
        }

    }

    private void addPicResult(Intent data) {

        mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

        if (mSelectPath != null && mSelectPath.size() > 0) {

            mList.clear();

            for (String filePath : mSelectPath) {

                PictureInfo pictureInfo = PictureUtils.processPictureFile(
                        CACHE_DIR_NAME, filePath);

                Uri newUri = Uri.fromFile(pictureInfo.getFile());

                mList.add(mList.size(), newUri.toString());
            }

            mAdapter.notifyDataSetChanged();

        }
    }

    private int upLoadNum = 0;

    private void upLoadPicture(int number) {

        upLoadNum = number;

        Uri uri = Uri.parse(mList.get(number));

        OkHttpMultipartUpLoad.getInstance().postPictures(
                String.valueOf(1), FileUtils.uri2Path(uri),
                new UpLoadPictureCallBack());
    }

    class UpLoadPictureCallBack implements OkHttpMultipartUpLoad.UploadFileCallBack {

        @Override
        public void uploadStart() {
            handler.sendEmptyMessage(0);
        }

        @Override
        public void uploadSuccess(UpLoadPicInfo upLoadPicInfo) {
            Message message = handler.obtainMessage();

            message.obj = upLoadPicInfo;

            message.what = 1;

            message.sendToTarget();
        }

        @Override
        public void uploadError(Exception e) {
            Message message = handler.obtainMessage();

            message.obj = e.getMessage() + "";

            message.what = 2;

            message.sendToTarget();
        }

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 0:
                    if (upLoadNum == 0)
                        ProgressDialog.getInstance().show(mContext, "图片上传中...");
                    break;

                case 1:
                    UpLoadPicInfo upLoadPicInfo = (UpLoadPicInfo) msg.obj;

                    upLoadPicList.add(upLoadPicInfo);

                    if (upLoadNum < mList.size() - 1) {

                        upLoadNum++;

                        upLoadPicture(upLoadNum);
                    } else {
                        ProgressDialog.getInstance().cancel();

                        if (mode == Mode.GOODS) {
                            setGoodsInfoFinish();
                        } else {
                            setBrandInfoFinish();
                        }

                    }
                    break;

                case 2:
                    ProgressDialog.getInstance().cancel();

                    ToastUtil.showLongToast(msg.obj + "");

                    break;
            }
        }

    };

    private String getPicturesUrl() {

        if (upLoadPicList.size() == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for (UpLoadPicInfo pic : upLoadPicList) {
                sb.append(pic.getNormalPicturePath());

                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);

            return sb.toString();
        }
    }

    private String productTypes;

    private String productKindName;

    @Subscribe
    public void onEventMainThread(EventProductType eventProductType) {

        productTypes = eventProductType.getTypeId();

        productKindName = eventProductType.getCategory();

        productTypeView.setText(productKindName);
    }

    private void getGoodsClass() {
        GetGoodsClassRequest request = new GetGoodsClassRequest(new ResponseListener<GetGoodsClassResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(GetGoodsClassResponse response) {
                if (response != null) {
                    goodsClassList = response.getResult().getGoodsClassInfoList();
                }
            }

            @Override
            public void onResponse(GetGoodsClassResponse response) {
                if (response == null || response.getResult() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");

                    return;
                }

                goodsClassList = response.getResult().getGoodsClassInfoList();

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

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    private String productFromText;

    private int productFromTag = 0;

    private String priceRange;

    private void showPriceRangeDialog() {//价格区间

        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final TextView price_range1, price_range2, price_range3, price_range4, price_range5, price_range6, price_range7, price_range8, no;
        dialog.setContentView(R.layout.price_range_dialig);
        price_range1 = (TextView) dialog.findViewById(R.id.price_range1);
        price_range2 = (TextView) dialog.findViewById(R.id.price_range2);
        price_range3 = (TextView) dialog.findViewById(R.id.price_range3);
        price_range4 = (TextView) dialog.findViewById(R.id.price_range4);
        price_range5 = (TextView) dialog.findViewById(R.id.price_range5);
        price_range6 = (TextView) dialog.findViewById(R.id.price_range6);
        price_range7 = (TextView) dialog.findViewById(R.id.price_range7);
        price_range8 = (TextView) dialog.findViewById(R.id.price_range8);
        no = (TextView) dialog.findViewById(R.id.no);
        price_range8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceView.setText("");

                priceRange = "";

                dialog.cancel();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        price_range1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                priceView.setText("0~300元");

                priceRange = "0,300";

                dialog.cancel();


            }
        });
        price_range2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceView.setText("300~500元");

                priceRange = "300,500";

                dialog.cancel();
            }
        });
        price_range3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceView.setText("500~800元");

                priceRange = "500,800";

                dialog.cancel();
            }
        });
        price_range4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceView.setText("800~1000元");

                priceRange = "800,1000";

                dialog.cancel();
            }
        });
        price_range5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceView.setText("1000~2000元");

                priceRange = "1000,2000";

                dialog.cancel();
            }
        });
        price_range6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceView.setText("2000~5000元");

                priceRange = "2000,5000";

                dialog.cancel();
            }
        });
        price_range7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, SmallPublicActivity.class)
                        .putExtra("key", "Price"), Constant.REQUEST_CODE.EDIT_PRICE);
                dialog.cancel();
            }
        });

        dialog.show();


    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
