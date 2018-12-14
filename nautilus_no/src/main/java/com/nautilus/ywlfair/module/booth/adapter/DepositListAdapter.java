package com.nautilus.ywlfair.module.booth.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.entity.bean.DepositInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class DepositListAdapter extends BaseAdapter {

    private Context mContext;

    private List<DepositInfo> depositInfoList;
    private String[] depositStatus = new String[]{"可交押金", "已申请退款", "退款受理中", "退押失败", "已退款", "押金已扣完", "退押失败", "退押失败"};
    //    可交押金；1:已交押金可申请退款；2：已申请退款；3:已锁定；4：退押失败；5：退押成功；6: 押金已扣完
    private OnViewsClickListener mOnViewsClickListener;

    public void setOnViewsClickListener(OnViewsClickListener listener) {
        mOnViewsClickListener = listener;
    }

    public DepositListAdapter(Context context, List<DepositInfo> depositInfoList) {
        this.mContext = context;

        this.depositInfoList = depositInfoList;
    }

    @Override
    public int getCount() {
        return depositInfoList.size();
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
    public View getView(final int position, View v, ViewGroup parent) {
        ViewHold viewHold = null;
        if (v == null) {
            viewHold = new ViewHold();

            v = View.inflate(mContext, R.layout.booth_deposit_details_list_item, null);

            viewHold.date = (TextView) v.findViewById(R.id.tv_data);

            viewHold.tvDeposit = (TextView) v.findViewById(R.id.tv_deposit);

            viewHold.tvPayDeposit = (TextView) v.findViewById(R.id.tv_pay_deposit);

            viewHold.tvDebitRecords = (TextView) v.findViewById(R.id.tv_debit_records);

            viewHold.tvPrice = (TextView) v.findViewById(R.id.tv_price);

            viewHold.buttonLoyout = v.findViewById(R.id.layout_pay_deposit);

            v.setTag(viewHold);
        } else {
            viewHold = (ViewHold) v.getTag();
        }


        viewHold.date.setText(TimeUtil.getYearMonthAndDayWithHour(Long.valueOf(depositInfoList.get(position).getPayTime())));
        viewHold.tvPrice.setText("缴纳押金：" + StringUtils.getMoneyFormat(depositInfoList.get(position).getPrice()));
        viewHold.tvPayDeposit.setText(depositStatus[depositInfoList.get(position).getDepositStatus() - 1]);
        viewHold.tvDebitRecords.setText(depositInfoList.get(position).getDeductionsNum() + "" + "条扣款记录");
        viewHold.tvDeposit.setText("￥ " + StringUtils.getMoneyFormat(depositInfoList.get(position).getSurplusAmount()));
        switch (depositInfoList.get(position).getDepositStatus()) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                viewHold.buttonLoyout.setVisibility(View.VISIBLE);
                break;

        }
        return v;
    }


    final static class ViewHold {
        private TextView
                date,
                tvDeposit,
                tvPayDeposit,
                tvDebitRecords,
                tvPrice;
        private View buttonLoyout;

    }

    /**
     * 控件被点击的事件监听接口
     */
    public interface OnViewsClickListener {
        void mViewClick(int status, int position);
    }
}
