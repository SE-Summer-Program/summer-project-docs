package com.sjtubus.utils;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.sjtubus.model.Station;
import com.yinglan.scrolllayout.ScrollLayout;

import java.util.List;
import java.util.Map;

public class MyMapStatusChangeListener implements BaiduMap.OnMapStatusChangeListener{
    private ScrollLayout mScrollLayout;
    private List<Marker> markers = null;
    private Map<String,BitmapDescriptor> bitmaps = null;
    private Float zoom = 16.0f;

    public MyMapStatusChangeListener(ScrollLayout mScrollLayout){
        this.mScrollLayout = mScrollLayout;
    }

    public void setBitmaps(Map<String,BitmapDescriptor> bitmaps){
        this.bitmaps = bitmaps;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    @Override
    public void onMapStatusChangeStart(MapStatus status){
        mScrollLayout.setToExit();
        this.zoom = status.zoom;
    }
    @Override
    public void onMapStatusChangeStart(MapStatus status,int reason){
    }

    @Override
    public void onMapStatusChange(MapStatus status){
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus status){
        Float zoom_new = status.zoom;
        if((zoom_new < 17.0f && this.zoom < 17.0f) ||
                (zoom_new >= 17.0f && this.zoom >= 17.0f))
                    return;
        for(Marker marker : markers){
            //从marker中获取info信息
            Bundle bundle = marker.getExtraInfo();
            Station station = (Station) bundle.getSerializable("info");

            BitmapDescriptor bd_temp = null;
            if(zoom_new >= 17.0f){
                bd_temp = bitmaps.get(station.getName() + "_bigZoom");
                marker.setIcon(bd_temp);
            }
            if(zoom_new < 17.0f){
                bd_temp = bitmaps.get(station.getName() + "_smallZoom");
                marker.setIcon(bd_temp);
            }
        }
    }
}


