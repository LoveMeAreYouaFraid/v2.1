package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;

/**
 * Created by Administrator on 2016/5/14.
 */
public class AppConfigDialog extends DialogFragment {

    private static AppConfigDialog mApplication;

    public static synchronized AppConfigDialog getInstance() {
        if (mApplication == null) {
            mApplication = new AppConfigDialog();

            mApplication.setCancelable(false);

        }
        return mApplication;
    }

    public interface LoginInputListener {

        void onLoginInputComplete(int type);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.my_dalog);

        TextView title = (TextView) dialog.findViewById(R.id.tv_title);

        TextView tvLeft = (TextView) dialog.findViewById(R.id.tv_left);

        TextView tvRight = (TextView) dialog.findViewById(R.id.tv_right);

        title.setText("初始化请求失败，请点击重试或切换网络后尝试再次连接");

        tvLeft.setText("重试");

        tvRight.setText("退出");

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginInputListener listener = (LoginInputListener) getActivity();
                listener.onLoginInputComplete(1);
                dismissAllowingStateLoss();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                dismissAllowingStateLoss();
            }
        });

        return dialog;
    }


}