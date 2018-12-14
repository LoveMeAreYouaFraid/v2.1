package com.nautilus.ywlfair.module.active;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.entity.bean.AddViewClassInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SmallPublicActivity extends Activity implements OnClickListener {
    private TextView appTitle, appTitleRight, textNum;
    private Intent intent;
    private String intentKey;
    private Context mContext;

    private static final String SHOP_ADDRESS = "Shopaddress";
    private static final String SPEC_NEEDS = "specNeeds";
    private static final String MEDIA_RES_MSG = "mediaResMsg";
    private EditText myEditText;
    private View view, priceLayout;
    private EditText name, power;
    private EditText edPrice1, edPrice2;
    private static final String CACHE_DATA = "cache_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.small_public_activity);
        intent = getIntent();
        mContext = this;
        priceLayout = findViewById(R.id.layout_price);
        intentKey = intent.getStringExtra("key");
        View back = findViewById(R.id.img_back);
        appTitle = (TextView) findViewById(R.id.tv_title);
        appTitleRight = (TextView) findViewById(R.id.tv_right_btn);
        // appTitleRight.setOnClickListener(this);
        back.setOnClickListener(this);
        view = findViewById(R.id.layout_ed_tv);
        myEditText = (EditText) findViewById(R.id.my_edit_text);
        textNum = (TextView) findViewById(R.id.text_num);
        view.setVisibility(View.VISIBLE);
        myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textNum.setText(myEditText.getText().length() + "/" + "200");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (intentKey.equals("GoodsStyle")) {

            appTitle.setText("其他");
            appTitleRight.setText("确定");
            appTitleRight.setVisibility(View.VISIBLE);
            textNum.setText(myEditText.getText().length() + "/" + "100");
            myEditText.setHint("请填写您现场携带的货品的其他风格");
            InputFilter[] filters = {new InputFilter.LengthFilter(100)};
            myEditText.setFilters(filters);
            myEditText.setFadingEdgeLength(100);
            appTitleRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    String style = myEditText.getText().toString().trim();

                    if (TextUtils.isEmpty(style)) {
                        ToastUtil.showShortToast("不能为空");
                        return;
                    }

                    Intent intent = new Intent();

                    intent.putExtra(Constant.KEY.GOODS_STYLE, myEditText.getText().toString());

                    intent.putExtra(Constant.KEY.GOODS_STYLE_TAG, "4");

                    setResult(RESULT_OK, intent);

                    finish();

                }
            });

        }
        if (intentKey.equals("Price")) {

            appTitle.setText("价格区间");
            appTitleRight.setText("确定");
            appTitleRight.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            priceLayout.setVisibility(View.VISIBLE);
            edPrice1 = (EditText) findViewById(R.id.ed_price1);
            edPrice2 = (EditText) findViewById(R.id.ed_price2);
            appTitleRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Integer.valueOf(edPrice2.getText().toString());
                        Integer.valueOf(edPrice1.getText().toString());
                    } catch (Exception e) {
                        ToastUtil.showShortToast("只能输入整数");
                        return;
                    }
                    Log.e("123", "asasdadsasa");
                    if (edPrice1.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("价格不能为空");
                        return;
                    }
                    if (edPrice2.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("价格不能为空");
                        return;
                    }
                    if (Integer.valueOf(edPrice2.getText().toString()) < Integer.valueOf(edPrice1.getText().toString())) {
                        ToastUtil.showShortToast("前者不能大于后者");
                        return;
                    }
                    setResult(RESULT_OK, new Intent().putExtra("Price", edPrice1.getText().toString() + "~" + edPrice2.getText().toString() + "元"));
                    finish();

                }
            });

        }

        if (intentKey.equals("AddView")) {
            view.setVisibility(View.GONE);
            appTitle.setText("请填写用电情况");
            appTitleRight.setText("确定");
            appTitleRight.setVisibility(View.VISIBLE);
            name = (EditText) findViewById(R.id.name);
            power = (EditText) findViewById(R.id.power);
            name.setVisibility(View.VISIBLE);
            power.setVisibility(View.VISIBLE);
            appTitleRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (name.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("电器命不能为空");
                        return;
                    }
                    if (power.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("电器功率不能为空");
                        return;
                    }
                    AddViewClassInfo addViewClassInf = new AddViewClassInfo();

                    addViewClassInf.setPower(power.getText().toString());

                    addViewClassInf.setName(name.getText().toString());

                    if(ActivitySignUpActivity1.booth.getAddViewClassInf() == null){
                        ActivitySignUpActivity1.booth.setAddViewClassInf(new ArrayList<AddViewClassInfo>());
                    }

                    ActivitySignUpActivity1.booth.getAddViewClassInf().add(addViewClassInf);

                    setResult(RESULT_OK);

                    finish();
                }
            });


        }
        if (intentKey.equals("AddViewEdit")) {

            final int position = getIntent().getIntExtra(Constant.KEY.POSITION,0);

            view.setVisibility(View.GONE);

            appTitle.setText("请填写用电情况");

            appTitleRight.setText("确定");

            appTitleRight.setVisibility(View.VISIBLE);

            name = (EditText) findViewById(R.id.name);

            power = (EditText) findViewById(R.id.power);
            name.setVisibility(View.VISIBLE);
            name.setText(ActivitySignUpActivity1.booth.getAddViewClassInf().get(position).getName());

            power.setVisibility(View.VISIBLE);
            power.setText(ActivitySignUpActivity1.booth.getAddViewClassInf().get(position).getPower());

            appTitleRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (name.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("电器命不能为空");
                        return;
                    }
                    if (power.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("电器命不能为空");
                        return;
                    }

                    ActivitySignUpActivity1.booth.getAddViewClassInf().get(position).setName(name.getText().toString());

                    ActivitySignUpActivity1.booth.getAddViewClassInf().get(position).setPower(power.getText().toString());

                    setResult(RESULT_OK);

                    finish();
                }
            });
        }
        if (intentKey.equals(SHOP_ADDRESS)) {
            appTitle.setText("填写实体店地址");
            appTitleRight.setText("确定");
            appTitleRight.setVisibility(View.VISIBLE);
            textNum.setText(myEditText.getText().length() + "/" + "200");
            myEditText.setHint("请填写是地点详细地址，具体到门牌号");
            appTitleRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("123", "asasdadsasa");
                    if (myEditText.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("地址不能为空");
                        return;
                    }
                    ActivitySignUpActivity1.booth.setRealshopFlagText(myEditText.getText().toString());
                    setResult(2);
                    finish();

                }
            });

        }
        if (intentKey.equals(SPEC_NEEDS)) {

            appTitle.setText("特殊需求");
            appTitleRight.setText("确定");
            appTitleRight.setVisibility(View.VISIBLE);
            textNum.setText(myEditText.getText().length() + "/" + "200");
            myEditText.setHint("非必填，我们会尽力满足您的需求的");
            if (ActivitySignUpActivity1.booth.getSpecNeeds().length() > 1) {
                myEditText.setText(ActivitySignUpActivity1.booth.getSpecNeeds());
            }
            LogUtil.e("123123", ActivitySignUpActivity1.booth.getSpecNeeds());
            appTitleRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myEditText.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("您的需求不能为空");
                        return;
                    }
                    ActivitySignUpActivity1.booth.setSpecNeeds(myEditText.getText().toString());
                    setResult(RESULT_OK);
                    finish();

                }
            });

        }

        if (intentKey.equals("introduction")) {

            appTitle.setText("简单文字介绍");
            appTitleRight.setText("确定");


            appTitleRight.setVisibility(View.VISIBLE);
            textNum.setText(myEditText.getText().length() + "/" + "200");
            myEditText.setHint("介绍摊主自己背后的故事，或者介绍货品,请文字尽可能精炼，可能会被用于各平台的宣传。");
            if (!getIntent().getStringExtra("msg").isEmpty()) {
                myEditText.setText(getIntent().getStringExtra("msg"));

            }
            appTitleRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("123", "asasdadsasa");
                    if (myEditText.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("文字介绍不能为空");
                        return;
                    }
                    setResult(RESULT_OK, new Intent().putExtra("introduction", myEditText.getText().toString()));
                    finish();

                }
            });

        }
        if (intentKey.equals(MEDIA_RES_MSG)) {
            appTitle.setText("媒体和赞助资源");
            appTitleRight.setText("确定");
            appTitleRight.setVisibility(View.VISIBLE);
            textNum.setText(myEditText.getText().length() + "/" + "200");
            myEditText.setHint("请填写具体的媒体和赞助资源，合作成功可以减免摊位费，没有的可以不填。");
            if (ActivitySignUpActivity1.booth.getMediaResMsg().length() > 1) {
                myEditText.setText(ActivitySignUpActivity1.booth.getMediaResMsg());
            }
            appTitleRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myEditText.getText().toString().length() == 0) {
                        ToastUtil.showShortToast("不能为空");
                        return;
                    }
                    ActivitySignUpActivity1.booth.setMediaResMsg(myEditText.getText().toString());
                    setResult(1);
                    finish();

                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

        }
    }
}
