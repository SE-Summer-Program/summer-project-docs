package com.sjtubus.model;

/**
 * @author Allen
 * @date 2018/7/10 19:05
 */
public class LineInfo {
    private String lineName;
    private String firstTime;
    private String lastTime;
    private String lineNameCN;
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

    public String getFirstTime() {
        return firstTime;
    }

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

    public void addRemainShift(){
        this.remainShift += 1;
    }

}
