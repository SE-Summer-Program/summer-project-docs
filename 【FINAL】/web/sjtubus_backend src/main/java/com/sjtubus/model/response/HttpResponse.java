package com.sjtubus.model.response;

import java.io.Serializable;

public class HttpResponse implements Serializable{
    private int error = 0;
    private String msg = "";

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
