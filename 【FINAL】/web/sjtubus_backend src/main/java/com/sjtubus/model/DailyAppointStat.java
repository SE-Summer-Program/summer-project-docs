package com.sjtubus.model;

import java.sql.Date;

/**
 * @author Allen
 * @date 2018/9/4 4:42
 */
public class DailyAppointStat {
    private Object date;
    private Object appoint_num;

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public Object getAppoint_num() {
        return appoint_num;
    }

    public void setAppoint_num(Object appoint_num) {
        this.appoint_num = appoint_num;
    }
}
