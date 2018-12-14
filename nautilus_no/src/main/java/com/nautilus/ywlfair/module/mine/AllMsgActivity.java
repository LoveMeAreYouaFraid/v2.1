package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

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
import com.nautilus.ywlfair.entity.request.GetUserNewMessagesRequest;
import com.nautilus.ywlfair.entity.response.GetUserMessagesResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.adapter.MyMsgHomeAdapter;
import com.nautilus.ywlfair.widget.LoadMoreListView;
import com.nautilus.ywlfair.widget.ProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/29.
 */
public class AllMsgActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;

    private MyMsgHomeAdapter myMsgHomeAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<MessageInfo> messageInfoList;

    private LoadMoreListView mListView;

    private String userId;

    private View mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all_msg_activity);

        mContext = this;

        messageInfoList = new ArrayList<>();

        userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        TextView title = (TextView) findViewById(R.id.tv_title);

        title.setText("我的消息");

        View back = findViewById(R.id.img_back);

        back.setOnClickListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        myMsgHomeAdapter = new MyMsgHomeAdapter(mContext, messageInfoList);

        mListView = (LoadMoreListView) findViewById(R.id.lv_message_list);

        mListView.setAdapter(myMsgHomeAdapter);

        mEmptyView = findViewById(R.id.empty);

        getData();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(messageInfoList.get(position).getMsgType())) {
                    startActivity(new Intent(mContext, MsgDetailActivity.class).putExtra(Constant.KEY.TYPE, messageInfoList.get(position).getMsgType()));
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


    private void getData() {//全部
        GetUserNewMessagesRequest request = new GetUserNewMessagesRequest(
                userId, "0", "-1", "0", "8", new ResponseListener<GetUserMessagesResponse>() {
            @Override
            public void onStart() {
                mListView.setEmptyView(null);
                ProgressDialog.getInstance().show(mContext, "获取数据中......");

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
                getFuckingMsgView(response);

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
                mSwipeRefreshLayout.setRefreshing(false);
                mListView.setEmptyView(mEmptyView);
                ProgressDialog.getInstance().cancel();


            }
        }

        );
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private void getFuckingMsgView(GetUserMessagesResponse response) {

        messageInfoList.clear();

        messageInfoList.addAll(response.getResult().getMessageList());

        List<String> typeList = new ArrayList<>();
        typeList.add("1");
        typeList.add("2");
        typeList.add("3");

        for (int i = 0; i < messageInfoList.size(); i++) {
            String type = messageInfoList.get(i).getMsgType();

            if (typeList.contains(type)) {

                typeList.remove(type);
            }

        }

        if (typeList.contains("3")) {
            MessageInfo info = new MessageInfo();
            info.getSender().setNickname("鹦鹉螺市集");
            info.setMsgType("3");

            messageInfoList.add(info);

        }
        if (Integer.valueOf(response.getResult().getNormalMsgNum()) == 0) {

            messageInfoList.get(0).setNormal(1);//无最新消息

        }
        if (Integer.valueOf(response.getResult().getShopMsgNum()) == 0) {
            messageInfoList.get(0).setShop(1);

        }
        if (Integer.valueOf(response.getResult().getVendorMsgNum()) == 0) {
            messageInfoList.get(0).setVendor(1);
        }


        if (GetUserInfoUtil.getUserInfo().getUserType() == 1) {

            if (typeList.contains("1")) {
                MessageInfo info = new MessageInfo();
                info.getSender().setNickname("摊主消息");
                info.setMsgType("1");

                messageInfoList.add(info);
            }
            if (typeList.contains("2")) {
                MessageInfo info = new MessageInfo();
                info.setMsgType("2");
                info.getSender().setNickname("店铺消息");

                messageInfoList.add(info);
            }

        }
        myMsgHomeAdapter.notifyDataSetChanged();
    }
}
