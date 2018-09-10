package com.sjtubus;

import com.sjtubus.dao.AppointmentDao;
import com.sjtubus.entity.Appointment;
import com.sjtubus.entity.JaccountUser;
import com.sjtubus.entity.User;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.service.AppointmentService;
import com.sjtubus.utils.StringCalendarUtils;
import edu.sjtu.api.applicationToolkit.model.App;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.sql.Time;
import java.util.List;

import static com.sjtubus.utils.StringCalendarUtils.*;

/**
 * @author allen
 * @date 2018/7/24 14:47
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppointmentServiceTest extends SjtubusApplicationTests {

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AppointmentDao appointmentDao;

    @Test
    public void a_testGetAppointInfo() {
        String line_name = "MinHangToQiBao";
        String type = "HolidayWorkday";
        String appoint_date = "2018-09-30";
        List<AppointInfo> infos = appointmentService.getAppointInfo(line_name,type,appoint_date,true);
        Assert.assertEquals("fail",4,infos.size());
        Assert.assertEquals("fail","12:20:00",infos.get(1).getDepartureTime());
        Assert.assertEquals("fail","MQHD1220",infos.get(1).getShiftId());
        Assert.assertEquals("fail",45,infos.get(1).getRemainSeat());
    }

    @Test
    public void c_testSearchAppointment() {
        String lineNameCN = "闵行到七宝";
        String line_type = "HolidayWorkday";
        Time time = new Time(17,0,0);
        Date date = new Date(118,6,27);
        System.out.println("date:" + date);
        List<Appointment> appointments =
                appointmentService.searchAppointment(lineNameCN,line_type,time,date);
        Assert.assertEquals("fail",1,appointments.size());
    }

    @Test
    public void d_testVerifyAppointment() {
        String username = "姚子航";
        String departure_date = "2018-07-27";
        String shift_id = "MQHD1700";

        Appointment appointment = appointmentDao.queryAppointmentByShiftIdAndAppointDateAndUserName(
                shift_id,UtilDateToSqlDate(StringToDate(departure_date)),username);
        System.out.println(appointment.getIsNormal());
        Assert.assertTrue("fail", appointment.getIsNormal());

        String result = appointmentService.verifyAppointment(username,departure_date,shift_id);
        System.out.println(result);
        Assert.assertEquals("fail","验证成功~",result);

        appointment = appointmentDao.queryAppointmentByShiftIdAndAppointDateAndUserName(
                shift_id,UtilDateToSqlDate(StringToDate(departure_date)),username);
        System.out.println(appointment.getShiftId());
        System.out.println(appointment.getIsNormal());
        Assert.assertTrue("fail", appointment.getIsNormal());
    }
}
