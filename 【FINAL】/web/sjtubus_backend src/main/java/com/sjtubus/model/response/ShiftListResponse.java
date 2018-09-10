package com.sjtubus.model.response;

import com.sjtubus.entity.Shift;
import com.sjtubus.model.Schedule;

import java.util.List;

public class ShiftListResponse extends  HttpResponse{

    private List<Shift> shiftList;

    public List<Shift> getShiftList() {
        return shiftList;
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }
}
