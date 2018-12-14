package com.nautilus.ywlfair.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.widget.MaskedImage;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.RecommendInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RecommendListAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private List<RecommendInfo> recommendInfoList;
	
	private DisplayImageOptions options = ImageLoadUtils.createNoRoundedOptions();

	public RecommendListAdapter(Context context,List<RecommendInfo> commentInfoList){
		this.mContext = context;
		
		this.recommendInfoList = commentInfoList;
		
	}

	@Override
	public int getCount() {
		return recommendInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return recommendInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHold viewHold = null;
		if(convertView == null){
			convertView = View.inflate(mContext, R.layout.recommend_list_item, null);
			
			viewHold = new ViewHold();
			
			viewHold.imageView = (MaskedImage) convertView.findViewById(R.id.iv_recommend_icon);
			
			viewHold.title = (TextView) convertView.findViewById(R.id.tv_recommend_title);
			
			viewHold.detail = (TextView) convertView.findViewById(R.id.tv_recommend_detail);
			
			convertView.setTag(viewHold);
			
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}
		
		RecommendInfo recommendInfo = recommendInfoList.get(position);
		
		viewHold.title.setText(recommendInfo.getTitle());
		
		viewHold.detail.setText(recommendInfo.getDesc());
		
		ImageLoader.getInstance().displayImage(recommendInfo.getImgUrl(), viewHold.imageView, options);
		
		return convertView;
	}
	
	final static class ViewHold {

        MaskedImage imageView;
        
        TextView title;
        
        TextView detail;
        
    }

}
