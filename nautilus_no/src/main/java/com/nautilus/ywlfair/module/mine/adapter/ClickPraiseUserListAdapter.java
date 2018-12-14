package com.nautilus.ywlfair.module.mine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.response.GetPraiseUserListResponse;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ClickPraiseUserListAdapter extends BaseAdapter{

	private Context mContext;

	private List<GetPraiseUserListResponse.PraiseUser> articleInfoList;

	public ClickPraiseUserListAdapter(Context context, List<GetPraiseUserListResponse.PraiseUser> articleInfoList){
		this.mContext = context;
		
		this.articleInfoList = articleInfoList;
	}

	@Override
	public int getCount() {
		return articleInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return articleInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHold viewHold = null;
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.praise_user_list_item, null);
			
			viewHold = new ViewHold();
			
			viewHold.imageView = (ImageView) convertView.findViewById(R.id.iv_header);
			
			viewHold.name = (TextView) convertView.findViewById(R.id.tv_name);
			
			viewHold.date = (TextView) convertView.findViewById(R.id.tv_time);
			
			convertView.setTag(viewHold);
			
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}

        GetPraiseUserListResponse.PraiseUser userInfo = articleInfoList.get(position);
		
		viewHold.name.setText(userInfo.getNickname());
		
		viewHold.date.setText(TimeUtil.getYearMonthAndDayWithHour(userInfo.getAddTime()));
		
		ImageLoader.getInstance().displayImage(userInfo.getAvatar(), viewHold.imageView, ImageLoadUtils.createDisplayOptions(100));
		
		return convertView;
	}
	
	final static class ViewHold {
		
		ImageView imageView;
        
        TextView name;
        
        TextView date;
        
    }

}
