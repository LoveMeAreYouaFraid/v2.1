package com.nautilus.ywlfair.module.active;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.AddPictureStringAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.FileUtils;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.PictureUtils;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.OkHttpMultipartUpLoad;
import com.nautilus.ywlfair.entity.bean.AddViewClassInfo;
import com.nautilus.ywlfair.entity.bean.BoothApplicationInfo;
import com.nautilus.ywlfair.entity.bean.PictureInfo;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;
import com.nautilus.ywlfair.entity.response.GetActivityBoothApplicationConfigResponse;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.nautilus.ywlfair.widget.WrapContentHeightGridView;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/3/3.
 */
public class ActivitySignUpActivity2 extends Activity implements View.OnClickListener {

    private static final int FOOD_PIC = 0;
    private static final int GOODS_PIC = 1;
    private static final int BIG_PIC = 2;

    private static final int[] picNumSet = new int[]{9, 5, 5};

    private static final String[] typeStrings = new String[]{"三证", "商品", "展位"};

    private int handlePicType = 0;

    private static final String CACHE_DIR_NAME = "choose_photo";

    private int upLoadType = 0;

    private List<ArrayList> dataSet;


    private List<ArrayList<UpLoadPicInfo>> upLoadPicSet;

    private TextView appTitle, appTitleRight, goodsStyle, goodsCategory, dragonNum, intrMsg, tvGoodsCategory;
    private TextView tvGoodsStyle, tvDragonNum, tvGoodsImage, tvBig;
    private Context mContext;
    private Intent intent;
    private View foodLayout;
    private LinearLayout addLayoutView;
    private static final int GOODS_STYLE = 100;
    private TextView addElectricity, titleIntrMsg;
    private static final int INTRODUCTION = 98;
    private static final int ADD_VIEW = 97;
    private static final int ADD_VIEW_EDIT = 96;
    private int upLoadNum = 0;
    private ArrayList<UpLoadPicInfo> upLoadPicList, goodsUpLoadPicList, bigUploadPicList;

    private ArrayList<String> foodSelectPath, goodsSelectPath, bigSelectPath;

    private List<ArrayList<String>> selectPathSet;

    private int picNum = 9;
    private ArrayList<String> mList, goodsPicList, bigPicList;
    private LinearLayout layout;

