package com.nautilus.ywlfair.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyDiyListAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private List<ArticleInfo> articleInfoList;
	
	private DisplayImageOptions options = ImageLoadUtils.createNoRoundedOptions();
	
	public MyDiyListAdapter(Context context,List<ArticleInfo> articleInfoList){
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
			convertView = View.inflate(mContext, R.layout.my_diy_item, null);
			
			viewHold = new ViewHold();
			
			viewHold.imageView = (AutoAdjustHeightImageView) convertView.findViewById(R.id.iv_picture);
			
			viewHold.title = (TextView) convertView.findViewById(R.id.tv_title);
			
			viewHold.date = (TextView) convertView.findViewById(R.id.tv_date);
			
			viewHold.praiseNum = (TextView) convertView.findViewById(R.id.tv_priase_num);
			
			viewHold.commentNum = (TextView) convertView.findViewById(R.id.tv_comment_num);
			
			convertView.setTag(viewHold);
			
		}else{
			viewHold = (ViewHold) convertView.getTag();
		}
		
		ArticleInfo articleInfo = articleInfoList.get(position);
		
		viewHold.title.setText(articleInfo.getTitle());
		
		viewHold.praiseNum.setText("赞   " + articleInfo.getLikeNum());
		
		viewHold.commentNum.setText("评论   " + articleInfo.getCommentNum());
		
		ImageLoader.getInstance().displayImage(articleInfo.getImgUrl(), viewHold.imageView, options);
		
		viewHold.date.setText(TimeUtil.getYearMonthAndDay(Long.valueOf(articleInfo.getDateTime())));
		
		return convertView;
	}
	
	final static class ViewHold {
		
		AutoAdjustHeightImageView imageView;
        
        TextView title;
        
        TextView date;
        
        TextView praiseNum;
        
        TextView commentNum;
        
    }

}
