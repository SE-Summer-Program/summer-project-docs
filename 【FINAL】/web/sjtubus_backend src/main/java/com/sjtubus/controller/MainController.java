package com.sjtubus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(value = "/")
    public String root(){
        return "index";
    }

    @GetMapping(value = "/index")
    public String index(){
        return "index";
    }

    @GetMapping(value = "/adminlogin")
    public String adminLogin(){
        return "login";
    }

    @GetMapping(value = "/error")
    public String error(){
        return "error";
    }
}