    private int type = 0;
    private int position = 0;
    private static final int COMMON_BOOTH = 95;
    private GetActivityBoothApplicationConfigResponse boothConfig;
    public static ActivitySignUpActivity2 instance = null;
    private AddPictureStringAdapter mAdapterUpLoad, mAdapterGoods, mAdapterBig;
    private WrapContentHeightGridView mListView, goodsPicListView, bigPicListView;
    private JsonUtil<BoothApplicationInfo> jsonUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup_activity2);

        mContext = this;

        jsonUtil = new JsonUtil();

        intent = getIntent();

        instance = this;

        upLoadPicList = new ArrayList<>();

        foodSelectPath = new ArrayList<>();

        goodsSelectPath = new ArrayList<>();

        goodsUpLoadPicList = new ArrayList<>();

        bigUploadPicList = new ArrayList<>();

        bigSelectPath = new ArrayList<>();

        upLoadPicSet = new ArrayList<>();

        selectPathSet = new ArrayList<>();

        selectPathSet.add(foodSelectPath);

        selectPathSet.add(goodsSelectPath);

        selectPathSet.add(bigSelectPath);

        upLoadPicSet.add(upLoadPicList);

        upLoadPicSet.add(goodsUpLoadPicList);

        upLoadPicSet.add(bigUploadPicList);

        if (intent.hasExtra(Constant.KEY.BOOTH_CONFIG)) {

            boothConfig = (GetActivityBoothApplicationConfigResponse) intent.getSerializableExtra(Constant.KEY.BOOTH_CONFIG);

        } else {

            finish();

        }

        foodLayout = findViewById(R.id.food_layout);
        tvGoodsCategory = (TextView) findViewById(R.id.tv_goods_category);
        View back = findViewById(R.id.img_back);
        layout = (LinearLayout) findViewById(R.id.add_layout_view);
        tvDragonNum = (TextView) findViewById(R.id.tv_dragon_num);
        intrMsg = (TextView) findViewById(R.id.intr_msg);
        View layoutGoodsStyle = findViewById(R.id.layout_goods_style);
        tvGoodsStyle = (TextView) findViewById(R.id.tv_goods_style);
        mListView = (WrapContentHeightGridView) findViewById(R.id.ll_pic_contain);
        goodsPicListView = (WrapContentHeightGridView) findViewById(R.id.ll_goods_pic_container);
        bigPicListView = (WrapContentHeightGridView) findViewById(R.id.ll_big_pic_container);
        goodsStyle = (TextView) findViewById(R.id.goods_style);
        dragonNum = (TextView) findViewById(R.id.dragon_num);
        titleIntrMsg = (TextView) findViewById(R.id.title_intr_msg);
        addLayoutView = (LinearLayout) findViewById(R.id.add_layout_view);
        addElectricity = (TextView) findViewById(R.id.add_electricity);
        goodsCategory = (TextView) findViewById(R.id.goods_category);
        appTitle = (TextView) findViewById(R.id.tv_title);
        appTitleRight = (TextView) findViewById(R.id.tv_right_btn);
        tvGoodsImage = (TextView) findViewById(R.id.tv_goods_image);
        tvBig = (TextView) findViewById(R.id.tv_big);
        appTitleRight.setOnClickListener(this);
        dragonNum.setOnClickListener(this);
        titleIntrMsg.setOnClickListener(this);
        layoutGoodsStyle.setOnClickListener(this);
        addElectricity.setOnClickListener(this);
        back.setOnClickListener(this);
        goodsCategory.setOnClickListener(this);
        appTitle.setText("活动报名");
        appTitleRight.setText("下一步");
        appTitleRight.setVisibility(View.VISIBLE);

        tvGoodsStyle.setText(ActivitySignUpActivity1.booth.getStuffStyleText());

        tvDragonNum.setText(ActivitySignUpActivity1.booth.getShelfNum());

        if (!ActivitySignUpActivity1.booth.getIntrMsg().equals("")) {
            intrMsg.setVisibility(View.VISIBLE);
            intrMsg.setText(ActivitySignUpActivity1.booth.getIntrMsg());
        }

        if (!ActivitySignUpActivity1.booth.getStuffKindsText().equals("")) {
            tvGoodsCategory.setText(ActivitySignUpActivity1.booth.getCitySurveyMsgText());
            goodsCategory.setVisibility(View.VISIBLE);
        }
        dataSet = new ArrayList<>();
        mList = new ArrayList<>();
        dataSet.add(mList);
        goodsPicList = new ArrayList<>();
        dataSet.add(goodsPicList);

        bigPicList = new ArrayList<>();
        dataSet.add(bigPicList);


        mAdapterUpLoad = new AddPictureStringAdapter(mContext, mList);
        mAdapterGoods = new AddPictureStringAdapter(mContext, goodsPicList);
        mAdapterBig = new AddPictureStringAdapter(mContext, bigPicList);
        mListView.setAdapter(mAdapterUpLoad);
        goodsPicListView.setAdapter(mAdapterGoods);
        bigPicListView.setAdapter(mAdapterBig);

        if (ActivitySignUpActivity1.booth.getBoothBuyType().equals("1")) {
            FoodLayoutgon();
        } else if (ActivitySignUpActivity1.booth.getBoothBuyType().equals("2")) {
            FoodLayoutVisble();
        }

        if (ActivitySignUpActivity1.booth.getFoodUriList() != null) {
            handlePicType = 0;

            mList.addAll(ActivitySignUpActivity1.booth.getFoodUriList());

            foodSelectPath.addAll(ActivitySignUpActivity1.booth.getFoodlist());

            mAdapterUpLoad.notifyDataSetChanged();
        }
        if (ActivitySignUpActivity1.booth.getGoodsUriList() != null) {
            handlePicType = 1;

            goodsPicList.addAll(ActivitySignUpActivity1.booth.getGoodsUriList());

            goodsSelectPath.addAll(ActivitySignUpActivity1.booth.getGoodsPicList());

            mAdapterGoods.notifyDataSetChanged();
        }
        if (ActivitySignUpActivity1.booth.getBigPicUriList() != null) {
            handlePicType = 2;

            bigPicList.addAll(ActivitySignUpActivity1.booth.getBigPicUriList());

            bigSelectPath.addAll(ActivitySignUpActivity1.booth.getBigPicList());

            mAdapterBig.notifyDataSetChanged();
        }
        /**
         * 需要上传的图片的点击事件
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handlePicType = 0;
                imageClick(mList, position);
            }
        });
        goodsPicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handlePicType = 1;
                imageClick(goodsPicList, position);
            }
        });
        bigPicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handlePicType = 2;
                imageClick(bigPicList, position);
            }
        });


    }

    private void imageClick(ArrayList<String> list, int position) {

        if (list.size() != 0 && position != list.size()) {

            Intent intent = new Intent(mContext,
                    ShowPicturesPagerActivity.class);

            ArrayList<Uri> uris = new ArrayList<>();

            for (String string : list) {
                uris.add(Uri.parse(string));
            }

            intent.putExtra(Constant.KEY.URIS, uris);

            intent.putExtra(Constant.KEY.POSITION, position);

            startActivityForResult(intent,
                    Constant.REQUEST_CODE.SHOW_PICTURES);
        } else {

            choosePictureFromAlbum();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_goods_style:
                Intent intent = new Intent(mContext, GoodsStyle.class);
                startActivityForResult(intent, GOODS_STYLE);
                break;
            case R.id.tv_right_btn:
                PreferencesUtil.putString(ActivitySignUpActivity1.actid, jsonUtil.bean2Json(ActivitySignUpActivity1.booth));
                ifNext();
                break;
            case R.id.add_electricity://新增
                startActivityForResult(new Intent(mContext, SmallPublicActivity.class).putExtra("key", "AddView"), ADD_VIEW);

                break;
            case R.id.title_intr_msg:
                startActivityForResult(new Intent(mContext, SmallPublicActivity.class)
                        .putExtra("key", "introduction")
                        .putExtra("msg", ActivitySignUpActivity1.booth.getIntrMsg()), INTRODUCTION);
                break;
            case R.id.dragon_num:
                GantryNum();
                break;
            case R.id.goods_category:
                isPhysicalStoreDialog();
                break;

        }
    }


    private void addView2(AddViewClassInfo addViewClassInf) {
        // TODO 动态添加布局(java方式)

        final View view;

        TextView name;

        TextView power;

        TextView edit;

        TextView dell;

        if (type == 1) {

            LayoutInflater flater = LayoutInflater.from(this);

            view = flater.inflate(R.layout.electricity_item, null);

            layout.setVisibility(View.VISIBLE);

            layout.addView(view);

            position = layout.indexOfChild(view);

        } else {
            view = layout.getChildAt(position);
        }

        name = (TextView) view.findViewById(R.id.tv_name);

        power = (TextView) view.findViewById(R.id.tv_power);

        edit = (TextView) view.findViewById(R.id.tv_edit);

        edit.setTag(position);

        dell = (TextView) view.findViewById(R.id.tv_dell);


        if (addViewClassInf != null) {
            name.setText(addViewClassInf.getName());
            power.setText(addViewClassInf.getPower());
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = (int) v.getTag();

                Intent intent = new Intent(mContext, SmallPublicActivity.class);

                intent.putExtra("key", "AddViewEdit");

                intent.putExtra(Constant.KEY.POSITION, position);

                intent.putExtra("value", "");

                startActivityForResult(intent, ADD_VIEW_EDIT);

                type = 0;

            }
        });

        dell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = layout.indexOfChild(view);

                ActivitySignUpActivity1.booth.getAddViewClassInf().remove(position);

                view.setVisibility(View.GONE);

                layout.removeViewInLayout(view);

                ToastUtil.showShortToast("删除成功");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == COMMON_BOOTH) {

                String productKind = data.getStringExtra(Constant.KEY.PRODUCT_KIND);

                String productId = data.getStringExtra(Constant.KEY.PRODUCT_KIND_ID);

                ActivitySignUpActivity1.booth.setStuffKinds(productId);

                ActivitySignUpActivity1.booth.setStuffKindsText(productKind);

                tvGoodsCategory.setText(productKind);

                tvGoodsCategory.setVisibility(View.VISIBLE);
            }
            if (requestCode == GOODS_STYLE) {

                String styleText = data.getStringExtra(Constant.KEY.GOODS_STYLE);

                String styleTag = data.getStringExtra(Constant.KEY.GOODS_STYLE_TAG);

                ActivitySignUpActivity1.booth.setStuffStyle(styleTag);

                ActivitySignUpActivity1.booth.setStuffStyleText(styleText);

                tvGoodsStyle.setText(styleText);
            }
            if (requestCode == ADD_VIEW) {
                type = 1;
                addView2(ActivitySignUpActivity1.booth.getAddViewClassInf().get(ActivitySignUpActivity1.booth.getAddViewClassInf().size() - 1));
            }
            if (requestCode == ADD_VIEW_EDIT) {

                type = 0;

                addView2(ActivitySignUpActivity1.booth.getAddViewClassInf().get(position));
            }

            if (requestCode == INTRODUCTION) {
                ActivitySignUpActivity1.booth.setIntrMsg(data.getStringExtra("introduction"));
                intrMsg.setVisibility(View.VISIBLE);
                intrMsg.setText(data.getStringExtra("introduction"));

            }

            if (requestCode == Constant.REQUEST_CODE.SELECT_IMAGE) {

                addPicResult(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT),
                        dataSet.get(handlePicType), selectPathSet.get(handlePicType));

            }

            if (requestCode == Constant.REQUEST_CODE.SHOW_PICTURES) {

                if (data == null || !data.hasExtra(Constant.KEY.URIS)) {
                    return;
                }

                deletePicResult(data, dataSet.get(handlePicType), selectPathSet.get(handlePicType));
            }

        }

    }

    private void addPicResult(ArrayList<String> mSelectPath, ArrayList<String> list, ArrayList<String> selectPath) {

        if (mSelectPath != null && mSelectPath.size() > 0) {

            list.clear();

            selectPath.clear();

            selectPath.addAll(mSelectPath);

            for (String filePath : mSelectPath) {

                PictureInfo pictureInfo = PictureUtils.processPictureFile(
                        CACHE_DIR_NAME, filePath);

                if(pictureInfo != null){
                    Uri newUri = Uri.fromFile(pictureInfo.getFile());

                    list.add(newUri.toString());
                }

            }

            if (handlePicType == 0) {
                mAdapterUpLoad.notifyDataSetChanged();

                ActivitySignUpActivity1.booth.setFoodlist(mSelectPath);

                ActivitySignUpActivity1.booth.setFoodUriList(list);
            }
            if (handlePicType == 1) {
                mAdapterGoods.notifyDataSetChanged();

                ActivitySignUpActivity1.booth.setGoodsPicList(mSelectPath);

                ActivitySignUpActivity1.booth.setGoodsUriList(list);
            }
            if (handlePicType == 2) {
                mAdapterBig.notifyDataSetChanged();

                ActivitySignUpActivity1.booth.setBigPicList(mSelectPath);

                ActivitySignUpActivity1.booth.setBigPicUriList(list);
            }

        }

    }

    private void deletePicResult(Intent data, ArrayList<String> list, ArrayList<String> selectPath) {
        ArrayList<Uri> uris = (ArrayList<Uri>) data
                .getSerializableExtra(Constant.KEY.URIS);

        if (uris.size() == 0) {
            list.clear();

            selectPath.clear();
        }

        for (int i = list.size() - 1; i > -1; i--) {
            Uri uri = Uri.parse(list.get(i));

            if (!uris.contains(uri)) {
                list.remove(i);

                selectPath.remove(i);
            }
        }

        if (handlePicType == 0) {
            mAdapterUpLoad.notifyDataSetChanged();

            ActivitySignUpActivity1.booth.setFoodlist(selectPath);

            ActivitySignUpActivity1.booth.setFoodUriList(list);
        }
        if (handlePicType == 1) {
            mAdapterGoods.notifyDataSetChanged();

            ActivitySignUpActivity1.booth.setGoodsPicList(selectPath);

            ActivitySignUpActivity1.booth.setGoodsUriList(list);
        }
        if (handlePicType == 2) {
            mAdapterBig.notifyDataSetChanged();

            ActivitySignUpActivity1.booth.setBigPicList(selectPath);

            ActivitySignUpActivity1.booth.setBigPicUriList(list);
        }
    }

    private void isPhysicalStoreDialog() {//摊位类型

        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final CheckBox CkMain, CkLady;
        final TextView determine, cancel;
        View main, lady;
        TextView tvmain, tvlady, tvTitle;
        dialog.setContentView(R.layout.dlg_sex);
        tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        CkMain = (CheckBox) dialog.findViewById(R.id.ck_man);
        CkLady = (CheckBox) dialog.findViewById(R.id.ck_lady);
        tvmain = (TextView) dialog.findViewById(R.id.tv_main);
        tvlady = (TextView) dialog.findViewById(R.id.tv_lady);
        tvmain.setText("普通摊位");
        tvlady.setText("食品摊位");
        tvTitle.setText("摊位类型");
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

                if (CkMain.isChecked() == true) {//普通摊位

                    if (boothConfig == null) {
                        ToastUtil.showLongToast("类型未加载，请稍后重试");
                        return;
                    }

                    FoodLayoutgon();

                    ActivitySignUpActivity1.booth.setBoothBuyType("1");

                    Intent intent = new Intent(mContext, CategoryActivity.class);

                    intent.putExtra(Constant.KEY.GOODS_CATEGORY, boothConfig);

                    intent.putExtra(Constant.KEY.TYPE, "normal");

                    intent.putExtra(Constant.KEY.DEFAULT_TEXT, ActivitySignUpActivity1.booth.getStuffKindsText());

                    startActivityForResult(intent, COMMON_BOOTH);

                } else {
                    if (boothConfig == null) {
                        ToastUtil.showLongToast("类型未加载，请稍后重试");
                        return;
                    }

                    FoodLayoutVisble();

                    ActivitySignUpActivity1.booth.setBoothBuyType("2");

                    Intent intent = new Intent(mContext, CategoryActivity.class);

                    intent.putExtra(Constant.KEY.GOODS_CATEGORY, boothConfig);

                    intent.putExtra(Constant.KEY.TYPE, "food");

                    intent.putExtra(Constant.KEY.DEFAULT_TEXT, ActivitySignUpActivity1.booth.getStuffKindsText());

                    startActivityForResult(intent, COMMON_BOOTH);

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

    private void FoodLayoutVisble() {
        foodLayout.setVisibility(View.VISIBLE);//食品摊位
        tvGoodsCategory.setVisibility(View.VISIBLE);
        tvGoodsCategory.setText("");
        dragonNum.setText("5.现场自带龙门架个数");
        titleIntrMsg.setText("6.简单文字介绍");
        tvGoodsImage.setText("7.携带到现场的货品图片");
        tvBig.setText("8.如需申请3×3米 大展位， 请上传之前展位现场照片，最好是全景照片。");
        tvGoodsCategory.setText(ActivitySignUpActivity1.booth.getStuffKindsText());
    }

    private void FoodLayoutgon() {
        tvGoodsCategory.setVisibility(View.VISIBLE);
        foodLayout.setVisibility(View.GONE);//普通摊位
        dragonNum.setText("3.现场自带龙门架个数");
        titleIntrMsg.setText("4.简单文字介绍");
        tvGoodsImage.setText("5.携带到现场的货品图片");
        tvBig.setText("6.如需申请3×3米 大展位， 请上传之前展位现场照片，最好是全景照片。");
        tvGoodsCategory.setText(ActivitySignUpActivity1.booth.getStuffKindsText());
    }

    //龙门架 dialog
    private void GantryNum() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final TextView num1, num2, num3, num4, num5, num6, num7, num8;
        dialog.setContentView(R.layout.long_men_jia_dialog);
        num1 = (TextView) dialog.findViewById(R.id.num1);
        num2 = (TextView) dialog.findViewById(R.id.num2);
        num3 = (TextView) dialog.findViewById(R.id.num3);
        num4 = (TextView) dialog.findViewById(R.id.num4);
        num5 = (TextView) dialog.findViewById(R.id.num5);
        num6 = (TextView) dialog.findViewById(R.id.num6);
        num7 = (TextView) dialog.findViewById(R.id.num7);
        num8 = (TextView) dialog.findViewById(R.id.num8);
        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDragonNum.setText("0");
                dialog.cancel();

            }
        });
        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDragonNum.setText("1");
                ActivitySignUpActivity1.booth.setShelfNum("1");
                dialog.cancel();
            }
        });
        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDragonNum.setText("2");
                ActivitySignUpActivity1.booth.setShelfNum("2");
                dialog.cancel();
            }
        });
        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDragonNum.setText("3");
                ActivitySignUpActivity1.booth.setShelfNum("3");
                dialog.cancel();
            }
        });
        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDragonNum.setText("4");
                ActivitySignUpActivity1.booth.setShelfNum("4");
                dialog.cancel();
            }
        });
        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDragonNum.setText("5");
                ActivitySignUpActivity1.booth.setShelfNum("5");
                dialog.cancel();
            }
        });
        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDragonNum.setText("6");
                ActivitySignUpActivity1.booth.setShelfNum("6");
                dialog.cancel();
            }
        });
        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }


    private void choosePictureFromAlbum() {

        picNum = picNumSet[handlePicType];

        Intent selectIntent = new Intent(mContext, MultiImageSelectorActivity.class);
        // whether show camera
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // max select image amount
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, picNum);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // default select images (support array list)

        ArrayList<String> mSelectPath = selectPathSet.get(handlePicType);

        if (mSelectPath != null && mSelectPath.size() > 0) {

            selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }

        startActivityForResult(selectIntent, Constant.REQUEST_CODE.SELECT_IMAGE);
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 20:
                    if(upLoadNum == 0)
                        ProgressDialog.getInstance().show(mContext, "图片上传中...");
                    break;


                case 21:

                    UpLoadPicInfo upLoadPicInfo = (UpLoadPicInfo) msg.obj;

                    upLoadPicSet.get(upLoadType).add(upLoadPicInfo);

                    int picNumLimit = dataSet.get(upLoadType).size();

                    if (upLoadNum < picNumLimit && upLoadType < 3) {

                        if (upLoadNum == picNumLimit - 1) {

                            upLoadType = upLoadType + 1;

                            upLoadNum = 0;

                            if (upLoadType < 3 && dataSet.get(upLoadType).size() > 0) {
                                upLoadPicture(upLoadNum, dataSet.get(upLoadType), upLoadType);
                            } else {
                                ProgressDialog.getInstance().cancel();

                                toThirdStep();
                            }
                        } else {
                            upLoadNum++;

                            upLoadPicture(upLoadNum, dataSet.get(upLoadType), upLoadType);
                        }

                    } else {

                        toThirdStep();
                    }

                    break;

                case 22:
                    ProgressDialog.getInstance().cancel();

                    ToastUtil.showLongToast(msg.obj + "");

                    break;
            }
        }

    };

    private void upLoadPicture(int number, ArrayList<String> list, int type) {

        upLoadType = type;

        upLoadNum = number;

        Uri uri = Uri.parse(list.get(number));

        if (FileUtils.uri2Path(uri) != null) {

            OkHttpMultipartUpLoad.getInstance().postPictures("5", FileUtils.uri2Path(uri), new UpLoadPictureCallBack());
        } else {
            ToastUtil.showLongToast("图片不存在，请检查添加的图片后重试！");
        }
    }

    class UpLoadPictureCallBack implements OkHttpMultipartUpLoad.UploadFileCallBack {

        @Override
        public void uploadStart() {

            if(upLoadNum == 0){
                ProgressDialog.getInstance().show(mContext, "正在上传图片");
            }

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

    private String getPicturesUrl(int type) {

        if (upLoadPicSet.get(type).size() == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for (UpLoadPicInfo pic : upLoadPicSet.get(type)) {
                sb.append(pic.getNormalPicturePath());

                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);

            return sb.toString();
        }
    }

    private List<UpLoadPicInfo> getPicturesUris(int type) {

        List<UpLoadPicInfo> list = new ArrayList<>();

        for (UpLoadPicInfo pic : upLoadPicSet.get(type)) {
            list.add(pic);
        }

        return list;
    }

    private void toThirdStep() {

        ProgressDialog.getInstance().cancel();
        ActivitySignUpActivity1.booth.setFoodCertPicUrl(getPicturesUrl(FOOD_PIC));
        ActivitySignUpActivity1.booth.setStuffPicUrl(getPicturesUrl(GOODS_PIC));
        ActivitySignUpActivity1.booth.setLargeGroundUrl(getPicturesUrl(BIG_PIC));
        Bundle bundle = new Bundle();

        bundle.putSerializable(Constant.KEY.BOOTH_CONFIG, boothConfig);

        startActivity(new Intent(this, ActivitySignUpActivity3.class).putExtras(bundle));
    }

    private void ifNext() {

        for (List<UpLoadPicInfo> list : upLoadPicSet) {
            list.clear();
        }

        if (ActivitySignUpActivity1.booth.getStuffStyleText().equals("")) {
            ToastUtil.showShortToast("请选择货品风格");
            return;
        }
        if (ActivitySignUpActivity1.booth.getBoothBuyType().equals("2")) {

            if (mList.size() == 0) {
                ToastUtil.showShortToast("请选择上传餐饮摊位三证");
                return;
            }

        } else {
            if (ActivitySignUpActivity1.booth.getStuffKindsText().equals("")) {
                ToastUtil.showShortToast("请选择货品类型");
                return;
            }
        }
        if (ActivitySignUpActivity1.booth.getShelfNum().equals("")) {
            ToastUtil.showShortToast("请选择携带的龙门架数量");
            return;
        }
        if (ActivitySignUpActivity1.booth.getIntrMsg().equals("")) {
            ToastUtil.showShortToast("请填写简单文字介绍");
            return;
        }
        if (goodsPicList.size() == 0) {
            ToastUtil.showShortToast("必须上传携带到现场的货品图片");
            return;
        }

        for (int i = 0; i < dataSet.size(); i++) {

            if (dataSet.get(i).size() > 0) {

                upLoadPicture(0, dataSet.get(i), i);

                break;
            }

        }

    }

}
