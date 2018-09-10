package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.AppointShortInfo;

import java.util.List;

public class AppointResponse extends HttpResponse {

    @SerializedName("appointInfos")
    private List<AppointShortInfo> appointInfoList;

    public List<AppointShortInfo> getAppointment() {
        return appointInfoList;
    }

    public void setAppointment(List<AppointShortInfo> appointInfoList) {
        this.appointInfoList = appointInfoList;
    }

}
