package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.LineInfo;
import com.sjtubus.model.RecordInfo;

import java.util.List;

public class RecordInfoResponse extends HttpResponse{

    @SerializedName("recordInfos")
    private List<RecordInfo> recordInfos;

    public List<RecordInfo> getRecordInfos() {
        return recordInfos;
    }

    public void setRecordInfos(List<RecordInfo> recordInfos) {
        this.recordInfos = recordInfos;
    }

}
