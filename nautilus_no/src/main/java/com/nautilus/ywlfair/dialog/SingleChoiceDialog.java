package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;

/**
 * Created by Administrator on 2016/5/14.
 */
public class SingleChoiceDialog {

    private static SingleChoiceDialog mApplication;

    private String[] data = new String[]{"个人", "公司"};

    private Context mContext;

    public static synchronized SingleChoiceDialog getInstance() {
        if (mApplication == null) {
            mApplication = new SingleChoiceDialog();
        }

        return mApplication;
    }

    public interface ItemChoiceListener {

        void onItemChoice(int position);
    }

    public void showDialog(Context context,String[] dataSet) {

        data = dataSet;

        mContext = context;

        final Dialog dialog = new Dialog(context, R.style.dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.single_choice_dialog);

        dialog.setCanceledOnTouchOutside(true);

        TextView title = (TextView) dialog.findViewById(R.id.tv_title);

        ListView listView = (ListView) dialog.findViewById(R.id.item_list);

        listView.setAdapter(new MyAdapter());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mContext instanceof ItemChoiceListener) {

                    ItemChoiceListener itemChoiceListener = (ItemChoiceListener)mContext;

                    itemChoiceListener.onItemChoice(position);

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.length;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.single_choice_item, null);

            view.setMinHeight(BaseInfoUtil.dip2px(40));

            view.setText(data[position]);

            return view;
        }
    }
}