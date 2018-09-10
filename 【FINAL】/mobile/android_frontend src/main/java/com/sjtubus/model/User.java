package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Allen on 2018/7/3.
 */

public class User {
    @SerializedName("userId")
    private String userId;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("realname")
    private String realname;
    @SerializedName("studentNumber")
    private String studentNumber;
    @SerializedName("phone")
    private String phone;
    @SerializedName("teacher")
    private Boolean isTeacher;
    @SerializedName("credit")
    private int credit;

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getTeacher() {
        return isTeacher;
    }

    public void setTeacher(Boolean teacher) {
        isTeacher = teacher;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }


    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }



}
