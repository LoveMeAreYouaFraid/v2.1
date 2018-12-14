package com.nautilus.ywlfair.module.vendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.module.BaseActivity;

/**
 * Created by Administrator on 2016/3/28.
 */
public class EditViewActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_edit_activity);

        mContext = this;

        findViewById(R.id.im_back).setOnClickListener(this);

        findViewById(R.id.tv_top_bar_right).setOnClickListener(this);

        editText = (EditText) findViewById(R.id.et_content);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_back:

                finish();

                break;

            case R.id.tv_top_bar_right:
                String companyName = editText.getText().toString().trim();

                if(TextUtils.isEmpty(companyName)){
                    ToastUtil.showLongToast("请输入公司名称");

                    return;
                }

                Intent intent = new Intent();

                intent.putExtra(Constant.KEY.COMPANY, companyName);

                setResult(RESULT_OK, intent);

                finish();

                break;
        }
    }
}
