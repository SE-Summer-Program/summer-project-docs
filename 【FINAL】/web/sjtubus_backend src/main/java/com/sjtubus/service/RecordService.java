package com.sjtubus.service;

import com.sjtubus.dao.AppointmentDao;
import com.sjtubus.dao.ShiftDao;
import com.sjtubus.entity.Appointment;
import com.sjtubus.entity.Shift;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.utils.StringCalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private ShiftDao shiftDao;

    /**
     * @description: 找出跟username对的上的所有预约条目
     * @date: 2018/07/18 14:09
     * @params:
     * @return:
     */
    public List<RecordInfo> getRecordInfo(String username,String user_role){
        List<Appointment> appointments = appointmentDao.findByUserNameAndUserRole(username,user_role);
        List<RecordInfo> recordInfos = new ArrayList<>();

        System.out.println("appoint : " + appointments.size());
        if (appointments == null || appointments.size()==0){
            return null;
        }
        for (Appointment appointment : appointments) {
            Shift shift = shiftDao.findByShiftId(appointment.getShiftId());

            RecordInfo info = new RecordInfo();
            String departuredate = StringCalendarUtils.DateToString(appointment.getAppointDate());
            String departuretime = StringCalendarUtils.TimeToString(shift.getDepartureTime());
            String arrivetime = StringCalendarUtils.TimeToString(shift.getArriveTime());
            info.setLineName(shift.getLineNameCn());
            info.setDepartureDate(departuredate);
            info.setDepartureTime(departuretime);
            info.setArriveTime(arrivetime);
            info.setShiftid(shift.getShiftId());
            info.setComment(appointment.getComment());

            // info.setSubmitTime(appointment.getSubmitTime());
            info.setSubmitTime(appointment.getSubmitTimeString());
            if(appointment.getIsNormal()) {
                info.setStatus("预约已上车");
            }else{
                info.setStatus("预约未上车");
            }

//            String departure = departuredate + " " + departuretime;
//            if (!StringCalendarUtils.isBeforeCurrentTime(departure)) {
//                info.setStatus("已出行");
//                System.out.println("status : 1");
//            }
//            else if (! appointment.getIsNormal()){
//                info.setStatus("预约失败");
//                System.out.println("status : 2");
//            }
//            else if (appointment.getIsNormal()){
//                info.setStatus("预约成功");
//                System.out.println("status : 3");
//            }
//            else{
//                info.setStatus("系统错误");
//                System.out.println("status : 4");
//            }
            recordInfos.add(info);
        }
        return recordInfos;
    }
}
