package com.sjtubus.model.response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Allen
 * @date 2018/7/23 16:09
 */
public class LocationResponse extends HttpResponse{
    private Map<String,String> locations;

    public Map<String, String> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, String> locations) {
        this.locations = locations;
    }
}
