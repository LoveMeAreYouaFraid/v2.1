package com.nautilus.ywlfair.module.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.cache.CacheUserInfo;
import com.nautilus.ywlfair.common.utils.DataCleanManager;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.PictureUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.OkHttpMultipartUpLoad;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.DetermineCancelDialog;
import com.nautilus.ywlfair.dialog.ListDialog;
import com.nautilus.ywlfair.entity.bean.PictureInfo;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.bean.UserMainEventItem;
import com.nautilus.ywlfair.entity.request.PutUserInfoRequest;
import com.nautilus.ywlfair.entity.response.PutUserInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.market.InvitationCodeActivity;
import com.nautilus.ywlfair.module.mine.ChangeUserInfoEditActivity.Mode;
import com.nautilus.ywlfair.module.vendor.VendorInfoActivity;
import com.nautilus.ywlfair.module.vendor.VendorVerifyActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.city.CityPicker;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


public class UserInfoActivity extends BaseActivity implements OnClickListener, ListDialog.DialogListener {
    private static final int AVATAR_SIZE = 512;
    @BindView(R.id.tv_invitation_type)
    TextView tvInvitationType;
    @BindView(R.id.tv_stall_information)
    TextView tvStallInformation;
    @BindView(R.id.tv_login_account)
    TextView tvLoginAccount;

    private Context mContext;

    private TextView sexView, signView, addressView;

    private int sexTag;

    private TextView nameView;

    private UserInfo currentUser;

    private ImageView headerView;

    private File mTempAvatarFile;

    private Uri mTempAvatarUri;

    private TextView tvFileSize;

    private static final String CACHE_DIR_NAME = "choose_photo";

    private String[] signType = {"", "QQ绑定登录", "微博绑定登录", "微信绑定登录", "微信绑定登录"};

