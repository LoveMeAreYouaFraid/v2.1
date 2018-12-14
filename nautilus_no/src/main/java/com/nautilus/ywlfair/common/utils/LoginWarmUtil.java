package com.nautilus.ywlfair.common.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.module.launch.LoginActivity;

/**
 * Created by Administrator on 2015/12/4.
 */
public class LoginWarmUtil {

    private AlertDialog loginDialog;

    private static LoginWarmUtil instance;

    public static LoginWarmUtil getInstance() {
        if (instance != null) {
            instance = null;
        }

        instance = new LoginWarmUtil();

        return instance;
    }

    public boolean checkLoginStatus(Context context) {

        if (!MyApplication.getInstance().isLogin()) {
            Intent intent = new Intent(context, LoginActivity.class);

            intent.putExtra(Constant.KEY.MODE, LoginActivity.Mode.PASSIVE);

            context.startActivity(intent);

            return false;
        } else {
            return true;
        }
    }

    /**
     * 用户若未登录，显示登录对话框
     */
    public void showLoginAlert(final Context context) {
        if (loginDialog != null) {
            loginDialog.show();
        } else {
            loginDialog = new AlertDialog.Builder(context, R.style.dialog).create();

            loginDialog.show();

            Window window = loginDialog.getWindow();

            window.setContentView(R.layout.dlg_unlogin);

            TextView loginTextView = (TextView) window
                    .findViewById(R.id.tv_login);

            loginTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            LoginActivity.class);

                    intent.putExtra(
                            Constant.KEY.MODE,
                            LoginActivity.Mode.PASSIVE);

                    context.startActivity(intent);

                    loginDialog.cancel();
                }
            });

            TextView cancelSchoolTextView = (TextView) window
                    .findViewById(R.id.tv_cancel);
            cancelSchoolTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    loginDialog.cancel();
                }
            });
        }
    }
}
