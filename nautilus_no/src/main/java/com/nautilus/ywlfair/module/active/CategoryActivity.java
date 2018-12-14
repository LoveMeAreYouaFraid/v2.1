package com.nautilus.ywlfair.module.active;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.entity.bean.ActivitySysConfig;
import com.nautilus.ywlfair.entity.response.GetActivityBoothApplicationConfigResponse;
import com.nautilus.ywlfair.widget.flowlayout.FlowLayout;
import com.nautilus.ywlfair.widget.flowlayout.TagAdapter;
import com.nautilus.ywlfair.widget.flowlayout.TagFlowLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 货品分类
 * Created by Administrator on 2016/3/7.
 */
public class CategoryActivity extends Activity implements View.OnClickListener {

    private TextView appTitle, appNext;

    private boolean isOnCk = false;

    private Context mContext;

    private GetActivityBoothApplicationConfigResponse response;

    private TagFlowLayout tagFlowLayout;

    private int otherId;

    private long lastChangeTime = 0;

    private EditText editText;

    private List<ActivitySysConfig> type;

    private List<ActivitySysConfig> checkedList;

    private String key;

    private TextView goodsType;

    private int[] otherIds = new int[]{13, 20, 37};

    private MyTagAdapter myTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_activity);

        mContext = this;

        Object obj = getIntent().getSerializableExtra(Constant.KEY.GOODS_CATEGORY);

        if(obj instanceof GetActivityBoothApplicationConfigResponse){

            response = (GetActivityBoothApplicationConfigResponse)obj ;
        }

        key = getIntent().getStringExtra(Constant.KEY.TYPE);

        View view = findViewById(R.id.img_back);
        view.setOnClickListener(this);

        editText = (EditText) findViewById(R.id.edit_text);

        appTitle = (TextView) findViewById(R.id.tv_title);

        appNext = (TextView) findViewById(R.id.tv_right_btn);

        tagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flow_layout);

        appTitle.setText("选择货品品类");

        appNext.setText("确定");

        appNext.setVisibility(View.VISIBLE);

        appNext.setOnClickListener(this);

        checkedList = new ArrayList<>();

        goodsType = (TextView) findViewById(R.id.tv_goods_type);

        if (key.equals("normal")) {

            goodsType.setText("您当前选择的为普通类货品，只能购买普通摊位");

            otherId = otherIds[1];

            if(response != null)
                type = response.getResult().getActivitySysConfig().getType2();


        } else if (key.equals("food")) {

            if(response != null)
                type = response.getResult().getActivitySysConfig().getType2();

            goodsType.setText("您当前选择的为食品类货品，只能购买食品类摊位");

            otherId = otherIds[1];

        } else if (key.equals("vendor")) {

            type = (List<ActivitySysConfig>) getIntent().getSerializableExtra(Constant.KEY.GOODS_CATEGORY);

            editText.setHint("请选择货品品类");

            goodsType.setVisibility(View.GONE);

            otherId = otherIds[0];

        }else if(key.equals("city")){

            TextView textView = (TextView) findViewById(R.id.my_msg);
            textView.setText("热门城市（可多选）");

            editText.setHint("您可以选择或者输入城市");

            goodsType.setVisibility(View.GONE);

            if(response != null)
                type = response.getResult().getActivitySysConfig().getType4();

            otherId = otherIds[2];
        }

        if (type == null) {
            type = new ArrayList<>();
        }

        myTagAdapter = new MyTagAdapter(type);

        tagFlowLayout.setAdapter(myTagAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isOnCk) {

                    long currentTime = System.currentTimeMillis();

                    if (currentTime - lastChangeTime > 400) {

                        getCurrentSelectTag(editText.getText().toString());

                        lastChangeTime = currentTime;

                    }
                }

                isOnCk = false;

            }
        });

        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                isOnCk = true;

                ActivitySysConfig config = type.get(position);

                if (checkedList.contains(config)) {

                    checkedList.remove(config);
                } else {
                    checkedList.add(config);

                }

                setCheckedValue();

                return false;
            }
        });

        String defaultString = getIntent().getStringExtra(Constant.KEY.DEFAULT_TEXT);

        if(!TextUtils.isEmpty(defaultString)){
            editText.setText(defaultString + "，");
        }

    }


    private void getCurrentSelectTag(String tagString) {

        tagString = tagString.replaceAll(",", "，");

        checkedList.clear();

        Set<Integer> checkedSet = new HashSet<>();

        if (!TextUtils.isEmpty(tagString)) {

            String[] strings = tagString.split("，");

            for (int i = 0; i < strings.length; i++) {

                String tag = strings[i];

                boolean isTag = false;

                for (int j = 0; j < type.size(); j++) {
                    ActivitySysConfig sysConfig = type.get(j);

                    if (sysConfig.getName().equals(tag)) {
                        checkedList.add(sysConfig);

                        isTag = true;

                        checkedSet.add(j);

                    }
                }

                if (!isTag) {
                    ActivitySysConfig activitySysConfig = new ActivitySysConfig();
                    activitySysConfig.setId(otherId + "");

                    activitySysConfig.setName(tag);

                    checkedList.add(activitySysConfig);
                }

            }

        }

        myTagAdapter.setSelectedList(checkedSet);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_btn:
                String productKindName = editText.getText().toString().trim();

                if (TextUtils.isEmpty(productKindName)) {

                    ToastUtil.showShortToast("品类不能为空");

                    return;
                }

                if(productKindName.endsWith("，")){
                    productKindName = productKindName.substring(0, productKindName.length() - 1);
                }

                Intent intent = new Intent();

                intent.putExtra(Constant.KEY.PRODUCT_KIND, productKindName);

                intent.putExtra(Constant.KEY.PRODUCT_KIND_ID, setCheckedIds());

                setResult(RESULT_OK, intent);

                finish();

                break;

            case R.id.img_back:
                finish();
                break;
        }

    }

    class MyTagAdapter extends TagAdapter {

        public MyTagAdapter(List datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, final int position, Object o) {

            final TextView stallTypeView = new TextView(mContext);

            if (type.get(position).getName().equals("其他")) {
                stallTypeView.setVisibility(View.GONE);
            }
            stallTypeView.setText(type.get(position).getName());

            stallTypeView.setTextSize(14);

            stallTypeView.setTextColor(getResources().getColor(R.color.normal_black));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 0, BaseInfoUtil.dip2px(10), BaseInfoUtil.dip2px(10));

            stallTypeView.setLayoutParams(params);

            stallTypeView.setGravity(Gravity.CENTER);

            stallTypeView.setPadding(BaseInfoUtil.dip2px(15), BaseInfoUtil.dip2px(10), BaseInfoUtil.dip2px(15), BaseInfoUtil.dip2px(10));

            stallTypeView.setBackgroundResource(R.drawable.bg_goods_selected_bule_or_whit);


            if (key.equals("normal")) {
                if (stallTypeView.getText().equals("预包装食品（需提交三证）") ||
                        stallTypeView.getText().equals("现场制作餐饮（需提交三证）")) {

                    stallTypeView.setVisibility(View.GONE);
                }
            } else if (key.equals("food")) {
                if (stallTypeView.getText().equals("预包装食品（需提交三证）") ||
                        stallTypeView.getText().equals("现场制作餐饮（需提交三证）")) {

                    stallTypeView.setVisibility(View.VISIBLE);

                } else {
                    stallTypeView.setVisibility(View.GONE);
                }
            }

            return stallTypeView;
        }
    }

    private void setCheckedValue() {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < checkedList.size(); i++) {

            ActivitySysConfig config = checkedList.get(i);

            sb.append(config.getName());

            sb.append("，");

        }

        editText.setText(sb.toString());

    }

    private String setCheckedIds() {

        StringBuffer idSb = new StringBuffer();

        for (int i = 0; i < checkedList.size(); i++) {

            ActivitySysConfig config = checkedList.get(i);

            idSb.append(config.getId());

            if(i != checkedList.size() - 1){

                idSb.append(",");
            }
        }

        return idSb.toString();

    }


}
