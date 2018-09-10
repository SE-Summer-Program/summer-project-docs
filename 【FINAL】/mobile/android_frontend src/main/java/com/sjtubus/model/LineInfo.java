package com.sjtubus.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Allen
 * @date 2018/7/10 19:05
 */

public class LineInfo {
    @SerializedName("lineName")
    private String lineName;
    @SerializedName("firstTime")
    private String firstTime;
    @SerializedName("lastTime")
    private String lastTime;
    @SerializedName("lineNameCN")
    private String lineNameCN;
    @SerializedName("remainShift")
    private int remainShift;

    public String getLineNameCN() {
        return lineNameCN;
    }

    public void setLineNameCN(String lineNameCN) {
        this.lineNameCN = lineNameCN;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getFirstTime() { return firstTime; }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getRemainShift() {
        return remainShift;
    }

    public void setRemainShift(int remainShift) {
        this.remainShift = remainShift;
    }
}
