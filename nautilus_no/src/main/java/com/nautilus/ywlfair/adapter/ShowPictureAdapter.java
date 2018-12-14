package com.nautilus.ywlfair.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingying
 */
public class ShowPictureAdapter extends BaseAdapter {

    private List<String> mItems;

    private Context mContext;

    public ShowPictureAdapter(Context context, List<String> items) {

        mContext = context;

        mItems = items;

    }

    @Override
    public int getCount() {
        return mItems.size();
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


        String item = mItems.get(position);

        holder.pictureImageView.setVisibility(View.VISIBLE);

        holder.addImageView.setVisibility(View.GONE);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.light_light_gray)
                .considerExifParams(true)
                .cacheInMemory(true) // default 不缓存至内存
                .build();
        ImageLoader.getInstance().displayImage(item, holder.pictureImageView, options);

        holder.pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,
                        ShowPicturesPagerActivity.class);

                ArrayList<Uri> uris = new ArrayList<>();

//                uris.addAll(mItems);

                for (String string : mItems){
                    uris.add(Uri.parse(string));
                }

                intent.putExtra(Constant.KEY.URIS, uris);

                intent.putExtra(Constant.KEY.POSITION, position);

                intent.putExtra(Constant.KEY.CAN_DELETE, false);

                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    final static class ViewHolder {

        ImageView pictureImageView;
        ImageView addImageView;

    }

}

