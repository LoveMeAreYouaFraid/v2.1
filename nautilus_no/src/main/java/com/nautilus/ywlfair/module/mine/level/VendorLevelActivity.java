package com.nautilus.ywlfair.module.mine.level;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.PrivilegeInfo;
import com.nautilus.ywlfair.entity.bean.VendorLevel;
import com.nautilus.ywlfair.entity.request.GetVendorVipRequest;
import com.nautilus.ywlfair.entity.response.GetVendorVipResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.webview.UseHelpActivity;
import com.nautilus.ywlfair.widget.CustomLineView;
import com.nautilus.ywlfair.widget.MyHorizontalScrollView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/23.
 */
public class VendorLevelActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private boolean mIsRequesting = false;

    private boolean isFirst = true;

    @BindView(R.id.line_container)
    MyHorizontalScrollView myHorizontalScrollView;

    @BindView(R.id.line_view)
    CustomLineView customLineView;

    @BindView(R.id.tv_top_bar_back)
    View backView;

    @BindView(R.id.iv_top_bar_right)
    View rightView;

    @BindView(R.id.ll_container)
    LinearLayout linearLayout;

    @BindView(R.id.level_grid)
    GridView gridView;

    @BindView(R.id.tv_score)
    TextView scoreView;

    @BindView(R.id.tv_is_can_buy)
    TextView isCanBuyView;

    @BindView(R.id.iv_money_code)
    ImageView moneyCodeView;

    @BindView(R.id.tv_num_privilege)
    TextView privilegeNumView;

    @BindView(R.id.tv_top_bar_title)
    TextView titleView;

    @BindView(R.id.ll_score_detail)
    View scoreDetail;

    @BindView(R.id.ll_check_code)
    View checkCode;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<PrivilegeInfo> privilegeInfoList;

    private VendorLevel vendorLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vendor_level_activity);

        ButterKnife.bind(this);

        mContext = this;

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!mIsRequesting){
                    getData();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    private void startAnimation(List<VendorLevel> vendorLevelList, VendorLevel vendorLevel) {

        if(isFirst){
            customLineView.startDraw(vendorLevelList, vendorLevel.getSaleAmount());

            customLineView.setLineScrollListener(new CustomLineView.OnLineScrollListener() {
                @Override
                public void onLineScroll(float originalX, float scrollX) {

                    ObjectAnimator.ofInt(myHorizontalScrollView, "scroll", (int) originalX, (int) scrollX)
                            .setDuration(500)
                            .start();

                }
            });

            isFirst = false;
        }else{
            customLineView.refreshProgress(vendorLevel.getSaleAmount());
        }

    }

    private void setValue(GetVendorVipResponse response) {
        backView.setOnClickListener(this);

        rightView.setOnClickListener(this);

        titleView.setFocusable(true);

        titleView.setFocusableInTouchMode(true);

        titleView.requestFocus();

        scoreDetail.setOnClickListener(this);

        checkCode.setOnClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == privilegeInfoList.size() - 1){
                    return;
                }

                Intent intent = new Intent(mContext, UseHelpActivity.class);

                intent.putExtra(Constant.KEY.URL, privilegeInfoList.get(position).getPrivilegeUrl());

                intent.putExtra(Constant.KEY.TYPE, 1);

                startActivity(intent);
            }
        });

        if (MyApplication.getInstance().isLogin()) {
            titleView.setText(GetUserInfoUtil.getUserInfo().getNickname());
        }

        List<VendorLevel> vendorLevelList = response.getResult().getVendorLevelList();

        vendorLevel = response.getResult().getVendorLevelInfo();

        privilegeInfoList = response.getResult().getPrivilegeInfoList();

        if (vendorLevel != null) {
            if (vendorLevelList != null ) {
                startAnimation(vendorLevelList, vendorLevel);
            }

            if (privilegeInfoList != null) {

                privilegeInfoList.add(new PrivilegeInfo());

                String privilegeId = "";

                for(VendorLevel level : vendorLevelList){
                    if(level.getLevel() == vendorLevel.getLevel()){
                        privilegeId = level.getPrivilegeId();
                    }

                }
                gridView.setAdapter(new PrivilegeGridAdapter(mContext, privilegeInfoList, privilegeId));
            }

            scoreView.setText(StringUtils.getScoreFormat(vendorLevel.getScore()));

            int privilegeCount = vendorLevel.getPrivilegeCount();

            if (privilegeCount == 0) {
                privilegeNumView.setText("您暂未开启特权");
            } else {
                privilegeNumView.setText("您已开启" + vendorLevel.getPrivilegeCount() + "项特权");
            }
        }

    }

    private void getData() {

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetVendorVipRequest request = new GetVendorVipRequest(userId, new ResponseListener<GetVendorVipResponse>() {
            @Override
            public void onStart() {
//                ProgressDialog.getInstance().show(mContext, "载入中...");
                mSwipeRefreshLayout.setRefreshing(true);

                mIsRequesting = true;
            }

            @Override
            public void onCacheResponse(GetVendorVipResponse response) {

            }

            @Override
            public void onResponse(GetVendorVipResponse response) {
                if (response != null) {
                    setValue(response);
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
//                ProgressDialog.getInstance().cancel();
                mSwipeRefreshLayout.setRefreshing(false);

                mIsRequesting = false;
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.iv_top_bar_right:
                Intent helpIntent = new Intent(mContext, UseHelpActivity.class);

                helpIntent.putExtra(Constant.KEY.URL, vendorLevel.getLevelHelpUrl());

                helpIntent.putExtra(Constant.KEY.TYPE, 3);

                startActivity(helpIntent);

                break;

            case R.id.ll_score_detail:
                Intent scoreIntent = new Intent(mContext, VendorScoreDetailActivity.class);

                startActivity(scoreIntent);

                break;

            case R.id.ll_check_code:
                Intent codeIntent = new Intent(mContext, GetMoneyCodeActivity.class);

                startActivity(codeIntent);

                break;
        }
    }
}
