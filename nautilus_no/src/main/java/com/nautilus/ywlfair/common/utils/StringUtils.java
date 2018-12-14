package com.nautilus.ywlfair.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.mapapi.model.LatLng;
import com.nautilus.ywlfair.widget.city.CityInfo;
import com.nautilus.ywlfair.widget.city.CityPicker.JSONParser;
import com.nautilus.ywlfair.widget.city.FileUtil;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 双经纬拆分出
     */
    public static LatLng gps(String gps) {

        LatLng latLng = null;
        double x = 0, y = 0;
        if (!TextUtils.isEmpty(gps)) {

            String[] ab = gps.split(";");

            if (ab.length > 0) {
                String a = ab[0];

                String[] b = a.split(",");

                if (b.length > 1) {

                    x = Double.valueOf(b[0]);
//121.469729,31.216707
                    y = Double.valueOf(b[1]);


                    if (x > 360 || y > 360 || x < 0 || y < 0) {
                        ToastUtil.showLongToast("服务器地址配置不正确");
                        y = 31.216707;
                        x = 121.469729;
                    }

                }
                latLng = new LatLng(y, x);
            }
        }

        return latLng;
    }

    /**
     * 手机号判断
     *
     * @param string
     * @return
     */
    public static boolean isMobileNumber(String string) {

        if (TextUtils.isEmpty(string))
            return false;
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(string);
        return m.matches();
    }

    /**
     * 判断邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 判断姓名
     */
    public static boolean isFormatName(String name) {

        // 允许汉字、字母及数字
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5a-zA-z\\d]+");

        return pattern.matcher(name).matches();
    }

    /**
     * 根据服务器返回的城市编码 返回城市名称
     */
    public static String getCityName(Context context, String cityCode) {
        if (!TextUtils.isEmpty(cityCode)) {
            String[] codes = cityCode.split(",");

            String province = "", city = "";
            if (codes.length > 1) {

                JSONParser parser = new JSONParser();
                String area_str = FileUtil.readAssets(context, "area.json");
                List<CityInfo> province_list = parser.getJSONParserResult(
                        area_str, "area0");

                for (CityInfo cityInfo : province_list) {
                    if (cityInfo.getId().equals(codes[0])) {
                        province = cityInfo.getCity_name();
                        break;
                    }
                }

                List<CityInfo> city_list = parser.getJSONParserResultArray(
                        area_str, "area1").get(codes[0]);

                for (CityInfo cityInfo : city_list) {
                    if (cityInfo.getId().equals(codes[1])) {
                        city = cityInfo.getCity_name();

                        break;
                    }
                }
            }
            return province + city;
        } else {
            return "暂无城市信息";
        }
    }

    /*
    统计字符串中的数字 字母 汉字 个数
     */
    public static int countNumber(String str) {
        int count = 0;
        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher(str);
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static int countLetter(String str) {
        int count = 0;
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(str);
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static int countChinese(String str) {
        int count = 0;
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        while (m.find()) {
            count++;
        }
        return count * 2;
    }

    public static String getProtectedPhone(String phone){

        if(TextUtils.isEmpty(phone) || phone.length() < 11){
            return "";
        }

        String a = phone.substring(0, 3);

        String b = phone.substring(7, 11);

        return a + "****" + b;
    }

    public static String getProtectedEmail(String mail){
        String string = mail.substring(0, mail.indexOf("@"));

        String last = mail.substring(mail.indexOf("@"));

        int max = string.length();

        String s = string.substring(0, 3);

        for (int i = 0; i < max - 3; i++){
            s = s + "*";
        }

        return s + last;
    }

    public static boolean isZipNO(String zipString){//判断邮政编码
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    public static String getCourierFeeString(double courierFee){
        if(courierFee == 0){
            return "免运费";
        }else{
            return String.valueOf(courierFee);
        }
    }

    public static String getUrlParameter(String url,String key){
        Map<String,String> map=new HashMap<String, String>();
        int index=url.indexOf("?");
        String queryString = index==-1?null:url.substring(index+1,url.length());
        if (queryString!=null&&queryString.length()>0) {
            String[] split = queryString.split("&");
            for (String string : split) {
                String[] params = string.split("=");
                if(params.length > 1)
                    map.put(params[0], params[1]);
            }
        }

        return map.get(key);
    }

    /**
     * 提取字符串中的数字
     */
    public static String getNumberFromString(String string){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);

        return m.replaceAll("").trim();
    }

    /**
     * 金额格式化
     */
    public static String getMoneyFormat(double amount){
        DecimalFormat format = new DecimalFormat("###,###,###.00");//使用系统默认的格式

        double absValue = Math.abs(amount);

        String formatString = format.format(absValue);

        if(absValue < 1){
            formatString = "0" + formatString;
        }

        if(amount < 0){
            formatString = "-" + formatString;
        }

        return formatString;
    }

    /**
     * 积分格式化
     */
    public static String getScoreFormat(double amount){
        DecimalFormat format = new DecimalFormat("###,###,###");//使用系统默认的格式

        String formatString = format.format(amount);
        if(amount > 0 && amount < 1){
            formatString = "0" + formatString;
        }
        return formatString;
    }

    /**
     * string 转long
     */
    public static long getLongValueFromString(String string){
        try{
            return Long.valueOf(string);

        }catch(Exception e){
            return 0;
        }
    }
}
