package com.nautilus.ywlfair.module.mine.level;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ScoreInfo;
import com.nautilus.ywlfair.entity.request.GetScoreListRequest;
import com.nautilus.ywlfair.entity.response.GetScoreListResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.webview.UseHelpActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/23.
 */
public class VendorScoreDetailActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private List<ScoreInfo> scoreInfoList;

    private ScoreInfoListAdapter adapter;

    private String startTime;

    private String endTime;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    @BindView(R.id.img_back)
    View backView;

    @BindView(R.id.tv_right_btn)
    TextView rightView;

    @BindView(R.id.tv_title)
    TextView titleView;

    @BindView(R.id.tv_total_score)
    TextView totalScoreView;

    @BindView(R.id.lv_vendor_score)
    LoadMoreListView listView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.empty)
    View emptyView;

    private String helpUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vendor_score_detail_activity);

        ButterKnife.bind(this);

        mContext = this;

        setTitleBar();

        scoreInfoList = new ArrayList<>();

        adapter = new ScoreInfoListAdapter(mContext, scoreInfoList);

        listView.setAdapter(adapter);

        startTime = TimeUtil.getYearMonthAndDay(System.currentTimeMillis());

        endTime = getThreeMonthAgo();

        getData(startTime, endTime, 0);
    }

    private void setTitleBar() {
        backView.setOnClickListener(this);

        rightView.setText("筛选");

        rightView.setOnClickListener(this);

        titleView.setText("摊主积分");

        findViewById(R.id.tv_instruction).setOnClickListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mIsRequesting) {
                    return;
                }

                mRequestingNumber = PAGE_START_NUMBER;

                mIsNoMoreResult = false;

                getData(startTime, endTime, 0);
            }
        });

        listView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!mIsNoMoreResult && scoreInfoList.size() > 0 && !mIsRequesting) {

                    mRequestingNumber = scoreInfoList.size();

                    getData(startTime, endTime, 0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_top_bar_right:
                break;

            case R.id.tv_instruction:

                Intent intent = new Intent(mContext, UseHelpActivity.class);

                intent.putExtra(Constant.KEY.TYPE, 2);

                intent.putExtra(Constant.KEY.URL, helpUrl);

                startActivity(intent);

                break;
        }
    }

    private void getData(String startTime, String endTime, int queryType) {

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetScoreListRequest request = new GetScoreListRequest(userId, startTime, endTime, queryType, mRequestingNumber, PER_PAGE_NUMBER,
                new ResponseListener<GetScoreListResponse>() {
                    @Override
                    public void onStart() {
                        mIsRequesting = true;

                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onCacheResponse(GetScoreListResponse response) {

                    }

                    @Override
                    public void onResponse(GetScoreListResponse response) {

                        if (response != null) {

                            if (mRequestingNumber == PAGE_START_NUMBER) {
                                scoreInfoList.clear();

                                GetScoreListResponse.VendorScoreInfo vendorScoreInfo =
                                        response.getResult().getVendorScoreInfo();

                                if(vendorScoreInfo != null){
                                    totalScoreView.setText(StringUtils.getScoreFormat(vendorScoreInfo.getScore()));

                                    helpUrl = vendorScoreInfo.getScoreHelpUrl();
                                }

                            }

                            if (response.getResult().getScoreInfoList().size() < PER_PAGE_NUMBER) {
                                mIsNoMoreResult = true;
                            }

                            scoreInfoList.addAll(response.getResult().getScoreInfoList());

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            ToastUtil.showLongToast(response.getMessage());

                        } else {
                            ToastUtil.showLongToast("获取数据失败，请您稍后重试");
                        }
                    }

                    @Override
                    public void onFinish() {
                        mSwipeRefreshLayout.setRefreshing(false);

                        listView.setEmptyView(emptyView);

                        mIsRequesting = false;
                    }
                });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private String getThreeMonthAgo() {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -3);

        return TimeUtil.getYearMonthAndDay(calendar.getTimeInMillis());

    }
}
