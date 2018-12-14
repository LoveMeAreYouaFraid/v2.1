package com.nautilus.ywlfair.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nautilus.ywlfair.widget.ShowImagesPagerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyCommentListAdapter extends BaseAdapter {

    private Context mContext;

    private List<CommentInfo> commentInfoList;

    public MyCommentListAdapter(Context context,
                                List<CommentInfo> commentInfoList) {
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
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_comment_list_item,
                    null);

            viewHold = new ViewHold();

            viewHold.imageView = (ImageView) convertView
                    .findViewById(R.id.iv_header);

            viewHold.name = (TextView) convertView
                    .findViewById(R.id.tv_comment_name);

            viewHold.date = (TextView) convertView.findViewById(R.id.tv_date);

            viewHold.officialTag = (ImageView) convertView.findViewById(R.id.iv_official_tag);

            viewHold.content = (TextView) convertView
                    .findViewById(R.id.tv_comment_content);

            viewHold.row1 = (TableRow) convertView.findViewById(R.id.tr_row1);

            viewHold.row2 = (TableRow) convertView.findViewById(R.id.tr_row2);

            viewHold.imageView1 = (AutoAdjustHeightImageView) convertView
                    .findViewById(R.id.iv_multi_pics_0);

            viewHold.imageView2 = (AutoAdjustHeightImageView) convertView
                    .findViewById(R.id.iv_multi_pics_1);

            viewHold.imageView3 = (AutoAdjustHeightImageView) convertView
                    .findViewById(R.id.iv_multi_pics_2);

            viewHold.imageView4 = (AutoAdjustHeightImageView) convertView
                    .findViewById(R.id.iv_multi_pics_3);

            viewHold.imageView5 = (AutoAdjustHeightImageView) convertView
                    .findViewById(R.id.iv_multi_pics_4);

            viewHold.imageView6 = (AutoAdjustHeightImageView) convertView
                    .findViewById(R.id.iv_multi_pics_5);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        CommentInfo commentInfo = commentInfoList.get(position);

        viewHold.name.setText(commentInfo.getAuthor().getNickName());

        viewHold.content.setText(commentInfo.getContent());

        ImageLoadUtils.setRoundHeadView(viewHold.imageView,
                commentInfo.getAuthor().getAvatar(), R.drawable.default_avatar, 100);

        viewHold.date.setText(TimeUtil.castLastDate(Long
                .valueOf(commentInfo.getAddTime())));


        if(commentInfo.getAuthor().getUserType() == 2){
            viewHold.officialTag.setVisibility(View.VISIBLE);
        }else{
            viewHold.officialTag.setVisibility(View.GONE);
        }

        showPictures(viewHold, commentInfo);

        return convertView;
    }

    final static class ViewHold {

        ImageView imageView;

        TextView name;

        TextView date;

        TextView content;

        ImageView officialTag;

        TableRow row1;

        TableRow row2;

        AutoAdjustHeightImageView imageView1;

        AutoAdjustHeightImageView imageView2;

        AutoAdjustHeightImageView imageView3;

        AutoAdjustHeightImageView imageView4;

        AutoAdjustHeightImageView imageView5;

        AutoAdjustHeightImageView imageView6;

    }


    private void showPictures(ViewHold viewHold, CommentInfo commentInfo) {

        AutoAdjustHeightImageView[] imageViews = new AutoAdjustHeightImageView[6];

        imageViews[0] = viewHold.imageView1;

        imageViews[1] = viewHold.imageView2;

        imageViews[2] = viewHold.imageView3;

        imageViews[3] = viewHold.imageView4;

        imageViews[4] = viewHold.imageView5;

        imageViews[5] = viewHold.imageView6;

        final List<PicInfo> picInfoList = commentInfo.getPhotos();

        if (picInfoList != null) {
            if (picInfoList.size() > 3) {
                viewHold.row1.setVisibility(View.VISIBLE);

                viewHold.row2.setVisibility(View.VISIBLE);

            } else if (picInfoList.size() > 0 && picInfoList.size() <= 3) {
                viewHold.row1.setVisibility(View.VISIBLE);

                viewHold.row2.setVisibility(View.GONE);
            }else{
                viewHold.row1.setVisibility(View.GONE);

                viewHold.row2.setVisibility(View.GONE);
            }

            for (int i = 0; i < picInfoList.size(); i++) {

                if (i >= imageViews.length) {
                    continue;
                }
                ImageLoader.getInstance().displayImage(
                        picInfoList.get(i).getThumbnailUrl(), imageViews[i], ImageLoadUtils.createNoRoundedOptions());
                imageViews[i].setTag(i);

                imageViews[i].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext,
                                ShowImagesPagerActivity.class);

                        intent.putExtra(Constant.KEY.PICINFO_LIST,
                                (ArrayList<PicInfo>) picInfoList);
                        intent.putExtra(Constant.KEY.POSITION,
                                (Integer) v.getTag());

                        mContext.startActivity(intent);
                    }
                });

            }
        }

    }
}
