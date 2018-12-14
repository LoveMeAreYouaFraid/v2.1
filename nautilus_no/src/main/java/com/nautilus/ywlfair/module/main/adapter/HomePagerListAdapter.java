package com.nautilus.ywlfair.module.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.HomePagerArticleInfo;
import com.nautilus.ywlfair.module.webview.ActiveWebViewActivity;
import com.nautilus.ywlfair.module.mine.AllActivityListActivity;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class HomePagerListAdapter extends BaseAdapter {

    private Context mContext;

    private List<HomePagerArticleInfo> list;

    public HomePagerListAdapter(Context context, List<HomePagerArticleInfo> list){
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
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.home_pager_list_item, null);

            viewHold = new ViewHold();

            viewHold.imageView = (AutoAdjustHeightImageView) convertView.findViewById(R.id.iv_active_image);

            viewHold.address = (TextView) convertView.findViewById(R.id.tv_active_address);

            viewHold.title = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(viewHold);

        }else{
            viewHold = (ViewHold) convertView.getTag();
        }

        final HomePagerArticleInfo homePagerArticleInfo = list.get(position);

        ImageLoader.getInstance().displayImage(homePagerArticleInfo.getImageUrl(),
                viewHold.imageView, ImageLoadUtils.createNoRoundedOptions());

        viewHold.title.setText(homePagerArticleInfo.getTitle());

        viewHold.address.setText(homePagerArticleInfo.getActName());

        viewHold.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                if(homePagerArticleInfo.getActId().equals("-1")){

                    intent.setClass(mContext, AllActivityListActivity.class);

                }else{
                    intent.setClass(mContext, ActiveWebViewActivity.class);

                    intent.putExtra(Constant.KEY.ITEM_ID, homePagerArticleInfo.getActId());
                }

                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    final static class ViewHold {

        AutoAdjustHeightImageView imageView;

        TextView title;

        TextView address;

    }
}
