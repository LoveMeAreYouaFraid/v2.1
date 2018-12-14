package com.nautilus.ywlfair.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;

/**
 * Created by dingying on 2015/7/13.
 */
public class SecondGoodsClassAdapter extends BaseAdapter {

    private List<GoodsClassInfo> typeList;

    private Context mContext;

    public SecondGoodsClassAdapter(Context context,List<GoodsClassInfo> typeList){

        this.mContext = context;

        this.typeList = typeList;
    }

    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public Object getItem(int i) {
        return typeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView textView = new TextView(mContext);

        textView.setPadding(25,30,25,30);

        textView.setTextSize(16);

        textView.setText(typeList.get(i).getClassName());

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.youjiantou);

        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());

        textView.setCompoundDrawables(null,null,drawable,null);

        return textView;
    }
}
