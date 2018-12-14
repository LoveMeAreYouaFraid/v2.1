package com.nautilus.ywlfair.module.goods.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.widget.AutoAdjustHeightImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/12/28.
 */
public class RecyclerViewAdapter extends BaseAdapter {

    private Context context;

    private List<GoodsInfo> data;

    private MOnClickListener mOnClickListener;

    public RecyclerViewAdapter(Context context, List<GoodsInfo> data) {

        this.context = context;

        this.data = data;

    }

    public void setMOnClickListener(MOnClickListener listener) {
        mOnClickListener = listener;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHold viewHold = null;
        if (convertView == null) {
            viewHold = new ViewHold();

            convertView = View.inflate(context, R.layout.goods_list_item, null);

            viewHold.imageView = (AutoAdjustHeightImageView) convertView.findViewById(R.id.item_list_iv_icon);
            viewHold.like = (ImageView) convertView.findViewById(R.id.img_lick);
            viewHold.title = (TextView) convertView.findViewById(R.id.item_list_tv_name);
            viewHold.seller = (TextView) convertView.findViewById(R.id.item_list_tv_seller_name);
            viewHold.price = (TextView) convertView.findViewById(R.id.item_list_tv_price);
            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        final GoodsInfo dataInfo = data.get(position);

        ImageLoader.getInstance().displayImage(dataInfo.getImageUrl(),
                viewHold.imageView, ImageLoadUtils.createNoRoundedOptions());

        viewHold.title.setText(dataInfo.getGoodsName());
        viewHold.seller.setText(dataInfo.getVendorNickname());
        viewHold.price.setText("￥ " + StringUtils.getMoneyFormat(dataInfo.getPrice()));
        final int hasLike = dataInfo.getHasLike();

        if (hasLike == 0) {
            viewHold.like.setImageResource(R.drawable.commodity_islike);
        } else {
            viewHold.like.setImageResource(R.drawable.commodity_like);
        }
        viewHold.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoginWarmUtil.getInstance().checkLoginStatus(context)) {
                    return;
                }
                boolean isSuccess = mOnClickListener.onClick((ImageView) v, position);

                if (isSuccess) {
                    dataInfo.setHasLike(hasLike == 0 ? 1 : 0);
                }


            }
        });


        if (position % 2 == 0) {
            convertView.setPadding(8, 8, 4, 0);
        } else {
            convertView.setPadding(4, 8, 8, 0);
        }

        return convertView;
    }

    final static class ViewHold {
        AutoAdjustHeightImageView imageView;
        ImageView like;
        TextView title;
        TextView seller;
        TextView price;
    }


    /**
     * post 点赞
     *
     * @param position
     * @param isLike
     */
    private void isLike(int position, int isLike) {
        int userId = 0;
        String itemId = data.get(position + 2).getGoodsId() + "";
        if (MyApplication.getInstance().isLogin()) {
            userId = GetUserInfoUtil.getUserInfo().getUserId();
        }
        Log.e("123", "itemId=" + itemId + "isLike" + isLike + "");
//        (String itemId, int itemType, int isLike,int userId,
        PostLikeRequest postLikeRequest = new PostLikeRequest(itemId, 4, isLike, userId, new ResponseListener<PostLikeAndJoinResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(PostLikeAndJoinResponse response) {

            }

            @Override
            public void onResponse(PostLikeAndJoinResponse response) {


            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onFinish() {

            }
        });
        postLikeRequest.setShouldCache(true);

        VolleyUtil.addToRequestQueue(postLikeRequest);
    }

    public interface MOnClickListener {
        boolean onClick(ImageView v, int position);
    }
}
