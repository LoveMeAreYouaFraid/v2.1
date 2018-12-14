package com.nautilus.ywlfair.module.market.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.OrderListAdapter;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.OfflineOrdersInfo;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/6/22.
 */
public class OrderStallAdapter extends BaseAdapter {

    private Context mContext;

    private List<OfflineOrdersInfo> orderInfoList;

    public OrderStallAdapter(Context context, List<OfflineOrdersInfo> orderInfoList) {
        this.mContext = context;

        this.orderInfoList = orderInfoList;


    }


    @Override
    public int getCount() {
        return orderInfoList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_code_order_form_item,
                    null);

            viewHold = new ViewHold();

            viewHold.imgLineDownHead = (ImageView) convertView.findViewById(R.id.img_line_down_head);

            viewHold.tvLineDownHeadName = (TextView) convertView.findViewById(R.id.tv_line_down_head_name);

            viewHold.tvLineDownStatus = (TextView) convertView.findViewById(R.id.tv_line_down_status);

            viewHold.ivLineDownProductDic = (ImageView) convertView.findViewById(R.id.iv_line_down_product_pic);

            viewHold.tvLineDownProductName = (TextView) convertView.findViewById(R.id.tv_line_down_product_name);

            viewHold.tvLineDownPrice = (TextView) convertView.findViewById(R.id.tv_line_down_price);

            viewHold.tvLineDownPayment = (TextView) convertView.findViewById(R.id.tv_line_down_payment);

            viewHold.tvSeeDetails = (TextView) convertView.findViewById(R.id.tv_see_details);

            viewHold.tvLineDownPriceTwo = (TextView) convertView.findViewById(R.id.tv_line_down_price_two);


            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }


        final OfflineOrdersInfo orderInfo = orderInfoList.get(position);

        viewHold.tvLineDownStatus.setText("交易成功");

        viewHold.tvLineDownProductName.setText(orderInfo.getOrderName());

        viewHold.tvLineDownHeadName.setText(orderInfo.getNickname());

//
//        String LineDownPriceTwo = "x 1";
//
//        viewHold.tvLineDownPriceTwo.setText(Html.fromHtml(LineDownPriceTwo));

        viewHold.tvLineDownPrice.setText("￥ " + StringUtils.getMoneyFormat(orderInfo.getPrice()));

        String LineDownPayment = "共计\t\t1\t件商品\t\t\t\t实收款\t\t\t\t" + "<font color='#f5703f'>" + "￥" + StringUtils.getMoneyFormat(orderInfo.getPrice()) + "</font>";

        viewHold.tvLineDownPayment.setText(Html.fromHtml(LineDownPayment));

        ImageLoader.getInstance().displayImage(orderInfo.getAvatar(), viewHold.imgLineDownHead, ImageLoadUtils.createDisplayOptions(80));

        return convertView;
    }

    final static class ViewHold {
        ImageView imgLineDownHead;

        TextView tvLineDownHeadName;

        TextView tvLineDownStatus;

        ImageView ivLineDownProductDic;

        TextView tvLineDownProductName;

        TextView tvLineDownPrice;

        TextView tvLineDownPayment;

        TextView tvSeeDetails;

        TextView tvLineDownPriceTwo;
    }

}
