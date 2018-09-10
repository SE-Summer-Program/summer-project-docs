package com.sjtubus;

import com.sjtubus.dao.LineDao;
import com.sjtubus.entity.TimeTable;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.LineInfo;
import com.sjtubus.service.LineService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author allen
 * @date 2018/7/25 1:51
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LineServiceTest extends SjtubusApplicationTests{
    @Autowired
    private LineService lineService;
    @Autowired
    private LineDao lineDao;

    @Test
    public void a_testGetStationByLineName() {
        String line_name = "MinHangToQiBao";
        List<TimeTable> stations = lineService.getStationByLineName(line_name);
        Assert.assertEquals("fail",2,stations.size());
        Assert.assertEquals("fail","闵行",stations.get(0).getStation());
        Assert.assertEquals("fail","七宝",stations.get(1).getStation());
    }

    @Test
    public void b_testGetLineInfo() {
        String line_name = "MinHangToQiBao";
        String type = "HolidayWorkday";
        LineInfo info = lineService.getLineInfo(type,line_name);
        Assert.assertEquals("fail",1,info.getRemainShift());
        Assert.assertEquals("fail","08:00:00",info.getFirstTime());
        Assert.assertEquals("fail","20:30:00",info.getLastTime());
    }

    @Test
    public void c_testGetAllLineName() {
        String type = "HolidayWorkday";
        List<String> names = lineService.getAllLineName();
        Assert.assertEquals("fail",6,names.size());
    }

}
