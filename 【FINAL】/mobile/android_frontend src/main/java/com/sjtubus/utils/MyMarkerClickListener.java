package com.sjtubus.utils;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.sjtubus.R;
import com.sjtubus.model.Station;
import com.yinglan.scrolllayout.ScrollLayout;

import java.util.Calendar;

public class MyMarkerClickListener implements BaiduMap.OnMarkerClickListener {
    private ScrollLayout mScrollLayout;
    private Station station;

    private TextView station_name;
    private TextView anticlockwise_time, clockwise_time, anticlockwise_holiday_time, clockwise_holiday_time;
    private TextView comment;

    public MyMarkerClickListener(ScrollLayout mScrollLayout){
        this.mScrollLayout = mScrollLayout;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //从marker中获取info信息
        Bundle bundle = marker.getExtraInfo();
        if(bundle == null) return false;

        station = (Station) bundle.getSerializable("info");
        initView();

        mScrollLayout.setToOpen();
        return true;
    }

    private void initView(){
        //将信息显示在界面上
        //TextView text = mScrollLayout.findViewById(R.id.map_station_name);
        station_name = mScrollLayout.findViewById(R.id.detail_station_name);
        anticlockwise_time = mScrollLayout.findViewById(R.id.detail_anticlockwise);
        clockwise_time = mScrollLayout.findViewById(R.id.detail_clockwise);
        anticlockwise_holiday_time = mScrollLayout.findViewById(R.id.detail_anticlockwise_holiday);
        clockwise_holiday_time = mScrollLayout.findViewById(R.id.detail_clockwise_holiday);
        comment = mScrollLayout.findViewById(R.id.detail_comment);

        initStationNameAndComment();
        initAnticlockwise();
        initClockwise();
        initAnticlockwiseHoliday();
        initclockwiseHoliday();
    }


    /*
     * 渲染站点名称和备注
     */
    private void initStationNameAndComment(){
        station_name.setText(station.getName());

        String comment_text = "1. <font color='#FF6347'><strong>红色班次</strong></font>提前6分钟从东川路地铁站发车，" +
                "巴士站点设在地铁站南侧的公交车集中候车点内。" + "<br>" +
                "2. <font color='#6495ED'><strong>蓝色班次</strong></font>终点站为东川路地铁站校园巴士专用站。" + "<br>" +
                "3. <font color='#66CD00'><strong>绿色班次</strong></font>为普通环线，<font color='#D1D1D1'><strong>灰色班次</strong></font>为已发出班次。" + "<br>" +
                "4. 周六、周日及法定节假日停运。" + "<br>" +
                "5. 问询电话：后勤保障处 54743939" + "<br>";
        comment.setText(Html.fromHtml(comment_text));
    }


    /*
     * 渲染工作日逆时针的时刻
     */
    private void initAnticlockwise(){
        StringBuilder anticlockwise_text = new StringBuilder();
        int index = 1;

        if (! MyDateUtils.isLegalHoliday(StringCalendarUtils.CalendarToString(Calendar.getInstance())).equals("无")){
            for (String time : station.getAntiClockLoop()) {
                anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
            for (String time : station.getAntiClockNonLoop()){
                anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
        } else if (StringCalendarUtils.isWeekend(Calendar.getInstance())){
            for (String time : station.getAntiClockLoop()) {
                anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
            for (String time : station.getAntiClockNonLoop()){
                anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
        } else {
            for (String time : station.getAntiClockLoop()) {
                if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)) {
                    //灰色
                    anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                    index++;
                    continue;
                }
                if (index <= 4) {
                    //橘红色
                    anticlockwise_text.append("<font color='#FF6347'>").append(time).append("</font>    ");
                } else {
                    //草绿色
                    anticlockwise_text.append("<font color='#66CD00'>").append(time).append("</font>    ");
                }
                index++;
            }
            for (String time : station.getAntiClockNonLoop()) {
                if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)) {
                    //灰色
                    anticlockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                    continue;
                }
                //湖蓝色
                anticlockwise_text.append("<font color='#6495ED'>").append(time).append("</font>    ");
            }
        }

        anticlockwise_time.setText(Html.fromHtml(anticlockwise_text.toString()));
    }


