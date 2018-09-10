package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

public class ShiftInfo {

    private String shiftid;

    private String lineName;
    private String lineNameCn;
    private String departureTime;
    private String arriveTime;

    private String busPlateNum;
    private int busSeatNum;

    private String driverName;
    private String driverPhone;

    private String comment;

    public String getShiftid() {
        return shiftid;
    }

    public void setShiftid(String shiftid) {
        this.shiftid = shiftid;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineNameCn() {
        return lineNameCn;
    }

    public void setLineNameCn(String lineNameCn) {
        this.lineNameCn = lineNameCn;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArriveTime() { return arriveTime; }

    public void setArriveTime(String arriveTime) { this.arriveTime = arriveTime; }

    public String getBusPlateNum() {
        return busPlateNum;
    }

    public void setBusPlateNum(String busPlateNum) {
        this.busPlateNum = busPlateNum;
    }

    public int getBusSeatNum() {
        return busSeatNum;
    }

    public void setBusSeatNum(int busSeatNum) {
        this.busSeatNum = busSeatNum;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
