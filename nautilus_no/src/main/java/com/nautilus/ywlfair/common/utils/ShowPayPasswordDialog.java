package com.nautilus.ywlfair.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.module.launch.register.RegisterActivity;
import com.nautilus.ywlfair.module.mine.wallet.PayPasswordStatusActivity;
import com.nautilus.ywlfair.widget.passwordview.OnPasswordInputFinish;
import com.nautilus.ywlfair.widget.passwordview.PassView;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ShowPayPasswordDialog implements OnPasswordInputFinish {

    private Dialog passwordDialog;

    private static ShowPayPasswordDialog instance;

    private Context mContext;

    private PassView passView;

    public static ShowPayPasswordDialog getInstance() {
        if (instance == null) {

            instance = new ShowPayPasswordDialog();
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

        passwordDialog = new Dialog(mContext, R.style.share_dialog);

        passwordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        passView = new PassView(mContext);

        passView.setOnFinishInput(this);

        passwordDialog.setContentView(passView);

        Window window = passwordDialog.getWindow();

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        passwordDialog.setCanceledOnTouchOutside(true);
        /* 设置位置 */
        window.setGravity(Gravity.BOTTOM);
		/* 设置动画 */
        window.setWindowAnimations(R.style.umeng_socialize_shareboard_animation);

        passwordDialog.show();
    }

    @Override
    public void inputFinish() {
        if (mContext instanceof PasswordInputListener) {

            ((PasswordInputListener) mContext).onInputFinished(passView.getStrPassword());

            clearText();
        }
    }

    @Override
    public void forgetPassword(){
        Intent phone = new Intent(mContext, RegisterActivity.class);

        phone.putExtra(Constant.KEY.NAME,"找回支付密码");

        phone.putExtra(Constant.KEY.TYPE, 4);

        mContext.startActivity(phone);

        disMissDialog();
    }

    public interface PasswordInputListener {

        void onInputFinished(String password);
    }

    public void disMissDialog(){

        if(passwordDialog != null && passwordDialog.isShowing()){
            passwordDialog.dismiss();
        }
    }

    public void clearText() {
        if (passView != null)
            passView.clearText();
    }
}
