package com.sjtubus.service;

import com.sjtubus.dao.AppointmentDao;
import com.sjtubus.dao.RideBusInfoDao;
import com.sjtubus.entity.RideBusInfo;
import com.sjtubus.model.DailyAppointStat;
import com.sjtubus.utils.StringCalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.sql.Time;

@Service
public class StatisticsService {
    @Autowired
    AppointmentDao appointmentDao;

    @Autowired
    RideBusInfoDao rideBusInfoDao;

    @Autowired
    ShiftService shiftService;


    public List<DailyAppointStat> generateAppointmentReport(Date startDate, Date endDate, String lineNameCn, String lineType, Time time){
        List<DailyAppointStat> result = new ArrayList<>();
        String timeString = shiftService.changeTimeToStringTime(time);
        String shiftid;
        if (lineNameCn.equals("闵行到七宝"))
            shiftid = "MQ";
        else if (lineNameCn.equals("闵行到徐汇"))
            shiftid = "MX";
        else if (lineNameCn.equals("七宝到闵行"))
            shiftid = "QM";
        else
            shiftid = "XM";
        shiftid = shiftid + shiftService.changeTypeToId(lineType);
        shiftid = shiftid + timeString;
        System.out.println("shiftid:"+shiftid);
        List<Object[]> countList = appointmentDao.queryAppointmentGroupByDate(shiftid, StringCalendarUtils.UtilDateToSqlDate(startDate), StringCalendarUtils.UtilDateToSqlDate(endDate));
        System.out.println("list:"+countList.size());
        for (Object[] objects : countList){
            System.out.println("date:"+objects[0]);
            System.out.println("count:"+objects[1]);
            DailyAppointStat dailyStat = new DailyAppointStat();
            dailyStat.setDate(objects[0]);
            dailyStat.setAppoint_num(objects[1]);
            result.add(dailyStat);
        }
        return result;
    }

    public List<RideBusInfo> generateRideInfoReport(Date startDate, Date endDate, String lineNameCn, String lineType, Time time){
        String timeString = shiftService.changeTimeToStringTime(time);
        String shiftid;
        if (lineNameCn.equals("闵行到七宝"))
            shiftid = "MQ";
        else if (lineNameCn.equals("闵行到徐汇"))
            shiftid = "MX";
        else if (lineNameCn.equals("七宝到闵行"))
            shiftid = "QM";
        else
            shiftid = "XM";
        shiftid = shiftid + shiftService.changeTypeToId(lineType);
        shiftid = shiftid + timeString;
        System.out.println("shiftid:"+shiftid);
        List<RideBusInfo> result = rideBusInfoDao.queryRideBusInfo(shiftid, StringCalendarUtils.UtilDateToSqlDate(startDate), StringCalendarUtils.UtilDateToSqlDate(endDate));
        System.out.println("list:"+result.size());
        return result;
    }
}
