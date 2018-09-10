package com.sjtubus.model.response;

import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.RecordInfo;

import java.util.List;

public class RecordInfoResponse extends HttpResponse{

    private List<RecordInfo> recordInfos;

    public List<RecordInfo> getRecordInfos() {
        return recordInfos;
    }

    public void setRecordInfos(List<RecordInfo> recordInfos) {
        this.recordInfos = recordInfos;
    }

}
