package com.nautilus.ywlfair.module.booth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.DepositInfo;
import com.nautilus.ywlfair.entity.bean.event.EventDeposit;
import com.nautilus.ywlfair.entity.request.GetVendorDepositsRequest;
import com.nautilus.ywlfair.entity.response.GetVendorDepositsResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.booth.adapter.DepositListAdapter;
import com.nautilus.ywlfair.module.vendor.ReturnDepositActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class DepositListActivity extends BaseActivity implements View.OnClickListener, DepositListAdapter.OnViewsClickListener {
    private ListView boothDepositDetails;

    private Context mContext;

    private DepositListAdapter adapter;

    private List<DepositInfo> depositInfoList;

    private View nullViewLayout;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.booth_deposit_details_list_activity);

        mContext = this;

        EventBus.getDefault().register(this);

        initViews();

        getData();

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getData();
                    }
                });
        boothDepositDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!depositInfoList.isEmpty()) {
                    startActivity(new Intent(mContext, DepositRecordDetailsActivity.class).putExtra(Constant.REQUEST.KEY.ITEM_ID,
                            depositInfoList.get(position).getId() + ""));
                }
            }
        });
    }

    private void initViews() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("摊位押金明细");

        View back = findViewById(R.id.img_back);
        nullViewLayout = findViewById(R.id.null_view_layout);
        back.setOnClickListener(this);

        boothDepositDetails = (ListView) findViewById(R.id.booth_deposit_details_listview);

        depositInfoList = new ArrayList<>();

        adapter = new DepositListAdapter(mContext, depositInfoList);

        boothDepositDetails.setAdapter(adapter);

        adapter.setOnViewsClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);
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
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetVendorDepositsRequest request = new GetVendorDepositsRequest(userId, new ResponseListener<GetVendorDepositsResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(GetVendorDepositsResponse response) {

            }

            @Override
            public void onResponse(GetVendorDepositsResponse response) {
                if (response != null && response.getResult().getDepositInfoList() != null) {
                    if (response.getResult().getDepositInfoList().size() == 0) {
                        nullViewLayout.setVisibility(View.VISIBLE);
                        boothDepositDetails.setVisibility(View.GONE);
                        return;
                    }
                    depositInfoList.clear();

                    depositInfoList.addAll(response.getResult().getDepositInfoList());

                    adapter.notifyDataSetChanged();

                    Log.e("123", response.getResult().getDepositInfoList() + "");

                } else {
                    Toast.makeText(MyApplication.getInstance(), "获取数据失败，请您稍后重试", Toast.LENGTH_SHORT).show();
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
                mSwipeRefreshLayout.setRefreshing(false);
                ProgressDialog.getInstance().cancel();
            }
        });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    private int changePosition;

    @Override
    public void mViewClick(int status, int position) {

        changePosition = position;

        if (status == 0) {
            Intent intent = new Intent(mContext, DepositOrderConfirmActivity.class);

            intent.putExtra(Constant.KEY.DEPOSIT, depositInfoList.get(position));

            startActivity(intent);
        } else if (status == 1) {
            Intent intent = new Intent(mContext, ReturnDepositActivity.class);

            intent.putExtra(Constant.KEY.DEPOSIT, depositInfoList.get(position));

            startActivity(intent);
        }

    }

    @Subscribe
    public void onEventMainThread(EventDeposit eventDeposit) {

        depositInfoList.get(changePosition).setDepositStatus(eventDeposit.getType());

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
