package com.nautilus.ywlfair.common.utils;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;
import com.nautilus.ywlfair.module.goods.GoodsListActivity;
import com.nautilus.ywlfair.widget.flowlayout.FlowLayout;
import com.nautilus.ywlfair.widget.flowlayout.TagAdapter;
import com.nautilus.ywlfair.widget.flowlayout.TagFlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/12/29.
 */
public class ShowGoodsTypeMenu implements View.OnTouchListener {
    private Dialog goodsMenuDialog;

    private static ShowGoodsTypeMenu instance;

    private Context mContext;

    private List<GoodsClassInfo> goodsClassList;

    private int checkType;

    public static ShowGoodsTypeMenu getInstance() {
        if (instance == null) {

            instance = new ShowGoodsTypeMenu();
        }

        return instance;
    }

    public void initMenuDialog(Context context, List<GoodsClassInfo> goodsClass, int height, final int checkType) {

        if (context == this.mContext && goodsMenuDialog != null) {

            if(!goodsMenuDialog.isShowing()){

                goodsMenuDialog.show();

            }

            return;
        }

        mContext = context;

        this.goodsClassList = goodsClass;

        this.checkType = checkType;

        View view = View.inflate(mContext, R.layout.goods_menu_view, null);

        view.setOnTouchListener(this);

        goodsMenuDialog = new Dialog(mContext, R.style.share_dialog);

        goodsMenuDialog.setContentView(view);

        goodsMenuDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height - BaseInfoUtil.dip2px(mContext, 50));

        Window window = goodsMenuDialog.getWindow();

        /* 设置位置 */
        window.setGravity(Gravity.TOP);

        /* 设置动画 */
//        window.setWindowAnimations(R.style.goods_menu_animation);

        TagFlowLayout mFlowLayout = (TagFlowLayout) view.findViewById(R.id.id_flow_layout);

        MyTagAdapter tagAdapter = null;

//        if(type == 0){
        tagAdapter = new MyTagAdapter(goodsClassList);
//        }else if(type == 1){
//            tagAdapter = new MyTagAdapter(childClass);
//        }else if(type == 2){
//            tagAdapter = new MyTagAdapter(childChildClasses);
//        }

        mFlowLayout.setAdapter(tagAdapter);

        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

                if (selectPosSet.isEmpty()) {

                } else {
                    int position = selectPosSet.iterator().next();

                    Intent intent = new Intent(mContext, GoodsListActivity.class);

                    intent.putExtra(Constant.KEY.SECOND_CLASS, (ArrayList) goodsClassList.get(position).getChildClassList());

                    intent.putExtra(Constant.KEY.GOODS_CATEGORY, goodsClassList.get(position));

                    intent.putExtra(Constant.KEY.CHECK_TYPE, checkType);

                    mContext.startActivity(intent);

                    goodsMenuDialog.cancel();
                }

            }
        });

        WindowManager.LayoutParams layoutParams = window.getAttributes();

        layoutParams.y = BaseInfoUtil.dip2px(mContext, 50);

        window.setAttributes(layoutParams);

        goodsMenuDialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (goodsMenuDialog != null && goodsMenuDialog.isShowing()) {
            goodsMenuDialog.dismiss();
        }
        return false;
    }

    class MyTagAdapter extends TagAdapter {

        public MyTagAdapter(List list) {
            super(list);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(FlowLayout parent, int position, Object o) {

            TextView stallTypeView = new TextView(mContext);

            stallTypeView.setBackgroundResource(R.drawable.butt_with);

            stallTypeView.setText(goodsClassList.get(position).getClassName());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 0, BaseInfoUtil.dip2px(10), BaseInfoUtil.dip2px(10));

            stallTypeView.setLayoutParams(params);

            stallTypeView.setTextColor(mContext.getResources().getColor(R.color.dark_gray));

            stallTypeView.setPadding(BaseInfoUtil.dip2px(20), BaseInfoUtil.dip2px(10), BaseInfoUtil.dip2px(20), BaseInfoUtil.dip2px(10));

//            stallTypeView.setBackgroundResource();

            return stallTypeView;
        }
    }


}
