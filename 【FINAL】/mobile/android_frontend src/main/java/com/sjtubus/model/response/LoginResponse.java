package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.User;

public class LoginResponse extends HttpResponse {
    @SerializedName("user_profile")
    private User user;

    public User getUser() {
        return user;
    }
}
