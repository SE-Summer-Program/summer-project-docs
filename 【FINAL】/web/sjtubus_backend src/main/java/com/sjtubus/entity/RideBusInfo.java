package com.sjtubus.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="RideBusInfo")
public class RideBusInfo {

    @Id
    @Column(name = "ride_id")
    private String rideId ;
    @Column(name = "shift_id")
    private String shiftId ;
    @Column(name = "bus_plate")
    private String busPlate;
    @Column(name = "ride_date")
    private Date rideDate;
    @Column(name = "line_type")
    private String lineType;
    @Column(name = "student_num")
    private int studentNum ;  //check (student_num >= 0)
    @Column(name = "teacher_num")
    private int teacherNum;     //check (teacher_num >= 0)
    @Column(name = "appoint_num")
    private int appointNum;     //check (appoint_num >= 0)
    @Column(name = "appoint_break")
    private int appointBreak ;  //check (appoint_break >= 0)
    @Column(name = "reserve_seat")
    private int reserveSeat;
    @Column(name = "seat_num")
    private int seatNum ;

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public Date getRideDate() {
        return rideDate;
    }

    public void setRideDate(Date rideDate) {
        this.rideDate = rideDate;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public int getTeacherNum() {
        return teacherNum;
    }

    public void setTeacherNum(int teacherNum) {
        this.teacherNum = teacherNum;
    }

    public int getAppointNum() {
        return appointNum;
    }

    public void setAppointNum(int appointNum) {
        this.appointNum = appointNum;
    }

    public int getAppointBreak() {
        return appointBreak;
    }

    public void setAppointBreak(int appointBreak) {
        this.appointBreak = appointBreak;
    }

    public int getReserveSeat() {
        return reserveSeat;
    }

    public void setReserveSeat(int reserveSeat) {
        this.reserveSeat = reserveSeat;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }


    public String getBusPlate() {
        return busPlate;
    }

    public void setBusPlate(String busPlate) {
        this.busPlate = busPlate;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }
}
