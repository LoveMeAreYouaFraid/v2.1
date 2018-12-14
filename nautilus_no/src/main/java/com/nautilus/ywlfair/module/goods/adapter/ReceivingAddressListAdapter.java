package com.nautilus.ywlfair.module.goods.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.entity.bean.ShippingAddressInfo;

import java.util.List;


/**
 * Created by Administrator on 2015/12/28.
 */
public class ReceivingAddressListAdapter extends BaseAdapter {
    private List<ShippingAddressInfo> mlist;
    private Context mContext;//用于接收传递过来的Context对象
    private OnViewsClickListener mOnViewsClickListener;
    private int mposition;
    private View mview;

    public ReceivingAddressListAdapter(Context context, List<ShippingAddressInfo> list) {
        this.mContext = context;
        this.mlist = list;
    }

    public void setOnViewsClickListener(OnViewsClickListener listener) {
        mOnViewsClickListener = listener;
    }

    @Override
    public int getCount() {

        try {
            return mlist.size();
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View v, final ViewGroup parent) {
        ViewHold viewHold = null;
        if (v == null) {
            viewHold = new ViewHold();
            v = View.inflate(mContext, R.layout.receiving_address_item, null);
            viewHold.Address = (TextView) v.findViewById(R.id.address);
            viewHold.Receiver = (TextView) v.findViewById(R.id.receiver);
            viewHold.ReceiverPhone = (TextView) v.findViewById(R.id.receiver_phone);
            viewHold.Default = (TextView) v.findViewById(R.id.tv_default);
            viewHold.Modify = (TextView) v.findViewById(R.id.tv_modify);
            viewHold.Delete = (TextView) v.findViewById(R.id.tv_delete);
            viewHold.cb_default = (CheckBox) v.findViewById(R.id.cb_default);
            viewHold.poscode = (TextView) v.findViewById(R.id.zip_code);
            v.setTag(viewHold);
        } else {
            viewHold = (ViewHold) v.getTag();
        }
        LogUtil.e("debug", position + "  " + mlist.get(position).getDefaultFlag() + mlist.get(position).getConsignee());
        if (Integer.valueOf(mlist.get(position).getDefaultFlag()) == 1) {

            viewHold.cb_default.setChecked(true);
        } else {
            viewHold.cb_default.setChecked(false);
        }

        viewHold.Address.setText("收货地址：" + mlist.get(position).getProvinceCity() + mlist.get(position).getAddress());
        viewHold.Receiver.setText("收货人：" + mlist.get(position).getConsignee());
        viewHold.ReceiverPhone.setText(mlist.get(position).getTelephone());
        viewHold.poscode.setText("邮编："+mlist.get(position).getPostCode());

        viewHold.cb_default.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnViewsClickListener != null) {
                    mOnViewsClickListener.mViewClick(position, v);
                }

            }
        });
        viewHold.Default.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnViewsClickListener != null) {
                    mOnViewsClickListener.mViewClick(position, v);
                }


            }
        });
        viewHold.Modify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnViewsClickListener != null) {
                    mOnViewsClickListener.mViewClick(position, v);
                }


            }
        });
        viewHold.Delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("123", v.getId() + "");
                if (mOnViewsClickListener != null) {
                    mOnViewsClickListener.mViewClick(position, v);
                }
            }
        });

        return v;
    }


    final static class ViewHold {
        private TextView Receiver,
                ReceiverPhone,
                Address,
                Default,
                Modify,
                Delete,
                poscode;
        private CheckBox cb_default;


    }


    /**
     * 控件被点击的事件监听接口
     */
    public interface OnViewsClickListener {

        void mViewClick(int position, View v);


    }
}
