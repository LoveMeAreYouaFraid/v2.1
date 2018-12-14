package com.nautilus.ywlfair.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.MessageInfo;
import com.nautilus.ywlfair.common.utils.TimeUtil;

public class MessageListAdapter extends BaseAdapter {

	private Context mContext;

	private List<MessageInfo> messageInfoList;
	
	private boolean isShowBox = false;
	
	private List<MessageInfo> checkedList;

	public MessageListAdapter(Context context, List<MessageInfo> commentInfoList) {
		this.mContext = context;

		this.messageInfoList = commentInfoList;

	}
	
	public void update(boolean isShowBox,List<MessageInfo> checkedList){
        this.isShowBox = isShowBox;
        
        this.checkedList = checkedList;

        notifyDataSetChanged();
    }

	@Override
	public int getCount() {
		return messageInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return messageInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHold viewHold = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.msg_home_item, null);

			viewHold = new ViewHold();

			viewHold.imageView = (ImageView) convertView
					.findViewById(R.id.iv_header);

			viewHold.title = (TextView) convertView.findViewById(R.id.tv_title);

			viewHold.date = (TextView) convertView.findViewById(R.id.tv_date);

			viewHold.detail = (TextView) convertView
					.findViewById(R.id.tv_detail);

			viewHold.checkDetail = (TextView) convertView
					.findViewById(R.id.tv_check_detail);

            viewHold.officialTag = (ImageView) convertView.findViewById(R.id.iv_official_tag);

            viewHold.unreadTip = (ImageView) convertView.findViewById(R.id.iv_unread_tip);
			
			viewHold.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

			convertView.setTag(viewHold);

		} else {
			viewHold = (ViewHold) convertView.getTag();
		}

		MessageInfo messageInfo = messageInfoList.get(position);

		viewHold.title.setText(messageInfo.getSender().getNickname());

		viewHold.detail.setText(messageInfo.getContent());

		if (messageInfo.getReadStatus() == 0) {
			viewHold.title.setTextColor(mContext.getResources().getColor(
					R.color.extra_dark_gray));

			viewHold.detail.setTextColor(mContext.getResources().getColor(
					R.color.extra_dark_gray));
			
			viewHold.unreadTip.setVisibility(View.VISIBLE);
		} else {
			viewHold.title.setTextColor(mContext.getResources().getColor(
					R.color.normal_gray));

			viewHold.detail.setTextColor(mContext.getResources().getColor(
					R.color.normal_gray));
			viewHold.unreadTip.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(messageInfo.getSender().getAvatar())) {
            ImageLoadUtils.setRoundHeadView(viewHold.imageView,
                    messageInfo.getSender().getAvatar(), R.drawable.default_avatar, 100);
		}

		viewHold.date.setText(TimeUtil.getYearMonthAndDay(Long
				.valueOf(messageInfo.getSendTime())));
		
		if(isShowBox ){
            viewHold.checkBox.setVisibility(View.VISIBLE);
            
            if(checkedList.contains(messageInfo)){
            	viewHold.checkBox.setChecked(true);
            }else{
            	viewHold.checkBox.setChecked(false);
            }
           
        }else{
        	viewHold.checkBox.setVisibility(View.GONE);
        }

        if(messageInfo.getSender().getUserType() == 2){
            viewHold.officialTag.setVisibility(View.VISIBLE);
        }else{
            viewHold.officialTag.setVisibility(View.GONE);
        }


		return convertView;
	}

	final static class ViewHold {

		ImageView imageView;

		TextView title;

		TextView date;

		TextView detail;

		TextView checkDetail;
		
		ImageView unreadTip;
		
		CheckBox checkBox;

        ImageView officialTag;

	}

}
