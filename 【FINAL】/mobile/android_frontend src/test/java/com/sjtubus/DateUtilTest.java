package com.sjtubus;

import org.junit.Test;

import java.util.Date;

import static com.sjtubus.utils.MyDateUtils.getBeginOfDay;
import static com.sjtubus.utils.MyDateUtils.getTomorrowStr;
import static com.sjtubus.utils.MyDateUtils.getYesterdayStr;
import static com.sjtubus.utils.MyDateUtils.isWithinOneWeek;
import static org.junit.Assert.assertEquals;

public class DateUtilTest {
    @Test
    public void testGetBeginOfDay() {
        String datestr = "2018-07-27";
        Date result = getBeginOfDay(datestr);
        assertEquals(result.getHours(),0);
        assertEquals(result.getMinutes(),0);
        assertEquals(result.getSeconds(),0);
    }

    @Test
    public void testGetYesterdayStr() {
        String datestr = "2018-07-27";
        String result = getYesterdayStr(datestr);
        assertEquals(result,"2018-07-26");
    }

    @Test
    public void testGetTomorrowStr() {
        String datestr = "2018-07-27";
        String result = getTomorrowStr(datestr);
        assertEquals(result,"2018-07-28");
    }

    @Test
    public void testIsWithinOneWeek(){
        String datestr = "2018-07-27";
        boolean result = isWithinOneWeek(datestr);
        assertEquals(result,true);
    }
}
