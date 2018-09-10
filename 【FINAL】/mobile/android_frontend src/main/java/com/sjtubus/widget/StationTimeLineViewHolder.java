package com.sjtubus.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.model.Station;
import com.squareup.picasso.Picasso;

public class StationTimeLineViewHolder extends RecyclerView.ViewHolder{
    private TextView stationName;
    private ImageView stationImage;

    StationTimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        TimelineView mTimelineView = itemView.findViewById(R.id.time_marker);
        stationName = itemView.findViewById(R.id.station_name);
        stationImage = itemView.findViewById(R.id.station_img);
        mTimelineView.initLine(viewType);
    }

    public void setData(Station station){
        stationName.setText(station.getName());
        Picasso
            .with(App.getInstance())
            .load(station.getImage_url())
            .into(stationImage);
    }
}
