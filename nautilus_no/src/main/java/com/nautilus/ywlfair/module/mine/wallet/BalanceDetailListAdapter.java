package com.nautilus.ywlfair.module.mine.wallet;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.BalanceInfo;
import com.nautilus.ywlfair.entity.bean.PrivilegeInfo;
import com.nautilus.ywlfair.entity.bean.VendorLevel;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class BalanceDetailListAdapter extends BaseAdapter {

    private Context mContext;

    private List<BalanceInfo> list;

    private int type;//0 收支明细  1 线下收款

    public BalanceDetailListAdapter(Context context, List<BalanceInfo> list, int type) {

        this.mContext = context;

        this.list = list;

        this.type = type;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold viewHold;

        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.balance_list_item, null);

            viewHold = new ViewHold();

            viewHold.nameView = (TextView) convertView.findViewById(R.id.tv_name);

            viewHold.amountView = (TextView) convertView.findViewById(R.id.tv_amount);

            viewHold.orderView = (TextView) convertView.findViewById(R.id.tv_order_no);

            viewHold.dateView = (TextView) convertView.findViewById(R.id.tv_date);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        BalanceInfo balanceInfo = list.get(position);

        viewHold.nameView.setText(balanceInfo.getOrderName());

        if(type == 1){
            viewHold.amountView.setText("+" + StringUtils.getMoneyFormat(balanceInfo.getOrderAmount()));

            viewHold.amountView.setTextColor(mContext.getResources().getColor(R.color.money_red));
        }else{
            if (balanceInfo.getAmount() > 0) {
                viewHold.amountView.setText("+" + StringUtils.getMoneyFormat(balanceInfo.getAmount()));

                viewHold.amountView.setTextColor(mContext.getResources().getColor(R.color.money_red));
            } else {
                viewHold.amountView.setText("" + StringUtils.getMoneyFormat(balanceInfo.getAmount()));

                viewHold.amountView.setTextColor(mContext.getResources().getColor(R.color.normal_black));
            }
        }

        viewHold.orderView.setText("订单编号：" + balanceInfo.getOrderId());

        viewHold.dateView.setText(TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(balanceInfo.getAddTime())));

        return convertView;
    }

    class ViewHold {

        TextView nameView;

        TextView amountView;

        TextView orderView;

        TextView dateView;
    }
}
