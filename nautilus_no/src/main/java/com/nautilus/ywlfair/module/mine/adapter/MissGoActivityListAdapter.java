package com.nautilus.ywlfair.module.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MissGoActivityListAdapter extends BaseAdapter {
    private Context mContext;
    private List<HomePagerActivityInfo> activityRecordList;
    private int[] ticketsList = new int[]{R.drawable.tickets0, R.drawable.tickets1, R.drawable.tickets2, R.drawable.tickets3, R.drawable.tickets4};

    public MissGoActivityListAdapter(Context context, List<HomePagerActivityInfo> activityRecordList) {
        this.mContext = context;
        this.activityRecordList = activityRecordList;

    }

    @Override
    public int getCount() {
        return activityRecordList.size();
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
    public View getView(int position, View v, ViewGroup parent) {
        HomePagerActivityInfo info = activityRecordList.get(position);
        ViewHold viewHold;

        if (v == null) {
            v = View.inflate(mContext, R.layout.my_tickets_list_item, null);
            viewHold = new ViewHold();
            viewHold.title = (TextView) v.findViewById(R.id.title);
            viewHold.data = (TextView) v.findViewById(R.id.data);
            viewHold.time = (TextView) v.findViewById(R.id.time);
            viewHold.price = (TextView) v.findViewById(R.id.price);
            viewHold.address = (TextView) v.findViewById(R.id.address);
            viewHold.payment = (TextView) v.findViewById(R.id.payment);
            viewHold.surplusTime = (TextView) v.findViewById(R.id.surplus_time);
            viewHold.status = (ImageView) v.findViewById(R.id.view_main_img_status);//活动状态
            viewHold.mianImg = (ImageView) v.findViewById(R.id.view_main_img);//海报
            viewHold.imgStatus = (ImageView) v.findViewById(R.id.img_imgStatus);
            viewHold.imgStatus.setVisibility(View.VISIBLE);

            v.setTag(viewHold);
        } else {
            viewHold = (ViewHold) v.getTag();
        }

        viewHold.title.setText(info.getName());
        viewHold.imgStatus.setImageResource(ticketsList[Integer.valueOf(info.getTicketStatus())]);
        ImageLoader.getInstance().displayImage(info.getPosterMain(), viewHold.mianImg, ImageLoadUtils.createNoRoundedOptions());

        viewHold.address.setText("活动地点：" + info.getAddress());
        viewHold.data.setText("活动日期：" + TimeUtil.getYearMonthAndDay(Long.valueOf(info.getStartTime())) + "~" + TimeUtil.getMonthAndDay(Long.valueOf(info.getEndTime())));
        viewHold.time.setText("活动时间：" + TimeUtil.getHourAndMin(Long.valueOf(info.getStartTime())) + "~" + TimeUtil.getHourAndMin(Long.valueOf(info.getEndTime())));
        viewHold.price.setText("门票价格：￥" + info.getMinAmount() + "~ ￥" + info.getMaxAmount());
        viewHold.status.setVisibility(View.VISIBLE);


        return v;
    }

    final static class ViewHold {

        TextView title, //标题
                data,
                time,//时间
                price,//票价
                address,
                payment,
                surplusTime;//地点
        ImageView status, mianImg, imgStatus;


    }
}
