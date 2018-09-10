package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.Administrator;
import com.sjtubus.model.Driver;
import com.sjtubus.model.User;

public class ProfileResponse extends HttpResponse {
    @SerializedName("user")
    private User user;
    @SerializedName("admin")
    private Administrator admin;
    @SerializedName("driver")
    private Driver driver;
    @SerializedName("role")
    private String role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
