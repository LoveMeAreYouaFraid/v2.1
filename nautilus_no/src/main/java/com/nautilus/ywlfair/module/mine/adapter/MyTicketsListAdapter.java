package com.nautilus.ywlfair.module.mine.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.internal.bind.DateTypeAdapter;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.response.MyTicketsListResponse;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class MyTicketsListAdapter extends BaseAdapter {

    private Context mContext;

    private List<MyTicketsListResponse.info> list;

    private int type;

    private SimpleDateFormat simpleDateFormat;

    private OnTicketClickListener onViewsClickListener;

    private List<TextView> timeViewList;

    private int[] imgList = new int[]{R.drawable.active_status_ing_new, R.drawable.active_status_past_new, R.drawable.active_status_be_new};


    public MyTicketsListAdapter(Context context, int type, List<MyTicketsListResponse.info> list) {
        this.mContext = context;

        this.list = list;

        this.type = type;

        timeViewList = new ArrayList<>();

        simpleDateFormat = new SimpleDateFormat("mm:ss");

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {

        ViewHold viewHold = null;

        if (v == null) {
            v = View.inflate(mContext, R.layout.my_tickets_list_item, null);

            viewHold = new ViewHold();

            viewHold.leftTimeView = (TextView) v.findViewById(R.id.surplus_time);

            viewHold.title = (TextView) v.findViewById(R.id.title);

            viewHold.data = (TextView) v.findViewById(R.id.data);

            viewHold.time = (TextView) v.findViewById(R.id.time);

            viewHold.price = (TextView) v.findViewById(R.id.price);

            viewHold.address = (TextView) v.findViewById(R.id.address);

            viewHold.payment = (TextView) v.findViewById(R.id.payment);

            viewHold.paymentLayout = v.findViewById(R.id.payment_layout);

            viewHold.imageView = (AutoAdjustHeightImageView) v.findViewById(R.id.view_main_img);

            viewHold.statusImage = (ImageView) v.findViewById(R.id.view_main_img_status);

            viewHold.itemView = v.findViewById(R.id.item_view);

            v.setTag(viewHold);

        } else {
            viewHold = (ViewHold) v.getTag();
        }

        MyTicketsListResponse.info info = list.get(position);

        if (type == 1 && !TextUtils.isEmpty(info.getOrderTime())) {

            viewHold.paymentLayout.setVisibility(View.VISIBLE);

            long leftTime = (Long.valueOf(info.getOrderTime()) + 900000) - System.currentTimeMillis();

            if (leftTime < 0) {

                viewHold.itemView.setVisibility(View.GONE);

            }

            viewHold.leftTimeView.setText(Html.fromHtml(getHtmlString(Long.valueOf(leftTime))));

        } else {
            viewHold.paymentLayout.setVisibility(View.GONE);
        }

        viewHold.statusImage.setImageDrawable(mContext.getResources().getDrawable(imgList[Integer.valueOf(info.getActivityStatus())]));

        //type 1=已付款 2=未付款
        viewHold.data.setText("门票类型：" + info.getTicketTypeName() + "\n活动日期：" + TimeUtil.getYearMonthAndDay(Long.valueOf(info.getStartdate())) + "~" + TimeUtil.getMonthAndDay(Long.valueOf(info.getEnddate())));

        viewHold.time.setText("活动时间：" + TimeUtil.getHourAndMin(Long.valueOf(info.getStartdate())) + "~" + TimeUtil.getHourAndMin(Long.valueOf(info.getEnddate())));

        viewHold.address.setText("活动地点：" + info.getAddress());

        String htmlString = "共计" + "<font color='#ec6432'> " + info.getNum() + "</font>" + "张\t\t\t总价：" + "<font color='#EC6432'>" + "￥" + info.getPrice() + "" + "</font>";

        viewHold.price.setText(Html.fromHtml(htmlString));

        ImageLoader.getInstance().displayImage(info.getImgUrl(), viewHold.imageView, ImageLoadUtils.createNoRoundedOptions());

        viewHold.title.setText(info.getName());

        viewHold.payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewsClickListener.onPayViewClick(position);

            }
        });

        return v;
    }

    private class ViewHold {
        private TextView leftTimeView;

        private TextView title;

        private TextView data;

        private TextView time;

        private TextView price;

        private TextView address;

        private TextView payment;

        private View paymentLayout;

        private AutoAdjustHeightImageView imageView;

        private ImageView statusImage;

        private View itemView;
    }


    private String getHtmlString(long time) {


        Date date = new Date(time);

        String dateString = simpleDateFormat.format(date);

        String[] dates = dateString.split(":");

        String htmlString = "" + "<font color='#EC6432'> " + dates[0] + "</font>" + " 分 " + "<font color='#EC6432'>" + dates[1] + "</font>" + " 秒";

        return htmlString;
    }

    public void setOnTicketClickListener(OnTicketClickListener onViewsClickListener) {
        this.onViewsClickListener = onViewsClickListener;
    }

    /**
     * 控件被点击的事件监听接口
     */
    public interface OnTicketClickListener {

        void onPayViewClick(int position);

    }
}
