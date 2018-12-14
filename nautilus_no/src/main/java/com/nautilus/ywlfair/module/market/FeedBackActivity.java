package com.nautilus.ywlfair.module.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.request.PostProblemFeedbackRequest;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.SelectListTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.VENDOR_ID;

/**
 * 扫码付款—>问题反馈
 * Created by Administrator on 2016/6/18.
 */
public class FeedBackActivity extends BaseActivity implements SelectListTextView.OnLikePressedListener {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.my_select_text)
    SelectListTextView mySelectText;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.other)
    LinearLayout other;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.ed_phone)
    EditText edPhone;
    private Context mContext;

    private String contentTxt = "";

    private String feedbackType = "2";

    private String vendorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.feed_back_activity);
        ButterKnife.bind(this);

        vendorId = getIntent().getStringExtra(VENDOR_ID);

        tvRightBtn.setText("提交");
        tvRightBtn.setVisibility(View.VISIBLE);
        tvTitle.setText("问题反馈");
        phone.setText(Html.fromHtml("<font color='#FF0000'>*</font>手机号"));
        mySelectText.setOnLikePressedListener(this);

    }

    /**
     * 点击空白位置 隐藏软键盘
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @OnClick({R.id.img_back, R.id.other, R.id.tv_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.other:
                feedbackType = "1";
                mySelectText.ClearImg();// 点击其他时，清除单选项的勾

                startActivityForResult(new Intent(mContext, OtherReasonsActivity.class).putExtra(Constant.KEY.TYPE, 0), 0);

                break;
            case R.id.tv_right_btn:
                if (vendorId.length() < 1) {
                    ToastUtil.showShortToast("卖家信息获取失败，请稍后再试");
                    return;
                }
                if (!StringUtils.isMobileNumber(edPhone.getText() + "")) {
                    ToastUtil.showShortToast("请输入正确的手机号");
                    return;
                }
                PostProblemFeedback();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                contentTxt = data.getStringExtra(Constant.KEY.INPUT_TXT);
                tvOther.setText(contentTxt);
            }
        }

    }

    private void PostProblemFeedback() {

        PostProblemFeedbackRequest request = new PostProblemFeedbackRequest(
                GetUserInfoUtil.getUserInfo().getUserId() + "", feedbackType,
                edPhone.getText() + "", contentTxt, vendorId, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "数据提交中.....");
            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if (response != null) {
                    ToastUtil.showShortToast("提交成功");
                    finish();
                } else {
                    ToastUtil.showShortToast("提交数据失败");
                }


            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast("提交请求数据失败");
            }

            @Override
            public void onFinish() {
                ProgressDialog.getInstance().cancel();

            }
        });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void payType(String type) {
        feedbackType = type;
    }
}
