package com.sjtubus;

import com.sjtubus.dao.ShiftDao;
import com.sjtubus.model.LineInfo;
import com.sjtubus.model.Schedule;
import com.sjtubus.model.ShiftInfo;
import com.sjtubus.service.ShiftService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.util.List;

import static com.sjtubus.utils.StringCalendarUtils.*;

/**
 * @author allen
 * @date 2018/7/25 2:04
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShiftServiceTest extends SjtubusApplicationTests {
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private ShiftDao shiftDao;

    @Test
    public void a_testGetSchedule() {
        String line_name = "MinHangToQiBao";
        String type = "HolidayWorkday";
        Schedule schedule = shiftService.getSchedule(type,line_name);
        Assert.assertEquals("fail",4,schedule.getScheduleShift().size());
        Assert.assertEquals("fail","08:00:00",schedule.getScheduleTime().get(0));
    }

    @Test
    public void b_testGetShiftInfo() {
        String shift_id = "MQHD1700";
        ShiftInfo info = shiftService.getShiftInfo(shift_id);
        Assert.assertEquals("fail",50,info.getBusSeatNum());
        Assert.assertEquals("fail","17:00:00",info.getDepartureTime());
    }

    @Test
    public void c_testChangeTypeToId() {
        String line_type = "NormalWorkday";
        String id = shiftService.changeTypeToId(line_type);
        Assert.assertEquals("fail","WD",id);

        line_type = "NormalWeekendAndLegalHoliday";
        id = shiftService.changeTypeToId(line_type);
        Assert.assertEquals("fail","WE",id);

        line_type = "HolidayWorkday";
        id = shiftService.changeTypeToId(line_type);
        Assert.assertEquals("fail","HD",id);

        line_type = "yaozihang";
        id = shiftService.changeTypeToId(line_type);
        Assert.assertEquals("fail","HE",id);
    }

    @Test
    public void d_testChangeTimeToStringTime() {
        Time time = new Time(17,0,0);
        String timestr = shiftService.changeTimeToStringTime(time);
        Assert.assertEquals("fail","1700",timestr);
    }

    @Test
    public void e_testModifySeat() {
        String shift_id = "MQHD1700";
        int num = shiftService.modifySeat(shift_id,100);
        Assert.assertEquals("fail",1,num);
        num = shiftService.modifySeat(shift_id,45);
    }

    @Test
    public void f_testGetTimeList() {
        String lineNameCn = "闵行到七宝";
        String type = "HolidayWorkday";
        List<Time> times = shiftService.getTimeList(lineNameCn,type);

        Assert.assertEquals("fail",4,times.size());
        Assert.assertEquals("fail","12:20:00",TimeToString(times.get(1)));
    }
}
