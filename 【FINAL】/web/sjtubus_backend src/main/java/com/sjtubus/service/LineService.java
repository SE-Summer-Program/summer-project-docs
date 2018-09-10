package com.sjtubus.service;

import com.sjtubus.dao.LineDao;
import com.sjtubus.dao.ShiftDao;
import com.sjtubus.dao.TimeTableDao;
import com.sjtubus.entity.Line;

import java.util.ArrayList;
import java.util.List;

import com.sjtubus.entity.Shift;
import com.sjtubus.entity.TimeTable;
import com.sjtubus.model.LineInfo;
import com.sjtubus.utils.StringCalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineService {
    @Autowired
    private LineDao lineDao;

    @Autowired
    private ShiftDao shiftDao;

    @Autowired
    private TimeTableDao timeTableDao;

    /**
     * @description: 根据线路名称获取站点列表
     * @date: 2018/7/10 18:41
     * @params: line_name - 线路名称
     * @return: 站点列表（String）
    */
    @Cacheable(cacheNames = "TimeTable",key = "#line_name")
    public List<TimeTable> getStationByLineName(String line_name) {
    	List<String> station_names = lineDao.findByName(line_name).getStation();
    	List<TimeTable> stations = new ArrayList<>(station_names.size());
    	for(String station_name: station_names){
    	    stations.add(timeTableDao.findByStation(station_name));
        }
        return stations;
    }

    /**
     * @description: 根据type与name获取首班，末班，座位数
     * @date: 2018/7/10 19:14
     * @params: type - 类别 line_name - 名称
     * @return: LineInfo - 线路信息
    */
    @Transactional
    @Cacheable(cacheNames = "LineInfo",key = "#type+#line_name")
    public LineInfo getLineInfo(String type,String line_name){
        List<Shift> shifts = shiftDao.findByLineTypeAndLineNameOrderByDepartureTime(type,line_name);
        LineInfo info = new LineInfo();
        info.setLineName(line_name);
        if(shifts == null || shifts.size()==0){
            return null;
        }
        info.setFirstTime(shifts.get(0).getDepartureTime().toString());
        info.setLastTime(shifts.get(shifts.size()-1).getDepartureTime().toString());
        info.setLineNameCN(shifts.get(0).getLineNameCn());
        info.setRemainShift(0);

        String currentdate = StringCalendarUtils.getCurrentDate();
        for (Shift shift : shifts){
            String departuretime = shift.getDepartureTime().toString();
            if (StringCalendarUtils.isBeforeCurrentTime(currentdate + " " + departuretime))
                continue;
            info.addRemainShift();
        }
        return info;
    }

    /**
     * @description: 获取所有线路名称
     * @date: 2018/7/10 18:40
     * @params: type - 线路类型
     * @return: 线路名列表
    */
    @Transactional
    @Cacheable(cacheNames = "AllLineName")
    public List<String> getAllLineName(){
        List<String> result = new ArrayList<>();
        List<Line> lines = lineDao.findAll();
        for (Line line:lines){
            String line_name = line.getName();
            result.add(line_name);
        }
        return result;
    }
}
