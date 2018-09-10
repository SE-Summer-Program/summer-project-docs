package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Collection {

    @SerializedName("userid")
    private int userid;
    @SerializedName("username")
    private String username;
    @SerializedName("shiftid")
    private String shiftid;

    private int frequence;

    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
