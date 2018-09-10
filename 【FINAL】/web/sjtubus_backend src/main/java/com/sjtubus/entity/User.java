package com.sjtubus.entity;

import javax.persistence.*;

@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private int userId ;
    @Column(name="username")
    private String username ;
    @Column(name="password")
    private String password ;
    @Column(name="credit")
    private int credit ;
    @Column(name="isteacher")
    private boolean isTeacher;   //enum('true', 'false')
    @Column(name="phone")
    private String phone;
//    @Transient
    @Column(name="realname")
    private String realname;
//    @Transient
    @Column(name="student_number")
    private String studentNumber;


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

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getStudentNumber() {
        return studentNumber;
    }

}
