package com.sjtubus.service;

import com.sjtubus.dao.TimeTableDao;
import com.sjtubus.entity.TimeTable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTableService {
    @Autowired
    private TimeTableDao timeTableDao;

    public TimeTable getTimeTableByLineName(String station, String type) {
    	return timeTableDao.findByStationAndType(station, type);
    }

    /**
     * @description: 获取校园巴士环线上某一具体站点的时刻表信息
     * @date: 2018/7/24 16：29
     * @params:
     * @return:
     */
    public TimeTable getScheduleOfLoopLine(String station){
        return timeTableDao.findByStation(station);
    }
}