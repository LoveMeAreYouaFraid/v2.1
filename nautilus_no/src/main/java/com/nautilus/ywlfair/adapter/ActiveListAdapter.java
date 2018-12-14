package com.nautilus.ywlfair.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.TicketInfo;
import com.nautilus.ywlfair.widget.MaskedImage;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ActiveListAdapter extends BaseAdapter {

    private Context mContext;

    private List<NautilusItem> activeList;

    private DisplayImageOptions options = ImageLoadUtils.createNoRoundedOptions();

    private OnLikePressedListener onLikePressedListener;

    private int[] activeTags = new int[]{R.drawable.active_status_ing, R.drawable.active_status_past, R.drawable.active_status_be};

    public ActiveListAdapter(Context context, List<NautilusItem> activeList) {
        this.mContext = context;

        this.activeList = activeList;

    }

    @Override
    public int getCount() {
        return activeList.size();
    }

    @Override
    public Object getItem(int position) {
        return activeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHold viewHold;

        final int clickPosition = position;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.active_list_item, null);

            viewHold = new ViewHold();

            viewHold.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHold.like = (ImageView) convertView.findViewById(R.id.like);
            viewHold.title = (TextView) convertView.findViewById(R.id.title);
            viewHold.tag = (ImageView) convertView.findViewById(R.id.iv_active_tag);
            viewHold.time = (TextView) convertView.findViewById(R.id.time);
            viewHold.price = (TextView) convertView.findViewById(R.id.price);
            viewHold.place = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        final NautilusItem nautilusItem = activeList.get(position);

        viewHold.title.setText(nautilusItem.getName());

        viewHold.time.setText(TimeUtil.getYearMonthAndDay(Long.valueOf(nautilusItem.getStartTime())) + "~" +
                TimeUtil.getMonthAndDay(Long.valueOf(nautilusItem.getEndTime())) + "  " +
                TimeUtil.getHourAndMin(Long.valueOf(nautilusItem.getStartTime())) + "~" +
                TimeUtil.getHourAndMin(Long.valueOf(nautilusItem.getEndTime())));

        if (nautilusItem.getTicketInfoList().size() > 0) {
            viewHold.price.setText("门票: ￥" + StringUtils.getMoneyFormat(nautilusItem.getTicketInfoList().get(0).getPrice()));
        } else {
            viewHold.price.setText("免门票");

        }

        viewHold.place.setText(nautilusItem.getAddress());

        viewHold.tag.setImageResource(activeTags[nautilusItem.getActivityStatus()]);

        ImageLoader.getInstance().displayImage(nautilusItem.getListPic(), viewHold.imageView, options);

        final int hasLike = nautilusItem.getHasLike();

        if (hasLike == 0) {
            viewHold.like.setImageResource(R.drawable.bt_like);

        }else {
            viewHold.like.setImageResource(R.drawable.bt_islike);
        }

        viewHold.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLikePressedListener != null) {
                    boolean isSuccess = onLikePressedListener.OnClick(clickPosition, (ImageView) v);

                    if (isSuccess) {
                        nautilusItem.setHasLike(hasLike == 0 ? 1 : 0);
                    }
                }
            }
        });

        return convertView;
    }

    public interface OnLikePressedListener{
        boolean OnClick(int position,ImageView imageView);
    }

    final static class ViewHold {

        ImageView imageView, like;

        TextView title, //标题
                time,//时间
                price,//票价
                place;//地点

        ImageView tag;

    }

    public OnLikePressedListener getOnLikePressedListener() {
        return onLikePressedListener;
    }

    public void setOnLikePressedListener(OnLikePressedListener onLikePressedListener) {
        this.onLikePressedListener = onLikePressedListener;
    }
}
