package com.nautilus.ywlfair.module.active;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.module.BaseActivity;

/**
 * Created by Administrator on 2016/3/4.
 */
public class GoodsStyle extends BaseActivity implements View.OnClickListener {
    private TextView appTitle, appTitleRight, vintage, manual, independent, other;
    private Context mContext;
    private static final int GOODS_TYPE = 9262;

    private String styleText, styleTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_style);
        mContext = this;
        View back = findViewById(R.id.img_back);
        appTitle = (TextView) findViewById(R.id.tv_title);
        appTitleRight = (TextView) findViewById(R.id.tv_right_btn);
        vintage = (TextView) findViewById(R.id.vintage);
        manual = (TextView) findViewById(R.id.manual);
        independent = (TextView) findViewById(R.id.independent);
        other = (TextView) findViewById(R.id.other);
        other.setOnClickListener(this);
        independent.setOnClickListener(this);
        manual.setOnClickListener(this);
        vintage.setOnClickListener(this);
        appTitleRight.setOnClickListener(this);
        back.setOnClickListener(this);
        appTitle.setText("选择货品风格");
        appTitleRight.setText("确定");
        appTitleRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_btn:
                if (TextUtils.isEmpty(styleText)) {
                    ToastUtil.showShortToast("请选择风格");
                    return;
                } else {

                    Intent intent = new Intent();

                    intent.putExtra(Constant.KEY.GOODS_STYLE, styleText);

                    intent.putExtra(Constant.KEY.GOODS_STYLE_TAG, styleTag);

                    setResult(RESULT_OK, intent);

                    finish();
                }
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.vintage:
                styleText = "vintage";
                styleTag = "1";

                myClick(1);

                break;
            case R.id.manual:
                styleText = "手工制品";
                styleTag = "2";
                myClick(2);
                break;
            case R.id.independent:
                styleText = "独立设计";
                styleTag = "3";

                myClick(3);
                break;
            case R.id.other:
                allRemove();
                startActivityForResult(new Intent(mContext, SmallPublicActivity.class).putExtra("key", "GoodsStyle"), GOODS_TYPE);

                break;
        }


    }

    private void allRemove() {
        vintage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        manual.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        independent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        other.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    private void myClick(int type) {
        allRemove();
        if (type == 1) {
            vintage.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.yes), null);
        }
        if (type == 2) {
            manual.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.yes), null);
        }
        if (type == 3) {
            independent.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.yes), null);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GOODS_TYPE) {

                setResult(RESULT_OK, data);

                finish();
            }
        }
    }
}
