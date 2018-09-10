package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.Station;

import java.util.ArrayList;

public class StationResponse extends HttpResponse {
    @SerializedName("stations")
    private ArrayList<Station> stations;

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
}