    //    0:非第三方; 1:QQ; 2:weibo; 3:douban
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_information);
        ButterKnife.bind(this);

        mContext = this;


        currentUser = GetUserInfoUtil.getUserInfo();

        if (currentUser == null) {
            finish();
        }

        headerView = (ImageView) findViewById(R.id.iv_header);
        ImageLoadUtils.setRoundHeadView(headerView, currentUser.getAvatar(),
                R.drawable.default_avatar, 100);

        sexView = (TextView) findViewById(R.id.text_xing);
        sexView.setText(currentUser.getSex() == 0 ? "男" : "女");

        tvFileSize = (TextView) findViewById(R.id.tv_file_size);

        tvFileSize.setText(cacheSize());

        addressView = (TextView) findViewById(R.id.tv_address);
        addressView.setText(StringUtils.getCityName(mContext,
                currentUser.getCity()));

        signView = (TextView) findViewById(R.id.text_qianmin);
        signView.setText(currentUser.getSignature());

        nameView = (TextView) findViewById(R.id.et_nicky_name);
        nameView.setText(currentUser.getNickname());

        findViewById(R.id.user_login_info).setOnClickListener(this);
        findViewById(R.id.xing).setOnClickListener(this);
        findViewById(R.id.qianmin).setOnClickListener(this);
        findViewById(R.id.ll_change_header).setOnClickListener(this);
        findViewById(R.id.name).setOnClickListener(this);
        findViewById(R.id.diqu).setOnClickListener(this);
        findViewById(R.id.layout_file_size).setOnClickListener(this);
        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);
        findViewById(R.id.layout_stall_information).setOnClickListener(this);
        findViewById(R.id.invitation_code).setOnClickListener(this);

        if (GetUserInfoUtil.getUserInfo().getUserType() == 1) {
            tvStallInformation.setText("");
        } else {
            tvStallInformation.setText("您还不是摊主");
        }
        tvLoginAccount.setText(signType[GetUserInfoUtil.getUserInfo().getThirdPartyFlag()]);

        if (!TextUtils.isEmpty(GetUserInfoUtil.getUserInfo().getHasInputInvitationCode())) {
            if (!GetUserInfoUtil.getUserInfo().getHasInputInvitationCode().equals("1")) {
                tvInvitationType.setText("未输入");
            } else {
                tvInvitationType.setText("已输入");
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_file_size:

                try {

                    DataCleanManager.cleanApplicationData(mContext, getExternalCacheDir().getAbsolutePath() + "/uil-images");

                    tvFileSize.setText(cacheSize());

                    ToastUtil.showShortToast("清除缓存成功");

                } catch (Exception e) {

                    e.printStackTrace();
                }
                break;
            case R.id.user_login_info:
                Intent intent = new Intent(this, LoginAccountInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.xing:

                changeType = 0;
                ListDialog.getInstance().showListDialog(mContext, new String[]{"男", "女"}, "选择性别", "", "");
//                SexDialog();
                // showGenderPopMenu();
                break;
            case R.id.qianmin:
                Intent signIntent = new Intent(mContext,
                        ChangeUserInfoEditActivity.class);

                signIntent.putExtra(Constant.KEY.MODE, Mode.SIGN);

                signIntent.putExtra(Constant.KEY.DEFAULT_TEXT, currentUser.getSignature());

                startActivityForResult(signIntent,
                        Constant.REQUEST_CODE.CHANGE_SIGN);
                break;
            case R.id.name:
                Intent nameIntent = new Intent(mContext,
                        ChangeUserInfoEditActivity.class);

                nameIntent.putExtra(Constant.KEY.MODE, Mode.NAME);

                nameIntent.putExtra(Constant.KEY.DEFAULT_TEXT, currentUser.getNickname());

                startActivityForResult(nameIntent, Constant.REQUEST_CODE.CHANGE_NAME);

                break;
            case R.id.ll_change_header:

                Intent selectIntent = new Intent(mContext, MultiImageSelectorActivity.class);
                // whether show camera
                selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // max select image amount
                selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
                selectIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                // default select images (support array list)
//                selectIntent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);

                startActivityForResult(selectIntent, Constant.REQUEST_CODE.SELECT_IMAGE);

                break;

            case R.id.tv_top_bar_back:
                onBackPressed();
                break;

            case R.id.diqu:
                changeType = 1;

                showCityPickDialog();
                break;

            case R.id.invitation_code:
                if (TextUtils.isEmpty(GetUserInfoUtil.getUserInfo().getHasInputInvitationCode())) {
                    ToastUtil.showLongToast("请稍后重试");
                    return;
                }
                if (GetUserInfoUtil.getUserInfo().getHasInputInvitationCode().equals("1")) {
                    startActivity(new Intent(mContext, InvitationCodeActivity.class));
                } else {
                    startActivityForResult(new Intent(mContext, InvitationCodeActivity.class), Constant.REQUEST_CODE.RC_SELECT_AVATAR_FROM_ALBUM);
                }

                break;
            case R.id.layout_stall_information:

                if (GetUserInfoUtil.getUserInfo().getUserType() == 1) {
                    Intent vendorInfoIntent = new Intent(mContext, VendorInfoActivity.class);
                    startActivity(vendorInfoIntent);
                } else {
                    startActivity(new Intent(mContext, VendorVerifyActivity.class));
                }
                break;
        }

    }


    private ArrayList<String> mSelectPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case Constant.REQUEST_CODE.CROP_IMAGE:
                    if (mTempAvatarFile.exists())
                        upLoadPicture(mTempAvatarUri);
                    break;

                case Constant.REQUEST_CODE.CHANGE_NAME:
                    String newName = data.getStringExtra(Constant.KEY.NAME);

                    nameView.setText(newName);

                    GetUserInfoUtil.getUserInfo().setNickname(newName);

                    break;

                case Constant.REQUEST_CODE.CHANGE_SIGN:
                    signView.setText(GetUserInfoUtil.getUserInfo().getSignature());
                    break;

                case Constant.REQUEST_CODE.SELECT_IMAGE:

                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                    if (mSelectPath != null && mSelectPath.size() > 0) {

                        mTempAvatarFile = new File(mSelectPath.get(0));

                        mTempAvatarUri = Uri.fromFile(mTempAvatarFile);

                        cropImageUri(mTempAvatarUri, AVATAR_SIZE, AVATAR_SIZE,
                                Constant.REQUEST_CODE.CROP_IMAGE);
                    }

                    break;

                case Constant.REQUEST_CODE.RC_SELECT_AVATAR_FROM_ALBUM:
                    tvInvitationType.setText("");
                    break;
            }

        }
    }

    private void getNewAvatar(String avatar) {

        if (!TextUtils.isEmpty(avatar)) {

            ImageLoadUtils.setRoundHeadView(headerView, avatar,
                    R.drawable.default_avatar, 100);
        }

    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }


    private String cityCode;

    private String cityString;

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
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                cityString = cityPicker.getCity_string();

                cityCode = cityPicker.getProvinceCode() + ","
                        + cityPicker.getCity_code_string();

                confirmChange();

                pickDialog.cancel();
            }
        });

        pickDialog.show();
    }

    private int changeType;

    private void confirmChange() {

        UserInfo userInfo = GetUserInfoUtil.getUserInfo();
        if (changeType == 0) {// type = 0 修改性别

        } else if (changeType == 1) {

            changeInfo(userInfo.getUserId() + "", userInfo.getAvatarPath(),
                    userInfo.getNickname(), userInfo.getSex() + "", cityCode,
                    userInfo.getSignature());

        } else if (changeType == 2) {

            changeInfo(userInfo.getUserId() + "", avatar, userInfo.getNickname(),
                    userInfo.getSex() + "", userInfo.getCity(),
                    userInfo.getSignature());

        }

    }

    private void changeInfo(String userId, final String avatarPath, final String nickName, final String sex, final String city, final String signature) {

        PutUserInfoRequest request = new PutUserInfoRequest(userId, avatarPath, nickName, sex, city, signature,
                new ResponseListener<PutUserInfoResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "提交中...");
                    }

                    @Override
                    public void onCacheResponse(PutUserInfoResponse response) {

                    }

                    @Override
                    public void onResponse(PutUserInfoResponse response) {
                        if (response == null || response.getResult().getUserInfo() == null) {
                            ToastUtil.showShortToast("操作失败,请检查网络连接");
                            return;
                        }

                        ToastUtil.showLongToast("修改成功");

                        if (changeType == 0) {

                            GetUserInfoUtil.getUserInfo()
                                    .setSex(sexTag);
                            sexView.setText(sexTag == 0 ? "男" : "女");


                        } else if (changeType == 1) {

                            GetUserInfoUtil.getUserInfo()
                                    .setCity(cityCode);

                            addressView.setText(cityString);
                        } else if (changeType == 2) {
                            GetUserInfoUtil.getUserInfo()
                                    .setAvatar(normalAvatarUrl);

                            getNewAvatar(normalAvatarUrl);

                            UserMainEventItem item = new UserMainEventItem();
                            item.setAvatar(normalAvatarUrl);
                            item.setType(1);

                            EventBus.getDefault().post(item);
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


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    ProgressDialog.getInstance().show(mContext, "图片上传中...");
                    break;
                case 11:
                    UpLoadPicInfo upLoadPicInfo = (UpLoadPicInfo) msg.obj;

                    avatar = upLoadPicInfo.getNormalPicturePath();

                    normalAvatarUrl = upLoadPicInfo.getNormalPictureUrl();

                    changeType = 2;

                    confirmChange();

                    break;

                case 12:

                    ProgressDialog.getInstance().cancel();

                    ToastUtil.showLongToast(msg.obj + "");

                    break;
            }
        }

        ;
    };

    private String avatar;

    private String normalAvatarUrl;

    private void upLoadPicture(Uri uri) {
        PictureInfo pictureInfo = PictureUtils.processPictureFile(
                CACHE_DIR_NAME, uri);

        OkHttpMultipartUpLoad.getInstance().postPictures("4",
                pictureInfo.getFile().getAbsolutePath(),
                new UpLoadPictureCallBack());

    }

    @Override
    public void buttType(String ButtType, String[] list, int pos) {
        switch (ButtType) {
            case ListDialog.LEFT_BUTT:
                sexTag = pos;
                UserInfo userInfo = GetUserInfoUtil.getUserInfo();
                changeInfo(userInfo.getUserId() + "", userInfo.getAvatarPath(),
                        userInfo.getNickname(), sexTag + "",
                        userInfo.getCity() + "", userInfo.getSignature());
                break;
            case ListDialog.RIGHT_BUTT:

                break;
        }
    }


    class UpLoadPictureCallBack implements OkHttpMultipartUpLoad.UploadFileCallBack {

        @Override
        public void uploadStart() {
            handler.sendEmptyMessage(10);
        }

        @Override
        public void uploadSuccess(UpLoadPicInfo upLoadPicInfo) {
            Message message = handler.obtainMessage();

            message.obj = upLoadPicInfo;

            message.what = 11;

            message.sendToTarget();
        }

        @Override
        public void uploadError(Exception e) {
            Message message = handler.obtainMessage();

            message.obj = e.getMessage() + "";

            message.what = 12;

            message.sendToTarget();
        }

    }

    @Override
    public void onBackPressed() {
        CacheUserInfo cacheUserInfo = new CacheUserInfo();
        cacheUserInfo.setUserInfo(GetUserInfoUtil.getUserInfo());
        setResult(RESULT_OK);
        super.onBackPressed();
    }


    private String cacheSize() {
        String s = null;
        try {
            s = DataCleanManager.getCacheSize(new File(getExternalCacheDir().getAbsolutePath()));

        } catch (Exception e) {

            e.printStackTrace();
        }
        return s;
    }

}
