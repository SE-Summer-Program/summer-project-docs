package com.sjtubus.model.response;

import com.sjtubus.model.AppointInfo;

import java.util.List;

public class AppointInfoResponse extends HttpResponse {

    private List<AppointInfo> appointInfos;

    public List<AppointInfo> getAppointInfos() {
        return appointInfos;
    }

    public void setAppointInfos(List<AppointInfo> appointInfos) {
        this.appointInfos = appointInfos;
    }

}
