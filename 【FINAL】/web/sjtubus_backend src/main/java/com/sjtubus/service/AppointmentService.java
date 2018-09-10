package com.sjtubus.service;

import com.sjtubus.dao.*;
import com.sjtubus.entity.*;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.utils.LunarUtils;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.utils.StringCalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private ShiftDao shiftDao;

    @Autowired
    private BusDao busDao;

    @Autowired
    private CollectionDao collectionDao;

    @Autowired
    private UserDao userDao;

    public List<Appointment> searchAppointment(String lineNameCn, String lineType, Time departureTime, Date appointDate){
        String time = shiftService.changeTimeToStringTime(departureTime);
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
        shiftid = shiftid + time;
        System.out.println("shiftid:"+shiftid);
        System.out.println("appointdate1:"+appointDate);
        System.out.println("appointdate2:"+StringCalendarUtils.UtilDateToSqlDate(appointDate));
        List<Appointment> appointmentList = appointmentDao.queryAppointmentByShiftIdAndAppointDate(shiftid, StringCalendarUtils.UtilDateToSqlDate(appointDate));
        return appointmentList;
    }

    /**
     * @description: 添加预约信息
     * @date: 2018/7/18 8:17
     * @params:  预约所需的用户名，预约日期，班次id，线路名
     * @return: 添加结果
     */
    public boolean addAppointment(String username,
                                  String user_role,
                                  String appoint_date,
                                  String shift_id,
                                  String line_name,
                                  String submit_time,
                                  String comment){
        Appointment appointment = new Appointment();
        java.sql.Date date = StringCalendarUtils.UtilDateToSqlDate(StringCalendarUtils.StringToDate(appoint_date));

        appointment.setAppointDate(date);
        appointment.setLineName(line_name);
        appointment.setLineNameCn(ShiftUtils.getChiLineName(line_name));
        appointment.setIsNormal(false);
        appointment.setShiftId(shift_id);
        appointment.setUserName(username);
        appointment.setSubmitTimeString(submit_time);
        appointment.setRealName("");
        appointment.setUserRole(user_role);
        appointment.setComment(comment);

        System.out.println("remain: " + getRemainSeat(shift_id,date));
        System.out.println("shiftid: " + shift_id);
        if(getRemainSeat(shift_id,date) > 0){
            appointmentDao.save(appointment);
            System.out.println("hello");
            return true;
        }else return false;
    }

    /**
     * @description: 删除预约信息
     * @date: 2018/7/25 21:35
     * @params:
     * @return:
     */
    public String deleteAppointment(String username, String user_role,String shiftid, String appoint_date){
        java.sql.Date date = StringCalendarUtils.UtilDateToSqlDate(StringCalendarUtils.StringToDate(appoint_date));

        Appointment oldappointment = appointmentDao.findByUserNameAndUserRoleAndShiftIdAndAppointDate(username,user_role, shiftid, date);
        if(oldappointment != null ){
            appointmentDao.delete(oldappointment);
            return "success";
        }
        else {
            return "fail";
        }
    }

    /**
     * @description: 符合当前线路和时间段类型的所有shift班次
     * @date: 2018/7/16 13:00
     * @params:
     * @return: List<AppointInfo>
     */
    public List<AppointInfo> getAppointInfo(String line_name, String type, String appoint_date,boolean AdminOrDriver){

        List<Shift> shifts =  shiftDao.findByLineTypeAndLineNameOrderByDepartureTime(type, line_name);
        List<AppointInfo> appointInfos = new ArrayList<>();

        if (shifts == null || shifts.size()==0) {
            return new ArrayList<>();
        }

        String legalholiday = LunarUtils.isLegalHoliday(appoint_date);
        if (! legalholiday.equals("无")){
            return new ArrayList<>();
        }

        for (Shift shift : shifts) {
            String departure_time = shift.getDepartureTime().toString();
            //比较发车时间和当前时间的先后
            String departure = appoint_date + " " + departure_time;
            if (!AdminOrDriver && StringCalendarUtils.isBeforeCurrentTime(departure))
                continue;

            AppointInfo info = new AppointInfo();
            info.setShiftId(shift.getShiftId());
            info.setDepartureTime(departure_time);
            info.setArriveTime(shift.getArriveTime().toString());
            //获取当前班次剩余可预约座位数（减去预留座位和已经被预约的座位）
            info.setRemainSeat(getRemainSeat(shift.getShiftId(), java.sql.Date.valueOf(appoint_date)));

            appointInfos.add(info);
        }

        return appointInfos;
    }

    /**
     * @description: 获取当前班次剩余可预约座位数（减去预留座位和已经被预约的座位）
     * @date: 2018/07/16 13:00
     * @params:
     * @return:
     */
    public int getRemainSeat(String shiftId, java.sql.Date appoint_date){
        List<Appointment> appointments = appointmentDao.findByShiftIdAndAppointDate(shiftId, appoint_date);
        Shift shift = shiftDao.findByShiftId(shiftId);
        Bus bus = busDao.findByBusId(shift.getBusId());
        int totalSeat = bus.getSeatNum();
        int reserveSeat = shift.getReserveSeat();
        int appointedSeat = appointments.size();
        return totalSeat - reserveSeat - appointedSeat;
    }

    /**
     * @description: 验证用户已上车
     * @date: 2018/07/16 13:00
     * @params:
     * @return:
     */
    public String verifyAppointment(String username,String departure_date,String shift_id){
        Appointment appointment = appointmentDao.queryAppointmentByShiftIdAndAppointDateAndUserName(shift_id, java.sql.Date.valueOf(departure_date),username);
        Shift shift = shiftDao.findByShiftId(shift_id);
        if(appointment == null){
            return "您没有预约该班次~";
        } else if (StringCalendarUtils.isBeforeCurrentTime
                (StringCalendarUtils.addHalfHour(StringCalendarUtils.TimeToString(shift.getArriveTime())))) {
            return "班车已到站";
        } else if (! StringCalendarUtils.isBeforeCurrentTime
                (StringCalendarUtils.minusHalfHour(StringCalendarUtils.TimeToString(shift.getDepartureTime())))) {
            return "班车未发出";
        } else{
            appointment.setIsNormal(true);
            Appointment result = appointmentDao.save(appointment);
            System.out.println(result.getAppointDate()+" "+result.getUserName()+" Normal:"+result.getIsNormal());
            return "验证成功~";
        }
    }

}
