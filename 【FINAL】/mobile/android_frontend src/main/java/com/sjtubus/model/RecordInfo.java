package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.utils.StringCalendarUtils;

/**
 * @author wxw
 * @date 2018/7/18 10:09
 * */

public class RecordInfo {

    private String confirmDate; //提交预约申请的时间

    private String id;
    private RecordInfo bean;

    @SerializedName("lineName")
    private String lineName; //中文的

    @SerializedName("departureDate")
    private String departureDate;
    @SerializedName("departureTime")
    private String departureTime; //预约的日期和时间
    @SerializedName("arriveTime")
    private String arriveTime;
    @SerializedName("shiftid")
    private String shiftid;

    @SerializedName("status")
    private String status;

    @SerializedName("submitTime")
    private String submitTime;

    private String departureMsg; //根据item_record的格式来的

    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RecordInfo getBean() {
        return bean;
    }

    public void setBean(RecordInfo bean) {
        this.bean = bean;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArriveTime() { return arriveTime; }

    public void setArriveTime(String arriveTime) { this.arriveTime = arriveTime; }

    public String getShiftid() {
        return shiftid;
    }

    public void setShiftid(String shiftid) {
        this.shiftid = shiftid;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getSubmiTime() { return submitTime; }

    public void setSubmitTime(String submitTime) { this.submitTime = submitTime; }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDepartureMsg() {
        departureMsg = getDepartureDate() + " " + StringCalendarUtils.HHmmssToHHmm(getDepartureTime());
        return departureMsg;
    }

    public void setDepartureMsg(String departureMsg) {
        this.departureMsg = departureMsg;
    }

    public String getDepartureTimeComplete(){
        return getDepartureDate() + " " + getDepartureTime();
    }

}
