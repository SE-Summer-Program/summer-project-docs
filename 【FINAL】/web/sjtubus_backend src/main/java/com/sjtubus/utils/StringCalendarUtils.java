package com.sjtubus.utils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StringCalendarUtils {

    /* 由 2018-07-17 格式的字符串获取 calendar */
    public static Calendar StringToCalendar(String datestr){
        Calendar calendar = new GregorianCalendar();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(datestr); //start_date是类似"2013-02-02"的字符串
            calendar.setTime(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return calendar;
    }

    public static java.util.Date StringToDate(String datestr){
        java.util.Date date= new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("date:"+date);
        return date;
    }

    public static java.util.Date StringToTime(String timestr){
        java.util.Date date= new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = dateFormat.parse(timestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String DateToString(java.util.Date date){
//        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String date_str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date_str = sdf.format(date);
        return date_str;
    }

    public static String TimeToString(Time time){
        String time_str;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        time_str = sdf.format(time);
        return time_str;
    }

    /**
     * @description: 比较某个时间和现在时间的先后
     * @date: 2018/07/16 13:57
     * @params: string time1， string time2
     * @return: 如果time1在time2之前，返回true
     */
    public static boolean isBeforeCurrentTime(String datetime) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        java.util.Date current = new java.util.Date();

        try {
            date = timeFormat.parse(datetime);
            current = timeFormat.parse(timeFormat.format(new java.util.Date()));
        } catch (Exception e){
            e.printStackTrace();
        }

        return date.before(current);
    }

    public static String getCurrentDate(){
        String current_date="";
        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        current_date=simpleDateFormat.format(date);
        return current_date;
    }

    /* 获取当天的时间 格式 2018-07-18 19:35:00 */
    public static String getCurrentTime(){
        String current_time;
        Date date = new Date();
        //最后的aa表示“上午”或“下午” HH表示24小时制  如果换成hh表示12小时制
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        current_time=simpleDateFormat.format(date);
        return current_time;
    }

    /* java.util.date转化成java.sql.date */
    public static java.sql.Date UtilDateToSqlDate(java.util.Date util_date){
        return new java.sql.Date(util_date.getTime());
    }

    /* java.sql.date转化成java.util.date */
    public static java.util.Date SqlDateToUtilDate(java.sql.Date sql_date){
        return new Date(sql_date.getTime());
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

    public static String CalendarToStringTime(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置你想要的格式
        return dateFormat.format(calendar.getTime());
    }

    public static Calendar StringTimeToCalendar(String datestr){
        Calendar calendar = new GregorianCalendar();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(datestr); //start_date是类似"2013-02-02"的字符串
            calendar.setTime(date);
        } catch (Exception e){
            e.printStackTrace();
        }
        return calendar;
    }
}
