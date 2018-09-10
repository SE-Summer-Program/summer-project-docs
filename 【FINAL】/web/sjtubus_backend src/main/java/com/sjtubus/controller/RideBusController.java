package com.sjtubus.controller;

import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.service.RideBusInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author Allen
 * @date 2018/9/6 15:27
 */

@RestController
@RequestMapping(value = "/ridebusinfo")
public class RideBusController {

    @Autowired
    private RideBusInfoService rideBusInfoService;

    @RequestMapping(value="/import",method = RequestMethod.POST)
    public HttpResponse importRideInfo(@RequestParam("ride_date")String ride_date,
                                       @RequestParam("shift_id") String shift_id,
                                       @RequestParam("bus_plate") String bus_plate,
                                       @RequestParam("line_type") String line_type,
                                       @RequestParam("teacher_num") int teacher_num,
                                       @RequestParam("student_num") int student_num,
                                       @RequestParam("remain_num") int remain_num,
                                       @RequestParam("seat_num")int seat_num, HttpSession session){
        HttpResponse response = new HttpResponse();
        String role = (String)session.getAttribute("role");
        if(role==null||!role.equals("admin")){
            response.setMsg("非管理员操作！");
            response.setError(1);
        }
        String result = rideBusInfoService.addRideBusInfo(ride_date,shift_id,bus_plate,line_type,teacher_num,student_num,remain_num,seat_num);
        if(!result.equals("success")){
            response.setError(1);
        }
        response.setMsg(result);
        return response;
    }
}
