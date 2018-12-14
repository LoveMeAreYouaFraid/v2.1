package com.nautilus.ywlfair.module.booth;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.CheckBox;
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
import com.nautilus.ywlfair.component.PayMethodResultListener;
import com.nautilus.ywlfair.component.PayMethodService;
import com.nautilus.ywlfair.entity.bean.MyBoothInfo;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.entity.bean.event.EventBoothHandle;
import com.nautilus.ywlfair.entity.request.GetBoothListRequest;
import com.nautilus.ywlfair.entity.request.PostCreateOrderRequest;
import com.nautilus.ywlfair.entity.response.GetBoothListResponse;
import com.nautilus.ywlfair.entity.response.PostUserOrderResponse;
import com.nautilus.ywlfair.module.booth.adapter.BoothListAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/8.
 */
public class MyBoothFragment extends Fragment implements BoothListAdapter.OnViewsClickListener, PayMethodResultListener {

    private static MyBoothFragment mInstance = null;

    private LoadMoreListView mListView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mEmptyView;

    private BoothListAdapter adapter;

    private static final int PAGE_START_NUMBER = 0;

    private static final int PER_PAGE_NUMBER = 10;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private boolean mIsNoMoreResult = false;

    private boolean mIsRequesting = false;

    private int type;

    private Context mContext;

    private List<MyBoothInfo> myBoothInfoList;

    private int checkPosition;

    private boolean mIsPaying = false;//是否正在启动支付

    public static MyBoothFragment getInstance(Bundle bundle) {

        mInstance = new MyBoothFragment();

        mInstance.setArguments(bundle);

        return mInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_booth_fragment, null);

        type = getArguments().getInt(Constant.KEY.TYPE);

        initViews(view);

        getData();

        return view;
    }

    private void initViews(View view) {

        mListView = (LoadMoreListView) view.findViewById(R.id.lv_my_booth);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        mEmptyView = view.findViewById(R.id.empty);

        myBoothInfoList = new ArrayList<>();

        adapter = new BoothListAdapter(getActivity(), type, myBoothInfoList);

        adapter.setOnViewsClickListener(this);

        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (type == 1) {
                    return;
                }

                checkPosition = position;

                Intent intent = new Intent(mContext, MyBoothDetailActivity.class);

                MyBoothInfo myBoothInfo = myBoothInfoList.get(position);

                intent.putExtra(Constant.KEY.ORDER_ID, myBoothInfo.getOrderID());

                startActivity(intent);
            }
        });

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

                        } else if (myBoothInfoList.size() > 0) {

                            if (!mIsRequesting) {
                                mRequestingNumber = myBoothInfoList.size();

                                getData();
                            }
                        }
                    }
                });
    }

    private void getData() {

        if (mIsRequesting) {
            return;
        }

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetBoothListRequest request = new GetBoothListRequest(userId, type, mRequestingNumber, PER_PAGE_NUMBER,
                new ResponseListener<GetBoothListResponse>() {
                    @Override
                    public void onStart() {
                        mSwipeRefreshLayout.setRefreshing(true);

                        mIsRequesting = true;

                    }

                    @Override
                    public void onCacheResponse(GetBoothListResponse response) {
                        if (response == null || response.getResult().getMyBoothInfoList() == null) {
                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {

                            myBoothInfoList.clear();

                            myBoothInfoList.addAll(response.getResult().getMyBoothInfoList());

                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onResponse(GetBoothListResponse response) {
                        if (response == null || response.getResult().getMyBoothInfoList() == null) {
                            ToastUtil.showShortToast("获取数据失败,请检查网络");
                            return;
                        }

                        if (mRequestingNumber == PAGE_START_NUMBER) {
                            myBoothInfoList.clear();

                            mListView.setFooter(false);
                        }

                        if (response.getResult().getMyBoothInfoList().size() < PER_PAGE_NUMBER) {
                            mIsNoMoreResult = true;

                            if (mRequestingNumber > 0)
                                mListView.setFooter(true);
                        }

                        myBoothInfoList.addAll(response.getResult().getMyBoothInfoList());

                        adapter.notifyDataSetChanged();

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

                        mIsRequesting = false;

                        mListView.setEmptyView(mEmptyView);
                    }
                });

        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPayViewClick(int position) {
        checkPosition = position;

        showPayMethodMenu(myBoothInfoList.get(checkPosition));
    }

    @Override
    public void leftTimeEnd() {

        getData();
    }

    private String channelType;

    /**
     * 显示选择支付方式
     */
    private void showPayMethodMenu(final MyBoothInfo myBoothInfo) {

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

                confirmOrderStatus(myBoothInfo.getOrderID() + "", channelType,
                        myBoothInfo.getPrice(), myBoothInfo.getActId() + "," + myBoothInfo.getBoothType(), myBoothInfo.getBoothId() + "");

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

    private void confirmOrderStatus(String orderNo, String channelType, double price, String skuId, String boothId) {

        if (mIsPaying) {
            return;
        }

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        PostCreateOrderRequest request = new PostCreateOrderRequest(userId, "2", skuId, price+"", 1+"", "", channelType, "", "", orderNo, boothId
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

    private void toPayActivity(OrderInfo orderInfo) {

        mIsPaying = true;

        PayMethodService.getInstance().startPay(mContext, orderInfo, MyBoothFragment.this);

    }

    @Override
    public void payResult(boolean isSuccess, boolean isTimeOut) {
        mIsPaying = false;

        if (isSuccess) {
            myBoothInfoList.remove(checkPosition);

            adapter.notifyDataSetChanged();
        } else {

            mRequestingNumber = PAGE_START_NUMBER;

            mIsNoMoreResult = false;

            getData();

        }
    }

    @Subscribe
    public void onEventMainThread(EventBoothHandle eventBoothHandle) {

        if (eventBoothHandle.getBoothType() == 0) {
            myBoothInfoList.get(checkPosition).setStatus(eventBoothHandle.getType());

            adapter.notifyDataSetChanged();

        } else {

            mRequestingNumber = PAGE_START_NUMBER;

            mIsNoMoreResult = false;

            getData();
        }

    }
}