    /*
     * 渲染工作日顺时针的时刻
     */
    private void initClockwise(){
        StringBuilder clockwise_text = new StringBuilder();

        if (! MyDateUtils.isLegalHoliday(StringCalendarUtils.CalendarToString(Calendar.getInstance())).equals("无")) {
            for (String time : station.getAntiClockLoop()) {
                clockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
            for (String time : station.getAntiClockNonLoop()) {
                clockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
        } else if (StringCalendarUtils.isWeekend(Calendar.getInstance())){
            for (String time : station.getClockLoop()) {
                clockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
            for (String time : station.getClockNonLoop()){
                clockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
        } else {
            for (String time : station.getClockLoop()) {
                if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)) {
                    //灰色
                    clockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                    continue;
                }
                //草绿色
                clockwise_text.append("<font color='#66CD00'>").append(time).append("</font>    ");
            }
            for (String time : station.getClockNonLoop()) {
                if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)) {
                    //灰色
                    clockwise_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                    continue;
                }
                //湖蓝色
                clockwise_text.append("<font color='#6495ED'>").append(time).append("</font>    ");
            }
        }

        clockwise_time.setText(Html.fromHtml(clockwise_text.toString()));
    }


    /*
     * 渲染寒暑假逆时针的时刻
     */
    private void initAnticlockwiseHoliday(){
        StringBuilder anticlockwiseHoliday_text = new StringBuilder();
        int index = 1;

        if (! MyDateUtils.isLegalHoliday(StringCalendarUtils.CalendarToString(Calendar.getInstance())).equals("无")) {
            for (String time : station.getAntiClockLoop()) {
                anticlockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
            for (String time : station.getAntiClockNonLoop()) {
                anticlockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
        } else if (StringCalendarUtils.isWeekend(Calendar.getInstance())){
            for (String time : station.getVacAntiClockLoop()) {
                anticlockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
            for (String time : station.getVacAntiClockNonLoop()){
                anticlockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
        } else {
            for (String time : station.getVacAntiClockLoop()) {
                if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)) {
                    //灰色
                    anticlockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                    index++;
                    continue;
                }
                if (index <= 1) {
                    //橘红色
                    anticlockwiseHoliday_text.append("<font color='#FF6347'>").append(time).append("</font>    ");
                } else {
                    //草绿色
                    anticlockwiseHoliday_text.append("<font color='#66CD00'>").append(time).append("</font>    ");
                }
                index++;
            }
            for (String time : station.getVacAntiClockNonLoop()) {
                if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)) {
                    //灰色
                    anticlockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                    continue;
                }
                //湖蓝色
                anticlockwiseHoliday_text.append("<font color='#6495ED'>").append(time).append("</font>    ");
            }
        }

        anticlockwise_holiday_time.setText(Html.fromHtml(anticlockwiseHoliday_text.toString()));
    }


    /*
     * 渲染寒暑假顺时针的时刻
     */
    private void initclockwiseHoliday(){
        StringBuilder clockwiseHoliday_text = new StringBuilder();

        if (! MyDateUtils.isLegalHoliday(StringCalendarUtils.CalendarToString(Calendar.getInstance())).equals("无")) {
            for (String time : station.getAntiClockLoop()) {
                clockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
            for (String time : station.getAntiClockNonLoop()) {
                clockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
        } else if (StringCalendarUtils.isWeekend(Calendar.getInstance())){
            for (String time : station.getVacClockLoop()) {
                clockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
            for (String time : station.getVacClockNonLoop()){
                clockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
            }
        } else {
            for (String time : station.getVacClockLoop()) {
                if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)) {
                    //灰色
                    clockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                    continue;
                }
                //草绿色
                clockwiseHoliday_text.append("<font color='#66CD00'>").append(time).append("</font>    ");
            }
            for (String time : station.getVacClockNonLoop()) {
                if (StringCalendarUtils.isBeforeCurrentTimeHHmm(time)) {
                    //灰色
                    clockwiseHoliday_text.append("<font color='#D1D1D1'>").append(time).append("</font>    ");
                    continue;
                }
                //湖蓝色
                clockwiseHoliday_text.append("<font color='#6495ED'>").append(time).append("</font>    ");
            }
        }

        clockwise_holiday_time.setText(Html.fromHtml(clockwiseHoliday_text.toString()));
    }
}
