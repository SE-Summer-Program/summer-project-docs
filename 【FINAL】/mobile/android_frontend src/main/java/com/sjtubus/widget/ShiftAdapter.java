package com.sjtubus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;
import com.sjtubus.R;
import com.sjtubus.model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftTimeLineViewHolder> {
    private List<String> times;
    private List<String> comments;
    private Context context;

    private boolean isLoopLineFlag;

    /* 校园巴士和校区巴士不共用一套setdatalist了 */
    public void setDataList(Schedule schedule) {
        times = schedule.getScheduleTime();
        comments = schedule.getScheduleComment();
        notifyDataSetChanged();
    }

    public void setDataListOfLoopLine(List<String> schedule){
        times = schedule;
        notifyDataSetChanged();
    }

    /* 校园巴士和校区巴士不共用一套view了 */
    public ShiftAdapter(Context context, boolean isLoopLineFlag){
        this.context = context;
        this.times = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.isLoopLineFlag = isLoopLineFlag;
    }

    @NonNull
    @Override
    public ShiftTimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isLoopLineFlag){
            View view = View.inflate(parent.getContext(), R.layout.item_shift_loop, null);
            return new ShiftTimeLineViewHolder(view, viewType);
        } else {
            View view = View.inflate(parent.getContext(), R.layout.item_shift, null);
            return new ShiftTimeLineViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftTimeLineViewHolder holder, int position) {
        if (isLoopLineFlag){
            holder.setData(times.get(position),"");
        } else {
            holder.setData(times.get(position),comments.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return times==null?0:times.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }
}
