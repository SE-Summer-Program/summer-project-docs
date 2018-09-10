package com.sjtubus.model;

import java.util.ArrayList;

import java.util.List;

/*
 * 这个类找时间重写一下
 */
public class Schedule {

    private String lineName;

    private String types;

    private List<String> scheduleTime = new ArrayList<>();
    private List<String> scheduleComment = new ArrayList<>();

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

}
