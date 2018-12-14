package com.nautilus.ywlfair.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.widget.MaskedImage;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.entity.bean.ReplyComment;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentDetailListAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<CommentInfo> commentInfoList;
	
	private DisplayImageOptions options = ImageLoadUtils.createDisplayOptions(80);

	public CommentDetailListAdapter(Context context, List<CommentInfo> commentInfoList) {
		this.mContext = context;
		
		this.commentInfoList = commentInfoList;
	}

	@Override
	public int getCount() {
		return commentInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return commentInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold viewHold = null;
		
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.comment_detail_item, null);
			
			viewHold = new ViewHold();
			
			viewHold.headImage = (ImageView) convertView.findViewById(R.id.iv_header);
			
			viewHold.userName = (TextView) convertView.findViewById(R.id.tv_name);
			
			viewHold.date = (TextView) convertView.findViewById(R.id.tv_time);
			
			viewHold.content = (TextView) convertView.findViewById(R.id.tv_content);
			
			viewHold.address = (TextView) convertView.findViewById(R.id.tv_address);
			
			viewHold.floorNum = (TextView) convertView.findViewById(R.id.tv_floor_num);
			
            viewHold.officialTag = (ImageView) convertView.findViewById(R.id.iv_official_tag);
			
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}

        CommentInfo commentInfo = commentInfoList.get(position);
		
		ImageLoader.getInstance().displayImage(commentInfo.getAuthor().getAvatar(), viewHold.headImage, options);
		
		viewHold.userName.setText(commentInfo.getAuthor().getNickName());
		
		viewHold.date.setText(TimeUtil.castLastDate(Long.valueOf(commentInfo.getUpdateTime())));

        viewHold.content.setText(commentInfo.getContent()+"");
		
		if(TextUtils.isEmpty(commentInfo.getLocation())){
			viewHold.address.setVisibility(View.GONE);
		}else{
			viewHold.address.setText(commentInfo.getLocation());
		}
		
		viewHold.floorNum.setText(commentInfo.getFloor() +"楼");

        setContent(commentInfo, viewHold.content);

        if(commentInfo.getAuthor().getUserType() == 2){
            viewHold.officialTag.setVisibility(View.VISIBLE);
        }else{
            viewHold.officialTag.setVisibility(View.GONE);
        }

		return convertView;
	}

    private void setContent(CommentInfo commentInfo,TextView view){
        if(commentInfo.getReplyCommentList() != null &&
                commentInfo.getReplyCommentList().size() > 0){
            String name = commentInfo.getReplyCommentList().get(0).getAuthor().getNickName();

            view.setText("回复@" + name + "：" + commentInfo.getContent()+"");
        }
    }

	final static class ViewHold {
        ImageView headImage;

		TextView userName;

		TextView date;

		TextView content;

		TextView address;

		TextView floorNum;
		
        ImageView officialTag;

	}
}
