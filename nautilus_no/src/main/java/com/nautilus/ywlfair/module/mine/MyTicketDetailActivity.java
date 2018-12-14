package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.ListViewUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.TicketInfoList;
import com.nautilus.ywlfair.entity.bean.TicketsOrderInfo;
import com.nautilus.ywlfair.entity.request.GetMyTicketDetailRequest;
import com.nautilus.ywlfair.entity.response.GetMyTicketDetailResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.adapter.MyTicketsDetailAdapter;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowTicketsPagerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class MyTicketDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title, data, time, price, address, payment, surplusTime, unitPrice, totalPrice, tvOrderNumber, createDate, payDate;

    private Context mContext;
    private ImageView mianImg;
    private List<TicketInfoList> ticketInfoList;

    private MyTicketsDetailAdapter adapter;

    private ListView mlistView;

    private TicketsOrderInfo orderInfo;

    private TextView ticket;

    private GetMyTicketDetailResponse responses;

    private String orderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        ticketInfoList = new ArrayList<>();

        setContentView(R.layout.sigin_detail_activity);

        orderId = getIntent().getStringExtra(Constant.KEY.ORDER_ID);

        TextView channelTitle = (TextView) findViewById(R.id.tv_title);
        channelTitle.setText("门票详情");

        View back = findViewById(R.id.img_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);

        mlistView = (ListView) findViewById(R.id.tickets_list);
        title = (TextView) findViewById(R.id.title);
        data = (TextView) findViewById(R.id.data);
        time = (TextView) findViewById(R.id.time);
        price = (TextView) findViewById(R.id.price);
        address = (TextView) findViewById(R.id.address);
        totalPrice = (TextView) findViewById(R.id.total_price);
        payment = (TextView) findViewById(R.id.payment);
        surplusTime = (TextView) findViewById(R.id.surplus_time);
        mianImg = (ImageView) findViewById(R.id.view_main_img);//海报
        mianImg.setOnClickListener(this);
        tvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
        createDate = (TextView) findViewById(R.id.tv_create_date);
        payDate = (TextView) findViewById(R.id.tv_pay_date);
        unitPrice = (TextView) findViewById(R.id.unit_price);
        ticket = (TextView) findViewById(R.id.tickets_code);
        ticket.setOnClickListener(this);
        adapter = new MyTicketsDetailAdapter(mContext, ticketInfoList);
        mlistView.setAdapter(adapter);

        if (TextUtils.isEmpty(orderId)) {
            ToastUtil.showShortToast("门票不存在");
            return;
        }

        getData();

    }

    @Override
    protected void onRestart() {
        getData();
        super.onRestart();
    }

    private void getData() {
        GetMyTicketDetailRequest request = new GetMyTicketDetailRequest(GetUserInfoUtil.getUserInfo().getUserId() + ""
                , orderId, new ResponseListener<GetMyTicketDetailResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "加载中...");
            }

            @Override
            public void onCacheResponse(GetMyTicketDetailResponse response) {

            }

            @Override
            public void onResponse(GetMyTicketDetailResponse response) {
                if (response == null || response.getResult().getTicketInfoList() == null) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                responses = response;
                orderInfo = response.getResult().getOrderInfo();
                title.setText(orderInfo.getName());
                data.setText("活动日期：" + TimeUtil.getYearMonthAndDay(Long.valueOf(orderInfo.getStartdate())) + "~" + TimeUtil.getMonthAndDay(Long.valueOf(orderInfo.getEnddate())));
                time.setText("活动时间：" + TimeUtil.getHourAndMin(Long.valueOf(orderInfo.getStartdate())) + "~" + TimeUtil.getHourAndMin(Long.valueOf(orderInfo.getEnddate())));
                address.setText("活动场馆：" + orderInfo.getVenue());
                price.setText("活动地点：" + orderInfo.getAddress());
                String htmlString = "单价" + "<font color='#ec6432'> " + "￥" + StringUtils.getMoneyFormat(orderInfo.getPrice()) + "</font>" + "<br>" + "x" + orderInfo.getNum();
                String htmlStrings = "总计" + "<font color='#ec6432'> " + "￥" + StringUtils.getMoneyFormat(orderInfo.getTotalPrice()) + "</font>";
                unitPrice.setText(Html.fromHtml(htmlString));
                totalPrice.setText(Html.fromHtml(htmlStrings));
                ImageLoader.getInstance().displayImage(orderInfo.getImgUrl(), mianImg, ImageLoadUtils.createNoRoundedOptions());
                tvOrderNumber.setText("订单编号：" + orderInfo.getOrderId());
                createDate.setText("创建时间：" + TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(orderInfo.getOrderTime())));
                payDate.setText("付款时间：" + TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(orderInfo.getPayTime())));

                ticketInfoList.clear();
                ticketInfoList.addAll(response.getResult().getTicketInfoList());
                adapter.notifyDataSetChanged();
                ListViewUtil.setListViewHeight(mlistView);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.view_main_img:

                Intent ActiveWeb = new Intent(mContext, ActiveWebViewActivity.class);

                ActiveWeb.putExtra(Constant.KEY.ITEM_ID, responses.getResult().getOrderInfo().getActId());

                mContext.startActivity(ActiveWeb);
                break;

            case R.id.tickets_code:

                Intent intent = new Intent(mContext, ShowTicketsPagerActivity.class);
                // Intent intent = new Intent(mContext, LeftRightSlideActivity.class);


                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY.PICINFO_LIST, (Serializable) responses.getResult().getTicketInfoList());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }

    }
}
