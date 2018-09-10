package com.sjtubus.model.response;

import com.sjtubus.model.DailyAppointStat;

import java.util.List;

public class AppointStatResponse extends HttpResponse {
    private List<DailyAppointStat> statistics;

    public List<DailyAppointStat> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<DailyAppointStat> statistics) {
        this.statistics = statistics;
    }
}
