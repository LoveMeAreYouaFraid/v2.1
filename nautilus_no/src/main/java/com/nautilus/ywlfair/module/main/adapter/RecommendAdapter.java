package com.nautilus.ywlfair.module.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class RecommendAdapter extends BaseAdapter {

    private Context mContext;

    private List<HomePagerActivityInfo> list;

    public RecommendAdapter(Context context, List<HomePagerActivityInfo> list) {

        this.mContext = context;

        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold viewHold;
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.home_pager_recommend_item, null);

            viewHold = new ViewHold();

            viewHold.imageView = (AutoAdjustHeightImageView) convertView.findViewById(R.id.iv_active_image);

            viewHold.date = (TextView) convertView.findViewById(R.id.tv_active_date);

            viewHold.title = (TextView) convertView.findViewById(R.id.tv_active_title);

            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        HomePagerActivityInfo homePagerActivityInfo = list.get(position);

        ImageLoader.getInstance().displayImage(homePagerActivityInfo.getListPic(), viewHold.imageView, ImageLoadUtils.createNoRoundedOptions());

        viewHold.title.setText(homePagerActivityInfo.getName());

        viewHold.date.setText(TimeUtil.getYearMonthAndDay(Long.valueOf(homePagerActivityInfo.getStartTime())) + "~" +
                TimeUtil.getYearMonthAndDay(Long.valueOf(homePagerActivityInfo.getEndTime())));

        return convertView;
    }

    class ViewHold {

        AutoAdjustHeightImageView imageView;

        TextView title;

        TextView date;
    }
}
