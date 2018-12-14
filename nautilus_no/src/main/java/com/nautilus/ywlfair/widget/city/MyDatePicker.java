package com.nautilus.ywlfair.widget.city;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.widget.city.ScrollerNumberPicker.OnSelectListener;
import java.util.ArrayList;

/**
 * 城市Picker
 *
 * @author zihao
 */
public class MyDatePicker extends LinearLayout {
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;

    private ArrayList<String> years;
    private ArrayList<String> months;

    private String city_string;

    public MyDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDates();
        // TODO Auto-generated constructor stub
    }

    public MyDatePicker(Context context) {
        super(context);
        initDates();
        // TODO Auto-generated constructor stub
    }

    private void initDates() {
        years = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String year = (2006 + i) + "";

            years.add(year);
        }

        months = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                months.add("0" + i);
            } else {
                months.add(i + "");
            }

        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        provincePicker.setData(years);
        cityPicker.setData(months);

        provincePicker.setOnSelectListener(new OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {
                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;

                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }

                tempProvinceIndex = id;

                Message message = new Message();
                message.what = REFRESH_VIEW;

                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        cityPicker.setOnSelectListener(new OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }

                temCityIndex = id;

                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_string() {

        city_string = provincePicker.getSelectedText()
                + "-" + cityPicker.getSelectedText() + "-01";
        return city_string;
    }

    public void setDefaultSelect(String date) {
        String[] strings = date.split("-");

        if (strings.length >= 2) {
            String year = strings[0];

            for (int i = 0; i < years.size(); i++) {
                if (year.equals(years.get(i))) {
                    provincePicker.setDefault(i);
                    break;
                }
            }

            String month = strings[1];

            for (int i = 0; i < months.size(); i++) {
                if (month.equals(months.get(i))) {
                    cityPicker.setDefault(i);
                    break;
                }
            }

        }
    }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }
}
