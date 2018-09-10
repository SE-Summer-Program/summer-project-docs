package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.ShiftInfo;

public class ShiftInfoResponse extends HttpResponse{

    @SerializedName("shiftInfo")
    private ShiftInfo shiftInfo;

    public ShiftInfo getShiftInfo() { return shiftInfo; }

    public void setShiftInfo(ShiftInfo shiftInfo) {
        this.shiftInfo = shiftInfo;
    }
}
