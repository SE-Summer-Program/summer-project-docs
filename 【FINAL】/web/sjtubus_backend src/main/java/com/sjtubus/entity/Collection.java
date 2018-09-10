package com.sjtubus.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Collection")
public class Collection {

    @Id
    @Column(name = "id")
    private int id ;

    @Basic
    @Column(name = "user_id")
    private int userid;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "shift_id")
    private String shiftid;

    @Basic
    @Column(name = "frequence")
    private int frequence;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShiftid() {
        return shiftid;
    }

    public void setShiftid(String shiftid) {
        this.shiftid = shiftid;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }
}
