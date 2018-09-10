package com.sjtubus.model.response;

import com.sjtubus.entity.Shift;

import java.sql.Time;
import java.util.List;

public class TimeListResponse extends HttpResponse{

    private List<Time> timeList;

    public List<Time> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<Time> timeList) {
        this.timeList = timeList;
    }
}
