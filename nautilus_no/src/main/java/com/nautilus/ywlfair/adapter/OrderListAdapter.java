package com.nautilus.ywlfair.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.OrderInfo;
import com.nautilus.ywlfair.module.webview.WebViewActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {

    private Context mContext;

    private List<OrderInfo> orderInfoList;

    private DisplayImageOptions options = ImageLoadUtils
            .createNoRoundedOptions();

    private String[] tips;

    private OnViewsClickListener mOnViewsClickListener;

    public OrderListAdapter(Context context, List<OrderInfo> orderInfoList) {
        this.mContext = context;

        this.orderInfoList = orderInfoList;

        tips = mContext.getResources().getStringArray(R.array.order_status_tip);

    }

    public void setOnViewsClickListener(OnViewsClickListener listener) {
        mOnViewsClickListener = listener;
    }

    @Override
    public int getCount() {
        return orderInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHold viewHold = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_order_form_item,
                    null);

            viewHold = new ViewHold();

            viewHold.lineUp = convertView.findViewById(R.id.layout_line_up);

            viewHold.lineDown = convertView.findViewById(R.id.layout_line_down);

            viewHold.imageView = (ImageView) convertView.findViewById(R.id.iv_product_pic);

            viewHold.vendorName = (TextView) convertView.findViewById(R.id.tv_vendor_name);

            viewHold.status = (TextView) convertView.findViewById(R.id.tv_status);

            viewHold.productName = (TextView) convertView.findViewById(R.id.tv_product_name);

            viewHold.price = (TextView) convertView.findViewById(R.id.tv_price);

            viewHold.num = (TextView) convertView.findViewById(R.id.tv_num);

            viewHold.skuAttr = (TextView) convertView.findViewById(R.id.tv_sku_attr);

            viewHold.totalPrice = (TextView) convertView.findViewById(R.id.tv_total_price);

            viewHold.leftBtn = (TextView) convertView.findViewById(R.id.tv_left_btn);

            viewHold.rightBtn = (TextView) convertView.findViewById(R.id.tv_right_btn);

            viewHold.postDaysView = (TextView) convertView.findViewById(R.id.tv_post_days);

            viewHold.controller = convertView.findViewById(R.id.ll_controller);

            viewHold.imgLineDownHead = (ImageView) convertView.findViewById(R.id.img_line_down_head);

            viewHold.tvLineDownHeadName = (TextView) convertView.findViewById(R.id.tv_line_down_head_name);

            viewHold.tvLineDownStatus = (TextView) convertView.findViewById(R.id.tv_line_down_status);

            viewHold.ivLineDownProductDic = (ImageView) convertView.findViewById(R.id.iv_line_down_product_pic);

            viewHold.tvLineDownProductName = (TextView) convertView.findViewById(R.id.tv_line_down_product_name);

            viewHold.tvLineDownPrice = (TextView) convertView.findViewById(R.id.tv_line_down_price);

            viewHold.tvLineDownPayment = (TextView) convertView.findViewById(R.id.tv_line_down_payment);

            viewHold.tvSeeDetails = (TextView) convertView.findViewById(R.id.tv_see_details);

            convertView.setTag(viewHold);

        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        final OrderInfo orderInfo = orderInfoList.get(position);

        if (orderInfoList.get(position).getOrderType().equals("5")) {
            if (orderInfo.getOrderStatus() != -1) {
                viewHold.lineDown.setVisibility(View.VISIBLE);
                viewHold.lineUp.setVisibility(View.GONE);
                lineDown(orderInfo, viewHold);
                viewHold.tvSeeDetails.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnViewsClickListener != null) {
                            mOnViewsClickListener.lookDetails(position);
                        }
                    }
                });
            }

        } else {
            if (orderInfo.getOrderStatus() != -1) {

                viewHold.lineDown.setVisibility(View.GONE);
                viewHold.lineUp.setVisibility(View.VISIBLE);
                viewHold.vendorName.setText(orderInfo.getVendorNickname());

                int orderStatus = orderInfo.getOrderStatus();

                ImageLoader.getInstance().displayImage(orderInfo.getSkuImgUrl(),
                        viewHold.imageView, options);

                viewHold.productName.setText(orderInfo.getGoodsName());

                viewHold.price.setText("￥" + StringUtils.getMoneyFormat(orderInfo.getPrice() / orderInfo.getNum()));

                viewHold.num.setText("x" + orderInfo.getNum());

                viewHold.skuAttr.setText(orderInfo.getSkuAttrValue());

                viewHold.totalPrice.setText("合计: " + StringUtils.getMoneyFormat(orderInfo.getPrice()));

                viewHold.status.setText(tips[orderStatus]);

                switch (orderStatus) {
                    case 0:
                        viewHold.controller.setVisibility(View.VISIBLE);
                        viewHold.leftBtn.setVisibility(View.VISIBLE);
                        viewHold.leftBtn.setText("马上付款");
                        viewHold.leftBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mOnViewsClickListener != null) {
                                    mOnViewsClickListener.onPayViewClick(position);
                                }
                            }
                        });

                        viewHold.rightBtn.setVisibility(View.VISIBLE);
                        viewHold.rightBtn.setText("取消订单");
                        viewHold.rightBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mOnViewsClickListener != null) {
                                    mOnViewsClickListener.onCancelViewClick(position, -1);
                                }
                            }
                        });

                        break;

                    case 1:
                        viewHold.leftBtn.setVisibility(View.GONE);
                        viewHold.leftBtn.setText("取消订单");
                        viewHold.leftBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // if (mOnViewsClickListener != null) {
                                // mOnViewsClickListener.onCancelViewClick(orderInfo);
                                // }
                                Toast.makeText(MyApplication.getInstance(), "待发货订单不能取消",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        viewHold.rightBtn.setText("发货提醒");
                        viewHold.rightBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MyApplication.getInstance(), "暂不支持该功能",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        viewHold.controller.setVisibility(View.GONE);

                        break;

                    case 2:
                        viewHold.controller.setVisibility(View.VISIBLE);
                        viewHold.leftBtn.setVisibility(View.VISIBLE);
                        viewHold.leftBtn.setText("查看物流");
                        viewHold.leftBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                String url = String.format(
                                        Constant.URL.NAUTILUS_URL_GET_KUAIDI,
                                        orderInfo.getCourierType(), orderInfo.getCourierNo());

                                WebViewActivity.startWebViewActivity(mContext, "-1", url, orderInfo.getOrderId(), 0, 0);
                            }
                        });

                        viewHold.rightBtn.setText("确认收货");
                        viewHold.rightBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mOnViewsClickListener != null) {
                                    mOnViewsClickListener.onCancelViewClick(position, 3);
                                }
                            }
                        });
                        break;

                    case 3:
                        viewHold.controller.setVisibility(View.VISIBLE);
                        viewHold.leftBtn.setText("申请售后");
                        viewHold.leftBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mOnViewsClickListener != null) {
                                    mOnViewsClickListener.onApplyClick();
                                }
                            }
                        });

                        viewHold.rightBtn.setVisibility(View.VISIBLE);
                        viewHold.rightBtn.setText("评价商品");
                        viewHold.rightBtn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mOnViewsClickListener != null) {
                                    mOnViewsClickListener.onCommentClick(position);
                                }
                            }
                        });
                        break;

                    case 4:
                    case 5:
                        viewHold.controller.setVisibility(View.GONE);
                        break;
                }

                if (!TextUtils.isEmpty(orderInfo.getPostDays())) {
                    viewHold.postDaysView.setVisibility(View.VISIBLE);
                    viewHold.postDaysView.setText("卖家承诺" + orderInfo.getPostDays() + "天发货");
                } else {
                    viewHold.postDaysView.setVisibility(View.GONE);
                }
            }

        }


        return convertView;
    }

    final static class ViewHold {

        View lineUp;

        View lineDown;

        ImageView imageView;

        TextView vendorName;

        TextView status;

        TextView productName;

        TextView price;

        TextView num;

        TextView skuAttr;

        TextView totalPrice;

        TextView leftBtn;

        TextView rightBtn;

        View controller;

        TextView postDaysView;

        ImageView imgLineDownHead;

        TextView tvLineDownHeadName;

        TextView tvLineDownStatus;

        ImageView ivLineDownProductDic;

        TextView tvLineDownProductName;

        TextView tvLineDownPrice;

        TextView tvLineDownPayment;

        TextView tvSeeDetails;

    }

    /**
     * 控件被点击的事件监听接口
     */
    public interface OnViewsClickListener {

        void onCancelViewClick(int position, int type);

        void onPayViewClick(int position);

        void onApplyClick();

        void lookDetails(int position);

        void onCommentClick(int position);
    }


    private void lineDown(final OrderInfo orderInfo, ViewHold viewHold) {

        ImageLoadUtils.setRoundHeadView(viewHold.imgLineDownHead,
                orderInfo.getVendorAvatar(), R.drawable.default_avatar, 120);

        viewHold.tvLineDownStatus.setText("交易成功");

        String goodsNameAndTime = orderInfo.getGoodsName() + "<Br/>" + " <font color = '#999999'>" + TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(orderInfo.getOrderTime())) + "</font>";
        viewHold.tvLineDownProductName.setText(Html.fromHtml(goodsNameAndTime));

        String DownPrice = "￥\t" + StringUtils.getMoneyFormat(orderInfo.getPrice()) + "" + "<Br/>" + "x\t" + orderInfo.getNum() + "";
        viewHold.tvLineDownPrice.setText(Html.fromHtml(DownPrice));

        String sf = "实付款";
//        if (GetUserInfoUtil.getUserInfo().getNickname().equals(orderInfo.getVendorNickname())) {
//            sf="实收款";
//        }

        String goodsNum = "共\t" + orderInfo.getNum() + "" + "\t件商品\t\t\t" + sf + ": \t\t\t" +
                "<font color = '#f5703f'>" + "￥" + StringUtils.getMoneyFormat(orderInfo.getPrice()) + "" + "</font>";

        viewHold.tvLineDownPayment.setText(Html.fromHtml(goodsNum));

        viewHold.tvLineDownHeadName.setText(orderInfo.getVendorNickname());

    }
}
