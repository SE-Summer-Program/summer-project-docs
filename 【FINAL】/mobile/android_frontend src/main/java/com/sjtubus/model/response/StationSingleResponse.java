package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.Station;

import java.util.ArrayList;

public class StationSingleResponse extends HttpResponse {

    @SerializedName("station")
    private Station station;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
