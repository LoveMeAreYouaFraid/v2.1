package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.widget.passwordview.OnPasswordInputFinish;
import com.nautilus.ywlfair.widget.passwordview.PassView;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ShowPasswordErrorDialog {

    private Dialog passwordDialog;

    private static ShowPasswordErrorDialog instance;

    private Context mContext;

    private TextView title;

    private TextView tvLeft;

    private TextView tvRight;

    private String[] strings1 = {"重新输入", "取消重设"};

    private String[] strings2 = {"关闭", "忘记密码"};

    private String[] strings3 = {"重新输入", "忘记密码"};

    private String[] strings4 = {"立即设置", "暂不设置"};

    private String[] strings5 = {"立即绑定", "取消"};

    public static ShowPasswordErrorDialog getInstance() {
        if (instance == null) {

            instance = new ShowPasswordErrorDialog();
        }

        return instance;
    }


    public void initMenuDialog(Context context, String num, final int type) {
        if (context == this.mContext && passwordDialog != null) {

            if (!passwordDialog.isShowing()) {

                setStatus(num, type);

                passwordDialog.show();

            }
            return;

        }

        mContext = context;

        passwordDialog = new Dialog(mContext, R.style.dialog);

        passwordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        passwordDialog.setContentView(R.layout.my_dalog);

        title = (TextView) passwordDialog.findViewById(R.id.tv_title);

        tvLeft = (TextView) passwordDialog.findViewById(R.id.tv_left);

        tvRight = (TextView) passwordDialog.findViewById(R.id.tv_right);

        setStatus(num, type);

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mContext instanceof OnUserSelectListener) {

                    if(type == 3 || type == 1 || type == 5 || type == 6)
                        ((OnUserSelectListener) mContext).onSelect(true);
                }

                passwordDialog.cancel();
            }
        });

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof OnUserSelectListener) {
                    if(type == 3 || type == 4)//3 4 类型点击右边按钮 才需要抛出选项
                        ((OnUserSelectListener) mContext).onSelect(false);
                }

                passwordDialog.cancel();

            }
        });

        passwordDialog.show();
    }

    private void setStatus(String num, int type) {

        title.setText(num);

        if (type == 3) {//3 .提现输入错误  4.账号冻结 1.修改密码错误 2.重置密码冻结 5.未设置支付密码 6未绑定支付宝

            tvLeft.setText(strings3[0]);

            tvRight.setText(strings3[1]);

        }else if(type == 4){

            tvLeft.setText(strings2[0]);

            tvRight.setText(strings2[1]);

        }else if(type == 1){

            tvLeft.setText(strings1[0]);

            tvRight.setText(strings1[1]);

        }else if(type == 2){
            tvRight.setVisibility(View.GONE);

            tvLeft.setText("确定");
        }else if(type == 5){
            tvLeft.setText(strings4[0]);

            tvRight.setText(strings4[1]);
        }else if(type == 6){
            tvLeft.setText(strings5[0]);

            tvRight.setText(strings5[1]);
        }

    }

}
