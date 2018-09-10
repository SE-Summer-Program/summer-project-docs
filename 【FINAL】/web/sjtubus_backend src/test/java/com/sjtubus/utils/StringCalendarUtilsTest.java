package com.sjtubus.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.*;

public class StringCalendarUtilsTest {

    @Test
    public void stringToDate() {
        String dateStr = "2018-07-25";
        java.util.Date date = StringCalendarUtils.StringToDate(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2018, calendar.get(Calendar.YEAR));
        assertEquals(7, calendar.get(Calendar.MONTH) + 1);
        assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void stringToTime() {
        String datetimeStr = "2018-07-25 23:56:00";
        java.util.Date date = StringCalendarUtils.StringToTime(datetimeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(2018, calendar.get(Calendar.YEAR));
        assertEquals(7, calendar.get(Calendar.MONTH) + 1);
        assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(56, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
    }

    @Test
    public void dateToString() throws ParseException {
        SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = simFormat.parse("2018-07-26");
        String dateStr = StringCalendarUtils.DateToString(date);
        assertEquals("2018-07-26", dateStr);
    }

    @Test
    public void timeToString() throws ParseException {
        SimpleDateFormat simFormat = new SimpleDateFormat("HH:mm:ss");
        java.util.Date date = simFormat.parse("00:00:05");
        String dateStr = StringCalendarUtils.DateToString(date);
        assertEquals("00:00:05", dateStr);
    }

    @Test
    public void isBeforeCurrentTime() {
        String dateStr1 = "2018-07-25 23:56:00";
        boolean result1 = StringCalendarUtils.isBeforeCurrentTime(dateStr1);
        assertTrue(result1);
        String dateStr2 = "2018-07-26 00:00:02";
        boolean result2 = StringCalendarUtils.isBeforeCurrentTime(dateStr2);
        assertTrue(result2);
        String dateStr3 = "2019-01-01 12:00:00";
        boolean result3 = StringCalendarUtils.isBeforeCurrentTime(dateStr3);
        assertFalse(result3);
    }

    @Test
    public void getCurrentDate() {
        String currentDate = StringCalendarUtils.getCurrentDate();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 设置你想要的格式
        assertEquals(currentDate, dateFormat.format(calendar.getTime()));
    }

    @Test
    public void getCurrentTime() {
        String currentDate = StringCalendarUtils.getCurrentTime();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置你想要的格式
        assertEquals(currentDate, dateFormat.format(calendar.getTime()));
    }
}