package com.sjtubus.model.response;

import com.sjtubus.entity.Administrator;
import com.sjtubus.entity.Driver;
import com.sjtubus.entity.User;

/**
 * @author Allen
 * @date 2018/7/13 15:53
 */
public class ProfileResponse extends HttpResponse {

    private User user;

    private Administrator admin;

    private Driver driver;

    private String role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
