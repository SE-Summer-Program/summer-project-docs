package com.sjtubus.model;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private String name;
    private List<LatLng> location = new ArrayList<>();

    public Route(String name, List<Double> latitude, List<Double> longtitude) {
        this.name = name;
        for(int i = 0; i < latitude.size(); i++){
            this.location.add(new LatLng(latitude.get(i),longtitude.get(i)));
        }
    }

    public String getName() {
        return name;
    }
    public List<LatLng> getLocation() {
        return location;
    }
}
