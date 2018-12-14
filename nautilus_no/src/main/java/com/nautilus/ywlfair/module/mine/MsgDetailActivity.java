package com.nautilus.ywlfair.module.mine;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
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
import com.nautilus.ywlfair.entity.bean.MessageInfo;
import com.nautilus.ywlfair.entity.bean.WalletInfo;
import com.nautilus.ywlfair.entity.request.GetUserNewMessagesRequest;
import com.nautilus.ywlfair.entity.request.GetWalletInfoRequest;
import com.nautilus.ywlfair.entity.request.PutMessagesRequest;
import com.nautilus.ywlfair.entity.response.GetUserMessagesResponse;
import com.nautilus.ywlfair.entity.response.GetWalletInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.module.active.CommentListActivity;
import com.nautilus.ywlfair.module.active.LotteryWebView;
import com.nautilus.ywlfair.module.booth.BoothDepositActivity;
import com.nautilus.ywlfair.module.booth.DepositListActivity;
import com.nautilus.ywlfair.module.booth.MyBoothDetailActivity;
import com.nautilus.ywlfair.module.goods.AgainOrderSelectionActivity;
import com.nautilus.ywlfair.module.market.DownLineDetailActivity;
import com.nautilus.ywlfair.module.mine.adapter.MyMsgDetailAdapter;
import com.nautilus.ywlfair.module.mine.level.VendorScoreDetailActivity;
import com.nautilus.ywlfair.module.mine.wallet.BalanceDetailActivity;
import com.nautilus.ywlfair.module.mine.wallet.BindAliPayActivity;
import com.nautilus.ywlfair.module.mine.wallet.BindAliStatusActivity;
import com.nautilus.ywlfair.module.vendor.VendorVerifyActivity;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;
import com.nautilus.ywlfair.module.webview.GoodsBePurchasedWebActivity;
import com.nautilus.ywlfair.module.webview.UseHelpActivity;
import com.nautilus.ywlfair.widget.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MsgDetailActivity extends BaseActivity implements View.OnClickListener, MyMsgDetailAdapter.mOnClickListener {

    private Context mContext;

    private boolean mIsNoMoreResult = false;

    private MyMsgDetailAdapter myMsgDetailAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<MessageInfo> messageInfoList;

    private LoadMoreListView mListView;

    private int PAGE_START_NUMBER = 0;

    private int PER_PAGE_NUMBER = 8;

    private boolean mIsRequesting = false;

    private View mEmptyView;

    private int mRequestingNumber = PAGE_START_NUMBER;

    private int msgType;

    private int positions;

    private int operatingType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all_msg_activity);

        mContext = this;

        msgType = Integer.valueOf(getIntent().getStringExtra(Constant.KEY.TYPE));

        messageInfoList = new ArrayList<>();

        mEmptyView = findViewById(R.id.empty);

        TextView title = (TextView) findViewById(R.id.tv_title);

        View back = findViewById(R.id.img_back);

        back.setOnClickListener(this);

        mListView = (LoadMoreListView) findViewById(R.id.lv_message_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.lv);

        myMsgDetailAdapter = new MyMsgDetailAdapter(mContext, messageInfoList, msgType);

        mListView.setAdapter(myMsgDetailAdapter);

        myMsgDetailAdapter.setMOnClickListener(this);

        getData(0);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mRequestingNumber = 0;

                getData(0);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                positions = position;

                operatingType = 1;

                if (messageInfoList.get(position).getReadStatus() == 0) {//未读设为已读
                    setMessageStatus(messageInfoList.get(position).getMessageId() + "");
                }

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                positions = position;
                showPayDepositAlert();

                return false;
            }
        });

        mListView.setOnLastItemVisibleListener(new LoadMoreListView.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {

                if (mIsRequesting) {
                    return;
                }
                mRequestingNumber = messageInfoList.size();
                getData(1);
            }
        });


        String[] titles = new String[]{"摊主消息", "店铺消息", "普通消息"};

        title.setText(titles[msgType - 1]);

    }

    private void showPayDepositAlert() {

        TextView tv_title, tv_choice_0, tv_choice_1, tv_cancel;
        final Dialog dialog = new Dialog(mContext, R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dlg_choose_picture_source);
        dialog.setCanceledOnTouchOutside(true);
        tv_choice_1 = (TextView) dialog.findViewById(R.id.tv_choice_1);
        tv_choice_1.setVisibility(View.GONE);
        View fenggexian = dialog.findViewById(R.id.fenggexian);
        fenggexian.setVisibility(View.GONE);
        tv_choice_0 = (TextView) dialog.findViewById(R.id.tv_choice_0);
        tv_choice_0.setText("设为已读");
        tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText("管理消息");


        if (messageInfoList.get(positions).getReadStatus() == 1) {
            tv_choice_0.setVisibility(View.GONE);
        } else {
            tv_choice_0.setVisibility(View.VISIBLE);
        }
        tv_choice_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatingType = 1;
                setMessageStatus(messageInfoList.get(positions).getMessageId() + "");
                dialog.cancel();
            }
        });
        tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tv_cancel.setText("删除消息");
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operatingType = 2;
                setMessageStatus(messageInfoList.get(positions).getMessageId() + "");
                dialog.cancel();
            }
        });
        dialog.show();

    }

    private void setMessageStatus(String unReadMessageIds) {//1设为已读 2 删除

        PutMessagesRequest request = new PutMessagesRequest(unReadMessageIds, operatingType,
                new ResponseListener<InterfaceResponse>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCacheResponse(InterfaceResponse response) {

                    }

                    @Override
                    public void onResponse(InterfaceResponse response) {
                        if (response != null) {
                            if (operatingType == 1) {
                                messageInfoList.get(positions).setReadStatus(1);

                            } else {

                                messageInfoList.remove(positions);

                            }
                            myMsgDetailAdapter.notifyDataSetChanged();

                        }

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

                    }
                });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }


    private void getData(final int Type) {
        GetUserNewMessagesRequest request = new GetUserNewMessagesRequest(
                GetUserInfoUtil.getUserInfo().getUserId() + "", msgType + "", "-1", mRequestingNumber + "", PER_PAGE_NUMBER + "", new ResponseListener<GetUserMessagesResponse>() {
            @Override
            public void onStart() {

                mIsNoMoreResult = true;

                mSwipeRefreshLayout.setRefreshing(true);

                mListView.setEmptyView(null);

            }

            @Override
            public void onCacheResponse(GetUserMessagesResponse response) {

            }

            @Override
            public void onResponse(GetUserMessagesResponse response) {
                if (response == null || response.getResult().getMessageList() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                if (response.getResult().getMessageList().size() < PER_PAGE_NUMBER) {

                    mIsRequesting = true;

                    if (mRequestingNumber > 0) {
                        mListView.setFooter(true);
                    }
                }

                if (mRequestingNumber == PAGE_START_NUMBER) {

                    messageInfoList.clear();

                    mListView.setFooter(false);
                }

                if (Type == 0) {

                    messageInfoList.clear();

                }

                messageInfoList.addAll(response.getResult().getMessageList());

                myMsgDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();

                    Toast.makeText(MyApplication.getInstance(), response.getMessage(),
                            Toast.LENGTH_SHORT).show();

                } else {
                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                }
            }

            @Override
            public void onFinish() {

                mSwipeRefreshLayout.setRefreshing(false);

                mListView.setEmptyView(mEmptyView);

            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }

    }

    @Override
    public void mOnClick(int position) {

        String[] extendFields;

        String id = "";

        int type = Integer.valueOf(messageInfoList.get(position).getSubType());

        MessageInfo messageInfo = messageInfoList.get(position);

        if (!TextUtils.isEmpty(messageInfo.getExtendField())) {

            extendFields = messageInfo.getExtendField().split(",");

            if (extendFields.length > 0) {
                id = extendFields[0];
            }

        }


        positions = position;

        if (messageInfo.getReadStatus() == 0) {//未读设为已读

            operatingType = 1;

            setMessageStatus(messageInfo.getMessageId() + "");
        }


        switch (type) {
            case 1:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                operatingType = 1;

                Intent ticketIntent = new Intent(mContext, MyTicketDetailActivity.class);

                ticketIntent.putExtra(Constant.KEY.ORDER_ID, id);

                startActivity(ticketIntent);

                break;
            case 2:
            case 14:
            case 17:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent intentBooth = new Intent(mContext, MyBoothDetailActivity.class);

                intentBooth.putExtra(Constant.KEY.ORDER_ID, id);

                startActivity(intentBooth);

                break;
            case 23:
            case 13:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent intent = new Intent(mContext, MyBoothDetailActivity.class);

                intent.putExtra(Constant.KEY.ORDER_ID, id);

                startActivity(intent);
                break;
            case 10:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent intenst = new Intent(mContext,
                        CommentDetailActivity.class);

                intenst.putExtra(Constant.KEY.COMMENT_ID, id);

                startActivity(intenst);
                break;
            case 3:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent commendListIntents = new Intent(mContext, CommentListActivity.class);

                commendListIntents.putExtra(Constant.KEY.ITEM_ID, id);

                commendListIntents.putExtra(Constant.KEY.ITEM_TYPE, "3");

                startActivity(commendListIntents);

                break;
            case 4:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent commendListInten = new Intent(mContext, CommentDetailActivity.class);

                commendListInten.putExtra(Constant.KEY.COMMENT_ID, id);

                startActivity(commendListInten);
                break;
            case 7:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent commendListIntent = new Intent(mContext, CommentListActivity.class);

                commendListIntent.putExtra(Constant.KEY.ITEM_ID, id);

                commendListIntent.putExtra(Constant.KEY.ITEM_TYPE, "5");

                startActivity(commendListIntent);
                break;


            case 16:
            case 15:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent activityInfo = new Intent(mContext, ActiveWebViewActivity.class);

                activityInfo.putExtra(Constant.KEY.ITEM_ID, id);

                activityInfo.putExtra(Constant.KEY.IS_MSG, "1");

                mContext.startActivity(activityInfo);

                break;

            case 18:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                String uri = MyApplication.getInstance().getActDrawLogUrl() + "actId=" + id + "&userId=" +
                        GetUserInfoUtil.getUserInfo().getUserId() + "";

                LotteryWebView.startLotteryWebView(mContext, "2", uri, id);
                break;
            case 12:
                //    startActivity(new Intent(mContext, DepositListActivity.class));
                startActivity(new Intent(mContext, BoothDepositActivity.class));
                break;

            case 11:
                if (GetUserInfoUtil.getUserInfo().getUserType() == 1) {
                    startActivity(new Intent(mContext, BoothDepositActivity.class));
                } else {
                    startActivity(new Intent(mContext, VendorVerifyActivity.class));
                }


                break;
            case 22:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent ActiveWebs = new Intent(mContext, ActiveWebViewActivity.class);

                ActiveWebs.putExtra(Constant.KEY.ITEM_ID, id);

                ActiveWebs.putExtra(Constant.KEY.IS_MSG, "1");

                mContext.startActivity(ActiveWebs);
                break;
            case 21:
            case 20:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent ActiveWeb = new Intent(mContext, ActiveWebViewActivity.class);

                ActiveWeb.putExtra(Constant.KEY.ITEM_ID, id);

                mContext.startActivity(ActiveWeb);
                break;
            case 19:
                startActivity(new Intent(mContext, DepositListActivity.class));
                break;

            case 5:
            case 6:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent detailIntent = new Intent(mContext, AgainOrderSelectionActivity.class);

                detailIntent.putExtra(Constant.KEY.ORDER_ID, id);

                startActivity(detailIntent);
                break;


            case 9:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                GoodsBePurchasedWebActivity.startWebActivity(mContext, id);
                break;
            case 24:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                startActivity(new Intent(mContext, DownLineDetailActivity.class)
                        .putExtra(Constant.KEY.ORDER_ID, id));
                break;
            case 25:
            case 31:
            case 33:
            case 34:
            case 35:
                mContext.startActivity(new Intent(mContext, VendorScoreDetailActivity.class));

                break;
            case 26:
            case 27:
            case 28:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                startActivity(new Intent(mContext, BalanceDetailActivity.class).putExtra(Constant.KEY.ITEM_ID, id));
                break;
            case 29:
            case 30:

                getData();
                break;
            case 32:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;

                }
                Intent intent1 = new Intent(mContext, ActiveWebViewActivity.class);

                intent1.putExtra(Constant.KEY.IS_MSG, "1");

                intent1.putExtra(Constant.KEY.ITEM_ID, id);

                mContext.startActivity(intent1);
                break;
            case 36:
            case 37:
                if (TextUtils.isEmpty(id) || id.equals("")) {
                    ToastUtil.showLongToast("获取数据失败");
                    return;
                }
                startActivity(new Intent(mContext, UseHelpActivity.class).putExtra(Constant.KEY.URL, id).putExtra(Constant.KEY.TYPE, 1));
                break;
        }
    }


    private void getData() {
        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        GetWalletInfoRequest request = new GetWalletInfoRequest(userId, new ResponseListener<GetWalletInfoResponse>() {
            @Override
            public void onStart() {
                mIsRequesting = true;

                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onCacheResponse(GetWalletInfoResponse response) {

            }

            @Override
            public void onResponse(GetWalletInfoResponse response) {
                if (response != null && response.getResult().getWallet() != null) {
                    WalletInfo walletInfo = response.getResult().getWallet();

                    if (!TextUtils.isEmpty(walletInfo.getAliPayBindStatus() + "")) {
                        switch (walletInfo.getAliPayBindStatus()) {
                            case 2:
                            case 1:
                                startActivity(new Intent(mContext, BindAliStatusActivity.class));
                                break;
                            case 0:
                            case 3:
                                startActivity(new Intent(mContext, BindAliPayActivity.class));
                                break;
                        }
                    }

                } else {
                    ToastUtil.showLongToast("请您稍后重试");
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
//                ProgressDialog.getInstance().cancel();
                mIsRequesting = false;

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }
}