package com.sjtubus.entity;

import javax.persistence.*;

@Entity
@Table(name="Driver")

public class Driver {
    private int driverId ;
    private String username ;
    private String password ;
    private String phone;

    @Id
    @GeneratedValue
    @Column(name = "driver_id")
    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
