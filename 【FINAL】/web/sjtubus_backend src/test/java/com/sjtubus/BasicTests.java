package com.sjtubus;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BasicTests extends SjtubusApplicationTests{
    @Test
    public void getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        System.out.println(cal.getTime());
    }

    @Test
    public void getCurrrentDate(){
        String current_date="";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        current_date=simpleDateFormat.format(date);

        String s = "2018-07-18";
        if (s.equals(current_date)){
            System.out.println("The same!");
        }
        else
            System.out.println("Different");
    }
}
