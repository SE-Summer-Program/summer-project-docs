package com.sjtubus.entity;

import com.sjtubus.utils.StringCalendarUtils;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "Appointment")
public class Appointment {
    @Id
    @GeneratedValue
    @Column(name = "appointment_id")
    private int appointmentId ;
    @Column(name = "user_id")
    private int userId ;
    @Column(name = "shift_id")
    private String shiftId ;
    @Column(name = "username")
    private String userName;
    @Column(name = "realname")
    private String realName;
    @Column(name = "user_role")
    private String userRole;
    @Column(name = "appoint_date")
    private Date appointDate ;
    @Column(name = "line_name")
    private String lineName ;
    @Column(name = "line_name_cn")
    private String lineNameCn;
    @Column(name = "submit_time")
    private Date submitTime;
    @Column(name = "isnormal")
    private boolean isNormal ;
    @Column(name = "comment")
    private String comment;

    public boolean getIsNormal() {
        return isNormal;
    }

    public void setIsNormal(boolean isNormal) {
        this.isNormal = isNormal;
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


    public Date getAppointDate() {

        return appointDate;
    }

    public void setAppointDate(Date appointDate) {
        this.appointDate = appointDate;
    }

    public String getShiftId() {

        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    public int getAppointmentId() {

        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {

        this.appointmentId = appointmentId;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String username) {

        this.userName = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getSubmitTime(){
        return submitTime;
    }

    public String getSubmitTimeString() {
        return StringCalendarUtils.DateToString(submitTime);
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public void setSubmitTimeString(String submitTimeString){

        Date submitdate = StringCalendarUtils.UtilDateToSqlDate(StringCalendarUtils.StringToTime(submitTimeString));
        this.submitTime = submitdate;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
