package com.sjtubus.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.sjtubus.R;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.RippleImageView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

//import static android.content.ContentValues.TAG;

public class GPSPositionActivity extends BaseActivity implements View.OnClickListener{

    //百度地图相关
    private MapView mMapView;
    private MapStatusUpdate msu = null;
    private BaiduMap mBaiduMap;
    private Float zoomLevel = 16.0f;
    private LatLng initPosition = new LatLng(31.03201,121.443287);

    //定位相关
    public LocationClient mLocationClient = null;
    private MyGPSLocationListener myListener;
    private static final int BAIDU_READ_PHONE_STATE =100;

    //View相关
    private RippleImageView rippleImageView;
    private boolean isAnimationShown = true;
    private TextView currentStreet;

    private static String TAG = "GPSPositionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        initPermission();
        initView();
        initLocation();
//        initRoutePlan();
    }

    public int getContentViewId(){
        return R.layout.activity_position;
    }

    private void initPermission(){
        //h获得定位权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ) {
            ToastUtils.showShort("没有权限,请手动开启定位权限");
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(GPSPositionActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
        }
    }

    private void initView(){

        Toolbar mToolbar = findViewById(R.id.toolbar_position);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.icon_back_128);

        rippleImageView = (RippleImageView) findViewById(R.id.position_rippleImageView);
        rippleImageView.startWaveAnimation();
        rippleImageView.setOnClickListener(this);

        currentStreet = findViewById(R.id.position_text);
        currentStreet.setText("定位已关闭");

        //获取地图控件引用
        mMapView = findViewById(R.id.position_map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setCompassEnable(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置为卫星显示
        //msu = MapStatusUpdateFactory.zoomTo(15.0f);// 设置地图初始化缩放比例
        //mBaiduMap.setMapStatus(msu);
        //mBaiduMap.setCompassIcon(BitmapFactory.
        //        decodeResource(getResources(),R.mipmap.compass));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(zoomLevel));// 设置地图初始化缩放比例
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(initPosition));// 设置地图初始中心
    }

    private void initLocation(){
        myListener = new MyGPSLocationListener(mBaiduMap);
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setScanSpan(3000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(5*60*1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedLocationDescribe(true);
        //可选，是否需要位置描述信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的位置信息，此处必须为true
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;//定位跟随态
        //mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态
        //mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;  //定位罗盘态

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        BitmapDescriptorFactory mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
//        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
//        mBaiduMap.setMyLocationConfiguration(config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(myListener);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
//        mSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();//开启定位
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();//停止定位
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    // 没有获取到权限，做特殊处理
                    ToastUtils.showShort("成功获取位置~");
                } else {
                    // 没有获取到权限，做特殊处理
                    ToastUtils.showShort("获取位置权限失败，请手动开启");
                }
                break;
            default:
                break;
        }
    }

    public class MyGPSLocationListener extends BDAbstractLocationListener {
        private BaiduMap baiduMap;
        private Boolean isFirstGetLocation = true;
        private Boolean onSetLocation = true;

        public MyGPSLocationListener(BaiduMap baiduMap){
            this.baiduMap = baiduMap;
        }

        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            double latitude = location.getLatitude();  //获取纬度信息
            double longitude = location.getLongitude();  //获取经度信息

            if (isAnimationShown) {
                String street_txt = "定位已开启 当前位置：" + street;
                currentStreet.setText(street_txt);
            }

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

            RequestBody requestBody = new FormBody.Builder()
                    .add("latitude", String.valueOf(location.getLatitude()))
                    .add("longitude", String.valueOf(location.getLongitude()))
                    .build();
            sendLocation(requestBody);

            Log.i(TAG, "send");
        }

        public void setLocation(Boolean c){
            this.onSetLocation = c;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.position_rippleImageView:
                // 定位开着，点了以后就关了
                if (isAnimationShown) {
                    isAnimationShown = false;
                    rippleImageView.changeViewIcon(R.mipmap.point_start);
                    rippleImageView.stopWaveAnimation();
                    currentStreet.setText("定位已关闭");
                }
                // 点了以后就开了
                else {
                    isAnimationShown = true;
                    rippleImageView.changeViewIcon(R.mipmap.point_end);
                    rippleImageView.startWaveAnimation();
                    initLocation();
                    //initView();
                    //问题在这里，关掉以后开不起来
                }
                break;
            default:
                break;
        }
    }

    public void sendLocation(RequestBody requestBody){
        RetrofitClient.getBusApi()
            .postLocation(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HttpResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.i(TAG, "onSubscribe");
                    addDisposable(d);
                }

                @Override
                public void onNext(HttpResponse response) {

                    Log.i(TAG, "向后台发送数据了~");
                }
                @Override
                public void onError(Throwable e) {
                    Log.i(TAG, "onError");
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "onComplete: ");
                    //mProgressBar.setVisibility(View.GONE);
                }
            });
    }
}
