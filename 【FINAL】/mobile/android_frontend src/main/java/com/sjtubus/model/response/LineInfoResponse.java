package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;
import com.sjtubus.model.LineInfo;

import java.util.List;

public class LineInfoResponse extends HttpResponse {

    @SerializedName("lineInfos")
    private List<LineInfo> lineInfos;

    public List<LineInfo> getLineInfos() {
        return lineInfos;
    }

    public void setLineInfos(List<LineInfo> lineInfos) {
        this.lineInfos = lineInfos;
    }

}
