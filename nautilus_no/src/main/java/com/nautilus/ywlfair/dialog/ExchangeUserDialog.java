package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nautilus.ywlfair.R;

/**
 * Created by Administrator on 2016/5/14.
 */
public class ExchangeUserDialog extends DialogFragment {

    private static ExchangeUserDialog mApplication;

    public static synchronized ExchangeUserDialog getInstance() {
        if (mApplication == null) {
            mApplication = new ExchangeUserDialog();

            mApplication.setCancelable(true);

        }
        return mApplication;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.transcutestyle);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.exchange_user_dialog);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.exchange_pic);

        ((AnimationDrawable)imageView.getDrawable()).start();

        return dialog;
    }

    public void disMissDialog(){

        if(mApplication.getDialog() != null && mApplication.getDialog().isShowing()){
            mApplication.dismissAllowingStateLoss();
        }
    }

    public void showDialog(FragmentManager fragmentManager){
        if(!mApplication.isAdded() && !mApplication.isVisible() && !mApplication.isRemoving()){
            mApplication.show(fragmentManager,"");
        }
    }

    @Override
    public void onPause() {

        disMissDialog();

        super.onPause();
    }
}