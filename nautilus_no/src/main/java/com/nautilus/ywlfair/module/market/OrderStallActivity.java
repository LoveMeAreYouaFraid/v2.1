package com.nautilus.ywlfair.module.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.nautilus.ywlfair.entity.bean.OfflineOrdersInfo;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.request.GetStallOrderInfoListRequest;
import com.nautilus.ywlfair.entity.response.GetStallOrderInfoListResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.market.adapter.OrderStallAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 摊主订单
 * Created by Administrator on 2016/6/22.
 */
public class OrderStallActivity extends BaseActivity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_btn)
    TextView tvRightBtn;
    @BindView(R.id.lv_order_list)
    LoadMoreListView lvOrderList;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.empty)
    TextView empty;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private OrderStallAdapter orderStallAdapter;

    private Context mContext;

    private List<OfflineOrdersInfo> orderInfoList;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.order_stall_activity);
        ButterKnife.bind(this);

        orderInfoList = new ArrayList<>();

        tvTitle.setText("我的订单");

        orderStallAdapter = new OrderStallAdapter(mContext, orderInfoList);

        lvOrderList.setAdapter(orderStallAdapter);

        getData();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestingNumber = PAGE_START_NUMBER;

                mIsNoMoreResult = false;

                getData();
            }
        });
        lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.isEmpty(orderInfoList.get(position).getOrderId())) {
                    ToastUtil.showShortToast("信息不全，请稍后再试");
                    return;
                }

                startActivity(new Intent(mContext, DownLineDetailActivity.class)
                        .putExtra(Constant.KEY.ORDER_ID, orderInfoList.get(position).getOrderId()));

            }
        });
        lvOrderList.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (mIsNoMoreResult) {

                } else if (orderInfoList.size() > 0) {

                    if (!mIsRequesting) {
                        mRequestingNumber = orderInfoList.size();
                        getData();
                    }
                }
            }
        });

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }

    private void getData() {

        GetStallOrderInfoListRequest request = new GetStallOrderInfoListRequest(GetUserInfoUtil.getUserInfo().getUserId() + "",
                "1", mRequestingNumber + "", PER_PAGE_NUMBER + "", new ResponseListener<GetStallOrderInfoListResponse>() {
            @Override
            public void onStart() {

                ProgressDialog.getInstance().show(mContext, "数据获取中.....");

                mIsRequesting = true;

                swipeContainer.setRefreshing(true);
            }

            @Override
            public void onCacheResponse(GetStallOrderInfoListResponse response) {

            }

            @Override
            public void onResponse(GetStallOrderInfoListResponse response) {
                if (response == null || response.getResult().getOrderInfoList() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                if (mRequestingNumber == PAGE_START_NUMBER) {
                    orderInfoList.clear();
                }

                if (response.getResult().getOrderInfoList().size() < PER_PAGE_NUMBER) {
                    mIsNoMoreResult = true;
                }

                orderInfoList.addAll(response.getResult().getOrderInfoList());

                orderStallAdapter.notifyDataSetChanged();
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
                mIsRequesting = false;

                lvOrderList.setEmptyView(empty);

                swipeContainer.setRefreshing(false);

                ProgressDialog.getInstance().cancel();

            }
        });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

}
