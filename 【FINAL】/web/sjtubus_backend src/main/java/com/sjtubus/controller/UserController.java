package com.sjtubus.controller;
import com.sjtubus.dao.UserDao;

import com.sjtubus.entity.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.UserListResponse;
import com.sjtubus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/username")
    public List<User> getAllUsers(){
        return userService.listAllUsers();
    }

    @GetMapping(value = "/phone")
    public User getUserByphone(String phone){
        return userService.findUserByPhone(phone);
    }


    @RequestMapping(path="/search")
    public UserListResponse getRelatedUsers(@RequestParam("content") String content){
        System.out.println("hello");
        UserListResponse response = new UserListResponse();
        try{
            System.out.println("hello");
            response.setUserList(userService.getUserInfo(content));
            response.setMsg("success");
        }
        catch (Exception e){
            response.setUserList(null);
            response.setMsg("fail");
            response.setError(1);
        }
        return response;
    }


    @RequestMapping(path="/add" )
    public HttpResponse addUser(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("credit") int credit,
                                @RequestParam("phone") String phone,
                                @RequestParam("isTeacher") boolean isTeacher){
        HttpResponse response = new HttpResponse();
        String result = userService.addUser(username, password, isTeacher, phone, credit);
        if (result.equals("success")){
            response.setMsg("success");
        }
        else{
            response.setMsg("fail");
            response.setError(1);
        }
        return response;
    }


    @RequestMapping(path="/delete" )
    public HttpResponse deleteUser(@RequestParam("userId") int userId){
        HttpResponse response = new HttpResponse();
        try{
            response.setMsg(userService.deleteUser(userId));
        }
        catch (Exception e){
            response.setMsg("fail");
            response.setError(1);
        }
        return response;
    }

    @RequestMapping(path="/modify")
    public HttpResponse modifyPhone(@RequestParam("userId") int userId,
                                    @RequestParam("username") String username,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("credit") int credit){
        HttpResponse response = new HttpResponse();
        try{
            System.out.println("userId:"+userId+" phone:"+phone);
            userService.modifyUser(userId, username, phone, credit);
            response.setMsg("success");
        }
        catch (Exception e){
            response.setMsg("fail");
            response.setError(1);
        }
        return response;
    }
}
