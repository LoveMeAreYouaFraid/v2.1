package com.nautilus.ywlfair.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.entity.bean.TicketListItem;
import com.nautilus.ywlfair.module.mine.TicketOrderActivity;
import com.nautilus.ywlfair.widget.RippleView;
import com.nautilus.ywlfair.widget.flowlayout.FlowLayout;
import com.nautilus.ywlfair.widget.flowlayout.TagAdapter;
import com.nautilus.ywlfair.widget.flowlayout.TagFlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/29.
 */
public class ShowTicketOrderMenu implements View.OnClickListener, RippleView.OnEndClickListener {

    private Dialog goodsMenuDialog;

    private List<TicketListItem> ticketListItemList;

    private static ShowTicketOrderMenu instance;

    private Context mContext;

    private TagFlowLayout mFlowLayout;

    private ImageView addBtn, subtractBtn;

    private EditText numEdit;

    private int goodsNum = 1;

    private TicketListItem ticketListItem;

    private HomePagerActivityInfo homePagerActivityInfo;

    private int selectPosition;

    public static ShowTicketOrderMenu getInstance() {
        if (instance == null) {

            instance = new ShowTicketOrderMenu();
        }

        return instance;
    }

    public void initMenuDialog(Context context, List<TicketListItem> ticketListItemList, HomePagerActivityInfo homePagerActivityInfo) {

        this.homePagerActivityInfo = homePagerActivityInfo;

        this.ticketListItemList = getTicketListItemList(ticketListItemList);

        if(this.ticketListItemList.size() == 0 ){
            ToastUtil.showLongToast("没有可购票");
            return;
        }

        if (context == this.mContext && goodsMenuDialog != null) {

            if (!goodsMenuDialog.isShowing()) {

                goodsMenuDialog.show();

            }
            return;

        }

        mContext = context;

        View view = View.inflate(mContext, R.layout.ticket_confirm_dialog, null);

        goodsMenuDialog = new Dialog(mContext, R.style.dialog);

        goodsMenuDialog.setCanceledOnTouchOutside(true);

        goodsMenuDialog.setContentView(view);

        goodsMenuDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Window window = goodsMenuDialog.getWindow();

        /* 设置位置 */
        window.setGravity(Gravity.BOTTOM);

        /* 设置动画 */
        window.setWindowAnimations(R.style.umeng_socialize_shareboard_animation);

        initViews(view);

        goodsMenuDialog.show();

    }

    private void initViews(View view) {

        RippleView confirm = (RippleView) view.findViewById(R.id.tv_confirm);
        confirm.setOnEndClickListener(confirm.getId(), this);

        addBtn = (ImageView) view.findViewById(R.id.iv_add);

        subtractBtn = (ImageView) view.findViewById(R.id.iv_subtract);

        numEdit = (EditText) view.findViewById(R.id.et_ticket_num);

        addBtn.setOnClickListener(this);

        subtractBtn.setOnClickListener(this);

        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.id_flow_layout);

        final MyTagAdapter tagAdapter = new MyTagAdapter(ticketListItemList);

        mFlowLayout.setAdapter(tagAdapter);

        mFlowLayout.performClick();

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

                if (selectPosSet.isEmpty()) {

                    ticketListItem = null;

                } else {

                    selectPosition = selectPosSet.iterator().next();

                    ticketListItem = ticketListItemList.get(selectPosition);
                }
            }
        });

        selectPosition = 0;

        ticketListItem = ticketListItemList.get(0);

        tagAdapter.setSelectedList(0);

        numEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numEdit.getText().length() == 0) {
                    numEdit.setText("1");
                }
                if (Integer.valueOf(numEdit.getText().toString()) > ticketListItem.getLeftNum()) {

                    ToastUtil.showLongToast("数量超出范围");

                }
            }
        });
    }

    @Override
    public void mViewClick(int id) {
        switch (id) {
            case R.id.tv_confirm:

                if (ticketListItem == null) {
                    ToastUtil.showLongToast("请选择门票类型");
                    return;
                }

                int number = Integer.valueOf(numEdit.getText().toString());

                if ( number > ticketListItem.getLeftNum()) {

                    ToastUtil.showLongToast("数量超出范围");

                    return;
                }

                Intent intent = new Intent(mContext, TicketOrderActivity.class);


                intent.putExtra(
                        Constant.KEY.MODE,
                        TicketOrderActivity.Mode.TICKET);

                intent.putExtra(Constant.KEY.TICKET, ticketListItem);

                intent.putExtra(Constant.KEY.NUMBER, number);

                intent.putExtra(Constant.KEY.NAUTILUSITEM, homePagerActivityInfo);

                mContext.startActivity(intent);

                goodsMenuDialog.dismiss();

                break;
        }

    }

    class MyTagAdapter extends TagAdapter {

        public MyTagAdapter(List data) {
            super(data);
        }

        @Override
        public View getView(FlowLayout parent, int position, Object o) {

            TicketListItem ticketListItem = ticketListItemList.get(position);

            TextView stallTypeView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.goods_sku_text, parent, false);

            stallTypeView.setText(ticketListItem.getTicketTypeName() + "：" + StringUtils.getMoneyFormat(ticketListItem.getPrice()));

            if(ticketListItem.getIsCanBuy() == 0){
                stallTypeView.setVisibility(View.GONE);
            }

            return stallTypeView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_add:

                numEdit.setText(goodsNum + "");

                if (TextUtils.isEmpty(numEdit.getText().toString())) {
                    goodsNum = 0;
                } else {
                    goodsNum = Integer.valueOf(numEdit.getText().toString());
                }

                if (goodsNum >= 999) {

                    numEdit.setText("999");
                } else {
                    if (goodsNum > ticketListItem.getLeftNum() - 1) {

                        ToastUtil.showLongToast("数量超出范围");

                    } else {
                        goodsNum++;
                    }

                }

                break;
            case R.id.iv_subtract:

                if (TextUtils.isEmpty(numEdit.getText().toString())) {
                    goodsNum = 1;
                } else {
                    goodsNum = Integer.valueOf(numEdit.getText().toString());
                }

                if (goodsNum == 1 || goodsNum == 0) {
                    numEdit.setText(goodsNum + "");
                } else {

                    goodsNum--;

                    numEdit.setText(goodsNum + "");
                }

                break;
        }
    }

    private List<TicketListItem> getTicketListItemList(List<TicketListItem> list){

        List<TicketListItem> ticketListItemList = new ArrayList<>();

        for (TicketListItem ticketListItem : list){

            if(ticketListItem.getIsCanBuy() == 1 && ticketListItem.getPrice() != 0){
                ticketListItemList.add(ticketListItem);
            }
        }

        return ticketListItemList;
    }
}
