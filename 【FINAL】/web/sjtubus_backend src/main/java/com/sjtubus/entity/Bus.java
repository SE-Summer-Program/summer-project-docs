package com.sjtubus.entity;
import javax.persistence.*;

@Entity
@Table(name = "Bus")
public class Bus {

    private int busId ;
    private int driverId ;
    //private int shiftId ;
    private int seatNum ;
    private String plateNum ;

    @Id
    @Column(name = "bus_id")
    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    @Basic
    @Column(name = "driver_id")
    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

//    @Basic
//    @Column(name = "shift_id")
//    public int getShiftId() {
//        return shiftId;
//    }
//
//    public void setShiftId(int shiftId) {
//        this.shiftId = shiftId;
//    }

    @Basic
    @Column(name = "seat_num")
    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    @Basic
    @Column(name = "plate_num")
    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

}
