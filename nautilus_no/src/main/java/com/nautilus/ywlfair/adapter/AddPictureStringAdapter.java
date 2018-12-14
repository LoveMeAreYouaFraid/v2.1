package com.nautilus.ywlfair.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nautilus.ywlfair.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *
 * @author dingying
 */
public class AddPictureStringAdapter extends BaseAdapter {

    private List<String> mItems;

    private Context mContext;

    public AddPictureStringAdapter(Context context, List<String> items) {

        mContext = context;

        mItems = items;

    }

    @Override
    public int getCount() {
        return mItems.size() + 1;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_gv_post_class_circle, parent, false);

            holder = new ViewHolder();

            holder.pictureImageView = (ImageView) convertView.findViewById(R.id.aahiv_picture);
            holder.addImageView = (ImageView) convertView.findViewById(R.id.aahiv_add);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position == mItems.size()) {

            holder.addImageView.setVisibility(View.VISIBLE);

            holder.pictureImageView.setVisibility(View.GONE);

        } else {
            String item = mItems.get(position);

            holder.pictureImageView.setVisibility(View.VISIBLE);
            holder.addImageView.setVisibility(View.GONE);

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.color.light_light_gray)
                    .considerExifParams(true)
                    .cacheInMemory(true) // default 不缓存至内存
                    .build();
           ImageLoader.getInstance().displayImage(item, holder.pictureImageView, options);
        }

        return convertView;
    }

    final static class ViewHolder {

        ImageView pictureImageView;
        ImageView addImageView;

    }

}

