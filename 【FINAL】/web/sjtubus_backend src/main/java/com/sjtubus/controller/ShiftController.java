package com.sjtubus.controller;

import com.sjtubus.dao.ShiftDao;
import com.sjtubus.entity.Shift;
import com.sjtubus.entity.TimeTable;
import com.sjtubus.model.Schedule;
import com.sjtubus.model.ShiftInfo;
import com.sjtubus.model.response.*;
import com.sjtubus.service.ShiftService;
import com.sjtubus.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping(value = "/shift")
public class ShiftController {

    @Autowired
    ShiftService shiftService;
    @Autowired
    ShiftDao shiftDao;
    @Autowired
    TimeTableService timeTableService;

    /**
     * @description: 根据line_type和line_name获取schedule对象
     * @date: 2018/7/10 18:20
     * @params: type - 线路类别 line_name - 线路名称
     * @return: Schedule - 线路时刻表
    */
    @RequestMapping(path="/search_schedule")
    public ScheduleResponse getSchedule(@RequestParam("type") String lineType,
                                        @RequestParam("line_name") String lineName){
        ScheduleResponse response = new ScheduleResponse();
        try{
            Schedule result =  shiftService.getSchedule(lineType, lineName);
            response.setSchedule(result);
        }
        catch (Exception e){
            response.setError(1);
            response.setSchedule(null);
        }
        return response;
    }

    @RequestMapping(value = "infos")
    public ShiftInfoResponse getShiftInfos(String shiftid){
        ShiftInfoResponse response = new ShiftInfoResponse();
        ShiftInfo shiftInfo;
        shiftInfo = shiftService.getShiftInfo(shiftid);
        if(shiftInfo == null){
            response.setError(1);
            response.setMsg("班次详细信息获取失败");
        }
        response.setShiftInfo(shiftInfo);
        return response;
    }

    @RequestMapping(path="/search_schedule_loopline",method = RequestMethod.GET)
    public StationSingleResponse getScheduleOfLoopLine(@RequestParam("station") String station){
        StationSingleResponse response = new StationSingleResponse();
        TimeTable timeTable = timeTableService.getScheduleOfLoopLine(station);
        if (timeTable == null){
            response.setError(1);
            response.setMsg("站点信息获取失败");
        }
        response.setStation(timeTable);
        return response;
    }

    @RequestMapping(value="/add")
    public HttpResponse addShift(@RequestParam("lineName") String lineName,
                                 @RequestParam("lineNameCn") String lineNameCn,
                                 @RequestParam("lineType") String lineType,
                                 @RequestParam("departureTime")Time departureTime,
                                 @RequestParam("reserveSeat") int reserveSeat,
                                 @RequestParam("comment") String comment,
                                 @RequestParam("busId") int busId,
                                 @RequestParam("arriveTime") Time arriveTime){
        HttpResponse response = new HttpResponse();
        try {
            //System.out.println("lineName:"+lineName);
            //System.out.println("departureTime:"+ String.valueOf(departureTime));
            String result = shiftService.addShift(lineName, lineNameCn, lineType, departureTime, reserveSeat, comment, busId, arriveTime);
            response.setMsg(result);
        }
        catch (Exception e){
            response.setError(1);
            response.setMsg("fail");
        }
        return response;
    }


    /**
     * @description: 管理员方法
     * @date: 2018/9/8 9:52
     * @params: content：匹配字符串
     * @return: 搜索结果
    */
    @RequestMapping(path="/search_shift")
    public ShiftListResponse searchShift(@RequestParam("content") String content){
        ShiftListResponse response = new ShiftListResponse();
        try {
            List<Shift> result = shiftService.searchShift(content);
            response.setError(0);
            response.setShiftList(result);
            response.setMsg("success");
        }
        catch (Exception e){
            response.setError(1);
            response.setShiftList(null);
        }
        return response;
    }


    @RequestMapping(value="/delete" )
    public HttpResponse deleteShift(@RequestParam("shiftId") String shiftId){
        HttpResponse response = new HttpResponse();
        try {
            String result = shiftService.deleteShift(shiftId);
            response.setMsg(result);
        }
        catch (Exception e){
            response.setError(1);
            response.setMsg("fail");
        }
        return response;
    }


    @RequestMapping(path="/search_time" )
    public TimeListResponse searchTimeList(@RequestParam("lineNameCn") String lineNameCn,
                                            @RequestParam("lineType") String lineType){
        TimeListResponse response = new TimeListResponse();
        try{
            List<Time> timeList = shiftService.getTimeList(lineNameCn, lineType);
            response.setTimeList(timeList);
        }
        catch (Exception e){
            response.setError(1);
            response.setTimeList(null);
        }
        return response;
    }

    @RequestMapping(path="/modify")
    public HttpResponse modifySeat(@RequestParam("shiftId") String shiftId,
                                   @RequestParam("reserveSeat") int reserveSeat){
        HttpResponse response = new HttpResponse();
        try{
            shiftService.modifySeat(shiftId, reserveSeat);
            response.setMsg("success");
        }
        catch (Exception e){
            response.setMsg("fail");
            response.setError(1);
        }
        return response;
    }

    //测试方法
//    @RequestMapping(value = "/list",method = RequestMethod.GET)
//    public List<Shift> listAll(){
//        return shiftDao.findAll();
//    }
}
