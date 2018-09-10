package com.sjtubus.model.response;

import com.sjtubus.entity.Appointment;
import com.sjtubus.entity.Driver;

import java.util.List;

public class DriverListResponse extends HttpResponse{
    private List<Driver> driverList;

    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }
}
