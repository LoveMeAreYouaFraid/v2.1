package com.nautilus.ywlfair.module.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.request.PostProblemFeedbackRequest;
import com.nautilus.ywlfair.entity.request.PostUserQuestionRequest;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.ACTIVITY_ID;
import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.ACT_ID;
import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.TYPE;
import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.VENDOR_ID;

/**
 * 其他原因
 * Created by Administrator on 2016/6/18.
 */
public class OtherReasonsActivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.ed_other_reasons)
    EditText edOtherReasons;
    @BindView(R.id.tv_text_num)
    TextView tvTextNum;

    private Context mContext;

    private int intentType;

    private String actId;

    private String[] titles = new String[]{"其他原因", "我要提问"};
    private String[] rightButts = new String[]{"保存", "提交"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.other_reason_layout);
        ButterKnife.bind(this);

        mContext = this;

        intentType = getIntent().getIntExtra(Constant.KEY.TYPE, 0);

        tvTitle.setText(titles[intentType]);
        tvRightBtn.setText(rightButts[intentType]);
        tvRightBtn.setVisibility(View.VISIBLE);

        if (intentType == 1) {
            actId = getIntent().getStringExtra(ACT_ID);
        }


        edOtherReasons.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                tvTextNum.setText((500 - s.toString().length()) + "");

            }
        });
    }

    @OnClick({R.id.img_back, R.id.tv_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_right_btn:
                if (edOtherReasons.getText().length() == 0) {
                    ToastUtil.showShortToast("请输入类容");
                    //请稍后尝试
                    return;
                }

                if (intentType == 0) {
                    setResult(RESULT_OK, new Intent().putExtra(Constant.KEY.INPUT_TXT, edOtherReasons.getText() + ""));
                    finish();
                } else if (intentType == 1) {
                    PostUserQuestion();
                }
                break;
        }
    }

    private void PostUserQuestion() {

        PostUserQuestionRequest request = new PostUserQuestionRequest(
                GetUserInfoUtil.getUserInfo().getUserId() + "", actId, edOtherReasons.getText() + "", new ResponseListener<InterfaceResponse>() {
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
                    ToastUtil.showShortToast("问题提交成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtil.showShortToast("请稍后尝试");
                }


            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast("请稍后尝试");
            }

            @Override
            public void onFinish() {
                ProgressDialog.getInstance().cancel();

            }
        });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }
}
