package com.sjtubus.model.response;

import com.sjtubus.model.Schedule;

import java.util.List;

public class ScheduleResponse extends HttpResponse {
    private Schedule schedule;

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
