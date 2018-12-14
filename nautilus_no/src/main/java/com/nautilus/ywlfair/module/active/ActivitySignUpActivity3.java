package com.nautilus.ywlfair.module.active;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.FileUtils;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.PictureUtils;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.OkHttpMultipartUpLoad;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.AddViewClassInfo;
import com.nautilus.ywlfair.entity.bean.BoothApplicationInfo;
import com.nautilus.ywlfair.entity.bean.PictureInfo;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;
import com.nautilus.ywlfair.entity.bean.event.EventActiveStatus;
import com.nautilus.ywlfair.entity.request.PostBoothApplication;
import com.nautilus.ywlfair.entity.response.GetActivityBoothApplicationConfigResponse;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/3/3.
 */
public class ActivitySignUpActivity3 extends Activity implements View.OnClickListener {
    private TextView appTitle, appTitleRight, specNeedsTitle, sponsoringResources, physicalStore,
            isPhysicalStore, physicalStoreAdders, specNeeds, mediaResMsg, logoUpImage, city, tvcity;
    private Context mContext;
    private static final int SPECIAL_REQUIREMENT = 90;
    public static String actid = "Caching" + GetUserInfoUtil.getUserInfo().getUserId() + "";
    private static final int SPONSORING_RESOURCES = 89;
    private static final int IS_PHYSICAL_STORE_DIALOG = 88;
    private static final int PRICE = 87;
    private static final String SPEC_NEEDS = "specNeeds";
    private static final String MEDIA_RES_MSG = "mediaResMsg";
    private static final String CACHE_DATA = "cache_data";
    private Intent intent;
    private LinearLayout logUpImage;
    private ArrayList<Uri> mList;
    private List<UpLoadPicInfo> upLoadPicList;
    private static final String CACHE_DIR_NAME = "choose_photo";

    private static final String SHOP_ADDRESS = "Shopaddress";
    private final int PIC_NUM = 3;
    private int upLoadNum = 0;
    private static final int CITY = 871;
    private TextView tvPrice, Price;
    private GetActivityBoothApplicationConfigResponse init;
    private JsonUtil<BoothApplicationInfo> jsonUtil;

