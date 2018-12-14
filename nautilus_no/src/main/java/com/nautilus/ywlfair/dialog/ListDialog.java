package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/5/14.
 */
public class ListDialog {

    private static ListDialog mApplication;

    private Context mContext;

    private Dialog dialog;

    private DialogView dv;

    private int listPosition = 0;

    private List<ListInfo> list;

    private boolean isClick = false;

    public static synchronized ListDialog getInstance() {
        mApplication = new ListDialog();
        return mApplication;
    }

    private MyAdapter myAdapter;

    public final static String LEFT_BUTT = "LeftButt";

    public final static String RIGHT_BUTT = "RightButt";

    public interface DialogListener {

        void buttType(String ButtType, String[] list, int pos);
    }


    private static class DialogView {
        TextView tvTitle;
        ListView itemList;
        TextView tvDetermine;
        TextView tvCancel;
        LinearLayout viewDetermineCancel;
    }

    private void getDialogView() {
        dv = new DialogView();
        dialog = new Dialog(mContext, R.style.dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_list_layout);
        dv.tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        dv.itemList = (ListView) dialog.findViewById(R.id.item_list);
        dv.tvDetermine = (TextView) dialog.findViewById(R.id.tv_determine);
        dv.tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        dv.viewDetermineCancel = (LinearLayout) dialog.findViewById(R.id.view_determine_cancel);
    }


    public void showDialog(Context context, String titleString, String leftButtString, String rightButtString) {
        mContext = context;

        getDialogView();
        if (!leftButtString.equals("")) {
            dv.tvDetermine.setText(leftButtString);
        }
        if (!rightButtString.equals("")) {
            dv.tvCancel.setText(rightButtString);
        }
        dv.viewDetermineCancel.setVisibility(View.VISIBLE);

        dv.tvDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListener itemChoiceListener = (DialogListener) mContext;
                itemChoiceListener.buttType(LEFT_BUTT, null, 0);
                dialog.dismiss();
            }
        });
        dv.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListener itemChoiceListener = (DialogListener) mContext;
                itemChoiceListener.buttType(RIGHT_BUTT, null, 0);
                dialog.dismiss();
            }
        });
        dv.tvTitle.setText(titleString);
        dialog.show();
    }


    public void showListDialog(Context context, final String[] dataSet, String titleString, String leftButtString, String rightButtString) {

        list = new ArrayList<>();

        mContext = context;

        getDialogView();
        for (int i = 0; i < dataSet.length; i++) {
            ListInfo listInfo = new ListInfo();
            listInfo.setName(dataSet[i]);
            if (i == 0) {
                listInfo.setImgType(1);
            } else {
                listInfo.setImgType(0);
            }

            list.add(listInfo);
        }
        dv.tvTitle.setText(titleString);
        myAdapter = new MyAdapter(list);
        dv.itemList.setAdapter(myAdapter);
        if (!leftButtString.equals("")) {
            dv.tvDetermine.setText(leftButtString);
        }
        if (!rightButtString.equals("")) {
            dv.tvCancel.setText(rightButtString);
        }
        dialog.findViewById(R.id.view_determine_cancel).setVisibility(View.VISIBLE);
        dv.tvDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListener itemChoiceListener = (DialogListener) mContext;

                itemChoiceListener.buttType(LEFT_BUTT, dataSet, listPosition);

                dialog.dismiss();
            }
        });

        dv.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListener itemChoiceListener = (DialogListener) mContext;

                itemChoiceListener.buttType(RIGHT_BUTT, dataSet, listPosition);

                dialog.dismiss();
            }
        });

        dv.itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                isClick = true;
                for (int i = 0; i < list.size(); i++) {

                    list.get(i).setImgType(0);
                }
                list.get(position).setImgType(1);

                listPosition = position;

                myAdapter.notifyDataSetChanged();

            }
        });

        dialog.show();
    }

    private class MyAdapter extends BaseAdapter {

        List<ListInfo> mlist;

        private MyAdapter(List<ListInfo> list) {
            mlist = list;
        }

        @Override
        public int getCount() {
            return mlist.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_view_dialog_item, null);

            TextView name = (TextView) convertView.findViewById(R.id.tv_item_name);

            ImageView type = (ImageView) convertView.findViewById(R.id.tv_item_type);
            if (mlist.get(position).getImgType() == 0) {
                type.setImageResource(R.drawable.img_ck_no);
            } else {
                type.setImageResource(R.drawable.img_ck_yes);
            }

            name.setText(mlist.get(position).getName());

            return convertView;
        }
    }

    public class ListInfo {
        String name;
        int imgType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImgType() {
            return imgType;
        }

        public void setImgType(int imgType) {
            this.imgType = imgType;
        }
    }

}