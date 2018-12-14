package com.nautilus.ywlfair.common.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.nautilus.ywlfair.common.MyApplication;

/**
 * Created by DingYing on 2015/9/25.
 */
public class ToastUtil {
    private static Toast mToast = null;

    public static void showShortToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);

        }
        mToast.setGravity(Gravity.CENTER, 0, 0);//将toast显示在activity中间，因为显示在下面，有时会被软键盘挡住
        mToast.show();
    }

    public static void showLongToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
