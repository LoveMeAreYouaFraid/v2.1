package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.FileUtils;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.PictureUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.OkHttpMultipartUpLoad;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.PictureInfo;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;
import com.nautilus.ywlfair.entity.request.PostBindAliPayRequest;
import com.nautilus.ywlfair.entity.response.PostBindAliPayResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.vendor.RegistrationStall;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/6/17.
 */
public class BindAliPayActivity extends BaseActivity implements View.OnClickListener {

    private static final String CACHE_DIR_NAME = "choose_photo";

    private Context mContext;

    private int aliPayType = 1;

    private String certificateImageUrl;

    private String selectedString;

    private String account, name, idCard;

    @BindView(R.id.ck_ali)
    CheckBox child1;

    @BindView(R.id.ck_wx)
    CheckBox child2;

    @BindView(R.id.et_ali_account)
    EditText countEdit;

    @BindView(R.id.et_count_name)
    EditText nameEdit;

    @BindView(R.id.iv_add_first)
    AutoAdjustHeightImageView addImage;

    @BindView(R.id.et_id_card)
    EditText cardEdit;

    @BindView(R.id.tv_tips)
    TextView tipView;

    @BindView(R.id.tv_number_type)
    TextView numberTypeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bind_ali_pay_activity);

        ButterKnife.bind(this);

        mContext = this;

        initView();
    }

    private void initView() {
        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);
        findViewById(R.id.rl_first_position).setOnClickListener(this);

        findViewById(R.id.rl_second_position).setOnClickListener(this);

        findViewById(R.id.bt_confirm).setOnClickListener(this);

        addImage.setOnClickListener(this);

        addImage.setTag(0);
    }

    private void exchangeType(){
        if(aliPayType == 1){
            countEdit.setText("");
            countEdit.setHint("请输入个人支付宝账户");

            nameEdit.setText("");
            nameEdit.setHint("请输入支付宝认证姓名");

            cardEdit.setText("");
            cardEdit.setHint("请输入身份证号码");

            tipView.setText("上传身份证头像面");

            numberTypeView.setText("身份证号码");
        }else{
            countEdit.setText("");
            countEdit.setHint("请输入企业支付宝账户");

            nameEdit.setText("");
            nameEdit.setHint("请输入支付宝企业姓名");

            cardEdit.setText("");
            cardEdit.setHint("请输入营业执照注册号");

            tipView.setText("上传营业执照");

            numberTypeView.setText("营业执照注册号");
        }
    }

    private void confirm() {

        account = countEdit.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            ToastUtil.showShortToast("请填写支付宝账号");
            return;
        }

        name = nameEdit.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShortToast("请填写支付宝认证名");
            return;
        }

        idCard = cardEdit.getText().toString().trim();

        if (TextUtils.isEmpty(idCard)) {
            ToastUtil.showShortToast("请填写证件号码");
            return;
        }

        if (TextUtils.isEmpty(selectedString)) {
            ToastUtil.showLongToast("请上传证件照");
            return;
        }

        upLoadPicture();

    }

    private void bindAliPay(){
        int userId = GetUserInfoUtil.getUserInfo().getUserId();

        PostBindAliPayRequest request = new PostBindAliPayRequest(userId, aliPayType, account, name, idCard, certificateImageUrl,
                new ResponseListener<PostBindAliPayResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(mContext, "正在提交...");
                    }

                    @Override
                    public void onCacheResponse(PostBindAliPayResponse response) {

                    }

                    @Override
                    public void onResponse(PostBindAliPayResponse response) {
                        if(response != null){
                            toNextActivity(aliPayType, account);
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

    private void toNextActivity(int aliPayType, String account){

        Intent intent = new Intent(mContext, BindAliStatusActivity.class);

        intent.putExtra(Constant.REQUEST.KEY.ALI_PAY_TYPE, aliPayType);

        intent.putExtra(Constant.REQUEST.KEY.ACCOUNT, account);

        startActivity(intent);

        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.rl_first_position:
                child2.setChecked(false);
                child1.setChecked(true);

                aliPayType = 1;

                exchangeType();
                break;

            case R.id.rl_second_position:
                child1.setChecked(false);
                child2.setChecked(true);

                aliPayType = 2;

                exchangeType();

                break;

            case R.id.bt_confirm:
                confirm();
                break;

            case R.id.iv_add_first:
                pressAddPicture(v);
                break;

        }
    }

    private void pressAddPicture(View view) {

        int tag = (int) view.getTag();

        if (tag == 0) {

            choosePicture();

        } else {
            Intent intent = new Intent(mContext,
                    ShowPicturesPagerActivity.class);

            ArrayList<Uri> uris = new ArrayList<>();

            uris.add(Uri.parse(selectedString));

            intent.putExtra(Constant.KEY.URIS, uris);

            intent.putExtra(Constant.KEY.POSITION, 0);

            intent.putExtra(Constant.KEY.CAN_DELETE, true);

            startActivityForResult(intent, Constant.REQUEST_CODE.SHOW_PICTURES);
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

                addPicResult(data);

            } else if (requestCode == Constant.REQUEST_CODE.SHOW_PICTURES) {

                deleteResult(data);

            }
        }
    }

    private void addPicResult(Intent data) {

        ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

        if (mSelectPath != null && mSelectPath.size() > 0) {

            PictureInfo pictureInfo = PictureUtils.processPictureFile(
                    CACHE_DIR_NAME, mSelectPath.get(0));

            Uri newUri = Uri.fromFile(pictureInfo.getFile());

            selectedString = newUri.toString();

            addImage.setTag(1);

            ImageLoader.getInstance().displayImage(newUri.toString(), addImage, ImageLoadUtils.createNoRoundedOptions());
        }
    }

    private void deleteResult(Intent data) {

        ArrayList<Uri> uris = (ArrayList<Uri>) data
                .getSerializableExtra(Constant.KEY.URIS);

        if (uris.size() == 0) {
            addImage.setTag(0);

            addImage.setImageResource(R.drawable.bg_add_identity_selector);

            selectedString = "";
        }

    }

    private void upLoadPicture() {

        Uri uri = Uri.parse(selectedString);

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
                    ProgressDialog.getInstance().show(mContext, "图片上传中...");

                    break;

                case 1:
                    UpLoadPicInfo upLoadPicInfo = (UpLoadPicInfo) msg.obj;

                    ProgressDialog.getInstance().cancel();

                    certificateImageUrl = upLoadPicInfo.getNormalPicturePath();

                    bindAliPay();

                    break;

                case 2:
                    ProgressDialog.getInstance().cancel();

                    ToastUtil.showLongToast(msg.obj + "");

                    break;
            }
        }

    };
}
