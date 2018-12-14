package com.nautilus.ywlfair.common.utils;

import android.annotation.SuppressLint;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class TimeUtil {

    @SuppressLint("SimpleDateFormat")
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    @SuppressLint("SimpleDateFormat")
    public static String getMinAndSecond(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(new Date(time));
    }

    @SuppressLint("SimpleDateFormat")
    public static String getYearAndMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(new Date(time));
    }

    @SuppressLint("SimpleDateFormat")
    public static String getYearMonthAndDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }

    @SuppressLint("SimpleDateFormat")
    public static String getMonthAndDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        return format.format(new Date(time));
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateFormat(long time) {
        String timeString = getYearMonthAndDay(time);
        return timeString.replaceAll("-", ".");
    }

    @SuppressLint("SimpleDateFormat")
    public static String getYearMonthAndDayWithHour(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String convertDateTime2DateStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        return df.format(date);
    }

    public static boolean startTimUtll(Long start) {
        java.util.Date dt = new java.util.Date();//获取当前时间
        Long time = dt.getTime();
        final int liangtian = 172800000;
        if (time + liangtian > start) {
            return false;
        } else {
            return true;

        }

    }

    /**
     * 传入毫秒数 返回HH:mm:ss格式的字符串
     *
     * @param time
     */
    public static String castLastDate(long time) {

        long duration = System.currentTimeMillis() - time;

        if (duration < 2592000000l) {
            long tempweek = duration / 604800000l;
            if (tempweek > 0) {
                return tempweek + "周前";
            }
            long tempday = duration / 86400000l;
            if (tempday > 0) {
                return tempday + "天前";
            }
            long temphour = duration / 3600000l;
            if (temphour > 0) {
                return temphour + "小时前";
            }
            long tempminutes = duration / 60000l;
            if (tempminutes > 0) {
                return tempminutes + "分钟前";
            }
            long tempseconds = duration / 1000l;
            if (tempseconds > 0) {
                return tempseconds + "秒前";
            }
        } else {
            return getYearMonthAndDayWithHour(time);
        }
        return "";
    }

    /**
     * 返回活动日期格式
     */
    public static String getDateFormat(Long start, Long end) {
        String dateFormat = TimeUtil.getYearMonthAndDay(start)
                + " ~ " + TimeUtil.getMonthAndDay(end);

        return dateFormat;
    }

    /**
     * 返回活动时间格式
     * 如果时间一样  则只返回一个
     */
    public static String getTimeFormat(Long start, Long end) {

        String startTime = TimeUtil.getHourAndMin(start);

        String endTime = TimeUtil.getHourAndMin(end);

        if(startTime.equals(endTime)){
            return startTime;
        }

        return startTime + " ~ " + endTime;
    }
}
