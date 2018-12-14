package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.PayMethodResultListener;
import com.nautilus.ywlfair.component.PayMethodService;
import com.nautilus.ywlfair.dialog.PayMethodMenuDialog;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.request.GetMyTicketsListRequest;
import com.nautilus.ywlfair.entity.request.PostCreateOrderRequest;
import com.nautilus.ywlfair.entity.response.MyTicketsListResponse;
import com.nautilus.ywlfair.entity.response.PostUserOrderResponse;
import com.nautilus.ywlfair.module.mine.adapter.MyTicketsListAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/14.
 */
public class MyTicketsListNoPayFragment extends Fragment implements
        MyTicketsListAdapter.OnTicketClickListener, PayMethodMenuDialog.CheckPayMethodListener, PayMethodResultListener {

    private static MyTicketsListNoPayFragment mInstance;

    private LoadMoreListView listExperience;

    private Context mContext;

    private Thread thread;

    private myHandler myHandler;

    private MyTicketsListAdapter myTicketsListAdapter;

    private SwipeRefreshLayout swipeContainer;

    private List<MyTicketsListResponse.info> list;

    private int PAGE_START_NUMBER = 0;

    private int PER_PAGE_NUMBER = 8;

    private boolean mIsRequesting = false;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private boolean isNoMoreResult = false;

    private View mEmptyView;

    private int ordAndNow;

    private int checkPosition;

    private String[] channel = new String[]{"ALI", "WX"};

    private boolean mIsPaying = false;//是否正在启动支付

    public static MyTicketsListNoPayFragment getInstance(Bundle bundle) {

        mInstance = new MyTicketsListNoPayFragment();

        mInstance.setArguments(bundle);

        return mInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        list = new ArrayList<>();

        Bundle arguments = getArguments();

        if (arguments != null) {
            ordAndNow = arguments.getInt(Constant.KEY.TYPE);
        }

        myHandler = new myHandler();

        getData();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.experience_liset, null);

        listExperience = (LoadMoreListView) view.findViewById(R.id.list_experience);

        myTicketsListAdapter = new MyTicketsListAdapter(mContext, ordAndNow, list);

        listExperience.setAdapter(myTicketsListAdapter);

        mEmptyView = view.findViewById(R.id.empty);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        swipeContainer.setColorSchemeResources(R.color.lv);

        myTicketsListAdapter.setOnTicketClickListener(this);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mIsRequesting) {
                    return;
                }

                mRequestingNumber = 0;

                getData();
            }
        });
        listExperience.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {

                if (mIsRequesting || isNoMoreResult) {

                    return;
                }
                mRequestingNumber = list.size();

                getData();
            }
        });

        listExperience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(list.get(position).getOrderId())) {
                    if (ordAndNow == 0) {
                        startActivity(new Intent(mContext, MyTicketDetailActivity.class).putExtra(Constant.KEY.ORDER_ID, list.get(position).getOrderId()));
                    }
                } else {
                    ToastUtil.showLongToast("数据获取失败...请检查网络设置");
                }

            }
        });
        return view;
    }


    private void getData() {
        GetMyTicketsListRequest request = new GetMyTicketsListRequest(GetUserInfoUtil.getUserInfo().getUserId() + ""
                , ordAndNow + "", mRequestingNumber + "", "8", new ResponseListener<MyTicketsListResponse>() {
            @Override
            public void onStart() {
                swipeContainer.setRefreshing(true);

                listExperience.setEmptyView(null);

                mIsRequesting = true;
            }

            @Override
            public void onCacheResponse(MyTicketsListResponse response) {

            }

            @Override
            public void onResponse(MyTicketsListResponse response) {
                if (response == null || response.getResult().getList() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }

                if (response.getResult().getList().size() < PER_PAGE_NUMBER) {

                    isNoMoreResult = true;

                    if (mRequestingNumber > 0)
                        listExperience.setFooter(true);
                }

                if (ordAndNow == 1) {
                    if (list.size() == 0) {
                        start();
                    }
                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    isNoMoreResult = false;

                    list.clear();

                    listExperience.setFooter(false);
                }


                list.addAll(response.getResult().getList());
                myTicketsListAdapter.notifyDataSetChanged();
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

                listExperience.setEmptyView(mEmptyView);

                mIsRequesting = false;
            }
        });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);

    }

    @Override
    public void onPayViewClick(int position) {

        checkPosition = position;

        PayMethodMenuDialog.getInstance(this).show(getActivity().getFragmentManager(), null);

    }

    @Override
    public void payResult(boolean isSuccess, boolean isTimeOut) {
        mIsPaying = false;

        if (isSuccess) {
            list.remove(checkPosition);

            myTicketsListAdapter.notifyDataSetChanged();
        } else {
            mRequestingNumber = 0;

            getData();
        }
    }

    @Override
    public void onCheckMethod(int position) {

        MyTicketsListResponse.info ticketInfo = list.get(checkPosition);

        LogUtil.e("orderId", ticketInfo.getOrderId() + " ");

        confirmOrderStatus(ticketInfo.getOrderId() + "", channel[position],
                Double.valueOf(ticketInfo.getPrice()), ticketInfo.getActId() + "," + ticketInfo.getTicketType(), Integer.valueOf(ticketInfo.getNum()));
    }

    private void toPayActivity(OrderInfo orderInfo) {

        mIsPaying = true;

        PayMethodService.getInstance().startPay(mContext, orderInfo, MyTicketsListNoPayFragment.this);

    }

    private void confirmOrderStatus(String orderNo, String channelType, double price, String skuId, int num) {

        if (mIsPaying) {
            return;
        }

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        PostCreateOrderRequest request = new PostCreateOrderRequest(userId, "1", skuId, price+"", num+"", "", channelType, "", "", orderNo, ""
                , "", "", "", new ResponseListener<PostUserOrderResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(getActivity(), "校验订单...");
            }

            @Override
            public void onCacheResponse(PostUserOrderResponse response) {

            }

            @Override
            public void onResponse(PostUserOrderResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getOrderInfo() != null) {


                    toPayActivity(response.getResult().getOrderInfo());

                } else {
                    ToastUtil.showLongToast("更新订单信息失败");
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
                ProgressDialog.getInstance().cancel();
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private class myHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    myTicketsListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void start() {
        thread = new Thread() {
            @Override
            public void run() {
                while ((true)) {
                    try {

                        if (list == null) {
                            return;
                        }
                        sleep(1000);

                        myHandler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        thread.start();
    }
}
