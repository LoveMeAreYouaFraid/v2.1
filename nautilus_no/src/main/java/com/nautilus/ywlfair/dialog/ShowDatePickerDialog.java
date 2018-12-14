package com.nautilus.ywlfair.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.widget.city.MyDatePicker;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ShowDatePickerDialog {

    private Dialog dateDialog;

    private static ShowDatePickerDialog instance;

    private MyDatePicker datePicker1, datePicker2;

    private Context mContext;

    public static ShowDatePickerDialog getInstance() {
        if (instance == null) {

            instance = new ShowDatePickerDialog();
        }

        return instance;
    }

    public void initMenuDialog(final Context context, String startTime, String endTime) {
        if (context == this.mContext && dateDialog != null) {

            if (!dateDialog.isShowing()) {

                setDefaultValue(startTime, endTime);

                dateDialog.show();

            }
            return;

        }

        mContext = context;

        dateDialog = new Dialog(mContext, R.style.dialog);

        dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dateDialog.setContentView(R.layout.date_picker_dialog);

        dateDialog.setCanceledOnTouchOutside(true);

        Window window = dateDialog.getWindow();
        /* 设置位置 */
        window.setGravity(Gravity.BOTTOM);
		/* 设置动画 */
        window.setWindowAnimations(R.style.umeng_socialize_shareboard_animation);

        datePicker1 = (MyDatePicker) dateDialog.findViewById(R.id.date_picker1);

        datePicker2 = (MyDatePicker) dateDialog.findViewById(R.id.date_picker2);

        setDefaultValue(startTime, endTime);

        dateDialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof OnDateSelectedListener){

                    String startTime = datePicker1.getCity_string();

                    String endTime = datePicker2.getCity_string();

                    ((OnDateSelectedListener)context).onSelected(startTime,endTime);
                }

                dateDialog.cancel();
            }
        });

        dateDialog.show();
    }

    private void setDefaultValue(String startTime, String endTime){
        datePicker1.setDefaultSelect(startTime);

        datePicker2.setDefaultSelect(endTime);
    }

    public interface OnDateSelectedListener{
        void onSelected(String startTime, String endTime);
    }

}
