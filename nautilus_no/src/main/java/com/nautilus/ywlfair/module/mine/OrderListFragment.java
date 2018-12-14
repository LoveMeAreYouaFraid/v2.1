package com.nautilus.ywlfair.module.mine;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.OrderListAdapter;
import com.nautilus.ywlfair.adapter.OrderListAdapter.OnViewsClickListener;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.PayMethodResultListener;
import com.nautilus.ywlfair.component.PayMethodService;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.bean.event.EventOrderChange;
import com.nautilus.ywlfair.entity.request.GetUserOrdersRequest;
import com.nautilus.ywlfair.entity.request.GetValidOrderRequest;
import com.nautilus.ywlfair.entity.request.PutOrderStatusRequest;
import com.nautilus.ywlfair.entity.response.GetUserOrdersResponse;
import com.nautilus.ywlfair.entity.response.GetValidOrderResponse;
import com.nautilus.ywlfair.module.active.EditPagerActivity;
import com.nautilus.ywlfair.module.goods.AgainOrderSelectionActivity;
import com.nautilus.ywlfair.module.market.PaySuccessActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class OrderListFragment extends Fragment implements OnViewsClickListener, PayMethodResultListener {

    private LoadMoreListView mListView;

    private OrderListAdapter mAdapter;

    private List<OrderInfo> orderList;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mEmptyView;

    private int orderStatus;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 10;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private boolean mIsPaying = false;//是否正在启动支付

    private int mRequestingNumber = PAGE_START_NUMBER;

    private String orderType = "0";

    public static OrderListFragment getInstance(Bundle bundle) {
        OrderListFragment instance = new OrderListFragment();

        instance.setArguments(bundle);

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle arguments = getArguments();

        if (arguments != null) {
            orderStatus = arguments.getInt(Constant.KEY.TYPE);
        }
        if (orderStatus == 100) {
            orderType = "0";
        } else {
            orderType = "3";

        }
        orderList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.order_list_fragment, null);

        mListView = (LoadMoreListView) view.findViewById(R.id.lv_order_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view
                .findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mEmptyView = view.findViewById(R.id.empty);

        mAdapter = new OrderListAdapter(getActivity(), orderList);

        mAdapter.setOnViewsClickListener(this);

        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mRequestingNumber = PAGE_START_NUMBER;

                        mIsNoMoreResult = false;

                        getData();
                    }
                });

        mListView
                .setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mIsNoMoreResult) {

                        } else if (orderList.size() > 0) {

                            if (!mIsRequesting) {
                                mRequestingNumber = orderList.size();
                                getData();
                            }
                        }
                    }
                });

        getData();

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (orderList.get(position).getOrderType().equals("5")) {
                    Intent intent = new Intent(getActivity(), PaySuccessActivity.class);

                    intent.putExtra(Constant.KEY.ORDER, orderList.get(position));

                    intent.putExtra(Constant.KEY.TYPE, "0");

                    getActivity().startActivity(intent);
                } else {
                    Intent detailIntent = new Intent(getActivity(), AgainOrderSelectionActivity.class);

                    detailIntent.putExtra(Constant.KEY.ORDER_ID, orderList.get(position).getOrderId());

                    startActivity(detailIntent);
                }


            }
        });

        return view;
    }

    private void getData() {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        GetUserOrdersRequest request = new GetUserOrdersRequest(userId, orderStatus + "", mRequestingNumber,
                PER_PAGE_NUMBER, orderType, new ResponseListener<GetUserOrdersResponse>() {
            @Override
            public void onStart() {
                mIsRequesting = true;

                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onCacheResponse(GetUserOrdersResponse response) {
                if (response == null || response.getResult().getOrderInfoList() == null) {
                    return;
                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    orderList.clear();

                    orderList.addAll(response.getResult().getOrderInfoList());

                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onResponse(GetUserOrdersResponse response) {
                if (response == null || response.getResult().getOrderInfoList() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                if (mRequestingNumber == PAGE_START_NUMBER) {
                    orderList.clear();
                }

                if (response.getResult().getOrderInfoList().size() < PER_PAGE_NUMBER) {
                    mIsNoMoreResult = true;
                }

                orderList.addAll(response.getResult().getOrderInfoList());

                mAdapter.notifyDataSetChanged();

                mListView.setEmptyView(mEmptyView);
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
                mIsRequesting = false;
                mListView.setEmptyView(mEmptyView);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }


    private OrderInfo deleteOrderInfo;

    private int changeType;

    private String alertTitle, alertMessage, toastMessage;

    private int changePosition;

    @Override
    public void onCancelViewClick(int position, int type) {
        this.changeType = type;

        this.changePosition = position;

        deleteOrderInfo = orderList.get(changePosition);

        if (type == -1) {
            alertTitle = "取消订单";

            alertMessage = "您是否确认要取消该订单?";

            toastMessage = "订单取消成功";
        } else if (type == 3) {
            alertTitle = "确认收货";

            alertMessage = "您是否确认收货?\n确认后,摊主将收到您支付的货款。";

            toastMessage = "确认收货成功";
        }

        showConfirmAlert();

    }


    @Override
    public void onPayViewClick(int position) {
        changePosition = position;

        showGenderPopMenu(orderList.get(position));
    }

    @Override
    public void onCommentClick(int position) {
        this.changePosition = position;

        toastMessage = "商品评价成功";

        Intent intent = new Intent(getActivity(),
                EditPagerActivity.class);

        intent.putExtra(Constant.KEY.ITEM_ID,
                orderList.get(changePosition).getGoodsId() + "");

        intent.putExtra(Constant.KEY.TYPE, "4");

        intent.putExtra(Constant.KEY.ORDER_STATUS, orderStatus);

        startActivity(intent);
    }

    @Override
    public void onApplyClick() {
        TextView tv_title, tv_choice_0, tv_choice_1, tv_cancel;
        final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_choose_picture_source);
        tv_choice_1 = (TextView) dialog.findViewById(R.id.tv_choice_1);
        tv_choice_1.setVisibility(View.GONE);
        View fenggexian = dialog.findViewById(R.id.fenggexian);
        fenggexian.setVisibility(View.GONE);
        tv_choice_0 = (TextView) dialog.findViewById(R.id.tv_choice_0);
        tv_choice_0.setText("请微信联系：鹦鹉螺服务大使");
        tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText("售后");
        tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tv_cancel.setText("确定");
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    @Override
    public void lookDetails(int position) {


    }


    private void changeOrderStatus() {

        PutOrderStatusRequest request = new PutOrderStatusRequest(deleteOrderInfo.getOrderId(), deleteOrderInfo.getOrderType(), changeType + "",
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {
                        ProgressDialog.getInstance().show(getActivity(), "操作中...");
                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {
                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {

                            Toast.makeText(MyApplication.getInstance(), toastMessage,
                                    Toast.LENGTH_SHORT).show();

                            if (orderStatus != 100 || changeType == -1) {

                                orderList.remove(deleteOrderInfo);
                            } else {
                                orderList.get(changePosition).setOrderStatus(changeType);
                            }

                            mAdapter.notifyDataSetChanged();
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


    private void showConfirmAlert() {
        AlertDialog.Builder builder = new Builder(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("确认", new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeOrderStatus();
            }
        });

        builder.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    private String channelType;

    /**
     * 显示选择支付方式
     */
    private void showGenderPopMenu(final OrderInfo orderInfo) {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final CheckBox CkMain, CkLady;
        final TextView determine, cancel;
        TextView TvMain, TvLady, tvtitle;
        View main, lady;
        dialog.setContentView(R.layout.dlg_sex);
        TvLady = (TextView) dialog.findViewById(R.id.tv_lady);
        TvLady.setText("微信");
        tvtitle = (TextView) dialog.findViewById(R.id.tv_title);
        tvtitle.setText("请选择平台");
        TvMain = (TextView) dialog.findViewById(R.id.tv_main);
        TvMain.setText("支付宝");
        CkMain = (CheckBox) dialog.findViewById(R.id.ck_man);
        CkLady = (CheckBox) dialog.findViewById(R.id.ck_lady);
        main = dialog.findViewById(R.id.main);
        lady = dialog.findViewById(R.id.lady);
        determine = (TextView) dialog.findViewById(R.id.tv_determine);
        cancel = (TextView) dialog.findViewById(R.id.tv_cancel);


        final String[] channel = new String[]{"ALI", "WX"};


        channelType = channel[0];

        CkMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CkLady.setChecked(false);
            }
        });
        CkLady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CkMain.setChecked(false);
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CkMain.setChecked(true);
                CkLady.setChecked(false);
            }
        });
        lady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CkMain.setChecked(false);
                CkLady.setChecked(true);
            }
        });
        determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CkMain.isChecked() == true) {
                    channelType = "ALI";


                } else {
                    channelType = "WX";
                }

                confirmOrderStatus(orderInfo.getOrderId() + "", channelType);

                dialog.cancel();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private void confirmOrderStatus(String orderId, String channel) {

        if (mIsPaying) {
            return;
        }

        GetValidOrderRequest request = new GetValidOrderRequest(orderId, channel, new ResponseListener<GetValidOrderResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(getActivity(), "校验订单...");
            }

            @Override
            public void onCacheResponse(GetValidOrderResponse response) {

            }

            @Override
            public void onResponse(GetValidOrderResponse response) {
                if (response != null && response.getResult() != null && response.getResult().getOrderInfo() != null) {

                    orderList.remove(changePosition);

                    orderList.add(changePosition, response.getResult().getOrderInfo());

                    toPayActivity(orderList.get(changePosition));

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


    private void toPayActivity(OrderInfo orderInfo) {

        mIsPaying = true;

        PayMethodService.getInstance().startPay(getActivity(), orderInfo, OrderListFragment.this);
    }


    @Subscribe
    public void onEventMainThread(EventOrderChange eventOrderChange) {

        if (orderStatus != eventOrderChange.getOrderStatus()) {
            return;
        }

        if (eventOrderChange.getStatus() == 4) {
            deleteOrderInfo = orderList.get(changePosition);

            changeType = eventOrderChange.getStatus();

            changeOrderStatus();
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void payResult(boolean isSuccess, boolean isTimeOut) {

        mIsPaying = false;

        if (isSuccess) {

            orderList.get(changePosition).setOrderStatus(1);

            mAdapter.notifyDataSetChanged();
        } else {
            mRequestingNumber = 0;

            getData();
        }
    }

}
