package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nautilus.ywlfair.R;

/**
 * Created by Administrator on 2016/5/14.
 */
public class PayMethodMenuDialog extends DialogFragment {

    private static PayMethodMenuDialog mApplication;

    private static CheckPayMethodListener checkPayMethodListener;

    public static synchronized PayMethodMenuDialog getInstance(CheckPayMethodListener listener) {
        if (mApplication == null) {
            mApplication = new PayMethodMenuDialog();

            checkPayMethodListener = listener;

            mApplication.setCancelable(true);

        }
        return mApplication;
    }

    public interface CheckPayMethodListener {

        void onCheckMethod(int checkPosition);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dlg_sex);

        TextView title = (TextView) dialog.findViewById(R.id.tv_title);

        title.setText("请选择支付方式");

        TextView first = (TextView) dialog.findViewById(R.id.tv_main);
        first.setText("支付宝");

        TextView second = (TextView) dialog.findViewById(R.id.tv_lady);
        second.setText("微信");

        final CheckBox firstCheck = (CheckBox) dialog.findViewById(R.id.ck_man);

        final CheckBox secondCheck = (CheckBox) dialog.findViewById(R.id.ck_lady);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstCheck.setChecked(true);

                secondCheck.setChecked(false);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstCheck.setChecked(false);

                secondCheck.setChecked(true);
            }
        });


        dialog.findViewById(R.id.tv_determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstCheck.isChecked()) {
                    if (checkPayMethodListener != null)
                        checkPayMethodListener.onCheckMethod(0);

                } else {
                    if (checkPayMethodListener != null)
                        checkPayMethodListener.onCheckMethod(1);
                }

                mApplication.dismissAllowingStateLoss();
            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplication.dismissAllowingStateLoss();
            }
        });

        return dialog;
    }

}