package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author wxw
 * @date 2018/7/16 9:32
 */
public class AppointInfo {

    private String shiftId;
    private String departureTime;
    private String arriveTime;
    private int remainSeat;
    private String submitTime;

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getRemainSeat() {
        return remainSeat;
    }

    public void setRemainSeat(int remainSeat) {
        this.remainSeat = remainSeat;
    }

    public String getSubmitTime() { return submitTime; }

    public void setSubmitTime(String submitTime) { this.submitTime = submitTime; }
}
