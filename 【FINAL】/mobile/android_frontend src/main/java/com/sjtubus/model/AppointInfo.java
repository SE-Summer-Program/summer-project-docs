package com.sjtubus.model;

public class AppointInfo implements Cloneable{

    public static final int PARENT_ITEM = 0; //父布局
    public static final int CHILD_ITEM = 1; //子布局

    private int type; //显示类型
    private boolean isExpand; //是否展开
    private AppointInfo childBean;

    private String id;
    private String shiftid;
    private String departure_time;
    private String arrive_time;

    private String date;
    private String line_type;
    private int remain_seat;

    private String departure_place;
    private String arrive_place;
    private int appoint_status; //无座，预约

    private String comment;

    /****************************************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isExpand(){
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AppointInfo getChildBean() {
        return childBean;
    }

    public void setChildBean(AppointInfo childBean) {
        this.childBean = childBean;
    }

    public String getShiftid() {
        return shiftid;
    }

    public void setShiftid(String shiftid) {
        this.shiftid = shiftid;
    }

    public String getDeparture_place() {
        return departure_place;
    }

    public void setDeparture_place(String departure_place) { this.departure_place = departure_place; }

    public String getArrive_place() {
        return arrive_place;
    }

    public void setArrive_place(String arrive_place) {
        this.arrive_place = arrive_place;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLine_type() {
        return line_type;
    }

    public void setLine_type(String line_type) {
        this.line_type = line_type;
    }

    public int getAppoint_status() {
        return appoint_status;
    }

    public void setAppoint_status(int appoint_status) {
        this.appoint_status = appoint_status;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void copy(AppointInfo appointInfo){
        this.departure_place = appointInfo.departure_place;
        this.arrive_place = appointInfo.arrive_place;
        this.departure_time = appointInfo.departure_time;
        this.arrive_time = appointInfo.arrive_time;
        this.date = appointInfo.date;
        this.shiftid = appointInfo.shiftid;
        this.line_type = appointInfo.line_type;

//        this.remain_seat = appointInfo.remain_seat;
//        this.appoint_status = appointInfo.appoint_status;
    }
}