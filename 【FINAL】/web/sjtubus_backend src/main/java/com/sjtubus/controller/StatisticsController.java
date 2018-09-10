package com.sjtubus.controller;

import com.sjtubus.entity.RideBusInfo;
import com.sjtubus.model.DailyAppointStat;
import com.sjtubus.model.response.AppointStatResponse;
import com.sjtubus.model.response.RideBusInfoResponse;
import com.sjtubus.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {
    @Autowired
    StatisticsService statisticsService;

    @RequestMapping(path="/appointment")
    public AppointStatResponse getAppointStatData(@RequestParam("startDate") String startDate,
                                                  @RequestParam("endDate") String endDate,
                                                  @RequestParam("lineNameCn") String lineNameCn,
                                                  @RequestParam("lineType") String lineType,
                                                  @RequestParam("time") Time time){
        AppointStatResponse response = new AppointStatResponse();
        try {
            //System.out.println("hello");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = formatter.parse(startDate);
            //SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = formatter.parse(endDate);
            //System.out.println(date1);
            //System.out.println(date2);
            List<DailyAppointStat> result = statisticsService.generateAppointmentReport(date1, date2, lineNameCn, lineType, time);
            response.setStatistics(result);
            response.setMsg("success");
        }
        catch(Exception e){
            response.setError(1);
            response.setMsg("error");
        }
        return response;
    }

    @RequestMapping(path="/ridebusinfo")
    public RideBusInfoResponse getRideInfoStatData(@RequestParam("startDate") String startDate,
                                                   @RequestParam("endDate") String endDate,
                                                   @RequestParam("lineNameCn") String lineNameCn,
                                                   @RequestParam("lineType") String lineType,
                                                   @RequestParam("time") Time time){
        RideBusInfoResponse response = new RideBusInfoResponse();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = formatter.parse(startDate);
            Date date2 = formatter.parse(endDate);
            List<RideBusInfo> result = statisticsService.generateRideInfoReport(date1, date2, lineNameCn, lineType, time);
            response.setRideBusInfos(result);
            response.setMsg("success");
        }
        catch(Exception e){
            response.setError(1);
            response.setMsg("error");
        }
        return response;
    }
}
