package com.sjtubus.utils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MyLocationListener extends BDAbstractLocationListener {
    private BaiduMap baiduMap;
    private Boolean isFirstGetLocation = true;
    private Boolean onSetLocation = true;
    private BDLocation mlocation = null;

    public MyLocationListener(BaiduMap baiduMap){
        this.baiduMap = baiduMap;
    }

    @Override
    public void onReceiveLocation(BDLocation location){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取地址相关的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
        /*
        String addr = location.getAddrStr();    //获取详细地址信息
        String country = location.getCountry();    //获取国家
        String province = location.getProvince();    //获取省份
        String city = location.getCity();    //获取城市
        String district = location.getDistrict();    //获取区县
        String street = location.getStreet();    //获取街道信息
        */
        mlocation = location;
        //判断是否为首次获取到位置数据
        if (isFirstGetLocation && onSetLocation)
        {
            //如果为首次定位，则直接定位到当前用户坐标
            LatLng latLng=new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate msuLocationMapStatusUpdate= MapStatusUpdateFactory//
                    .newLatLng(latLng);
            baiduMap.animateMapStatus(msuLocationMapStatusUpdate);

            isFirstGetLocation=false;
        }

        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();

        // 设置定位数据
        baiduMap.setMyLocationData(locData);
    }

    public void setLocation(Boolean c){
            this.onSetLocation = c;
    }

    public BDLocation getLocation(){
        return mlocation;
    }
}
