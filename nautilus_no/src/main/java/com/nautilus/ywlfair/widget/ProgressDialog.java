package com.nautilus.ywlfair.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.MyApplication;

/**
 * Copyright (©) 2014
 * <p/>
 * 正在处理的半透明对话框
 *
 * @author Dongxiang
 * @version 1.0, 6/13/14 14:30
 * @since 5/13/14
 */
public class ProgressDialog {

    private static final String TAG = ProgressDialog.class.getSimpleName();

    private static ProgressDialog instance = new ProgressDialog();

    private ProgressDialog() {
    }

    /**
     * @return ProgressDialog
     */
    public static ProgressDialog getInstance() {
        return instance;
    }

    private Dialog mProgressDialog;

    private Context mContext;

    /**
     * @param context
     * @param msg
     */
    public void show(Context context, String msg) {

        show(context, msg, true);

    }

    /**
     * @param context
     * @param strId
     */
    public void show(Context context, int strId) {

        show(context, MyApplication.getInstance().getString(strId), true);

    }

    /**
     * @param context
     * @param msg
     */
    public void show(Context context, String msg, boolean isCancelable) {

        mContext = context;

        if(!isValidContext(mContext)){
            return;
        }

        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }

        mProgressDialog = new Dialog(context, R.style.dialog);

        View view = LayoutInflater.from(context).inflate(
                R.layout.dlg_progress, null);

        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        tvInfo.setText(msg);

        mProgressDialog.setContentView(view);
        if (context != null) {
            mProgressDialog.show();
        }

        mProgressDialog.setCancelable(isCancelable);

    }

    /**
     * @param listener
     */
    public void setOnCancelListener(OnCancelListener listener) {
        mProgressDialog.setOnCancelListener(listener);
    }

    /**
     * dismiss
     */
    public void cancel() {

        if (mContext != null && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    private boolean isValidContext(Context c) {

        Activity a = (Activity) c;

        if (a == null || a.isFinishing()) {
            return false;
        } else {
            return true;
        }
    }

}

