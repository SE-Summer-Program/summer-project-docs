package com.sjtubus.utils;

import java.util.Calendar;
import java.util.HashMap;

/*
 * 已经不考虑节假日的问题了
 */

public class ShiftUtils {

    private static String[] line_list = {"闵行到徐汇", "徐汇到闵行", "闵行到七宝", "七宝到闵行"};
    private static String[] line_list_E = {"MinHangToXuHui", "XuHuiToMinHang", "MinHangToQiBao", "QiBaoToMinHang"};

    private static String[] type_list = {"在校期-工作日", "在校期-双休日/节假日", "寒暑假-工作日","寒暑假-双休日"};
    private static String[] type_list_E = {"NormalWorkday","NormalWeekendAndLegalHoliday","HolidayWorkday","HolidayWeekend"};

    private static HashMap<String,String> type_engTochi = new HashMap<>();
    private static HashMap<String,String> type_chiToeng = new HashMap<>();

    private static HashMap<String,String> name_engTochi = new HashMap<>();
    private static HashMap<String,String> name_chiToeng = new HashMap<>();

    static {
        type_engTochi.put("NormalWorkday","在校期-工作日");
        type_engTochi.put("NormalWeekendAndLegalHoliday","在校期-双休日/节假日");
        type_engTochi.put("HolidayWorkday","寒暑假-工作日");
        type_engTochi.put("HolidayWeekend","寒暑假-双休日");
        type_chiToeng.put("在校期-工作日","NormalWorkday");
        type_chiToeng.put("在校期-双休日/节假日","NormalWeekendAndLegalHoliday");
        type_chiToeng.put("寒暑假-工作日","HolidayWorkday");
        type_chiToeng.put("寒暑假-双休日","HolidayWeekend");
    }

    static {
        name_engTochi.put("LoopLineAntiClockwise", "校园巴士逆时针");
        name_engTochi.put("LoopLineClockwise", "校园巴士顺时针");
        name_engTochi.put("MinHangToXuHui", "闵行到徐汇");
        name_engTochi.put("XuHuiToMinHang", "徐汇到闵行");
        name_engTochi.put("MinHangToQiBao", "闵行到七宝");
        name_engTochi.put("QiBaoToMinHang", "七宝到闵行");
        name_chiToeng.put("校园巴士逆时针", "LoopLineAntiClockwise");
        name_chiToeng.put("校园巴士顺时针", "LoopLineClockwise");
        name_chiToeng.put("闵行到徐汇", "MinHangToXuHui");
        name_chiToeng.put("徐汇到闵行", "XuHuiToMinHang");
        name_chiToeng.put("闵行到七宝", "MinHangToQiBao");
        name_chiToeng.put("七宝到闵行", "QiBaoToXuHui");
    }

    public static String ERROR = "error";

    public static String getTypeByCalendar(Calendar calendar){
        //date = calendar.getTime();
        boolean isWeekendFlag = StringCalendarUtils.isWeekend(calendar);
        boolean isHoildayFlag = StringCalendarUtils.isHoilday(calendar);
        if (!isHoildayFlag && !isWeekendFlag){
            return type_list_E[0];
        }
        else if (!isHoildayFlag){
            return type_list_E[1];
        }
        else if (!isWeekendFlag){
            return type_list_E[2];
        }
        else{
            return type_list_E[3];
        }
    }

    public static String getTypeByYearMonthDay(int year, int month, int day){
        //date = calendar.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        boolean isWeekendFlag = StringCalendarUtils.isWeekend(calendar);
        boolean isHoildayFlag = StringCalendarUtils.isHoilday(calendar);
        if (!isHoildayFlag && !isWeekendFlag){
            return type_list_E[0];
        }
        else if (!isHoildayFlag){
            return type_list_E[1];
        }
        else if (!isWeekendFlag){
            return type_list_E[2];
        }
        else{
            return type_list_E[3];
        }
    }

    public static String getTypeOfToday(){
        Calendar calendar = Calendar.getInstance();
        boolean isWeekendFlag = StringCalendarUtils.isWeekend(calendar);
        boolean isHoildayFlag = StringCalendarUtils.isHoilday(calendar);
        if (!isHoildayFlag && !isWeekendFlag){
            return type_list_E[0];
        }
        else if (!isHoildayFlag){
            return type_list_E[1];
        }
        else if (!isWeekendFlag){
            return type_list_E[2];
        }
        else{
            return type_list_E[3];
        }
    }

    public static String getLineByDepartureAndArrive(String departure_place_str, String arrive_place_str){
        if (departure_place_str.contains("闵行") && arrive_place_str.contains("徐汇")) {
            return line_list_E[0];
        }
        else if (departure_place_str.contains("徐汇") && arrive_place_str.contains("闵行")) {
            return line_list_E[1];
        }
        else if (departure_place_str.contains("闵行") && arrive_place_str.contains("七宝")) {
            return line_list_E[2];
        }
        else if (departure_place_str.contains("七宝") && arrive_place_str.contains("闵行")) {
            return line_list_E[3];
        }
        else {
            return ERROR;
        }
    }

    public static String getEngType(String type){
        return type_chiToeng.get(type);
    }

    public static String getChiType(String type){
        return type_engTochi.get(type);
    }

    public static String getEngLineName(String linename) { return name_chiToeng.get(linename); }

    public static String getChiLineName(String linename) { return name_engTochi.get(linename); }

    public static boolean isLoopLine(String linename){
        return (linename.equals("LoopLineAntiClockwise") || linename.equals("LoopLineClockwise"));
    }
}
