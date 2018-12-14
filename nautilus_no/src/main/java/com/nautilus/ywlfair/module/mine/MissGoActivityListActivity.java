package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.entity.request.GetMissActivityListRequest;
import com.nautilus.ywlfair.entity.response.GetMissActivityListResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.adapter.MissGoActivityListAdapter;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MissGoActivityListActivity extends BaseActivity implements View.OnClickListener {
    private MissGoActivityListAdapter missGoActivityListAdapter;
    private Context mContext;
    private LoadMoreListView mListView;
    private List<HomePagerActivityInfo> activityRecordList;
    private int startNum = 0;
    private int PER_PAGE_NUMBER = 8;
    private SwipeRefreshLayout swipeContainer;

    private boolean mIsRequesting = false;

    private boolean isNoMoreResult = false;
    private TextView empty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sigin_list_activity);
        View v = findViewById(R.id.layout_view);
        v.setVisibility(View.GONE);
        activityRecordList = new ArrayList<>();
        mContext = this;
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeContainer.setColorSchemeResources(R.color.lv);

        TextView appTitle = (TextView) findViewById(R.id.tv_title);
        appTitle.setText("想去的市集");
        View back = findViewById(R.id.img_back);
        back.setOnClickListener(this);
        empty = (TextView) findViewById(R.id.empty);
        empty.setText("没有想去的市集哦~");
        missGoActivityListAdapter = new MissGoActivityListAdapter(mContext, activityRecordList);
        mListView = (LoadMoreListView) findViewById(R.id.sign_list_view);
        mListView.setAdapter(missGoActivityListAdapter);
        getData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(mContext, ActiveWebViewActivity.class).putExtra(Constant.KEY.ITEM_ID, activityRecordList.get(position).getActId()));
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mIsRequesting) {

                    isNoMoreResult = false;

                    startNum = 0;

                    getData();
                }

            }
        });

        mListView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (mIsRequesting || isNoMoreResult) {

                    return;
                }
                startNum = activityRecordList.size();
                getData();

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

        GetMissActivityListRequest request = new GetMissActivityListRequest(GetUserInfoUtil.getUserInfo().getUserId() + ""
                , startNum + "", PER_PAGE_NUMBER + "", new ResponseListener<GetMissActivityListResponse>() {
            @Override
            public void onStart() {
                mIsRequesting = true;

                swipeContainer.setRefreshing(true);

                mListView.setEmptyView(null);
            }

            @Override
            public void onCacheResponse(GetMissActivityListResponse response) {

            }

            @Override
            public void onResponse(GetMissActivityListResponse response) {
                if (response == null || response.getResult().getActivityRecordList() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                if (response.getResult().getActivityRecordList().size() < PER_PAGE_NUMBER) {
                    isNoMoreResult = true;

                    if (startNum > 0)
                        mListView.setFooter(true);
                }

                if (startNum == 0) {
                    activityRecordList.clear();

                    mListView.setFooter(false);
                }

                activityRecordList.addAll(response.getResult().getActivityRecordList());

                missGoActivityListAdapter.notifyDataSetChanged();

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
                swipeContainer.setRefreshing(false);

                mListView.setEmptyView(empty);

                mIsRequesting = false;
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }
}
