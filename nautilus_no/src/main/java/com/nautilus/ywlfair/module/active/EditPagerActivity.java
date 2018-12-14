package com.nautilus.ywlfair.module.active;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.AddPictureAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.FileUtils;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.KeyBoardUtil;
import com.nautilus.ywlfair.common.utils.PictureUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.OkHttpMultipartUpLoad;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.entity.bean.PictureInfo;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;
import com.nautilus.ywlfair.entity.bean.event.EventCommentType;
import com.nautilus.ywlfair.entity.bean.event.EventOrderChange;
import com.nautilus.ywlfair.entity.request.PostCommentRequest;
import com.nautilus.ywlfair.entity.response.PostCommentResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.nautilus.ywlfair.widget.WrapContentHeightGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;


public class EditPagerActivity extends BaseActivity implements OnClickListener, RatingBar.OnRatingBarChangeListener {

    private Context mContext;

    private boolean isck = true;

    private EditText contentEt;

    private WrapContentHeightGridView mGridView;

    private AddPictureAdapter mAdapter;

    private TextView addressInfo;

    private static final int PIC_NUM = 6;

    private ArrayList<Uri> mList;

    private static final String CACHE_DIR_NAME = "choose_photo";

    private String itemId;

    private int upLoadNum = 0;

    private List<UpLoadPicInfo> upLoadPicList;

    private String location = "";

    private String coordinates = "";

    private String commentType;

    private RatingBar ratingBar;

    private int ratingValue = 10;

