package com.sjtubus.model;

import java.util.ArrayList;

import java.util.List;

public class Schedule {

    private String lineName;

    private String types;

    private List<String> scheduleShift = new ArrayList<>();
    private List<String> scheduleTime = new ArrayList<>();
    private List<String> scheduleComment = new ArrayList<>();

    public Schedule(){}

    public Schedule(String lineName,String types){
        this.lineName = lineName;
        this.types = types;
    }

    public String getLineName(){
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getTypes(){
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }


    public List<String> getScheduleTime(){ return scheduleTime; }

    public List<String> getScheduleComment(){ return scheduleComment; }

    public void setScheduleTime(List<String> scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public void setScheduleComment(List<String> scheduleComment) {
        this.scheduleComment = scheduleComment;
    }

    public List<String> getScheduleShift() {
        return scheduleShift;
    }

    public void setScheduleShift(List<String> scheduleShift) {
        this.scheduleShift = scheduleShift;
    }
}
