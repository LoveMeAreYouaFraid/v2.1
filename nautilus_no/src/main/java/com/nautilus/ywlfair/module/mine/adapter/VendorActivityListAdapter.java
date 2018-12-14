package com.nautilus.ywlfair.module.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class VendorActivityListAdapter extends BaseAdapter {

    private Context mContext;

    private List<HomePagerActivityInfo> vendorActivityInfo;

    private int[] imgList = new int[]{R.drawable.active_status_ing_new, R.drawable.active_status_past_new, R.drawable.active_status_be_new};

    private int actType;

    private int[] ticketsList = new int[]{R.drawable.tickets0, R.drawable.tickets1, R.drawable.tickets2, R.drawable.tickets3, R.drawable.tickets4};
    private int[] boothList = new int[]{R.drawable.booth0, R.drawable.booth1, R.drawable.booth1, R.drawable.booth3, R.drawable.booth4};
    private int[] activityStatusList = new int[]{R.drawable.active_status_ing, R.drawable.active_status_past, R.drawable.active_status_be};

    private List<TextView> timeViewList;

    public VendorActivityListAdapter(Context context, int actType, List<HomePagerActivityInfo> vendorActivityInfo) {
        this.mContext = context;

        this.vendorActivityInfo = vendorActivityInfo;

        this.actType = actType;

        timeViewList = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return vendorActivityInfo.size();
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
        HomePagerActivityInfo info = vendorActivityInfo.get(position);
        ViewHold viewHold;
        if (v == null) {
            v = View.inflate(mContext, R.layout.my_tickets_list_item, null);
            viewHold = new ViewHold();
            viewHold.imageView = (ImageView) v.findViewById(R.id.image_view);
            viewHold.title = (TextView) v.findViewById(R.id.title);
            viewHold.data = (TextView) v.findViewById(R.id.data);
            viewHold.time = (TextView) v.findViewById(R.id.time);
            viewHold.price = (TextView) v.findViewById(R.id.price);
            viewHold.address = (TextView) v.findViewById(R.id.address);
            viewHold.payment = (TextView) v.findViewById(R.id.payment);
            viewHold.surplusTime = (TextView) v.findViewById(R.id.surplus_time);
            viewHold.paymentLayout = v.findViewById(R.id.payment_layout);
            viewHold.Img = (ImageView) v.findViewById(R.id.view_main_img);
            viewHold.actStatus = (ImageView) v.findViewById(R.id.view_main_img_status);
            viewHold.imgStatus = (ImageView) v.findViewById(R.id.img_imgStatus);
            viewHold.imgStatus.setVisibility(View.VISIBLE);
            v.setTag(viewHold);
        } else {
            viewHold = (ViewHold) v.getTag();
        }
        String priceType;

        ImageLoader.getInstance().displayImage(info.getPosterMain(), viewHold.Img, ImageLoadUtils.createNoRoundedOptions());

        viewHold.title.setText(info.getName());
        viewHold.address.setText("活动地点：" + info.getAddress());

        viewHold.data.setText("活动日期：" +
                TimeUtil.getDateFormat(StringUtils.getLongValueFromString(info.getStartTime()),
                        StringUtils.getLongValueFromString(info.getEndTime())));

        viewHold.time.setText("活动时间：" + TimeUtil.getTimeFormat(StringUtils.getLongValueFromString(info.getStartTime()),
                StringUtils.getLongValueFromString(info.getEndTime())));

        if (MyApplication.getInstance().getUserType() == 0) {

            viewHold.imgStatus.setImageResource(ticketsList[Integer.valueOf(info.getTicketStatus())]);
            priceType = "门票价格：";
        } else {
            priceType = "摊位价格：";
            viewHold.imgStatus.setImageResource(boothList[Integer.valueOf(info.getTicketStatus())]);

        }
        viewHold.actStatus.setImageResource(activityStatusList[info.getActivityStatus()]);

        if (info.getMinAmount() == info.getMaxAmount()) {
            viewHold.price.setText(priceType + "￥" + StringUtils.getMoneyFormat(info.getMinAmount()));
        } else {
            viewHold.price.setText(priceType + "￥" + StringUtils.getMoneyFormat(info.getMinAmount())
                    + "~" + StringUtils.getMoneyFormat(info.getMaxAmount()));
        }

        viewHold.paymentLayout.setVisibility(View.GONE);

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
        ImageView actStatus, Img, imgStatus;

    }

}
