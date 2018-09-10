package com.sjtubus.widget;

import com.sjtubus.R;
import com.sjtubus.activity.LineActivity;
import com.sjtubus.model.LineInfo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder>{

    private List<LineInfo> mLineList;
    private LineActivity context;
    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView linename;
        TextView firsttime;
        TextView lasttime;
        TextView remainShift;

        public ViewHolder(View view){
            super(view);
            linename = (TextView) view.findViewById(R.id.line_name);
            firsttime = (TextView) view.findViewById(R.id.line_first_time);
            lasttime = (TextView) view.findViewById(R.id.line_last_time);
//            remainShift = (TextView) view.findViewById(R.id.line_remain_shift);
        }
    }

    public void setDataList(List<LineInfo> list) {
        mLineList = list;
        notifyDataSetChanged();
    }

    public LineAdapter(LineActivity context){
        this.context = context;
        this.mLineList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String line_name = mLineList.get(position).getLineNameCN();
        String first_time = "首班车 ： " + mLineList.get(position).getFirstTime();
        String last_time = "末班车 ： " + mLineList.get(position).getLastTime();
//        int remain_shift = mLineList.get(position).getRemainShift();
//        String remain_shift_str;
//        if (remain_shift > 0)
//            remain_shift_str = "今日剩余" + remain_shift + "班车未发";
//        else
//            remain_shift_str = "已超过末班时间";
        holder.linename.setText(line_name);
        holder.firsttime.setText(first_time);
        holder.lasttime.setText(last_time);
//        holder.remainShift.setText(remain_shift_str);
    }

    public String getLinename(int index){
        return mLineList.get(index).getLineName();
    }

    public String getLinenameCn(int index) { return  mLineList.get(index).getLineNameCN(); }

    @Override
    public int getItemCount() {
        return mLineList.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
    }
}
