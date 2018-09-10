package com.sjtubus.service;

import com.sjtubus.dao.BusDao;
import com.sjtubus.dao.DriverDao;
import com.sjtubus.dao.ShiftDao;
import com.sjtubus.dao.TimeTableDao;
import com.sjtubus.entity.Bus;
import com.sjtubus.entity.Driver;
import com.sjtubus.entity.Shift;
import com.sjtubus.entity.TimeTable;
import com.sjtubus.model.Schedule;
import com.sjtubus.model.ShiftInfo;
import com.sjtubus.utils.StringCalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService {
    @Autowired
    private ShiftDao shiftDao;

    @Autowired
    private BusDao busDao;

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private TimeTableDao timeTableDao;

    /**
     * @description: 将查询到的shift列表重新组织成schedule对象
     * @date: 2018/7/10 19:33
     * @params: type - 类别 line_name - 名称
     * @return: Schedule - Schedule对象
    */
    @Transactional
    @Cacheable(cacheNames = "Schedule",key = "#type+#line_name")
    public Schedule getSchedule(String type, String line_name){
        List<Shift> shiftInfo = shiftDao.findByLineTypeAndLineNameOrderByDepartureTime(type, line_name);
        List<String> startTimeList = new ArrayList<>();
        List<String> commentList = new ArrayList<>();
        List<String> shiftidList = new ArrayList<>();
        int size = shiftInfo.size();
        for (int i = 0; i < size; i++ ){
            String startTime = shiftInfo.get(i).getDepartureTime().toString();
            String comment = shiftInfo.get(i).getComment();

            /* 防止重复处理 */
            if (startTimeList.size() >= 1) {
                String lastStartTime = startTimeList.get(startTimeList.size() - 1);
                String lastComment = commentList.get(commentList.size() - 1);
                if (startTime.equals(lastStartTime) && comment.equals(lastComment))
                    continue;
            }

            startTimeList.add(startTime);
            commentList.add(comment);
            String shiftid = shiftInfo.get(i).getShiftId();
            shiftidList.add(shiftid);
        }
        //System.out.println("startTimeListSize:"+startTimeList.size());
        Schedule result = new Schedule(line_name, type);
        result.setLineName(line_name);
        result.setTypes(type);
        result.setScheduleTime(startTimeList);
        result.setScheduleComment(commentList);
        result.setScheduleShift(shiftidList);
        return result;
    }

    /**
     * @description: 通过shiftid获取某一班次的详细信息
     * @date: 2018/7/22 11：07
     * @params: shiftid
     * @return: ShiftInfo对象
     */
    @Cacheable(cacheNames = "ShiftInfo",key = "#shiftid")
    public ShiftInfo getShiftInfo(String shiftid){
        ShiftInfo shiftInfo = new ShiftInfo();
        Shift shift = shiftDao.findByShiftId(shiftid);
        Bus bus = busDao.findByBusId(shift.getBusId());
        Driver driver = driverDao.findByDriverId(bus.getDriverId());
        shiftInfo.setShiftid(shiftid);
        shiftInfo.setLineName(shift.getLineName());
        shiftInfo.setLineNameCn(shift.getLineNameCn());
        shiftInfo.setDepartureTime(StringCalendarUtils.TimeToString(shift.getDepartureTime()));
        shiftInfo.setArriveTime(StringCalendarUtils.TimeToString(shift.getArriveTime()));
        shiftInfo.setBusPlateNum(bus.getPlateNum());
        shiftInfo.setBusSeatNum(bus.getSeatNum());
        shiftInfo.setDriverName(driver.getUsername());
        shiftInfo.setDriverPhone(driver.getPhone());
        shiftInfo.setComment(shift.getComment());
        return shiftInfo;
    }

    /**
     * @description: 管理员通过该添加班次，先根据参数解析成id，然后存入数据库，返回一个字符串
     * @date: 2018/7/11 23:28
     * @params: lineName, lineNameCn, lineType, departureTime, reserveSeat, comment
     * @return:
    */
    @CacheEvict(cacheNames = {"Schedule","LineInfo"},key = "#lineType+#lineName")
    public String addShift(String lineName, String lineNameCn, String lineType, Time departureTime, int reserveSeat, String comment, int busId, Time arriveTime){
        //生成对应的id
        System.out.println("departure:"+departureTime);
        String departure = changeTimeToStringTime(departureTime);
        String shiftid;
        if(lineName.equals("LoopLineAntiClockwise")){
            if (lineType.equals("HolidayWorkday"))
                shiftid = "LLAH" + departure;
            else
                shiftid = "LLAW" + departure;
        }
        else if (lineName.equals("LoopLineClockwise")){
            if (lineType.equals("HolidayWorkday"))
                shiftid = "LLCH" + departure;
            else
                shiftid = "LLCW" + departure;
        }
        else{
            if (lineName.equals("MinHangToQiBao"))
                shiftid = "MQ";
            else if (lineName.equals("MinHangToXuHui"))
                shiftid = "MX";
            else if (lineName.equals("QiBaoToMinHang"))
                shiftid = "QM";
            else if (lineName.equals("XuHuiToMinHang"))
                shiftid = "XM";
            else if (lineName.equals("XuHuiToQiBao"))
                shiftid = "XQ";
            else
                shiftid = "QX";
            shiftid = shiftid + changeTypeToId(lineType);
            shiftid = shiftid + departure;
        }

        List<Shift> sameShifts = shiftDao.queryByLinetypeAndLinenameAndDepartureTime(lineType, lineName, departureTime);
        //System.out.println(sameShifts.size());
        int sum = sameShifts.size();
        if (sum == 1){
            if (lineName.equals("LoopLineAntiClockwise") || lineName.equals("LoopLineClockwise"))
                return "existed";
            String newShiftid = shiftid + "A";
            System.out.println("new shiftid:" + newShiftid);
            Shift oldShift = sameShifts.get(0);
            int oldSeat = oldShift.getReserveSeat();
            String oldComment = oldShift.getComment();
            shiftDao.delete(oldShift);
            Shift newShift = new Shift();
            newShift.setShiftId(newShiftid);
            newShift.setLineName(lineName);
            newShift.setLineNameCn(lineNameCn);
            newShift.setLineType(lineType);
            newShift.setReserveSeat(oldSeat);
            newShift.setDepartureTime(departureTime);
            newShift.setComment(oldComment);
            newShift.setBusId(busId);
            newShift.setArriveTime(arriveTime);
            shiftDao.save(newShift);
            shiftid = shiftid + "B";
        }
        else if (sum > 1){
            char letter = (char)(sum + 65);
            shiftid = shiftid + letter;
        }
        //System.out.println(shiftid);
        //存储新的shift对象
        Shift shift = new Shift();
        shift.setShiftId(shiftid);
        shift.setLineName(lineName);
        shift.setLineNameCn(lineNameCn);
        shift.setDepartureTime(departureTime);
        shift.setLineType(lineType);
        shift.setReserveSeat(reserveSeat);
        shift.setComment(comment);
        shift.setBusId(busId);
        shift.setArriveTime(arriveTime);
        shiftDao.save(shift);
        return "success";
    }

/**
 * @description: 根据content去数据库进行比对，凡是班次信息中含有content的内容的则将它返回给用户
 * @date: 2018/7/17 17:10
 * @params: content
 * @return: 含有关键字的班次列表
*/
    public List<Shift> searchShift(String content){
        return shiftDao.queryByRelatedContent(content);
    }

    @CacheEvict(cacheNames = {"Schedule","LineInfo"},allEntries = true)
    public String deleteShift(String shiftId){
        System.out.println("delete:"+ shiftId);
        Shift oldShift = shiftDao.queryShiftByShiftId(shiftId);
        shiftDao.delete(oldShift);
        return "success";
    }

    public List<Time> getTimeList(String lineNameCn, String lineType){
        List<Time> timeList =  shiftDao.getTimeListByLineNameCnAndLineType(lineType, lineNameCn);
        List<Time> result = new ArrayList<>();
        int size = timeList.size();
        for (int i = 0; i < size; i++){
            Time time = timeList.get(i);
            if (!result.contains(time)){
                result.add(time);
            }
        }
        return result;
    }


    /**
     * @description: 根据shiftid寻找班次，修改其预约座位数为reserveSeat
     * @date: 2018/7/17 21:07
     * @params: 班次号shiftid，预约座位数reserveSeat
     * @return: 修改条目数
    */
    @CacheEvict(cacheNames = "ShiftInfo",key = "#shiftId")
    public int modifySeat(String shiftId, int reserveSeat){
        return shiftDao.updateReserveSeat(reserveSeat, shiftId);
    }


    /**
     * @description: 将线路类型转换成shiftid中的类型部分，仅限于校区间巴士
     * @date: 2018/7/15 11:41
     * @params: lineType
     * @return: shiftidType 如WD、WE...
     */
    public String changeTypeToId(String lineType){
        String shiftidType = "";
        switch(lineType) {
            case "NormalWorkday":
                shiftidType = "WD";
                break;
            case "NormalWeekendAndLegalHoliday":
                shiftidType = "WE";
                break;
            case "HolidayWorkday":
                shiftidType = "HD";
                break;
            default:
                shiftidType = "HE";
        }
        return shiftidType;
    }

    /**
     * @description: 将一个Time类型的出发时间转换成shiftid中的时间格式
     * @date: 2018/7/15 11:57
     * @params: departureTime，如08:00:00
     * @return: 字符串型的时间，如0800
     */
    public String changeTimeToStringTime(Time departureTime){
        String tempHour = String.valueOf(departureTime.getHours());
        String tempMinute = String.valueOf(departureTime.getMinutes());
        int hourLen = tempHour.length();
        int minuteLen = tempMinute.length();
        String tempZero = "";
        for (int i = 0; i < (2-hourLen); i++){
            tempZero += '0';
        }
        tempHour = tempZero + tempHour;
        tempZero = "";
        for (int i = 0; i < (2-minuteLen); i++){
            tempZero += '0';
        }
        tempMinute = tempZero + tempMinute;
        return tempHour + tempMinute;

    }
}
