package com.nautilus.ywlfair.module.mine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.MessageInfo;
import com.nautilus.ywlfair.module.goods.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class MyMsgDetailAdapter extends BaseAdapter {
    private Context mContext;
    private mOnClickListener mOnClickListener;
    private List<MessageInfo> messageInfoList;
    private int type;
    private String[] stringMsgType = {"购票完成", "购买摊位完成", "文章被评论", "评论被回复", "购买商品发货通知",
            "产品交易完成", "产品评价", "打款", "商品被购买", "评论被@了", "提交的摊主申请被拒绝",
            "提交的摊主申请已通过", "摊位退订失败", "摊位退订成功", "报名成功",
            "报名失败", "分配摊位号", "活动中奖", "退押金成功", "门票开售提醒", "活动开始通知", "购摊提醒", "摆摊通知", "线下交易收款"
            , "积分奖励", "提现申请", "提现成功", "提现失败", "支付宝账号绑定成功", "支付宝账号绑定失败",
            "积分购摊退订成功", "回答摊主问题", "摊主权益", "官方发放", "官方扣减", "摊主权益", "摊主权益"};

    private List<String> userList = Arrays.asList(stringMsgType);

    //    36:权益申请通过；37:权益被拒绝
    public MyMsgDetailAdapter(Context context, List<MessageInfo> commentInfoList, int type) {
        this.mContext = context;
        this.messageInfoList = commentInfoList;
        this.type = type;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        MessageInfo info = messageInfoList.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.msg_detail_item, null);

            viewHold = new ViewHold();

            viewHold.detail = (TextView) convertView.findViewById(R.id.tv_detail);

            viewHold.date = (TextView) convertView.findViewById(R.id.tv_date);

            viewHold.detailButton = (TextView) convertView
                    .findViewById(R.id.ck_detail);
            viewHold.detailView = convertView.findViewById(R.id.layout_ck_detail);

            viewHold.imgDian = convertView.findViewById(R.id.img_dian);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        switch (info.getSubType()) {

            case 8:
                viewHold.detailView.setVisibility(View.GONE);
                break;
        }
        String htmlStrings = "未知消息类型";
        if (Integer.valueOf(info.getSubType()) <= userList.size()) {
            htmlStrings = "<font color='#2099F1'> " + userList.get(info.getSubType() - 1) + "</font>" + "<br><br>" + "\n" +
                    info.getContent();
        }

        viewHold.detail.setText(Html.fromHtml(htmlStrings));

        viewHold.date.setText(TimeUtil.getYearMonthAndDay(Long.valueOf(info.getSendTime())));

        if (info.getReadStatus() == 0) {
            viewHold.imgDian.setVisibility(View.VISIBLE);
        } else {
            viewHold.imgDian.setVisibility(View.INVISIBLE);
        }
        viewHold.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.mOnClick(position);
            }
        });
        return convertView;
    }

    final static class ViewHold {

        TextView detail;

        TextView date;

        TextView detailButton;

        View detailView;

        View imgDian;

    }

    /* 设置开关状态改变监听器 */
    public void setMOnClickListener(mOnClickListener o) {
        this.mOnClickListener = o;
    }

    /* 内部接口，开关状态改变监听器 */
    public interface mOnClickListener {
        void mOnClick(int position);
    }
}
