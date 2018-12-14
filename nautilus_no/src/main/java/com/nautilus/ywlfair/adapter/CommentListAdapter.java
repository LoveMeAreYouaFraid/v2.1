package com.nautilus.ywlfair.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.widget.ShowImagesPagerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CommentListAdapter extends BaseAdapter {

	private Context mContext;

	private List<CommentInfo> commentInfoList;

	private DisplayImageOptions options = ImageLoadUtils
			.createNoRoundedOptions();

	public CommentListAdapter(Context context, List<CommentInfo> commentInfoList) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHold viewHold = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.comment_list_item,
					null);

			viewHold = new ViewHold();

			viewHold.headImage = (ImageView) convertView
					.findViewById(R.id.iv_header);

			viewHold.userName = (TextView) convertView
					.findViewById(R.id.tv_name);

			viewHold.date = (TextView) convertView.findViewById(R.id.tv_time);

			viewHold.content = (TextView) convertView
					.findViewById(R.id.tv_content);

			viewHold.address = (TextView) convertView
					.findViewById(R.id.tv_address);

			viewHold.praiseNum = (TextView) convertView
					.findViewById(R.id.tv_praise);

            viewHold.officialTag = (ImageView) convertView.findViewById(R.id.iv_official_tag);

			viewHold.commentNum = (TextView) convertView
					.findViewById(R.id.tv_comment_num);

			viewHold.delete = (TextView) convertView
					.findViewById(R.id.tv_delete);

			viewHold.imageGallery = (LinearLayout) convertView
					.findViewById(R.id.ll_gallery);

            viewHold.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar1);

            viewHold.commentTag = (TextView) convertView.findViewById(R.id.tv_comment_tag);

            convertView.setTag(viewHold);

		} else {
			viewHold = (ViewHold) convertView.getTag();
		}

		CommentInfo commentInfo = commentInfoList.get(position);

		viewHold.userName.setText(commentInfo.getAuthor().getNickName());

		viewHold.date.setText(TimeUtil.castLastDate(Long.valueOf(commentInfo
                .getAddTime())));

		viewHold.content.setText(commentInfo.getContent());

		viewHold.praiseNum.setText("赞 " + commentInfo.getLikeNum());
		viewHold.praiseNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onClickPraiseListener != null) {
					onClickPraiseListener.onClickPraise(position, v);

				}
			}
		});

		if(commentInfo.getDelFlag() == 1){
			viewHold.delete.setVisibility(View.INVISIBLE);

			viewHold.praiseNum.setVisibility(View.INVISIBLE);

			viewHold.commentNum.setVisibility(View.INVISIBLE);

			viewHold.address.setVisibility(View.INVISIBLE);

		}else{
            viewHold.delete.setVisibility(View.VISIBLE);

            viewHold.praiseNum.setVisibility(View.VISIBLE);

            viewHold.commentNum.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(commentInfo.getLocation())) {
                viewHold.address.setVisibility(View.INVISIBLE);
            } else {
                viewHold.address.setVisibility(View.VISIBLE);
                viewHold.address.setText(commentInfo.getLocation());
            }

            if (MyApplication.getInstance().isLogin()
                    && commentInfo.getAuthor().getUserId() == GetUserInfoUtil.getUserInfo().getUserId()) {

                viewHold.delete.setVisibility(View.VISIBLE);

                viewHold.delete.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (onClickPraiseListener != null) {
                            onClickPraiseListener.onDeleteComment(position);
                        }
                    }
                });

            } else {
                viewHold.delete.setVisibility(View.INVISIBLE);
            }
        }

		viewHold.commentNum.setText("回复 " + commentInfo.getReplyNum());

		ImageLoadUtils.setRoundHeadView(viewHold.headImage,
                commentInfo.getAuthor().getAvatar(), R.drawable.default_avatar, 100);
		viewHold.imageGallery.removeAllViews();

        viewHold.commentNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickPraiseListener != null) {
                    onClickPraiseListener.onComment(position);
                }
            }
        });

		final List<PicInfo> photos = commentInfo.getPhotos();

		if (photos != null && commentInfo.getDelFlag() == 0) {
			for (int i = 0; i < photos.size(); i++) {

				ImageView imageView = new ImageView(mContext);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						BaseInfoUtil.dip2px(100), BaseInfoUtil.dip2px(100));

				params.setMargins(0, 0, BaseInfoUtil.dip2px(10), 0);

				imageView.setLayoutParams(params);

				ImageLoader.getInstance().displayImage(
						photos.get(i).getThumbnailUrl(), imageView, options);

				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

				imageView.setTag(i);
				
				imageView.setBackgroundColor(mContext.getResources().getColor(R.color.light_light_gray));

				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								ShowImagesPagerActivity.class);

						intent.putExtra(Constant.KEY.PICINFO_LIST,
								(ArrayList<PicInfo>) photos);
						intent.putExtra(Constant.KEY.POSITION,
								(Integer) v.getTag());

						mContext.startActivity(intent);
					}
				});

				viewHold.imageGallery.addView(imageView);

			}
		}

        if(commentInfo.getItemType() == 4){
            viewHold.ratingBar.setVisibility(View.VISIBLE);

            viewHold.ratingBar.setRating(commentInfo.getRating() / 2);

            viewHold.commentTag.setText("(买家评论)");
        }else if(commentInfo.getItemType() == 5){
            viewHold.ratingBar.setVisibility(View.GONE);

            viewHold.commentTag.setText("(粉丝评论)");
        }else{
            viewHold.ratingBar.setVisibility(View.GONE);

            viewHold.commentTag.setText("");
        }

        if(commentInfo.getAuthor().getUserType() == 2){
            viewHold.officialTag.setVisibility(View.VISIBLE);
        }else{
            viewHold.officialTag.setVisibility(View.GONE);
        }

		return convertView;
	}

	final static class ViewHold {

		ImageView headImage;

		TextView userName;

		TextView date;

		TextView content;

		TextView address;

		TextView praiseNum;

		TextView delete;

		TextView commentNum;

		LinearLayout imageGallery;

        RatingBar ratingBar;

        TextView commentTag;

        ImageView officialTag;

	}

	public interface OnClickPraiseListener {
		 void onClickPraise(int position, View view);

		 void onDeleteComment(int position);

        void onComment(int position);

	}

	private OnClickPraiseListener onClickPraiseListener;

	public void setOnClickPraiseListener(OnClickPraiseListener listener) {
		onClickPraiseListener = listener;
	}

}
