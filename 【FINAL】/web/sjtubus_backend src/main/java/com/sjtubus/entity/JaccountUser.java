package com.sjtubus.entity;

import javax.persistence.*;

@Entity
@Table(name="JaccountUsers")

public class JaccountUser {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private int userId ;

    @Column(name="username")
    private String username ;

    @Column(name="credit")
    private int credit ;

    @Column(name="isteacher")
    private boolean isTeacher;

    @Column(name="phone")
    private String phone;

    @Column(name = "realname")
    private String realname;

    @Column(name = "student_number")
    private String studentNumber;

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isTeacher() {

        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public int getCredit() {

        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

}
