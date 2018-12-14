package com.nautilus.ywlfair.module.booth.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.MyBoothInfo;
import com.nautilus.ywlfair.entity.request.PostBindPhoneRequest;
import com.nautilus.ywlfair.widget.TextViewParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoothListAdapter extends BaseAdapter {

    private Context mContext;

    private int type;

    private SimpleDateFormat simpleDateFormat;

    private List<TextView> timeViewList;

    private List<MyBoothInfo> myBoothInfoList;

    private OnViewsClickListener onViewsClickListener;

    public BoothListAdapter(Context context, int type, List<MyBoothInfo> myBoothInfoList) {
        this.mContext = context;

        this.type = type;

        simpleDateFormat = new SimpleDateFormat("mm:ss");

        timeViewList = new ArrayList<>();

        this.myBoothInfoList = myBoothInfoList;
    }

    @Override
    public int getCount() {
        return myBoothInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return myBoothInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        View convertView = View.inflate(mContext, R.layout.my_booth_list_item, null);

        TextView titleView = (TextView) convertView.findViewById(R.id.tv_active_name);

        TextView priceView = (TextView) convertView.findViewById(R.id.tv_booth_price);

        TextView boothType = (TextView) convertView.findViewById(R.id.tv_booth_type);

        ImageView icon = (ImageView) convertView.findViewById(R.id.iv_icon);

        ImageView boothStatus = (ImageView) convertView.findViewById(R.id.iv_booth_status);

        LinearLayout unPayLayout = (LinearLayout) convertView.findViewById(R.id.ll_un_pay);

        TextView payType = (TextView) convertView.findViewById(R.id.tv_pay_type);

        TextView timeTextView = (TextView) convertView.findViewById(R.id.tv_time);

        TextView dateView = (TextView) convertView.findViewById(R.id.tv_active_date);

        TextView timeView = (TextView) convertView.findViewById(R.id.tv_active_time);

        TextView addressView = (TextView) convertView.findViewById(R.id.tv_address);

        TextView boothRound = (TextView) convertView.findViewById(R.id.tv_booth_round);

        View payBtn = convertView.findViewById(R.id.tv_pay_btn);

        MyBoothInfo myBoothInfo = myBoothInfoList.get(position);

        String roundNo = myBoothInfo.getRoundNo();

        if(!TextUtils.isEmpty(roundNo)){
            boothRound.setVisibility(View.VISIBLE);

            boothRound.setText("活动场次：" + roundNo);
        }else{
            boothRound.setVisibility(View.GONE);
        }

        titleView.setText(myBoothInfo.getName());

        boothType.setText("摊位类型：" + myBoothInfo.getBoothType());

        priceView.setText("￥" + StringUtils.getMoneyFormat(myBoothInfo.getPrice()));

        dateView.setText("本场市集日期：" + TimeUtil.getDateFormat(myBoothInfo.getStartdate(), myBoothInfo.getEnddate()));

        timeView.setText("本场摊主进场时间：" + TimeUtil.getTimeFormat(myBoothInfo.getStartdate(), myBoothInfo.getEnddate()));

        addressView.setText("活动地址：" + myBoothInfo.getAddress());

        switch (myBoothInfo.getStatus()){
            case 1:
                boothStatus.setImageResource(R.drawable.to_be);
                break;

            case 2:
                boothStatus.setImageResource(R.drawable.cancel_ing);
                break;

            case 3:
                boothStatus.setImageResource(R.drawable.has_cancel);
                break;

            case 4:
                boothStatus.setImageResource(R.drawable.cancel_failed);
                break;

            case 5:
                boothStatus.setImageResource(R.drawable.has_done);
                break;
        }

        ImageLoader.getInstance().displayImage(myBoothInfo.getImgUrl(), icon, ImageLoadUtils.createNoRoundedOptions());

        if (type == 0) { //已付款
            unPayLayout.setVisibility(View.GONE);

            payType.setText("实付：");

        } else {
            unPayLayout.setVisibility(View.VISIBLE);

            payType.setText("待付：");

            long time = 900000 - System.currentTimeMillis() + myBoothInfo.getOrderTime();

            if( time < 0){

                timeTextView.setText("即将自动取消");

            } else {

                timeViewList.add(timeTextView);

                timeTextView.setTag(myBoothInfo.getOrderTime());

                setTimeText(timeTextView);

                runnable.run();
            }

        }

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1 && onViewsClickListener != null){
                    onViewsClickListener.onPayViewClick(position);
                }
            }
        });

        return convertView;
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            handler.postDelayed(this,1000);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            for (int i = 0; i < timeViewList.size(); i++){

                setTimeText( timeViewList.get(i));

            }
        }
    };

    private void setTimeText(TextView view){

        long longTime = (long) view.getTag();

        long time = 900000 - System.currentTimeMillis() + longTime;

        view.setText(Html.fromHtml(getHtmlString(time)));

        if(time > -999 && time < 1000){

            if(onViewsClickListener != null){

                onViewsClickListener.leftTimeEnd();
            }

        }

    }

    /**
     * 控件被点击的事件监听接口
     */
    public interface OnViewsClickListener {

        void onPayViewClick(int position);

        void leftTimeEnd();

    }

    public void setOnViewsClickListener(OnViewsClickListener onViewsClickListener) {
        this.onViewsClickListener = onViewsClickListener;
    }

    private String getHtmlString(long time){

        Date date = new Date(time);

        String dateString = simpleDateFormat.format(date);

        String[] dates = dateString.split(":");

        String htmlString = "<font color='#EC6432'> "+ dates[0] + "</font>" + " 分 " + "<font color='#EC6432'>" + dates[1] + "</font>" + " 秒";

        return htmlString;
    }
}
