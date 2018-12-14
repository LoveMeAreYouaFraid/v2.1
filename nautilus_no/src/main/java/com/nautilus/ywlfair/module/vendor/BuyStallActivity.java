package com.nautilus.ywlfair.module.vendor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.BoothCategory;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.entity.request.GetActivityInfoRequest;
import com.nautilus.ywlfair.entity.response.GetActivityInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.TicketOrderActivity;
import com.nautilus.ywlfair.module.mine.TicketOrderActivity.Mode;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.flowlayout.FlowLayout;
import com.nautilus.ywlfair.widget.flowlayout.TagFlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BuyStallActivity extends BaseActivity implements OnClickListener {

    private Context mContext;

    private NautilusItem mNautilusItem;

    private TextView stallPrice;

    private TextView stallNum;

    private TextView tvBootMsg;

    private TextView activeDate;

    private TextView activeIntroduceView;

    private BoothCategory currentBooth;

    private TagFlowLayout mFlowLayout;

    private List<TextView> tagViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.buy_stall);

        mContext = this;

        Intent intent = getIntent();

        tagViews = new ArrayList<>();

        if (intent.hasExtra(Constant.KEY.NAUTILUSITEM)) {
            mNautilusItem = (NautilusItem) intent.getSerializableExtra(
                    Constant.KEY.NAUTILUSITEM);

            initViews();

        } else if (intent.hasExtra(Constant.KEY.ITEM_ID)) {
            String itemId = intent.getStringExtra(Constant.KEY.ITEM_ID);

            getData(itemId);
        }


    }

    private void initViews() {


        tvBootMsg = (TextView) findViewById(R.id.tv_booth_msg);

        stallPrice = (TextView) findViewById(R.id.new_price);

        stallPrice.setText("折扣价：" + StringUtils.getMoneyFormat(currentBooth.getPrice()));

        stallNum = (TextView) findViewById(R.id.old_price);
//
        String startTimeLine = mNautilusItem.getStartTime();

        String endTimeLine = mNautilusItem.getEndTime();

        String dateText = "活动日期: " + TimeUtil.getYearMonthAndDay(Long.valueOf(startTimeLine)) + " ~ "
                + TimeUtil.getYearMonthAndDay(Long.valueOf(endTimeLine));

        activeDate = (TextView) findViewById(R.id.tv_date);

        activeDate.setText(dateText);

        View buyStall = findViewById(R.id.bt_buy_stall);

        if (mNautilusItem.getIsBoothApply() == 2 && mNautilusItem.getIsBoothBuy() == 0) {

            buyStall.setEnabled(true);

        } else {
            buyStall.setEnabled(false);

            buyStall.setBackgroundResource(R.drawable.yuanjiaohui);
        }

        buyStall.setOnClickListener(this);

        if (mNautilusItem.getActivityStatus() != 2) {
            buyStall.setEnabled(false);

            buyStall.setBackgroundResource(R.drawable.yuanjiaohui);
        }

        View topBarBack = findViewById(R.id.iv_top_bar_back);

        topBarBack.setOnClickListener(this);

        activeIntroduceView = (TextView) findViewById(R.id.tv_active_introduce);

        activeIntroduceView.setText(mNautilusItem.getAttentionMsg());

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

                if (selectPosSet.isEmpty()) {

                } else {


                    tvBootMsg.setText("摊位说明：\n" + currentBooth.getDesc());

                    if (currentBooth.getDiscount() < currentBooth.getPrice()) {

                        stallNum.setText("原价 " + StringUtils.getMoneyFormat(currentBooth.getPrice()));

                        stallPrice.setText("折扣价：" + StringUtils.getMoneyFormat(currentBooth.getDiscount()));

                        stallNum.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

                        stallPrice.setVisibility(View.VISIBLE);

                    } else {

                        stallNum.setText("摊位价格：" + StringUtils.getMoneyFormat(currentBooth.getPrice()));

                        stallNum.getPaint().setFlags(0);  // 取消设置的的划线

                        stallPrice.setVisibility(View.GONE);
                    }

                }

            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                for (int i = 0; i < parent.getChildCount(); i++) {
                    View childView = parent.getChildAt(i);

                    if (i == position) {
//                        childView.findViewById(R.id.rl_tag).setEnabled(false);

                    } else {
//                        childView.findViewById(R.id.rl_tag).setEnabled(true);
                    }
                }
                return false;
            }
        });
    }


    private void getData(String itemId) {
        GetActivityInfoRequest request = new GetActivityInfoRequest(String.valueOf(itemId), new ResponseListener<GetActivityInfoResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(GetActivityInfoResponse response) {
            }

            @Override
            public void onResponse(GetActivityInfoResponse response) {

                if (response == null || response.getResult().getNautilusItem() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }

                mNautilusItem = response.getResult().getNautilusItem();

                initViews();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_buy_stall:

                if (currentBooth == null) {
                    ToastUtil.showShortToast("请选择购买的摊位类型");
                    return;
                }

                if (currentBooth.getLeftNum() == 0) {
                    ToastUtil.showShortToast("该类型摊位已售完");
                    return;
                }
                Intent intent = new Intent(mContext, TicketOrderActivity.class);

                intent.putExtra(Constant.KEY.MODE, Mode.STALL);

                intent.putExtra(Constant.KEY.NAUTILUSITEM, mNautilusItem);

                intent.putExtra(Constant.KEY.STALL, currentBooth);

                startActivityForResult(intent, Constant.REQUEST_CODE.TO_PAY);

                break;

            case R.id.iv_top_bar_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
        }
    }
}
