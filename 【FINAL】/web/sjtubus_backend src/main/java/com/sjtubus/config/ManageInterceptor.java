package com.sjtubus.config;

import com.sjtubus.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManageInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println(request.getRequestURI());
        //登录不做拦截
        if(request.getRequestURI().equals("/user/add") || request.getRequestURI().equals("/book/add")
                || request.getRequestURI().equals("/user/users") || request.getRequestURI().equals("/book/modify")
                || request.getRequestURI().equals("/order/orders"))
        {
            //验证session是否存在
            Object obj = request.getSession().getAttribute("user");
            if(obj == null)
            {
                response.sendRedirect("/login_warning");
                return false;
            }else{
                User user = (User)obj;
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
