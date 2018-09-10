package com.sjtubus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sjtubus.R;
import com.sjtubus.model.Station;

import java.util.ArrayList;
import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationTimeLineViewHolder> {
    private List<Station> stations;
    private Context context;

    public void setDataList(List<Station> stations) {
        this.stations = stations;
        notifyDataSetChanged();
    }

    public StationAdapter(Context context){
        this.context = context;
        this.stations = new ArrayList<>();
    }

    @NonNull
    @Override
    public StationTimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_station, null);
        return new StationTimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull StationTimeLineViewHolder holder, int position) {
        holder.setData(stations.get(position));
    }

    @Override
    public int getItemCount() {
        return stations==null?0:stations.size();
    }
}
