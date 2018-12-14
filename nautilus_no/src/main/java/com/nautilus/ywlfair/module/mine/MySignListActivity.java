package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.request.GetSignListRequest;
import com.nautilus.ywlfair.entity.response.GetSignListResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.adapter.MySignListAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MySignListActivity extends BaseActivity implements View.OnClickListener {
    private MySignListAdapter mySignListAdapter;
    private Context mContext;
    private LoadMoreListView mListView;
    private TextView tvNums;
    private int PAGE_START_NUMBER = 0;
    private int PER_PAGE_NUMBER = 4;
    private boolean mIsRequesting = false;
    private int mRequestingNumber = PAGE_START_NUMBER;
    private List<GetSignListResponse.activity> activityInfo;
    private SwipeRefreshLayout swipeContainer;
    private TextView empty;
    private boolean isNoMoreResult = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        activityInfo = new ArrayList<>();
        setContentView(R.layout.sigin_list_activity);
        TextView appTitle = (TextView) findViewById(R.id.tv_title);
        appTitle.setText("活动签到");
        View back = findViewById(R.id.img_back);
        back.setOnClickListener(this);
        empty = (TextView) findViewById(R.id.empty);
        empty.setText("您还没有签到哦~");
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeContainer.setColorSchemeResources(R.color.lv);

        tvNums = (TextView) findViewById(R.id.tv_sign_num);
        mListView = (LoadMoreListView) findViewById(R.id.sign_list_view);
        mySignListAdapter = new MySignListAdapter(mContext, activityInfo);
        mListView.setAdapter(mySignListAdapter);
        getData();

        mListView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (mIsRequesting || isNoMoreResult) {

                    return;
                }
                mRequestingNumber = activityInfo.size();

                getData();

            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mIsRequesting) {

                    mRequestingNumber = 0;

                    getData();
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
        }

    }

    private void getData() {
        GetSignListRequest request = new GetSignListRequest(GetUserInfoUtil.getUserInfo().getUserId() + "", mRequestingNumber + "", PER_PAGE_NUMBER + "",
                new ResponseListener<GetSignListResponse>() {
                    @Override
                    public void onStart() {
                        swipeContainer.setRefreshing(true);

                        mListView.setEmptyView(null);

                    }

                    @Override
                    public void onCacheResponse(GetSignListResponse response) {

                    }

                    @Override
                    public void onResponse(GetSignListResponse response) {
                        if (response == null || response.getResult().getActivityInfo() == null) {
                            ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                            return;
                        }
                        if (response.getResult().getActivityInfo().size() < PER_PAGE_NUMBER) {
                            isNoMoreResult = true;

                            if (mRequestingNumber > 0)
                                mListView.setFooter(true);
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {
                            activityInfo.clear();

                            isNoMoreResult = false;

                            mListView.setFooter(false);
                        }

                        tvNums.setText(response.getResult().getSignNum() + "");

                        activityInfo.addAll(response.getResult().getActivityInfo());

                        mySignListAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            ToastUtil.showShortToast(response.getMessage());

                        } else {
                            ToastUtil.showShortToast("获取数据失败，请您稍后重试");

                        }
                    }

                    @Override
                    public void onFinish() {
                        swipeContainer.setRefreshing(false);

                        mListView.setEmptyView(empty);
                    }
                });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

}
