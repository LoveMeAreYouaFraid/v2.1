package com.nautilus.ywlfair.module.recommend.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
public class ExperienceListAdapter extends BaseAdapter {
    private Context mContext;//用于接收传递过来的Context对象

    private List<ArticleInfo> commentInfoList;

    private DisplayImageOptions options = ImageLoadUtils.createNoRoundedOptions();

    public ExperienceListAdapter(Context context, List<ArticleInfo> commentInfoList) {
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
            convertView = View.inflate(mContext, R.layout.experience_item, null);

            viewHold = new ViewHold();

            viewHold.imageView = (AutoAdjustHeightImageView) convertView.findViewById(R.id.recommend_image);

            viewHold.title = (TextView) convertView.findViewById(R.id.recommend_title);

            viewHold.tag = (TextView) convertView.findViewById(R.id.recommend_tag);

            viewHold.author = (TextView) convertView.findViewById(R.id.recommend_author);

            viewHold.likeNum = (TextView) convertView.findViewById(R.id.tv_like);

            viewHold.replyNum = (TextView) convertView.findViewById(R.id.tv_comment);

            viewHold.recommend_context = (TextView) convertView.findViewById(R.id.recommend_context);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        ArticleInfo articleInfo = commentInfoList.get(position);

        ImageLoader.getInstance().displayImage(articleInfo.getImgUrl(), viewHold.imageView, options);

        viewHold.title.setText(articleInfo.getTitle());

        viewHold.author.setText(articleInfo.getEditorName() +"   "+ TimeUtil.getYearMonthAndDay(Long.valueOf(articleInfo.getDateTime())));

        viewHold.likeNum.setText(articleInfo.getLikeNum() + "");
        viewHold.recommend_context.setText(articleInfo.getDesc());
        viewHold.tag.setText(articleInfo.getTagName());
        viewHold.replyNum.setText(articleInfo.getCommentNum() + "");

        return convertView;
    }

    final static class ViewHold {

        AutoAdjustHeightImageView imageView;

        TextView title;

        TextView author;

        TextView likeNum;

        TextView replyNum;
        TextView recommend_context;
        TextView tag;

    }


}



