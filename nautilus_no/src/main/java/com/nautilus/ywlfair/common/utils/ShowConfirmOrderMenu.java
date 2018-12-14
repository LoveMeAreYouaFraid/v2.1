package com.nautilus.ywlfair.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.SkuInfo;
import com.nautilus.ywlfair.module.goods.OrderSelectionActivity;
import com.nautilus.ywlfair.widget.RippleView;
import com.nautilus.ywlfair.widget.flowlayout.FlowLayout;
import com.nautilus.ywlfair.widget.flowlayout.TagAdapter;
import com.nautilus.ywlfair.widget.flowlayout.TagFlowLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/29.
 */
public class ShowConfirmOrderMenu implements View.OnClickListener, RippleView.OnEndClickListener {

    private Dialog goodsMenuDialog;

    private GoodsInfo goodsInfo;

    private static ShowConfirmOrderMenu instance;

    private Context mContext;

    private TagFlowLayout mFlowLayout;

    private ImageView addBtn, subtractBtn;

    private EditText numEdit;

    private int goodsNum = 1;

    private int limitNum;

    private SkuInfo currentSku;

    private int selectPosition;

    public static ShowConfirmOrderMenu getInstance() {
        if (instance == null) {

            instance = new ShowConfirmOrderMenu();
        }

        return instance;
    }

    public void initMenuDialog(Context context, GoodsInfo goodsInfo) {

        if (context == this.mContext && goodsMenuDialog != null) {

            if(!goodsMenuDialog.isShowing()){

                goodsMenuDialog.show();

            }
            return;

        }

        this.goodsInfo = goodsInfo;

        mContext = context;

        View view = View.inflate(mContext, R.layout.purchase_of_goods_dialog, null);

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

        TextView goodsName = (TextView) view.findViewById(R.id.tv_goods_name);
        goodsName.setText(goodsInfo.getGoodsName());

        final TextView goodsPrice = (TextView) view.findViewById(R.id.tv_goods_price);

        final TextView goodsStock = (TextView) view.findViewById(R.id.tv_goods_stock);

        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_goods_image);

        TextView postDays = (TextView) view.findViewById(R.id.tv_post_days);

        if (goodsInfo.getSkuInfoList() != null && goodsInfo.getSkuInfoList().size() > 0) {

            currentSku = goodsInfo.getSkuInfoList().get(0);

            goodsPrice.setText("￥ " + currentSku.getPrice());

            ImageLoader.getInstance().displayImage(goodsInfo.getImageUrl(), imageView, ImageLoadUtils.createNoRoundedOptions());

            goodsStock.setText("库存" + currentSku.getLeftNum() + "件");

            limitNum = currentSku.getLeftNum();

            postDays.setText("卖家承诺"+ currentSku.getPostDays() + "天发货");

            if (currentSku.getLeftNum() <= 0) {
                numEdit.setText("0");

            }
        }

        mFlowLayout = (TagFlowLayout) view.findViewById(R.id.id_flow_layout);

        final MyTagAdapter tagAdapter = new MyTagAdapter(goodsInfo.getSkuInfoList());

        mFlowLayout.setAdapter(tagAdapter);

        mFlowLayout.performClick();

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

                if (selectPosSet.isEmpty()) {
                    tagAdapter.setSelectedList(selectPosition);

                } else {

                    selectPosition = selectPosSet.iterator().next();

                    currentSku = goodsInfo.getSkuInfoList().get(selectPosition);

                    goodsPrice.setText("￥ " + currentSku.getPrice());

                    goodsStock.setText("库存" + currentSku.getLeftNum() + "件");

                    limitNum = currentSku.getLeftNum();

                    ImageLoader.getInstance().displayImage(currentSku.getSkuAttrImageUrl(), imageView, ImageLoadUtils.createNoRoundedOptions());
                }

            }
        });

        selectPosition = 0;

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
                if (Integer.valueOf(numEdit.getText().toString()) > limitNum) {
                    ToastUtil.showLongToast("数量超出范围");
                }
            }
        });
    }

    @Override
    public void mViewClick(int id) {
        switch (id) {
            case R.id.tv_confirm:
                if (goodsInfo == null) {
                    ToastUtil.showLongToast("商品不存在");
                    return;
                }

                if (currentSku == null) {
                    ToastUtil.showLongToast("请选择商品分类");
                    return;
                }
                if (currentSku.getLeftNum() <= 0) {
                    ToastUtil.showLongToast("库存不足");
                    return;
                }
                if (Integer.valueOf(numEdit.getText().toString()) > limitNum) {
                    ToastUtil.showLongToast("数量超出范围");
                    return;
                }

                Intent intent = new Intent(mContext, OrderSelectionActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable(Constant.KEY.GOODS_INFO, goodsInfo);

                bundle.putSerializable(Constant.KEY.SKU_INFO, currentSku);

                bundle.putInt(Constant.KEY.NUMBER, goodsNum);

                intent.putExtras(bundle);

                mContext.startActivity(intent);

                goodsMenuDialog.dismiss();

                break;
        }

    }

    class MyTagAdapter extends TagAdapter {

        public MyTagAdapter(List datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, Object o) {
            SkuInfo skuInfo = goodsInfo.getSkuInfoList().get(position);

            TextView stallTypeView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.goods_sku_text, parent, false);

            stallTypeView.setText(skuInfo.getSkuAttrValue());

            return stallTypeView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:

                if (goodsInfo == null || currentSku == null) {
                    ToastUtil.showLongToast("商品不存在");
                    return;
                }

                Intent intent = new Intent(mContext, OrderSelectionActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable(Constant.KEY.GOODS_INFO, goodsInfo);

                bundle.putSerializable(Constant.KEY.SKU_INFO, currentSku);

                bundle.putInt(Constant.KEY.NUMBER, goodsNum);

                intent.putExtras(bundle);

                mContext.startActivity(intent);

                goodsMenuDialog.dismiss();

                break;

            case R.id.iv_add:
                if (currentSku.getLeftNum() <= 0) {
                    Toast.makeText(mContext, "库存不足", Toast.LENGTH_LONG).show();
                    return;
                }
                numEdit.setText(goodsNum + "");

                if (TextUtils.isEmpty(numEdit.getText().toString())) {
                    goodsNum = 0;
                } else {
                    goodsNum = Integer.valueOf(numEdit.getText().toString());
                }

                if (goodsNum >= 999) {

                    numEdit.setText("999");
                } else {
                    if (goodsNum > limitNum - 1) {
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
}
