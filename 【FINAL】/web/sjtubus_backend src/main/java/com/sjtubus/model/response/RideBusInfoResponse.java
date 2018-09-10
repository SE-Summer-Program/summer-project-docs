package com.sjtubus.model.response;

import com.sjtubus.entity.RideBusInfo;
import com.sjtubus.model.AppointInfo;

import java.util.List;

/**
 * @author Allen
 * @date 2018/9/4 14:33
 */
public class RideBusInfoResponse extends HttpResponse{

    private List<RideBusInfo> rideBusInfos;

    public List<RideBusInfo> getRideBusInfos() {
        return rideBusInfos;
    }

    public void setRideBusInfos(List<RideBusInfo> rideBusInfos) {
        this.rideBusInfos = rideBusInfos;
    }
}
