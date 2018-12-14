package com.nautilus.ywlfair.module.active.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.entity.bean.VideoInfo;
import com.nautilus.ywlfair.module.VideoPlayerActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class ActiveVideoListAdapter extends BaseAdapter {

    private Context mContext;

    private List<VideoInfo> videoList;

    private DisplayImageOptions options = ImageLoadUtils
            .createNoRoundedOptions();

    public ActiveVideoListAdapter(Context mContext, List<VideoInfo> videoList) {
        this.mContext = mContext;

        this.videoList = videoList;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.video_list_item,
                    null);
        }

        VideoInfo videoInfo = videoList.get(position);

        final AutoAdjustHeightImageView imageView = (AutoAdjustHeightImageView) convertView
                .findViewById(R.id.iv_video_cover);

        ImageLoader.getInstance().displayImage(videoInfo.getImgUrl(),
                imageView, options);

        TextView dateView = (TextView) convertView
                .findViewById(R.id.tv_date);
        TextView description = (TextView) convertView
                .findViewById(R.id.tv_description);

        description.setText(videoInfo.getVideoDesc());

        dateView.setText(TimeUtil.getDateFormat(Long.valueOf(videoInfo.getUploadDate())));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoList.get(position).getVideoUrl().isEmpty()) {
                    Toast.makeText(mContext, "没有视频", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Log.e("123", videoList.get(position).getVideoUrl());
                    mContext.startActivity(new Intent(mContext, VideoPlayerActivity.class).putExtra("VideoUrl",
                            videoList.get(position).getVideoUrl()));
                }


            }
        });
        return convertView;
    }

    public final int getCount() {
        return videoList.size();
    }

    public final Object getItem(int position) {
        return videoList.get(position);
    }

    public final long getItemId(int position) {
        return position;
    }
}