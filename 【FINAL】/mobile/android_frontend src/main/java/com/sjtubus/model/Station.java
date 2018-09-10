package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Station  implements Serializable{
    @SerializedName("station")
    private String name;//站点中文名
    @SerializedName("longitude")
    private Double longitude;//经度
    @SerializedName("latitude")
    private Double latitude;//纬度
    @SerializedName("image_url")
    private String image_url;//图片地址
    //V = Vactation
    //A = AntiClockwise
    //C = Clockwise
    //L = Loopline
    //N = Nonloopline
    @SerializedName("antiClockLoop")
    private List<String> AntiClockLoop = null;
    @SerializedName("antiClockNonLoop")
    private List<String> AntiClockNonLoop = null;
    @SerializedName("clockLoop")
    private List<String> ClockLoop = null;
    @SerializedName("clockNonLoop")
    private List<String> ClockNonLoop = null;
    @SerializedName("vacAntiClockLoop")
    private List<String> VacAntiClockLoop = null;
    @SerializedName("vacAntiClockNonLoop")
    private List<String> VacAntiClockNonLoop = null;
    @SerializedName("vacClockLoop")
    private List<String> VacClockLoop = null;
    @SerializedName("vacClockNonLoop")
    private List<String> VacClockNonLoop = null;

    public Station(String name, Double latitude, Double longitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public String getName(){
        return this.name;
    }
    public Double getLongitude(){
        return this.longitude;
    }
    public Double getLatitude() {
        return this.latitude;
    }

    public void setAntiClockLoop(List<String> AntiClockLoop) {
        this.AntiClockLoop = AntiClockLoop;
    }
    public void setAntiClockNonLoop(List<String> AntiClockNonLoop) {
        this.AntiClockNonLoop = AntiClockNonLoop;
    }
    public void setClockLoop(List<String> ClockLoop) {
        this.ClockLoop = ClockLoop;
    }
    public void setClockNonLoop(List<String> ClockNonLoop) {
        this.ClockNonLoop = ClockNonLoop;
    }
    public void setVacAntiClockLoop(List<String> VacAntiClockLoop) {
        this.VacAntiClockLoop = VacAntiClockLoop;
    }
    public void setVacAntiClockNonLoop(List<String> VacAntiClockNonLoop) {
        this.VacAntiClockNonLoop = VacAntiClockNonLoop;
    }
    public void setVacClockLoop(List<String> VacClockLoop) {
        this.VacClockLoop = VacClockLoop;
    }
    public void setVacClockNonLoop(List<String> VacClockNonLoop) {
        this.VacClockNonLoop = VacClockNonLoop;
    }
    public List<String> getAntiClockLoop() {
        return this.AntiClockLoop;
    }
    public List<String> getAntiClockNonLoop() {
        return this.AntiClockNonLoop;
    }
    public List<String> getClockLoop() {
        return this.ClockLoop;
    }
    public List<String> getClockNonLoop() {
        return this.ClockNonLoop;
    }
    public List<String> getVacAntiClockLoop() {
        return this.VacAntiClockLoop;
    }
    public List<String> getVacAntiClockNonLoop() {
        return this.VacAntiClockNonLoop;
    }
    public List<String> getVacClockLoop() {
        return this.VacClockLoop;
    }
    public List<String> getVacClockNonLoop() {
        return this.VacClockNonLoop;
    }
    public List<String> getAntiClockTotal(){
        List<String> list1 = this.AntiClockLoop;
        List<String> list2 = this.AntiClockNonLoop;
        list1.addAll(list2);
        return list1;
    }
    public List<String> getClockTotal(){
        List<String> list1 = this.ClockLoop;
        List<String> list2 = this.ClockNonLoop;
        list1.addAll(list2);
        return list1;
    }
    public List<String> getVacAntiClockTotal(){
        List<String> list1 = this.VacAntiClockLoop;
        List<String> list2 = this.VacAntiClockNonLoop;
        list1.addAll(list2);
        return list1;
    }
    public List<String> getVacClockTotal(){
        List<String> list1 = this.VacClockLoop;
        List<String> list2 = this.VacClockNonLoop;
        list1.addAll(list2);
        return list1;
    }
    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}
