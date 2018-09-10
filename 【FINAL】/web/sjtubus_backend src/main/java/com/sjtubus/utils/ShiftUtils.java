package com.sjtubus.utils;

import java.util.HashMap;

public class ShiftUtils {

    private static HashMap<String,String> type_engTochi = new HashMap<>();
    private static HashMap<String,String> type_chiToeng = new HashMap<>();

    private static HashMap<String,String> name_engTochi = new HashMap<>();
    private static HashMap<String,String> name_chiToeng = new HashMap<>();

    static {
        type_engTochi.put("NormalWorkday","在校期-工作日");
        type_engTochi.put("NormalWeekend","在校期-双休日");
        type_engTochi.put("HolidayWorkday","寒暑假-工作日");
        type_engTochi.put("HolidayWeekend","寒暑假-双休日");
        type_chiToeng.put("在校期-工作日","NormalWorkday");
        type_chiToeng.put("在校期-双休日","NormalWeekend");
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

    public static String getEngType(String type){
        return type_chiToeng.get(type);
    }

    public static String getChiType(String type){
        return type_engTochi.get(type);
    }

    public static String getEngLineName(String linename) { return name_chiToeng.get(linename); }

    public static String getChiLineName(String linename) { return name_engTochi.get(linename); }
}
