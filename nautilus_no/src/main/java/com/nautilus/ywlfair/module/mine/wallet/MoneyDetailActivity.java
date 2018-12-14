package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.ShowDatePickerDialog;
import com.nautilus.ywlfair.entity.bean.BalanceInfo;
import com.nautilus.ywlfair.entity.request.GetBalanceDetailListRequest;
import com.nautilus.ywlfair.entity.response.GetBalanceDetailListResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.market.DownLineDetailActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/16.
 */
public class MoneyDetailActivity extends BaseActivity implements View.OnClickListener, ShowDatePickerDialog.OnDateSelectedListener {

    private Context mContext;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 20;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private View mEmptyView;

    private BalanceDetailListAdapter adapter;

    private String startTime;

    private String endTime;

    private List<BalanceInfo> list;

    private int type;//1 线下交易收款

    @BindView(R.id.ll_chose_date)
    View choseDate;

    @BindView(R.id.tv_date)
    TextView dateView;

    @BindView(R.id.list)
    LoadMoreListView listView;

    @BindView(R.id.tv_title)
    TextView titleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.money_detail_activity);

        mContext = this;

        ButterKnife.bind(this);

        type = getIntent().getIntExtra(Constant.KEY.TYPE, 0);

        init();

        getData(startTime, endTime, type);

    }

    private void init() {
        findViewById(R.id.tv_top_bar_back).setOnClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mIsRequesting){
                    return;
                }

                mRequestingNumber = PAGE_START_NUMBER;

                mIsNoMoreResult = false;

                getData(startTime, endTime, type);
            }
        });

        listView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (!mIsNoMoreResult && list.size() > 0 && !mIsRequesting) {

                    mRequestingNumber = list.size();

                    getData(startTime, endTime, type);
                }
            }
        });

        mEmptyView = findViewById(R.id.empty);

        choseDate.setOnClickListener(this);

        startTime = getThreeMonthAgo();

        endTime = TimeUtil.getYearMonthAndDay(System.currentTimeMillis());

        list = new ArrayList<>();

        adapter = new BalanceDetailListAdapter(mContext, list, type);

        listView.setAdapter(adapter);

        dateView.setText(startTime + "-" + endTime);

        if(type == 1){
            titleView.setText("收款记录");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();

                if(type == 1){
                    intent.setClass(mContext, DownLineDetailActivity.class);

                    intent.putExtra(Constant.KEY.ORDER_ID, list.get(position).getOrderId());
                }else{
                    intent.setClass(mContext, BalanceDetailActivity.class);

                    intent.putExtra(Constant.KEY.ITEM_ID, list.get(position).getBalanceDetailId());

                }

                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_top_bar_back:
                finish();
                break;

            case R.id.ll_chose_date:
                ShowDatePickerDialog.getInstance().initMenuDialog(mContext, startTime, endTime);
                break;
        }
    }

    private void getData(String startTime, String endTime, int queryType){
        int userId = GetUserInfoUtil.getUserInfo().getUserId();

        GetBalanceDetailListRequest request = new GetBalanceDetailListRequest(userId, startTime, endTime,
                queryType,mRequestingNumber,PER_PAGE_NUMBER,
                new ResponseListener<GetBalanceDetailListResponse>() {
                    @Override
                    public void onStart() {
                        mIsRequesting = true;

                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onCacheResponse(GetBalanceDetailListResponse response) {

                    }

                    @Override
                    public void onResponse(GetBalanceDetailListResponse response) {
                        if(response != null){

                            if (mRequestingNumber == PAGE_START_NUMBER) {
                                list.clear();
                            }

                            if (response.getResult().getBalanceDetailList().size() < PER_PAGE_NUMBER) {
                                mIsNoMoreResult = true;
                            }

                            list.addAll(response.getResult().getBalanceDetailList());

                            adapter.notifyDataSetChanged();
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

                        listView.setEmptyView(mEmptyView);

                        mIsRequesting = false;

                    }
                });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private String getThreeMonthAgo(){

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -3);

        String date = TimeUtil.getYearAndMonth(calendar.getTimeInMillis());

        return date + "-01";

    }

    private String getDateWithDay(String dateString){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = simpleDateFormat.parse(dateString);

            LogUtil.e("date", date.getTime() + "   111");

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

            LogUtil.e("date", calendar.getTimeInMillis() + "   222");

            return TimeUtil.getYearMonthAndDay(calendar.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onSelected(String startTime, String endTime) {

        this.startTime = startTime;

        this.endTime = getDateWithDay(endTime);

        dateView.setText(startTime + "-" + this.endTime);

        getData(startTime, endTime, 0);
    }


}
