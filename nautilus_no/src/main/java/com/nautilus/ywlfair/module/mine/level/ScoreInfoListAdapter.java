package com.nautilus.ywlfair.module.mine.level;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.BalanceInfo;
import com.nautilus.ywlfair.entity.bean.ScoreInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ScoreInfoListAdapter extends BaseAdapter {

    private Context mContext;

    private List<ScoreInfo> list;

    public ScoreInfoListAdapter(Context context, List<ScoreInfo> list) {

        this.mContext = context;

        this.list = list;
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

            viewHold.orderAmountView = (TextView) convertView.findViewById(R.id.tv_order_amount);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        ScoreInfo scoreInfo = list.get(position);

        viewHold.nameView.setText(scoreInfo.getOrderName());

        if(scoreInfo.getScore() > 0){
            viewHold.amountView.setText("+" + StringUtils.getScoreFormat(scoreInfo.getScore()));
        }else {
            viewHold.amountView.setText(StringUtils.getScoreFormat(scoreInfo.getScore()));
        }

        viewHold.orderView.setText("订单编号：" + scoreInfo.getOrderId());

        viewHold.dateView.setText(TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(scoreInfo.getAddTime())));

        if(scoreInfo.getOrderType() == 1 || scoreInfo.getOrderType() == 4){
            viewHold.orderAmountView.setVisibility(View.VISIBLE);

            viewHold.orderAmountView.setText("订单金额：" + StringUtils.getMoneyFormat(scoreInfo.getOrderPrice()));
        }else{
            viewHold.orderAmountView.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHold {

        TextView nameView;

        TextView amountView;

        TextView orderView;

        TextView dateView;

        TextView orderAmountView;
    }
}
