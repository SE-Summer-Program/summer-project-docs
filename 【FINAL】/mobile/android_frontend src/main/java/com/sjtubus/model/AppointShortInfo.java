package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

public class AppointShortInfo {
    @SerializedName("shiftId")
    private String shiftid;
    @SerializedName("departureTime")
    private String departure_time;
    @SerializedName("arriveTime")
    private String arrive_time;
    @SerializedName("remainSeat")
    private int remain_seat;
    @SerializedName("submitTime")
    private String submit_time;

    public String getShiftid() {
        return shiftid;
    }

    public void setShiftid(String shiftid) {
        this.shiftid = shiftid;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public int getRemain_seat() {
        return remain_seat;
    }

    public void setRemain_seat(int remain_seat) {
        this.remain_seat = remain_seat;
    }

    public String getSubmit_time() { return submit_time; }

    public void setSubmit_time(String submit_time) { this.submit_time = submit_time; }
}
