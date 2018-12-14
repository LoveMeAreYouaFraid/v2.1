package com.nautilus.ywlfair.module.booth.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.DebitRecordsInfo;

import java.util.List;

/**
 * Created by lipeng on 2016/3/30.
 */
public class MoneyBackAdapter extends BaseAdapter {

    private Context mContext;
    private List<DebitRecordsInfo> list;


    public MoneyBackAdapter(Context context, List<DebitRecordsInfo> list) {
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
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHold viewHold = null;
        if (v == null) {
            viewHold = new ViewHold();

            v = View.inflate(mContext, R.layout.deposit_record_details_item, null);

            viewHold.tvPrice = (TextView) v.findViewById(R.id.tv_price);

            viewHold.tvData = (TextView) v.findViewById(R.id.tv_data);

            viewHold.tvDetail = (TextView) v.findViewById(R.id.tv_detail);

            viewHold.line = v.findViewById(R.id.line);

            viewHold.dian = (ImageView) v.findViewById(R.id.dian);

            v.setTag(viewHold);
        } else {
            viewHold = (ViewHold) v.getTag();
        }

        viewHold.tvPrice.setText(StringUtils.getMoneyFormat(list.get(position).getPrice()));

        viewHold.tvData.setText(TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(list.get(position).getLogTime())));

        viewHold.tvDetail.setText(list.get(position).getDesc());

        if (position + 1 == list.size()) {

            if (list.size() > 1) {
                viewHold.line.setVisibility(View.GONE);
            }

        }
        if (list.size() == 1) {
            viewHold.line.setVisibility(View.GONE);
        }
        if (position == list.size() - 1) {
//            viewHold.dian.setImageDrawable(mContext.getDrawable(R.drawable.list_up));
            viewHold.dian.setImageDrawable(mContext.getResources().getDrawable(R.drawable.list_up));
        } else {
//            viewHold.dian.setImageDrawable(mContext.getDrawable(R.drawable.list_down));

            viewHold.dian.setImageDrawable(mContext.getResources().getDrawable(R.drawable.list_down));
        }
        return v;
    }


    final static class ViewHold {
        private TextView
                tvPrice,
                tvData,
                tvDetail;
        private View line;
        private ImageView dian;

    }
}