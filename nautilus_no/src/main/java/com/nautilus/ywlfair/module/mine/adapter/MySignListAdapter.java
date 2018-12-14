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
import com.nautilus.ywlfair.entity.bean.SignInfo;
import com.nautilus.ywlfair.entity.response.GetSignListResponse;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MySignListAdapter extends BaseAdapter {

    private Context mContext;

    private List<GetSignListResponse.activity> activityInfo;

    public MySignListAdapter(Context context, List<GetSignListResponse.activity> activityInfo) {

        this.mContext = context;

        this.activityInfo = activityInfo;

    }

    @Override
    public int getCount() {
        return activityInfo.size();
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
        ViewHold viewHold;
        GetSignListResponse.activity activity = activityInfo.get(position);

        if (v == null) {
            v = View.inflate(mContext, R.layout.my_tickets_list_item, null);
            viewHold = new ViewHold();
            viewHold.imageView = (ImageView) v.findViewById(R.id.view_main_img);
            viewHold.title = (TextView) v.findViewById(R.id.title);
            viewHold.data = (TextView) v.findViewById(R.id.data);
            viewHold.time = (TextView) v.findViewById(R.id.time);
            viewHold.price = (TextView) v.findViewById(R.id.price);
            viewHold.address = (TextView) v.findViewById(R.id.address);
            viewHold.payment = (TextView) v.findViewById(R.id.payment);
            viewHold.surplusTime = (TextView) v.findViewById(R.id.surplus_time);
            viewHold.paymentLayout = v.findViewById(R.id.payment_layout);
            v.setTag(viewHold);
        } else {
            viewHold = (ViewHold) v.getTag();
        }
        viewHold.title.setText(activity.getName());

        ImageLoader.getInstance().displayImage(activity.getImgUrl(), viewHold.imageView, ImageLoadUtils.createNoRoundedOptions());

        viewHold.data.setText("活动地点：" + activity.getAddress());

        viewHold.time.setText("签到时间：" + TimeUtil.getYearMonthAndDay(Long.valueOf(activity.getAddTime())) + "\t\t" + TimeUtil.getHourAndMin(Long.valueOf(activity.getAddTime())));

        return v;
    }

    final static class ViewHold {

        ImageView imageView;

        TextView title, //标题
                data,
                time,//时间
                price,//票价
                address,
                payment,
                surplusTime;//地点
        View paymentLayout;

    }
}
