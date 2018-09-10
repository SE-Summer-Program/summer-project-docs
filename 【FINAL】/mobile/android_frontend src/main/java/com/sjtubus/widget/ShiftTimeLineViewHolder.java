package com.sjtubus.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.sjtubus.R;

public class ShiftTimeLineViewHolder extends RecyclerView.ViewHolder {
    private TextView shiftTime;
    private TextView shiftComment;

    ShiftTimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        TimelineView mTimelineView = itemView.findViewById(R.id.time_marker);
        shiftTime = itemView.findViewById(R.id.shift_time);
        shiftComment = itemView.findViewById(R.id.shift_comment);
        mTimelineView.initLine(viewType);
    }

    public void setData(String time,String comment){
        shiftTime.setText(time+" 发车");
        if(shiftComment!=null) shiftComment.setText(comment);
    }

}
