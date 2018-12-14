package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nautilus.ywlfair.R;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ShowUserConfirmDialog {

    private Dialog passwordDialog;

    private static ShowUserConfirmDialog instance;

    private Context mContext;

    private String[] strings = {"需先绑定手机号码再设置支付密码，现在就去绑定吗？", "现在就去", "以后再说"};

    public static ShowUserConfirmDialog getInstance() {
        if (instance == null) {

            instance = new ShowUserConfirmDialog();
        }

        return instance;
    }


    public void initMenuDialog(Context context) {
        if (context == this.mContext && passwordDialog != null) {

            if (!passwordDialog.isShowing()) {

                passwordDialog.show();

            }
            return;

        }

        mContext = context;

        passwordDialog = new Dialog(mContext, R.style.dialog);

        passwordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        passwordDialog.setContentView(R.layout.my_dalog);

        TextView title = (TextView) passwordDialog.findViewById(R.id.tv_title);

        TextView tvLeft = (TextView) passwordDialog.findViewById(R.id.tv_left);

        TextView tvRight = (TextView) passwordDialog.findViewById(R.id.tv_right);

        title.setText(strings[0]);

        tvLeft.setText(strings[1]);

        tvRight.setText(strings[2]);

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof OnUserSelectListener){
                    ((OnUserSelectListener)mContext).onSelect(true);
                }

                passwordDialog.cancel();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mContext instanceof OnUserSelectListener){
                    ((OnUserSelectListener)mContext).onSelect(false);
                }

                passwordDialog.cancel();

            }
        });

        passwordDialog.show();
    }

}
