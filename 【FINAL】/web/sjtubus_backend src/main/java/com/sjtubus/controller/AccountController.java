package com.sjtubus.controller;

import com.sjtubus.dao.UserDao;
import com.sjtubus.entity.Administrator;
import com.sjtubus.entity.Driver;
import com.sjtubus.entity.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.ProfileResponse;
import com.sjtubus.service.AdministratorService;
import com.sjtubus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Action;

/**
 * @author Allen
 * @date 2018/7/13 10:49
 */

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdministratorService administratorService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public HttpResponse login(HttpServletRequest request,
                              @RequestParam("phone")String phone,
                              @RequestParam("password")String password){
        HttpResponse response = new HttpResponse();
        if(phone == null || password == null){
            response.setError(1);
            response.setMsg("登陆信息填写不全~");
            return response;
        }
        User user = userService.findUserByPhone(phone);
        if(user == null) {
            response.setError(1);
            response.setMsg("该用户不存在~");
            return response;
        } else if (!user.getPassword().equals(password)){
            response.setError(1);
            response.setMsg("手机号码或密码不正确哦~");
            return response;
        } else {
            HttpSession session = request.getSession(true);
            //session过期时间为3天
            session.setMaxInactiveInterval(60*60*24*3);
            session.setAttribute("user",user);
            session.setAttribute("role","user");
            response.setError(0);
            response.setMsg("登录成功！");
            return response;
        }
    }

    @RequestMapping(value = "/admin",method = RequestMethod.POST)
    public HttpResponse adminlogin(HttpServletRequest request,
                              @RequestParam("username")String username,
                              @RequestParam("password")String password){
        HttpResponse response = new HttpResponse();
        if(username == null || password == null){
            response.setError(1);
            response.setMsg("登陆信息填写不全~");
            return response;
        }
        Administrator admin = administratorService.findAdminByUsername(username);
        if(admin == null ){
            response.setError(1);
            response.setMsg("该管理员不存在~");
            return response;
        }else if (!admin.getPassword().equals(password)){
            response.setError(1);
            response.setMsg("用户名或密码不正确~");
            return response;
        }else {
            HttpSession session = request.getSession(true);
            //session过期时间为3天
            session.setMaxInactiveInterval(60*60*24*3);
            System.out.println("管理员登陆客户端！");
            session.setAttribute("user",admin);
            session.setAttribute("role","admin");
            response.setError(0);
            response.setMsg("登录成功！");
            return response;
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public HttpResponse register(HttpServletRequest request,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("username")String username,
                                 @RequestParam("password")String password){
        HttpResponse response = new HttpResponse();
        String token = request.getParameter("token");
        if(!token.equals("dks3824LHEBF92IUD2RG709")){
            response.setError(1);
            response.setMsg("缺少密钥!");
            return response;
        }
        User user = userService.findUserByPhone(phone);
        if(user!=null){
            response.setError(1);
            response.setMsg("该手机号已被注册~");
            return response;
        }
        String result = userService.addUser(username,password,false,phone,100);
        if(result.equals("existed")){
            response.setError(1);
            response.setMsg("该用户名已被注册~");
            return response;
        }
        response.setError(0);
        response.setMsg("注册成功!");
        return response;
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public HttpResponse logout(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        //销毁cookies
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        //销毁session
        if(session!=null) session.invalidate();
        HttpResponse response1 = new HttpResponse();
        response1.setMsg("成功登出!");
        response1.setError(0);
        return response1;
    }

    @RequestMapping(value = "/profile",method = RequestMethod.GET)
    public ProfileResponse getProfile(HttpServletRequest request){
        ProfileResponse response = new ProfileResponse();
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("user") == null){
            response.setMsg("未登录");
            response.setError(1);
            return response;
        }
        if(session.getAttribute("role").equals("user") || session.getAttribute("role").equals("jaccountuser")) {
            User user = (User) session.getAttribute("user");
            response.setUser(user);
            response.setRole((String)session.getAttribute("role"));
            response.setMsg("已登陆~"+user.getUsername());
        }else if(session.getAttribute("role").equals("admin")){
            Administrator admin = (Administrator) session.getAttribute("user");
            response.setAdmin(admin);
            response.setRole("admin");
            response.setMsg("已登陆~"+admin.getUsername());
        }else if(session.getAttribute("role").equals("driver")){
            Driver driver = (Driver) session.getAttribute("user");
            response.setDriver(driver);
            response.setRole("driver");
            response.setMsg("已登陆~"+driver.getUsername());
        }
        response.setError(0);
        return response;
    }

    @RequestMapping(value = "/update_infos", method = RequestMethod.POST)
    public HttpResponse updatePersonInfos(HttpServletRequest request,
                                          @RequestParam("userId") int userId,
                                          @RequestParam("phone") String phone,
                                          @RequestParam("studentnum") String studentnum,
                                          @RequestParam("realname") String realname,
                                          HttpSession session){
        HttpResponse response = new HttpResponse();
        String role = (String)session.getAttribute("role");
        if (role.equals("user")) {
            User user = (User)session.getAttribute("user");
            if (user==null || user.getUserId()!=userId) {
                response.setMsg("Id不匹配!");
                response.setError(1);
                return response;
            }
            boolean result = userService.updatePersonInfos(userId, phone, studentnum, realname);
            if (result) {
                if (studentnum != null && !studentnum.isEmpty()) {
                    user.setStudentNumber(studentnum);
                }
                if (realname != null && !realname.isEmpty()) {
                    user.setRealname(realname);
                }
                response.setMsg("完善个人信息成功!");
                response.setError(0);
                return response;
            } else {
                response.setMsg("完善个人信息失败!");
                response.setError(1);
                return response;
            }
        } else {
            response.setMsg("身份不是普通用户!");
            response.setError(1);
            return response;
        }
    }
}
