package com.sjtubus.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShiftUtilsTest {

    @Test
    public void getEngType() {
        String type1 = "在校期-工作日";
        assertEquals("NormalWorkday", ShiftUtils.getEngType(type1));
        String type2 = "在校期-双休日";
        assertEquals("NormalWeekend", ShiftUtils.getEngType(type2));
        String type3 = "寒暑假-工作日";
        assertEquals("HolidayWorkday", ShiftUtils.getEngType(type3));
        String type4 = "寒暑假-双休日";
        assertEquals("HolidayWeekend", ShiftUtils.getEngType(type4));
    }

    @Test
    public void getChiType() {
        String type1 = "NormalWorkday";
        assertEquals("在校期-工作日", ShiftUtils.getChiType(type1));
        String type2 = "NormalWeekend";
        assertEquals("在校期-双休日", ShiftUtils.getChiType(type2));
        String type3 = "HolidayWorkday";
        assertEquals("寒暑假-工作日", ShiftUtils.getChiType(type3));
        String type4 = "HolidayWeekend";
        assertEquals("寒暑假-双休日", ShiftUtils.getChiType(type4));
    }

    @Test
    public void getEngLineName() {
        String type = "校园巴士逆时针";
        assertEquals("LoopLineAntiClockwise", ShiftUtils.getEngLineName(type));
    }

    @Test
    public void getChiLineName() {
        String type = "MinHangToXuHui";
        assertEquals("闵行到徐汇", ShiftUtils.getChiLineName(type));
    }
}