package com.nautilus.ywlfair.module.vendor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.FileUtils;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.PictureUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.OkHttpMultipartUpLoad;
import com.nautilus.ywlfair.entity.bean.PictureInfo;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/3/28.
 */
public class IdentityVerifyActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private int handlePicType;

    private List<AutoAdjustHeightImageView> identityViewList;

    private static final String CACHE_DIR_NAME = "choose_photo";

    private Map<Integer, String> upLoadPicMap;

    private ArrayList<UpLoadPicInfo> upLoadPicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.identity_verify_activity);

        mContext = this;

        identityViewList = new ArrayList<>();

        upLoadPicList = new ArrayList<>();

        findViewById(R.id.im_back).setOnClickListener(this);

        findViewById(R.id.tv_top_bar_right).setOnClickListener(this);

        AutoAdjustHeightImageView firstIdentity = (AutoAdjustHeightImageView) findViewById(R.id.iv_add_first);
        firstIdentity.setOnClickListener(this);
        firstIdentity.setTag(0);
        identityViewList.add(firstIdentity);

        AutoAdjustHeightImageView secondIdentity = (AutoAdjustHeightImageView) findViewById(R.id.iv_add_second);
        secondIdentity.setOnClickListener(this);
        secondIdentity.setTag(0);
        identityViewList.add(secondIdentity);

        AutoAdjustHeightImageView thirdIdentity = (AutoAdjustHeightImageView) findViewById(R.id.iv_add_third);
        thirdIdentity.setOnClickListener(this);
        thirdIdentity.setTag(0);
        identityViewList.add(thirdIdentity);

        setDefaultInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;

            case R.id.tv_top_bar_right:

                if (upLoadPicMap.get(0) == null) {
                    ToastUtil.showLongToast("请上传手持身份证半身照");
                    return;
                }

                if (upLoadPicMap.get(1) == null) {
                    ToastUtil.showLongToast("请上传身份证人像面照");
                    return;
                }

                if (upLoadPicMap.get(2) == null) {
                    ToastUtil.showLongToast("请上传身份证国徽面照");
                    return;
                }

                ProgressDialog.getInstance().show(mContext, "图片上传中...");

                upLoadPicList.clear();

                upLoadPicture(0);

                break;

            case R.id.iv_add_first:
                pressAddPicture(v, 0);
                break;

            case R.id.iv_add_second:
                pressAddPicture(v, 1);

                break;

            case R.id.iv_add_third:
                pressAddPicture(v, 2);
                break;
        }
    }

    private void pressAddPicture(View view, int type) {
        handlePicType = type;

        int tag = (int) view.getTag();

        if (tag == 0) {

            choosePicture();

        } else {
            Intent intent = new Intent(mContext,
                    ShowPicturesPagerActivity.class);

            ArrayList<Uri> uris = new ArrayList<>();

            String uri = upLoadPicMap.get(handlePicType);

            uris.add(Uri.parse(uri));

            intent.putExtra(Constant.KEY.URIS, uris);

            intent.putExtra(Constant.KEY.POSITION, 0);

            intent.putExtra(Constant.KEY.CAN_DELETE, true);

            startActivityForResult(intent, Constant.REQUEST_CODE.SHOW_PICTURES);
        }

    }

    private void setDefaultInfo() {
        upLoadPicMap = RegistrationStall.vendorInfo.getIdCartPicMap();

        if (upLoadPicMap != null) {
            for (int i = 0; i < identityViewList.size(); i++) {
                AutoAdjustHeightImageView imageView = identityViewList.get(i);

                String uri = upLoadPicMap.get(i);

                ImageLoader.getInstance().displayImage(uri, imageView, ImageLoadUtils.createNoRoundedOptions());

                imageView.setTag(1);
            }
        } else {
            upLoadPicMap = new HashMap<>();
        }
    }

    private void choosePicture() {
        Intent selectIntent = new Intent(mContext, MultiImageSelectorActivity.class);
        // whether show camera
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
        selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

        startActivityForResult(selectIntent, Constant.REQUEST_CODE.SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE.SELECT_IMAGE) {

                addPicResult(data, identityViewList.get(handlePicType));

            } else if (requestCode == Constant.REQUEST_CODE.SHOW_PICTURES) {

                deleteResult(data, identityViewList.get(handlePicType));

            }
        }
    }

    private void addPicResult(Intent data, AutoAdjustHeightImageView imageView) {

        ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

        if (mSelectPath != null && mSelectPath.size() > 0) {

            PictureInfo pictureInfo = PictureUtils.processPictureFile(
                    CACHE_DIR_NAME, mSelectPath.get(0));

            Uri newUri = Uri.fromFile(pictureInfo.getFile());

            upLoadPicMap.put(handlePicType, newUri.toString());

            imageView.setTag(1);

            ImageLoader.getInstance().displayImage(newUri.toString(), imageView, ImageLoadUtils.createNoRoundedOptions());
        }
    }

    private void deleteResult(Intent data, AutoAdjustHeightImageView imageView) {

        ArrayList<Uri> uris = (ArrayList<Uri>) data
                .getSerializableExtra(Constant.KEY.URIS);

        if (uris.size() == 0) {
            imageView.setTag(0);

            imageView.setImageResource(R.drawable.bg_add_identity_selector);

            upLoadPicMap.put(handlePicType, null);
        }

    }

    private int upLoadNum = 0;

    private void upLoadPicture(int number) {

        upLoadNum = number;

        Uri uri = Uri.parse(upLoadPicMap.get(number));

        OkHttpMultipartUpLoad.getInstance().postPictures(
                String.valueOf(6), FileUtils.uri2Path(uri),
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
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (upLoadNum == 0) {
                        ProgressDialog.getInstance().show(mContext, "图片上传中...");
                    }

                    break;

                case 1:
                    UpLoadPicInfo upLoadPicInfo = (UpLoadPicInfo) msg.obj;

                    upLoadPicList.add(upLoadPicInfo);

                    if (upLoadNum < upLoadPicMap.size() - 1) {

                        upLoadNum++;

                        upLoadPicture(upLoadNum);
                    } else {
                        ProgressDialog.getInstance().cancel();

                        RegistrationStall.vendorInfo.setIdCardUrlString(getPicturesUrl());

                        RegistrationStall.vendorInfo.setIdCartPicMap(upLoadPicMap);

                        ToastUtil.showLongToast("身份证照片已上传");

                        setResult(RESULT_OK);

                        finish();
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

}
