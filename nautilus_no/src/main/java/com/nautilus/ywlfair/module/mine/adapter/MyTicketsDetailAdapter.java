package com.nautilus.ywlfair.module.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.entity.bean.TicketInfoList;

import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class MyTicketsDetailAdapter extends BaseAdapter {


    private Context mContext;

    private List<TicketInfoList> ticketInfoList;

    private String[] imgList = new String[]{"未使用", "已使用"};

    public MyTicketsDetailAdapter(Context context, List<TicketInfoList> ticketInfoList) {
        this.mContext = context;

        this.ticketInfoList = ticketInfoList;

    }

    @Override
    public int getCount() {
        return ticketInfoList.size();
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

        if (v == null) {
            v = View.inflate(mContext, R.layout.my_tickets_detail_list_item, null);
            viewHold = new ViewHold();
            viewHold.ticketsType = (TextView) v.findViewById(R.id.tickets_type);
            viewHold.tv = (TextView) v.findViewById(R.id.tickets_code);
            v.setTag(viewHold);
        } else {
            viewHold = (ViewHold) v.getTag();
        }


        viewHold.ticketsType.setText(imgList[Integer.valueOf(ticketInfoList.get(position).getStatus())]);

//        if (Integer.valueOf(ticketInfoList.get(position).getStatus()) == 1) {
//            viewHold.ticketsType.setBackground(mContext.getResources().getDrawable(R.drawable.y_use_img));
//        } else {
//            viewHold.ticketsType.setBackground(mContext.getResources().getDrawable(R.drawable.n_use_img));
//        }
        viewHold.tv.setText("验证码" + (position + 1) + "" + "\t\t\t\t" + ticketInfoList.get(position).getTicketCode() + "\t\t\t\t");

        return v;
    }

    final static class ViewHold {

        TextView tv, ticketsType; //标题

    }
}