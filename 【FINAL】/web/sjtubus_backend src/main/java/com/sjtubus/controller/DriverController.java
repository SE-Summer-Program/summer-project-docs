package com.sjtubus.controller;


import com.sjtubus.entity.Driver;
import com.sjtubus.entity.User;
import com.sjtubus.model.response.DriverListResponse;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(path = "/driver")
public class DriverController {

    @Autowired
    DriverService driverService;

    @RequestMapping(path="/search")
    public DriverListResponse getRelatedDrivers(@RequestParam("content") String content){
        DriverListResponse response = new DriverListResponse();
        try {
            response.setDriverList(driverService.getDriverInfo(content));
            response.setMsg("success");
        }
        catch (Exception e){
            response.setMsg("fail");
            response.setDriverList(null);
            response.setError(1);
        }
        return response;
    }

    @RequestMapping(path="/add")
    public HttpResponse addDriver(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("phone") String phone){
        HttpResponse response = new HttpResponse();
        try{
            response.setMsg( driverService.addDriver(username, password, phone));
        }
        catch (Exception e)
        {
            response.setMsg("fail");
            response.setError(1);
        }
        return response;
    }


    @RequestMapping(path="/delete" )
    public HttpResponse deleteDriver(@RequestParam("driverId") int driverId){
        System.out.println("driverId:"+driverId);
        HttpResponse response = new HttpResponse();
        try{
            response.setMsg(driverService.deleteDriver(driverId));
        }
        catch (Exception e){
            response.setMsg("fail");
            response.setError(1);
        }
        return response;
    }

    @RequestMapping(path="/modify")
    public HttpResponse modifyDriver(@RequestParam("driverId") int driverId,
                                     @RequestParam("username") String username,
                                     @RequestParam("phone") String phone){
        HttpResponse response = new HttpResponse();
        try{
            driverService.modifyDriver(driverId, username, phone);
            response.setMsg("success");
        }
        catch (Exception e)
        {
            response.setMsg("fail");
            response.setError(1);
        }
        return response;
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public HttpResponse login(HttpServletRequest request,
                              @RequestParam("username")String username,
                              @RequestParam("password")String password){
        System.out.println("收到登陆请求！");
        HttpResponse response = new HttpResponse();
        if(username == null || password == null){
            response.setError(1);
            response.setMsg("登陆信息不全~");
            return response;
        }
        Driver driver = driverService.findDriverByUsername(username);
        System.out.println("查找用户结束！");
        if(driver == null){
            response.setError(1);
            response.setMsg("该司机不存在~");
            return response;
        }else if (!driver.getPassword().equals(password)){
            response.setError(1);
            response.setMsg("用户名或密码不正确~");
            return response;
        }else {
            HttpSession session = request.getSession(true);
            //session过期时间为3天
            session.setMaxInactiveInterval(60*60*24*3);
            session.setAttribute("user",driver);
            session.setAttribute("role","driver");
            response.setError(0);
            response.setMsg("登录成功！");
            return response;
        }
    }
}