    private ArrayList<String> mSelectPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity3);
        mContext = this;
        intent = getIntent();
        upLoadPicList = new ArrayList<>();
        mList = new ArrayList<>();
        mSelectPath = new ArrayList<>();
        jsonUtil = new JsonUtil();

        View price = findViewById(R.id.layout_price);
        View back = findViewById(R.id.img_back);
        tvcity = (TextView) findViewById(R.id.tv_city);
        city = (TextView) findViewById(R.id.city);
        Price = (TextView) findViewById(R.id.price);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        physicalStore = (TextView) findViewById(R.id.physical_store);
        logUpImage = (LinearLayout) findViewById(R.id.ll_pic_containnn);
        isPhysicalStore = (TextView) findViewById(R.id.is_physical_store);
        physicalStoreAdders = (TextView) findViewById(R.id.physical_store_adders);
        specNeeds = (TextView) findViewById(R.id.tv_special_requirement);
        mediaResMsg = (TextView) findViewById(R.id.tv_sponsoring_resources);
        physicalStore.setOnClickListener(this);
        appTitle = (TextView) findViewById(R.id.tv_title);
        specNeedsTitle = (TextView) findViewById(R.id.spec_needs_title);
        appTitleRight = (TextView) findViewById(R.id.tv_right_btn);
        sponsoringResources = (TextView) findViewById(R.id.sponsoring_resources);
        logoUpImage = (TextView) findViewById(R.id.logo_up_image);
        sponsoringResources.setOnClickListener(this);
        specNeedsTitle.setOnClickListener(this);
        city.setOnClickListener(this);
        back.setOnClickListener(this);
        price.setOnClickListener(this);
        init = (GetActivityBoothApplicationConfigResponse) intent.getExtras().getSerializable(Constant.KEY.BOOTH_CONFIG);
        appTitleRight.setText("提交");
        appTitleRight.setOnClickListener(this);
        appTitleRight.setVisibility(View.VISIBLE);
        appTitle.setText("活动报名");


        if (ActivitySignUpActivity1.booth.getmSelectPath() != null) {
            mSelectPath = ActivitySignUpActivity1.booth.getmSelectPath();

        }

        resolveLogos();

        RefreshData();
    }

    private void RefreshData() {
        if (ActivitySignUpActivity1.booth.getBoothBuyType().equals("1")) {
            tvPrice.setText("7.货品价格区间");
            physicalStore.setText("8.是否有实体店");
            logoUpImage.setText("9.品牌logo");
            specNeedsTitle.setText("10.特殊需求");
            sponsoringResources.setText("11.媒体和赞助资源");
            city.setText("12.您可以参加一下那些城市的活动");
        } else {
            tvPrice.setText("9.货品价格区间");
            physicalStore.setText("10.是否有实体店");
            logoUpImage.setText("11.品牌logo");
            specNeedsTitle.setText("12.特殊需求");
            sponsoringResources.setText("13.媒体和赞助资源");
            city.setText("14.您可以参加一下那些城市的活动");
            if (ActivitySignUpActivity1.booth.getRealshopFlagText().length() > 1) {
                physicalStoreAdders.setVisibility(View.VISIBLE);
                physicalStoreAdders.setText(ActivitySignUpActivity1.booth.getRealshopFlagText());
            }
        }
        if (ActivitySignUpActivity1.booth.getStuffPriceAreaText().length() > 1) {
            Price.setText(ActivitySignUpActivity1.booth.getStuffPriceAreaText());
            Price.setVisibility(View.VISIBLE);
        }
        if (ActivitySignUpActivity1.booth.getRealshopFlag().equals("1")) {
            physicalStoreAdders.setVisibility(View.VISIBLE);
            physicalStoreAdders.setText(ActivitySignUpActivity1.booth.getRealshopFlagText());
            isPhysicalStore.setText("是");
        } else {
            physicalStoreAdders.setVisibility(View.GONE);
            isPhysicalStore.setText("否");
        }
        if (ActivitySignUpActivity1.booth.getSpecNeeds().length() > 1) {
            specNeeds.setVisibility(View.VISIBLE);
            specNeeds.setText(ActivitySignUpActivity1.booth.getSpecNeeds());
        }
        if (ActivitySignUpActivity1.booth.getMediaResMsg().length() > 1) {
            mediaResMsg.setVisibility(View.VISIBLE);
            mediaResMsg.setText(ActivitySignUpActivity1.booth.getMediaResMsg());
        }
        if (ActivitySignUpActivity1.booth.getCitySurveyMsgText().length() > 1) {
            tvcity.setVisibility(View.VISIBLE);
            tvcity.setText(ActivitySignUpActivity1.booth.getCitySurveyMsgText());
        }

    }

    private void choosePictureFromAlbum() {

        Intent selectIntent = new Intent(mContext, MultiImageSelectorActivity.class);
        // whether show camera
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // max select image amount
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, PIC_NUM);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // default select images (support array list)

        if (mSelectPath != null && mSelectPath.size() > 0) {

            selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }

        startActivityForResult(selectIntent, Constant.REQUEST_CODE.SELECT_IMAGE);
    }


    private void resolveLogos() {

        logUpImage.removeAllViews();

        if (mSelectPath != null) {

            mList.clear();

            for (int i = 0; i <= mSelectPath.size(); i++) {

                ImageView imageView = new ImageView(mContext);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        BaseInfoUtil.dip2px(100), BaseInfoUtil.dip2px(100));

                params.setMargins(0, 0, BaseInfoUtil.dip2px(10), 0);

                imageView.setLayoutParams(params);

                if (i == mSelectPath.size()) {

                    if (mSelectPath.size() == PIC_NUM) {
                        return;
                    }

                    imageView
                            .setImageResource(R.drawable.ic_growth_file_add_selector);

                    imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            choosePictureFromAlbum();
                        }
                    });
                } else {
                    String filePath = mSelectPath.get(i);

                    PictureInfo pictureInfo = PictureUtils.processPictureFile(
                            CACHE_DIR_NAME, filePath);

                    Uri newUri = Uri.fromFile(pictureInfo.getFile());

                    ImageLoadUtils
                            .setItemImageView(imageView, newUri.toString(),
                                    R.drawable.default_image,
                                    ImageScaleType.EXACTLY, false);

                    mList.add(newUri);

                    imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    ShowPicturesPagerActivity.class);

                            ArrayList<Uri> uris = new ArrayList<>();
                            uris.addAll(mList);

                            intent.putExtra(Constant.KEY.URIS, uris);
                            intent.putExtra(Constant.KEY.POSITION, (Integer) v.getTag());

                            startActivityForResult(intent,
                                    Constant.REQUEST_CODE.SHOW_PICTURES);
                        }
                    });
                }

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                imageView.setTag(i);

                logUpImage.addView(imageView);
            }

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:

                finish();
                break;
            case R.id.spec_needs_title://特殊需求
                startActivityForResult(new Intent(mContext, SmallPublicActivity.class).putExtra("key", SPEC_NEEDS), SPECIAL_REQUIREMENT);
                break;
            case R.id.sponsoring_resources://媒体资源
                startActivityForResult(new Intent(mContext, SmallPublicActivity.class).putExtra("key", MEDIA_RES_MSG), SPONSORING_RESOURCES);
                break;
            case R.id.physical_store:
                isPhysicalStoreDialog();
                break;
            case R.id.tv_right_btn:

                if (mList.size() > 0) {

                    upLoadPicture(0);

                } else {

                    SubmitData();
                }

                break;
            case R.id.layout_price:
                showPriceRangeDialog();
                break;
            case R.id.city:
                Intent intent = new Intent(mContext, CategoryActivity.class);

                intent.putExtra(Constant.KEY.TYPE, "city");

                intent.putExtra(Constant.KEY.GOODS_CATEGORY, init);

                intent.putExtra(Constant.KEY.DEFAULT_TEXT, ActivitySignUpActivity1.booth.getCitySurveyMsgText());

                startActivityForResult(intent, CITY);

                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    ProgressDialog.getInstance().show(mContext, "正在提交...");
                    break;


                case 2:
                    ProgressDialog.getInstance().cancel();
                    ToastUtil.showShortToast("服务器未响应");

                    break;

                case 20:
                    if (upLoadNum == 0) {
                        ProgressDialog.getInstance().show(mContext, "正在上传图片");
                    }

                    break;
                case 21:

                    UpLoadPicInfo upLoadPicInfo = (UpLoadPicInfo) msg.obj;

                    upLoadPicList.add(upLoadPicInfo);

                    if (upLoadNum < mList.size() - 1) {

                        upLoadNum++;

                        upLoadPicture(upLoadNum);

                    } else {

                        ProgressDialog.getInstance().cancel();

                        SubmitData();

                    }

                    break;

                case 22:
                    ProgressDialog.getInstance().cancel();

                    ToastUtil.showLongToast(msg.obj + "");

                    break;

            }
        }

        ;
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

    class UpLoadPictureCallBack implements OkHttpMultipartUpLoad.UploadFileCallBack {

        @Override
        public void uploadStart() {
            handler.sendEmptyMessage(20);
        }

        @Override
        public void uploadSuccess(UpLoadPicInfo upLoadPicInfo) {
            Message message = handler.obtainMessage();

            message.obj = upLoadPicInfo;

            message.what = 21;

            message.sendToTarget();
        }

        @Override
        public void uploadError(Exception e) {
            Message message = handler.obtainMessage();

            message.obj = e.getMessage() + "";

            message.what = 22;

            message.sendToTarget();
        }

    }

    private void upLoadPicture(int number) {

        upLoadNum = number;

        Uri uri = mList.get(number);
        if (FileUtils.uri2Path(uri) != null) {

            OkHttpMultipartUpLoad.getInstance().postPictures("3", FileUtils.uri2Path(uri), new UpLoadPictureCallBack());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CITY) {

                String cityString = data.getStringExtra(Constant.KEY.PRODUCT_KIND);

                String cityId = data.getStringExtra(Constant.KEY.PRODUCT_KIND_ID);

                ActivitySignUpActivity1.booth.setCitySurveyMsgText(cityString);

                ActivitySignUpActivity1.booth.setCitySurveyMsg(cityId);

                city.setText(cityString);

            }

            if (requestCode == SPECIAL_REQUIREMENT) {//特殊需求
                specNeeds.setVisibility(View.VISIBLE);
                specNeeds.setText(ActivitySignUpActivity1.booth.getSpecNeeds());
            }
            if (requestCode == PRICE) {//价格区间
                ActivitySignUpActivity1.booth.setStuffPriceAreaText(data.getStringExtra("Price"));
                Price.setText(ActivitySignUpActivity1.booth.getStuffPriceAreaText());

            }

            if (requestCode == Constant.REQUEST_CODE.SELECT_IMAGE) {

                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                if (mSelectPath != null) {

                    ActivitySignUpActivity1.booth.setmSelectPath(mSelectPath);

                    resolveLogos();

                }

            }

            if (requestCode == Constant.REQUEST_CODE.SHOW_PICTURES) {

                if (data == null || !data.hasExtra(Constant.KEY.URIS)) {
                    return;
                }

                @SuppressWarnings("unchecked")
                ArrayList<Uri> uris = (ArrayList<Uri>) data
                        .getSerializableExtra(Constant.KEY.URIS);

                deleteDefaultSelectPic(uris);

            }
        }
        if (resultCode == 1) {//媒体资源
            if (requestCode == SPONSORING_RESOURCES) {
                mediaResMsg.setVisibility(View.VISIBLE);
                mediaResMsg.setText(ActivitySignUpActivity1.booth.getMediaResMsg());
            }
        }
        if (resultCode == 2) {//实体店地址
            if (requestCode == IS_PHYSICAL_STORE_DIALOG) {
                isPhysicalStore.setText("是");
                ActivitySignUpActivity1.booth.setRealshopFlag("1");
                physicalStoreAdders.setVisibility(View.VISIBLE);
                physicalStoreAdders.setText(ActivitySignUpActivity1.booth.getRealshopFlagText());

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
            Uri uri = mList.get(i);

            if (!uris.contains(uri)) {
                mList.remove(i);

                mSelectPath.remove(i);
            }
        }

        ActivitySignUpActivity1.booth.setmSelectPath(mSelectPath);

        resolveLogos();

    }

    void isPhysicalStoreDialog() {//是否有实体店

        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final CheckBox CkMain, CkLady;
        final TextView determine, cancel;
        View main, lady;
        TextView tvmain, tvlady, title;
        dialog.setContentView(R.layout.dlg_sex);
        title = (TextView) dialog.findViewById(R.id.tv_title);
        CkMain = (CheckBox) dialog.findViewById(R.id.ck_man);
        CkLady = (CheckBox) dialog.findViewById(R.id.ck_lady);
        tvmain = (TextView) dialog.findViewById(R.id.tv_main);
        tvlady = (TextView) dialog.findViewById(R.id.tv_lady);
        tvmain.setText("有");
        title.setText("是否有实体店");
        tvlady.setText("没有");
        main = dialog.findViewById(R.id.main);
        lady = dialog.findViewById(R.id.lady);
        determine = (TextView) dialog.findViewById(R.id.tv_determine);
        cancel = (TextView) dialog.findViewById(R.id.tv_cancel);


        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CkMain.setChecked(true);
                CkLady.setChecked(false);
            }
        });
        lady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CkMain.setChecked(false);
                CkLady.setChecked(true);
            }
        });
        determine.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (CkMain.isChecked() == true) {
                    startActivityForResult(new Intent(mContext, SmallPublicActivity.class).putExtra("key", SHOP_ADDRESS), IS_PHYSICAL_STORE_DIALOG);
                    ActivitySignUpActivity1.booth.setRealshopFlag("1");

                } else {
                    isPhysicalStore.setText("否");
                    physicalStoreAdders.setVisibility(View.GONE);
                    physicalStoreAdders.setText("");
                    ActivitySignUpActivity1.booth.setRealshopFlag("0");
                    ActivitySignUpActivity1.booth.setRealshopFlagText("");
                }


                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

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
                ActivitySignUpActivity1.booth.setStuffPriceArea("");
                ActivitySignUpActivity1.booth.setStuffPriceAreaText("");
                Price.setText("");
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
                ActivitySignUpActivity1.booth.setStuffPriceArea("21");
                ActivitySignUpActivity1.booth.setStuffPriceAreaText("0~300元");
                Price.setText("0~300元");
                dialog.cancel();


            }
        });
        price_range2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySignUpActivity1.booth.setStuffPriceArea("22");
                ActivitySignUpActivity1.booth.setStuffPriceAreaText("300~500元");
                Price.setText("300~500元");
                dialog.cancel();
            }
        });
        price_range3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySignUpActivity1.booth.setStuffPriceArea("23");
                ActivitySignUpActivity1.booth.setStuffPriceAreaText("500~800元");
                Price.setText("500~800元");
                dialog.cancel();
            }
        });
        price_range4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySignUpActivity1.booth.setStuffPriceArea("24");
                ActivitySignUpActivity1.booth.setStuffPriceAreaText("800~1000元");
                Price.setText("800~1000元");
                dialog.cancel();
            }
        });
        price_range5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySignUpActivity1.booth.setStuffPriceArea("25");
                ActivitySignUpActivity1.booth.setStuffPriceAreaText("1000~2000元");
                Price.setText("1000~2000元");
                dialog.cancel();
            }
        });
        price_range6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySignUpActivity1.booth.setStuffPriceArea("26");
                ActivitySignUpActivity1.booth.setStuffPriceAreaText("2000~5000元");
                Price.setText("2000~5000元");
                dialog.cancel();
            }
        });
        price_range7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySignUpActivity1.booth.setStuffPriceArea("27");
                startActivityForResult(new Intent(mContext, SmallPublicActivity.class).putExtra("key", "Price"), PRICE);
                dialog.cancel();
            }
        });

        dialog.show();


    }

    private void SubmitData() {

        getFoodElecMsg();

        ActivitySignUpActivity1.booth.setLogoUrl(getPicturesUrl());

        PostBoothApplication request = new PostBoothApplication(ActivitySignUpActivity1.booth,
                new ResponseListener<InterfaceResponse>() {


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

                            GantryNum("", 0);


                            EventBus.getDefault().post(new EventActiveStatus(2, 1, null));
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            GantryNum(response.getMessage(), 2);

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

    private void getFoodElecMsg() {

        StringBuffer sb = new StringBuffer();

        List<AddViewClassInfo> addViewClassInf = ActivitySignUpActivity1.booth.getAddViewClassInf();

        if (addViewClassInf == null) {
            return;
        }

        for (int i = 0; i < addViewClassInf.size(); i++) {
            AddViewClassInfo addViewClassInfo = addViewClassInf.get(i);

            sb.append(addViewClassInfo.getName());

            sb.append(",");

            sb.append(addViewClassInfo.getPower() + "w;");

        }

        ActivitySignUpActivity1.booth.setFoodElecMsg(sb.toString());
    }

    private void GantryNum(String msg, int type) {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final TextView num1, num2, num3;
        dialog.setContentView(R.layout.dialog_confirm);
        num1 = (TextView) dialog.findViewById(R.id.tv_title);
        num2 = (TextView) dialog.findViewById(R.id.tv_left);
        num3 = (TextView) dialog.findViewById(R.id.tv_right);
        if (type == 0) {
            num1.setText("您的报名请求已提交，我们尽快审核，并通过站内信和短信通知您，请注意查收");
        } else if (type == 2) {
            num1.setText(msg);
        }

        num2.setText("确定");
        num3.setVisibility(View.GONE);
        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesUtil.putString(actid, jsonUtil.bean2Json(ActivitySignUpActivity1.booth));
                ActivitySignUpActivity1.instance.finish();
                ActivitySignUpActivity2.instance.finish();
                finish();
                dialog.cancel();
            }
        });

        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                } else {
                    return true; //默认返回 false，这里false不能屏蔽返回键，改成true就可以了
                }
            }
        });

    }
}