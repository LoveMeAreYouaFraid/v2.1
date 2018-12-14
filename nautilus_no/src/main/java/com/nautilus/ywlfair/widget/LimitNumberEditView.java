package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.nautilus.ywlfair.R;

/**
 * Created by Administrator on 2016/4/25.
 */
public class LimitNumberEditView extends EditText {

    private int limitNum = 0;

    public LimitNumberEditView(Context context) {
        super(context);

        addTextChangedListener(textWatcher);
    }

    public LimitNumberEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        addTextChangedListener(textWatcher);
    }

    public LimitNumberEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LimitNumberEditView, defStyleAttr, 0);

        for (int i = 0; i < typedArray.length(); i++){
            int attr = typedArray.getIndex(i);

            switch (attr){
                case R.styleable.LimitNumberEditView_limitNum:
                    limitNum = typedArray.getInteger(i,0);
                    break;

            }
        }

        addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputText = getText().toString();

            if (inputText.length() > limitNum) {
                setText(inputText.substring(0, limitNum));

                setSelection(limitNum);
            }
        }
    };
}
