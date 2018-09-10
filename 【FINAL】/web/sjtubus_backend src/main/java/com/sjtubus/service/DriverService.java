package com.sjtubus.service;

import com.sjtubus.dao.DriverDao;
import com.sjtubus.entity.Driver;
import com.sjtubus.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverService {
    @Autowired
    DriverDao driverDao;

    /**
     * @description: 根据content比对数据库中的driver，找出username或者phone包含content字段的driver
     * @date: 2018/7/18 16:54
     * @params: content
     * @return: Driver列表
    */
    public List<Driver> getDriverInfo(String content){
        return driverDao.queryByRelatedContent(content);
    }


    /**
     * @description: 添加新司机
     * @date: 2018/7/19 21:25
     * @params: 用户名，密码，电话
     * @return: 字符串，添加成功返回success，若用户名已存在则返回existed
    */
    public String addDriver(String username, String password, String phone){
        Driver oldDriver = driverDao.queryDriverByUsername(username);
        if (oldDriver == null){
            Driver newDriver = new Driver();
            newDriver.setUsername(username);
            newDriver.setPassword(password);
            newDriver.setPhone(phone);
            driverDao.save(newDriver);
            return "success";
        }
        else
            return "existed";
    }


    /**
     * @description: 删除司机用户
     * @date: 2018/7/19 21:52
     * @params: driverID
     * @return: 字符串，成功为success，失败为fail
    */
    public String deleteDriver(int driverId){
        Driver olddriver = driverDao.queryDriverByDriverId(driverId);
        if( olddriver != null ){
            driverDao.delete(olddriver);
            return "success";
        }
        else {
            return "fail";
        }
    }

    /**
     * @description: 根据driverId找到对应的司机，将其用户名和电话改成新的值
     * @date: 2018/7/18 19:04
     * @params: 司机ID， 司机用户名， 司机电话
     * @return: 修改的条目数
    */
    public int modifyDriver(int driverId, String username, String phone){
        return driverDao.modifyDriver(driverId, username, phone);
    }

    public Driver findDriverByUsername(String username){
        return driverDao.findByUsername(username);
    }
}
