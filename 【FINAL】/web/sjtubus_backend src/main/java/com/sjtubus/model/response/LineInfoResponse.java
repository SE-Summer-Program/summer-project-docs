package com.sjtubus.model.response;

import com.sjtubus.model.LineInfo;

import java.util.List;

public class LineInfoResponse extends HttpResponse {
    public List<LineInfo> getLineInfos() {
        return lineInfos;
    }

    public void setLineInfos(List<LineInfo> lineInfos) {
        this.lineInfos = lineInfos;
    }

    private List<LineInfo> lineInfos;

}
