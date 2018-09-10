package com.sjtubus.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Allen on 2018/7/5.
 */

public class HttpResponse {
    @SerializedName("error")
    private int error;
    @SerializedName("msg")
    private String msg;

    @Override
    public String toString() {
        return "error: " + error + ", msg: " + msg;
    }

    public int getError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }

}
