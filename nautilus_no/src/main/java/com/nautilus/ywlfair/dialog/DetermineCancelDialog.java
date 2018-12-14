package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
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
import com.nautilus.ywlfair.widget.city.MyDatePicker;

/**
 * Created by Administrator on 2016/6/28.
 */

public class DetermineCancelDialog {

    private static DetermineCancelDialog mApplication;

    private Context mContext;

    private static DialogListener dialogListener;

    public static synchronized DetermineCancelDialog getInstance() {
        if (mApplication == null) {
            mApplication = new DetermineCancelDialog();
        }

        return mApplication;
    }


    public interface DialogListener {

        void onItemChoice(int position);
    }

    public void showDialog(Context context, String title, String left, String right) {

        mContext = context;

        final Dialog dialog = new Dialog(context, R.style.dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_confirm);

        dialog.setCanceledOnTouchOutside(true);

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvLeft = (TextView) dialog.findViewById(R.id.tv_left);
        TextView tvRight = (TextView) dialog.findViewById(R.id.tv_right);

        tvTitle.setText(title);
        tvLeft.setText(left);
        tvRight.setText(right);

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogListener dialogListener = (DialogListener) mContext;
                dialogListener.onItemChoice(0);
                dialog.cancel();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogListener dialogListener = (DialogListener) mContext;
                dialogListener.onItemChoice(1);
                dialog.cancel();
            }
        });

        dialog.show();
    }


}
