package com.sjtubus.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyDateUtils {

    /*
     * 获取昨天的开始时间。如2018-07-18调用，返回2018-07-17 00:00:00
     */
    public static Date getBeginOfDay(String datestr) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(datestr);
        } catch (Exception e){
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /* 同上，但返回 2018-07-17 */
    public static String getYesterdayStr(String datestr) {
//        Calendar cal = new GregorianCalendar();
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        return StringCalendarUtils.CalendarToString(cal);
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(datestr));
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return StringCalendarUtils.CalendarToString(cal);
    }

    public static String getTomorrowStr(String datestr) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(datestr));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return StringCalendarUtils.CalendarToString(cal);
    }

    /* 判断所给的日期是否在将来一周以内 */
    public static boolean isWithinOneWeek(String datestr){
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(StringCalendarUtils.getCurrrentDate()));
        cal.add(Calendar.WEEK_OF_MONTH, 1);
        String oneWeekLater = StringCalendarUtils.CalendarToString(cal);

        return StringCalendarUtils.isBeforeDateOfSecondPara(datestr, oneWeekLater);
    }

    public static boolean isWithinLastOneWeek(String datestr){
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(StringCalendarUtils.getCurrrentDate()));
        cal.add(Calendar.WEEK_OF_MONTH, -1);
        String oneWeekBefore = StringCalendarUtils.CalendarToString(cal);

        return StringCalendarUtils.isBeforeDateOfSecondPara(oneWeekBefore, datestr);
    }

    public static boolean isWithinLastOneMonth(String datestr){
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(StringCalendarUtils.getCurrrentDate()));
        cal.add(Calendar.MONTH, -1);
        String oneMonthBefore = StringCalendarUtils.CalendarToString(cal);

        return StringCalendarUtils.isBeforeDateOfSecondPara(oneMonthBefore, datestr);
    }

    public static boolean isWithinLastThreeMonth(String datestr){
        Calendar cal = new GregorianCalendar();
        cal.setTime(getBeginOfDay(StringCalendarUtils.getCurrrentDate()));
        cal.add(Calendar.MONTH, -3);
        String threeMonthBefore = StringCalendarUtils.CalendarToString(cal);

        return StringCalendarUtils.isBeforeDateOfSecondPara(threeMonthBefore, datestr);
    }

    public static String addHalfHour(String datestr){
        Calendar cal = StringCalendarUtils.StringTimeToCalendar(datestr);
        cal.add(Calendar.MINUTE, 30);

        return StringCalendarUtils.CalendarToStringTime(cal);
    }

    public static String minusHalfHour(String datestr){
        Calendar cal = StringCalendarUtils.StringTimeToCalendar(datestr);
        cal.add(Calendar.MINUTE, -30);

        return StringCalendarUtils.CalendarToStringTime(cal);
    }

    /* 由calendar判断是否是双休日 */
    public static boolean isWeekend(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return (day == Calendar.SATURDAY || day == Calendar.SUNDAY);
    }
    /* 由calendar判断是否是节假日 是否是二月，七月，八月 */
    public static boolean isHoilday(Calendar calendar){
        int month = calendar.get(Calendar.MONTH);
        return (month == Calendar.FEBRUARY || month == Calendar.AUGUST
                || month == Calendar.JULY);
    }

    public static String isLegalHoliday(String datestr){
        Calendar calendar = StringCalendarUtils.StringToCalendar(datestr);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        LunarUtils lunar = LunarUtils.getLunarDate(datestr);
        String lunar_month = LunarUtils.getChinaMonthString(LunarUtils.getLunarMonth(lunar));
        String lunar_day = LunarUtils.getChinaDayString(LunarUtils.getLunarDay(lunar));

        if (month == Calendar.JANUARY && day == 1){
            return "元旦";
        } else if (lunar_month.equals("腊月") && lunar_day.equals("三十")){
            return "除夕";
        } else if (lunar_month.equals("正月") && (lunar_day.equals("初一") || lunar_day.equals("初二"))){
            return "春节";
        } else if (month == Calendar.APRIL && day == 5){
            return "清明";
        } else if (month == Calendar.MAY && day == 1){
            return "劳动节";
        } else if (lunar_month.equals("五月") && lunar_day.equals("初五")){
            return "端午";
        } else if (lunar_month.equals("八月") && lunar_day.equals("十五")){
            return "中秋";
        } else if (month == Calendar.OCTOBER && (day >= 1 && day <= 3)){
            return "国庆";
        } else {
            return "无";
        }
    }

    public static void main(String[] args) {
        String datestr = "2018-06-18 00:15:00";
        System.out.print(minusHalfHour(datestr));
    }
}
