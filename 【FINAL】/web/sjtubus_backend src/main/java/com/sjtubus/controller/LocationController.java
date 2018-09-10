package com.sjtubus.controller;

import com.sjtubus.entity.Driver;
import com.sjtubus.entity.User;
import com.sjtubus.model.response.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Allen
 * @date 2018/7/23 2:03
 */

@RestController
public class LocationController {

    @Autowired
    ValueOperations<String,String> valueOperations;

    @Autowired
    HashOperations<String, String, String> hashOperations;

    @RequestMapping(value = "/locate",method = RequestMethod.POST)
    public void locate(String latitude, String longitude, HttpSession session){
        Driver driver = (Driver) session.getAttribute("user");
        if(driver == null) return;
        hashOperations.put("location",driver.getUsername(),latitude+" "+longitude);
    }

    @RequestMapping(value = "/location",method = RequestMethod.GET)
    public LocationResponse location(){
        LocationResponse response = new LocationResponse();
        response.setLocations(hashOperations.entries("location"));
        return response;
    }
}
