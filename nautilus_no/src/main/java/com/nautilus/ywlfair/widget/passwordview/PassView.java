package com.nautilus.ywlfair.widget.passwordview;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nautilus.ywlfair.R;

public class PassView extends LinearLayout implements OnClickListener {

    private TextView[] tvList;
    private TextView[] tv;
    private ImageView iv_del;
    private View view;
    private String strPassword;
    private int currentIndex = -1;

    public PassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public PassView(final Context context) {
        super(context);
        view = View.inflate(context, R.layout.item_paypassword, null);
        tvList = new TextView[6];
        tvList[0] = (TextView) view.findViewById(R.id.pay_box1);
        tvList[1] = (TextView) view.findViewById(R.id.pay_box2);
        tvList[2] = (TextView) view.findViewById(R.id.pay_box3);
        tvList[3] = (TextView) view.findViewById(R.id.pay_box4);
        tvList[4] = (TextView) view.findViewById(R.id.pay_box5);
        tvList[5] = (TextView) view.findViewById(R.id.pay_box6);
        tv = new TextView[10];
        tv[0] = (TextView) view.findViewById(R.id.pay_keyboard_zero);
        tv[1] = (TextView) view.findViewById(R.id.pay_keyboard_one);
        tv[2] = (TextView) view.findViewById(R.id.pay_keyboard_two);
        tv[3] = (TextView) view.findViewById(R.id.pay_keyboard_three);
        tv[4] = (TextView) view.findViewById(R.id.pay_keyboard_four);
        tv[5] = (TextView) view.findViewById(R.id.pay_keyboard_five);
        tv[6] = (TextView) view.findViewById(R.id.pay_keyboard_sex);
        tv[7] = (TextView) view.findViewById(R.id.pay_keyboard_seven);
        tv[8] = (TextView) view.findViewById(R.id.pay_keyboard_eight);
        tv[9] = (TextView) view.findViewById(R.id.pay_keyboard_nine);
        iv_del = (ImageView) view.findViewById(R.id.pay_keyboard_del);

        for (int i = 0; i <= 9; i++) {
            tv[i].setOnClickListener(this);
        }

        iv_del.setOnClickListener(this);

        addView(view);

    }

    public void setOnFinishInput(final OnPasswordInputFinish pass) {
        tvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {
                    strPassword = "";
                    for (int i = 0; i < 6; i++) {
                        strPassword += tvList[i].getText().toString().trim();
                    }
                    pass.inputFinish();
                }
            }
        });

        findViewById(R.id.tv_forget_pwd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pass.forgetPassword();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_keyboard_one:
                getPass("1");
                break;
            case R.id.pay_keyboard_two:
                getPass("2");
                break;
            case R.id.pay_keyboard_three:
                getPass("3");
                break;
            case R.id.pay_keyboard_four:
                getPass("4");
                break;
            case R.id.pay_keyboard_five:
                getPass("5");
                break;
            case R.id.pay_keyboard_sex:
                getPass("6");
                break;
            case R.id.pay_keyboard_seven:
                getPass("7");
                break;
            case R.id.pay_keyboard_eight:
                getPass("8");
                break;
            case R.id.pay_keyboard_nine:
                getPass("9");
                break;
            case R.id.pay_keyboard_zero:
                getPass("0");
                break;
            case R.id.pay_keyboard_del:
                if (currentIndex - 1 >= -1) {
                    tvList[currentIndex--].setText("");
                }
                break;
        }
    }

    public void getPass(String str) {
        if (currentIndex >= -1 && currentIndex < 5) {
            tvList[++currentIndex].setText(str);
        }
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void clearText() {
        strPassword = "";

        for (int i = 0; i < 6; i++) {
            tvList[i].setText("");
        }

        currentIndex = -1;
    }

}
