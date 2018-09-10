package com.sjtubus.controller;

import com.sjtubus.entity.Appointment;
import com.sjtubus.entity.User;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.response.AppointInfoResponse;
import com.sjtubus.model.response.AppointmentResponse;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.service.AppointmentService;
import com.sjtubus.service.RecordService;
import com.sjtubus.utils.StringCalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/appointment")
public class AppointmentController {

    @Autowired
    RecordService recordService;

    @Autowired
    AppointmentService appointmentService;

    @RequestMapping(path = "/search")
    public AppointmentResponse searchAppointment(@RequestParam("lineNameCn") String lineNameCn,
                                                 @RequestParam("lineType") String lineType,
                                                 @RequestParam("departureTime") Time departureTime,
                                                 @RequestParam("appointDate") String appointDate) {
        AppointmentResponse response = new AppointmentResponse();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
            Date date =  formatter.parse(appointDate);
            System.out.println("date:"+date);
            List<Appointment> appointmentList = appointmentService.searchAppointment(lineNameCn, lineType, departureTime, date);
            response.setAppointmentList(appointmentList);
            response.setError(0);
        }
        catch(Exception e){
            response.setError(1);
        }
        return response;
    }

    /**
     * @description:
     * @date:
     * @params:
     * @return:
     */
    @RequestMapping(value = "/infos")
    public AppointInfoResponse getAppointInfo(String line_name,
                                              String type,
                                              String appoint_date,
                                              HttpSession session){
        String role = (String)session.getAttribute("role");
        AppointInfoResponse response = new AppointInfoResponse();
        List<AppointInfo> appoints;
        if(role!=null&&(role.equals("admin")||role.equals("driver"))){
            appoints = appointmentService.getAppointInfo(line_name, type, appoint_date,true);
        }else {
            appoints = appointmentService.getAppointInfo(line_name, type, appoint_date,false);
        }
        response.setAppointInfos(appoints);
        return response;
    }

    /**
     * @description: 获取与username用户相关的所有预约记录
     * @date:
     * @params:
     * @return:
     */
    @RequestMapping(value = "/record",method = RequestMethod.GET)
    public RecordInfoResponse getRecordInfos(String username,String user_role){
        String current_time = StringCalendarUtils.getCurrentTime();
        RecordInfoResponse response = new RecordInfoResponse();
        List<RecordInfo> recordInfos;
        recordInfos = recordService.getRecordInfo(username,user_role);
        response.setRecordInfos(recordInfos);
        return response;
    }

    /**
     * @description: 用户提交预约申请
     * @date: 2018/7/18 8:10
     * @params:
     * @return: HttpResponse 返回预约反馈信息
     */
    @RequestMapping(value = "/appoint",method = RequestMethod.POST)
    public HttpResponse appoint(String username,
                                String user_role,
                                String appoint_date,
                                String shift_id,
                                String line_name,
                                String submit_time,
                                String comment){
        HttpResponse response  = new HttpResponse();
        boolean result = appointmentService.addAppointment(username,user_role,appoint_date,shift_id,line_name, submit_time, comment);
        if(result){
            response.setMsg("预约成功!");
            response.setError(0);
            return response;
        }else{
            response.setMsg("预约失败!");
            response.setError(1);
            return response;
        }
    }

    /**
     * @description: 用户通过扫码验证已上车
     * @date: 2018/7/23 2:00
     * @params: 用户名，上车日期，班次id
     * @return: response
     */
    @RequestMapping(value = "/verify",method = RequestMethod.POST)
    public HttpResponse verifyUser(String username,
                                   String departure_date,
                                   String shift_id, HttpSession session){
        HttpResponse response = new HttpResponse();
        String role = (String)session.getAttribute("role");
        if(role==null||!role.equals("admin")){
            response.setMsg("非管理员操作！");
            response.setError(1);
            return response;
        }
        String result = appointmentService.verifyAppointment(username,departure_date,shift_id);
        response.setMsg(result);
        return response;
    }

    /**
     * @description: 取消预约
     * @date: 2018/7/25 21:32
     * @params:
     * @return:
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public HttpResponse deleteAppoint(String username,
                                      String user_role,
                                      String shiftid,
                                      String appoint_date){
        HttpResponse response = new HttpResponse();
        String result = appointmentService.deleteAppointment(username,user_role, shiftid, appoint_date);
        response.setMsg(result);
        return response;
    }

}
