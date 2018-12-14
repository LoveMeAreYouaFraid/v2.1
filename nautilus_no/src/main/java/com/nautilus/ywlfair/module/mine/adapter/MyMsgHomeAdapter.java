package com.nautilus.ywlfair.module.mine.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.MessageInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyMsgHomeAdapter extends BaseAdapter {
    private Context mContext;

    private List<MessageInfo> messageInfoList;

    private int[] normalList = {R.drawable.system, R.drawable.system_n};
    private int[] vendorList = {R.drawable.stall, R.drawable.stall_n};
    private int[] shopList = {R.drawable.shop, R.drawable.shop_n};

    private String[] msgTypeStringList = new String[]{"摊主消息", "摊主消息", "店铺消息", "鹦鹉螺市集"};

    private String[] stringMsgType = {"购票完成", "购买摊位完成", "文章被评论", "评论被回复", "购买商品发货通知",
            "产品交易完成", "产品评价", "打款", "商品被购买", "评论被@了", "提交的摊主申请被拒绝",
            "提交的摊主申请已通过", "摊位退订失败", "摊位退订成功", "报名成功",
            "报名失败", "分配摊位号", "活动中奖", "退押金成功", "门票开售提醒", "活动开始通知", "购摊提醒", "摆摊通知", "线下交易收款"
            , "积分奖励", "提现申请", "提现成功", "提现失败", "支付宝账号绑定成功", "支付宝账号绑定失败",
            "积分购摊退订成功", "回答摊主问题", "摊主权益", "官方发放", "官方扣减", "摊主权益", "摊主权益"};

    private List<String> userList = Arrays.asList(stringMsgType);

    public MyMsgHomeAdapter(Context context, List<MessageInfo> commentInfoList) {
        this.mContext = context;

        this.messageInfoList = commentInfoList;


    }

    @Override
    public int getCount() {
        return messageInfoList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;

        MessageInfo info = messageInfoList.get(position);

        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.msg_home_item, null);

            viewHold = new ViewHold();

            viewHold.imageView = (ImageView) convertView.findViewById(R.id.iv_header);

            viewHold.title = (TextView) convertView.findViewById(R.id.tv_title);

            viewHold.date = (TextView) convertView.findViewById(R.id.tv_date);

            viewHold.detail = (TextView) convertView.findViewById(R.id.tv_detail);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        getImageView(position, viewHold);

        viewHold.title.setText(msgTypeStringList[Integer.valueOf(info.getMsgType())]);

        if (info.getSubType() >= 1) {
            String htmlStrings = "<font color='#2099F1'> " + "[" + userList.get(info.getSubType() - 1) + "]" + "</font>" + info.getContent();

            viewHold.detail.setText(Html.fromHtml(htmlStrings));
        } else {
            viewHold.detail.setText(Html.fromHtml(info.getContent()));
        }


        if (Long.valueOf(info.getSendTime()) != 0) {
            viewHold.date.setText(TimeUtil.getYearMonthAndDay(Long.valueOf(info.getSendTime())));
        } else {
            viewHold.date.setText(TimeUtil.getYearMonthAndDay(System.currentTimeMillis()));

        }


        return convertView;
    }

    final static class ViewHold {

        ImageView imageView;

        TextView title;

        TextView date;

        TextView detail;


    }

    private void getImageView(int position, ViewHold viewHold) {
        int normal = messageInfoList.get(0).getNormal();
        int shop = messageInfoList.get(0).getShop();
        int vendor = messageInfoList.get(0).getVendor();

        switch (Integer.valueOf(messageInfoList.get(position).getMsgType())) {
            case 1:
                viewHold.imageView.setImageResource(vendorList[vendor]);
                break;
            case 2:
                viewHold.imageView.setImageResource(shopList[shop]);
                break;
            case 3:
                viewHold.imageView.setImageResource(normalList[normal]);
                break;
        }
    }
}