    private int orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_pager_activity);

        mContext = this;

        itemId = getIntent().getStringExtra(Constant.KEY.ITEM_ID);

        commentType = getIntent().getStringExtra(Constant.KEY.TYPE);

        orderStatus = getIntent().getIntExtra(Constant.KEY.ORDER_STATUS, 0);

        upLoadPicList = new ArrayList<>();

        initViews();

        initDate();

    }

    private void initViews() {
        View topBarBack = findViewById(R.id.tv_top_bar_back);
        topBarBack.setOnClickListener(this);

        View topBarRight = findViewById(R.id.tv_top_bar_right);
        topBarRight.setOnClickListener(this);

        contentEt = (EditText) findViewById(R.id.et_content);

        mGridView = (WrapContentHeightGridView) findViewById(R.id.gv_pictures);

        addressInfo = (TextView) findViewById(R.id.tv_address);
        addressInfo.setOnClickListener(this);

        if (commentType.equals("4")) {
            View setRating = findViewById(R.id.rl_set_rating);
            setRating.setVisibility(View.VISIBLE);

            ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
            ratingBar.setOnRatingBarChangeListener(this);
        }

    }

    private void initDate() {
        mList = new ArrayList<>();

        mAdapter = new AddPictureAdapter(this, mList);

        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if (mList.size() != 0 && position != mList.size()) {

                    Intent intent = new Intent(mContext,
                            ShowPicturesPagerActivity.class);

                    ArrayList<Uri> uris = new ArrayList<>();
                    uris.addAll(mList);

                    intent.putExtra(Constant.KEY.URIS, uris);
                    intent.putExtra(Constant.KEY.POSITION, position);

                    startActivityForResult(intent,
                            Constant.REQUEST_CODE.SHOW_PICTURES);

                } else {

                    choosePictureSource();
                }


            }
        });
    }

    private ArrayList<String> mSelectPath;

    /**
     * 选择图片
     */
    private void choosePictureSource() {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Constant.REQUEST_CODE.SHOW_PICTURES) {

                if (data == null || !data.hasExtra(Constant.KEY.URIS)) {
                    return;
                }

                @SuppressWarnings("unchecked")
                ArrayList<Uri> uris = (ArrayList<Uri>) data
                        .getSerializableExtra(Constant.KEY.URIS);

                deleteDefaultSelectPic(uris);

                mAdapter.notifyDataSetChanged();
            }

            if (requestCode == Constant.REQUEST_CODE.SELECT_IMAGE) {

                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                if (mSelectPath != null && mSelectPath.size() > 0) {

                    mList.clear();

                    for (String filePath : mSelectPath) {

                        PictureInfo pictureInfo = PictureUtils.processPictureFile(
                                CACHE_DIR_NAME, filePath);

                        Uri newUri = Uri.fromFile(pictureInfo.getFile());

                        mList.add(mList.size(), newUri);
                    }

                    mAdapter.notifyDataSetChanged();

                }

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

    }

    private void upLoadPicture(int number) {

        Uri uri = mList.get(number);

        OkHttpMultipartUpLoad.getInstance().postPictures("2",FileUtils.uri2Path(uri), new UpLoadPictureCallBack());
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        ratingValue = (int) (rating * 2);
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

    private void createComment() {

        String userId = GetUserInfoUtil.getUserInfo()
                .getUserId()
                + "";

        String content = contentEt.getText().toString();

        PostCommentRequest request = new PostCommentRequest(userId,
                content, itemId, commentType, ratingValue + "", getPicturesUrl(),
                location, coordinates, new ResponseListener<PostCommentResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "提交中...");
            }

            @Override
            public void onCacheResponse(PostCommentResponse response) {
            }

            @Override
            public void onResponse(PostCommentResponse response) {
                if (response == null || response.getResult().getCommentInfo() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }

                if (commentType.equals("4")) {
                    EventBus.getDefault().post(new EventOrderChange(4, orderStatus));
                } else {
                    List<PicInfo> picInfoList = new ArrayList<>();

                    for (Uri uri : mList) {
                        PicInfo picInfo = new PicInfo();
                        picInfo.setImgUrl(uri.toString());
                        picInfo.setThumbnailUrl(uri.toString());
                        picInfoList.add(picInfo);
                    }
                    response.getResult().getCommentInfo().setPhotos(picInfoList);

                    EventCommentType eventCommentType = new EventCommentType(response.getResult().getCommentInfo(), 0, 0);

                    EventBus.getDefault().post(eventCommentType);
                }

                setResult(RESULT_OK);

                finish();
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

    private void confirm() {

        if (mList.size() > 0) {

            // 开始上传图片
            upLoadPicture(upLoadNum);
        } else {
            createComment();
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {

                case 0:
                    if (upLoadNum == 0) {

                        ProgressDialog.getInstance().show(mContext, "图片上传中...", false);
                    }
                    break;

                case 1:

                    UpLoadPicInfo upLoadPicInfo = (UpLoadPicInfo) msg.obj;

                    upLoadPicList.add(upLoadPicInfo);

                    if (upLoadNum < mList.size() - 1) {

                        upLoadNum++;

                        upLoadPicture(upLoadNum);
                    } else {
                        ProgressDialog.getInstance().cancel();

                        createComment();
                    }

                    break;

                case 2:
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:

                KeyBoardUtil.hideSoftKeyboard(EditPagerActivity.this);

                finish();
                break;

            case R.id.tv_top_bar_right:

                String content = contentEt.getText().toString();

                if (TextUtils.isEmpty(content)) {
                    ToastUtil.showShortToast("请输入评论内容");
                    return;
                }
                confirm();
                break;

            case R.id.tv_address:


                if (isck == false) {
                    addressInfo.setText("添加位置");
                    location = "";
                    coordinates = "";
                    isck = true;
                } else {
                    String locDescription = MyApplication.getInstance()
                            .getLocationDescription();
                    location = TextUtils.isEmpty(locDescription) ? "" : locDescription;
                    if (MyApplication.getInstance().getLongitude() != 0
                            && MyApplication.getInstance().getLatitude() != 0) {
                        coordinates = MyApplication.getInstance().getLongitude() + ","
                                + MyApplication.getInstance().getLatitude();
                    }
                    addressInfo.setText(location);
                    isck = false;
                }

                break;
        }
    }
}
