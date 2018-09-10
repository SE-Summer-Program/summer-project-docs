package com.sjtubus.model.response;

import com.sjtubus.entity.User;

import java.util.List;

public class UserListResponse extends HttpResponse{
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
