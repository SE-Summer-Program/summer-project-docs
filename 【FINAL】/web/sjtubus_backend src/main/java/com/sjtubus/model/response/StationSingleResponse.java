package com.sjtubus.model.response;

import com.sjtubus.entity.TimeTable;

import java.util.List;

public class StationSingleResponse extends HttpResponse {

    private TimeTable station;

    public TimeTable getStation() {
        return station;
    }

    public void setStation(TimeTable station) {
        this.station = station;
    }
}
