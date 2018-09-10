package com.sjtubus.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.model.Station;
import com.sjtubus.model.response.LocationResponse;
import com.sjtubus.model.response.StationResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.BusLocationSimulator;
import com.sjtubus.utils.MyLocationListener;
import com.sjtubus.utils.MyMapStatusChangeListener;
import com.sjtubus.utils.MyMarkerClickListener;
import com.sjtubus.utils.NetworkUtils;
import com.sjtubus.utils.ToastUtils;
import com.yinglan.scrolllayout.ScrollLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class MapActivity extends BaseActivity {

    //百度地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Float zoomLevel = 16.0f;
    private LatLng initPosition = new LatLng(31.03201,121.443287);

    //定位相关
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener;
    private static final int BAIDU_READ_PHONE_STATE =100;

    //覆盖物相关
    private List<Marker> markers = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();
    private Map<String,BitmapDescriptor> bitmaps = new ArrayMap<>();
    private BaiduMap.OnMarkerClickListener mMarkClickListener = null;
    //private Map<String,BitmapDescriptor> bitmaps = new ArrayMap<String, BitmapDescriptor>();
    //private BaiduMap.OnMarkerClickListener mMarkClickListener = null;

    //搜索相关
    private RoutePlanSearch mSearch = null;
    private boolean useDefaultIcon = false;
    private DrivingRoutePlanOption routePlan = new DrivingRoutePlanOption();

    //时刻表相关
    private MyMapStatusChangeListener mMapStatusChangeListener = null;
    private ScrollLayout layoutStationInfo;
    private ScrollLayout chooseStation_layout;

    private HashMap<String,Marker> busmap = new HashMap<>();
    //巴士运行相关
    private List<Marker> buslist = new ArrayList<>();
    private BusLocationSimulator simulator = new BusLocationSimulator();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-mm-ss");

    private TimerTask task;
    private ScheduledExecutorService pool;
    private FloatingActionButton chooseStation_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getPermission();
        initView();
        initLocation();
        if(NetworkUtils.isNetworkAvailable(MapActivity.this)){
            retrieveData();
        }else{
            ToastUtils.showShort("当前网络不可用!请检查网络连接!");
        }
    }

    public int getContentViewId(){
        return R.layout.activity_map;
    }

    private void getPermission(){
        //获得定位权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MapActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
        }
    }

    private void initView(){
        //获取地图控件引用
        mMapView = findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setCompassEnable(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置为卫星显示
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(zoomLevel));// 设置地图初始化缩放比例
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(initPosition));// 设置地图初始中心

        //显示站点信息的布局相关
        layoutStationInfo = findViewById(R.id.stationInfo_Layout);
        /*设置 setting*/
        layoutStationInfo.setMinOffset(0);
        layoutStationInfo.setMaxOffset((int) (this.getResources().getDisplayMetrics().heightPixels * 0.3));
        layoutStationInfo.setExitOffset(-100);
        layoutStationInfo.setIsSupportExit(true);
        layoutStationInfo.setAllowHorizontalScroll(true);
        layoutStationInfo.setToExit();
        layoutStationInfo.getBackground().setAlpha(0);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                layoutStationInfo.scrollToExit();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                layoutStationInfo.scrollToExit();
                return false;
            }
        });
        mMapStatusChangeListener = new MyMapStatusChangeListener(layoutStationInfo);
        mBaiduMap.setOnMapStatusChangeListener(mMapStatusChangeListener);

        //选择站点的布局相关
        chooseStation_btn = findViewById(R.id.chooseStation_btn);
        chooseStation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutStationInfo.setToExit();
                chooseStation_layout.setToClosed();
            }
        });
        chooseStation_layout = findViewById(R.id.chooseStation_Layout);
        /*设置 setting*/
        chooseStation_layout.setMinOffset(0);
        chooseStation_layout.setMaxOffset(-100);
        chooseStation_layout.setExitOffset(-100);
        chooseStation_layout.setIsSupportExit(true);
        chooseStation_layout.setAllowHorizontalScroll(false);
        chooseStation_layout.setOnScrollChangedListener(mOnScrollChangedListener);
        chooseStation_layout.setToOpen();
        chooseStation_layout.getBackground().setAlpha(0);

        ChooseStationListener listener = new ChooseStationListener();
        Button nearest_btn = findViewById(R.id.nearestStation);
        nearest_btn.setOnClickListener(listener);
        Button JJT_btn = findViewById(R.id.JJT);
        JJT_btn.setOnClickListener(listener);
        Button XYY_btn = findViewById(R.id.XYY);
        XYY_btn.setOnClickListener(listener);
        Button DSY_btn = findViewById(R.id.DSY);
        DSY_btn.setOnClickListener(listener);
        Button DZY_btn = findViewById(R.id.DZY);
        DZY_btn.setOnClickListener(listener);
        Button XTSG_btn = findViewById(R.id.XTSG);
        XTSG_btn.setOnClickListener(listener);
        Button XZBL_btn = findViewById(R.id.XZBL);
        XZBL_btn.setOnClickListener(listener);
        Button DXXY_btn = findViewById(R.id.DXXY);
        DXXY_btn.setOnClickListener(listener);
        Button KXM_btn = findViewById(R.id.KXM);
        KXM_btn.setOnClickListener(listener);
        Button JDXY_btn = findViewById(R.id.JDXY);
        JDXY_btn.setOnClickListener(listener);
        Button MM_btn = findViewById(R.id.MM);
        MM_btn.setOnClickListener(listener);
        Button CJXY_btn = findViewById(R.id.CJXY);
        CJXY_btn.setOnClickListener(listener);
        Button WXYXL_btn = findViewById(R.id.WXYXL);
        WXYXL_btn.setOnClickListener(listener);
        Button XSFWZX_btn = findViewById(R.id.XSFWZX);
        XSFWZX_btn.setOnClickListener(listener);
        Button XQXSGY_btn = findViewById(R.id.XQXSGY);
        XQXSGY_btn.setOnClickListener(listener);
        Button DSCYDL_btn = findViewById(R.id.DSCYDL);
        DSCYDL_btn.setOnClickListener(listener);
        Button HLSHZX_btn = findViewById(R.id.HLSHZX);
        HLSHZX_btn.setOnClickListener(listener);
        Button BYGTSG_btn = findViewById(R.id.BYGTSG);
        BYGTSG_btn.setOnClickListener(listener);
        Button CLXY_btn = findViewById(R.id.CLXY);
        CLXY_btn.setOnClickListener(listener);
    }

    private void initLocation(){
        myListener = new MyLocationListener(mBaiduMap);
        myListener.setLocation(false);//设置不以自己为中心
        mLocationClient = new LocationClient(App.getInstance());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数

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
        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setOpenGps(true);
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

    private void setStations(List<Station>stations){
        this.stations = stations;
    }

    private void initRoutePlan(){
        //初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        MyGetRoutePlanResultListener routeListener = new MyGetRoutePlanResultListener();
        mSearch.setOnGetRoutePlanResultListener(routeListener);
        //此处暂时为硬编码，应导入数据库数据
        //拾取坐标系统给的经纬度是反的！！！切记！！！
        //retrieveData();

        PlanNode stNode = PlanNode.withLocation(new LatLng(stations.get(0).getLatitude(),stations.get(0).getLongitude()));//菁菁堂
        List<PlanNode> pbNode = new ArrayList<>();
        for(int i = 1; i < stations.size(); i++){
            PlanNode node_temp  = PlanNode.withLocation(new LatLng(stations.get(i).getLatitude(),stations.get(i).getLongitude()));
            pbNode.add(node_temp);
        }
        //开始规划路线
        routePlan.from(stNode).passBy(pbNode).to(stNode);
        mSearch.drivingSearch(routePlan);

    }

    private void addMarker(){
        initBitmap();
        for(Station station : stations){
            BitmapDescriptor bd_temp = bitmaps.get(station.getName() + "_smallZoom");
            MarkerOptions marker_temp = new MarkerOptions()
                    .title(station.getName())
                    .position(new LatLng(station.getLatitude(),station.getLongitude()))
                    .icon(bd_temp).anchor(0.5f, 0.5f).zIndex(9);
            //添加marker
            Marker marker = (Marker) mBaiduMap.addOverlay(marker_temp);
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
            Bundle bundle = new Bundle();
            //info必须实现序列化接口
            bundle.putSerializable("info", station);
            marker.setExtraInfo(bundle);

            markers.add(marker);
        }
        mMarkClickListener = new MyMarkerClickListener(layoutStationInfo);
        mBaiduMap.setOnMarkerClickListener(mMarkClickListener);
        mMapStatusChangeListener.setMarkers(markers);
        mMapStatusChangeListener.setBitmaps(bitmaps);
    }

    private void startBus(){
        task = new TimerTask()
        {
            @Override
            public void run()
            {
                //execute task
                RetrofitClient
                    .getBusApi()
                    .getLocation()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LocationResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(LocationResponse response) {
                            addBus(response.getLocations());
                            Log.d(TAG, "onNext: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            addBus(null);
                        }
                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: ");
                        }
                    });
            }
        };
        pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(task, 0 , 3000, TimeUnit.MILLISECONDS);
    }

    private void addBus(Map<String,String>locations){
//        String time = simpleDateFormat.format(new Date());//目前为当前时间，也可以为任意时间
//        List<BusLocationSimulator.BusLocation> busLocations = simulator.getBusLocation(time);//获得巴士坐标信息
//        if(buslist.size()==0) {
//            for (BusLocationSimulator.BusLocation busLocation : busLocations) {
//                MarkerOptions marker_temp = new MarkerOptions()
//                        .position(busLocation.location)//位置
//                        .rotate(busLocation.rotate < 90 ? busLocation.rotate : busLocation.rotate - 180)//角度
//                        .icon(busLocation.rotate < 90 ? BitmapDescriptorFactory.fromResource(R.drawable.bus_right) :
//                                BitmapDescriptorFactory.fromResource(R.drawable.bus_left))//图标源
//                        .scaleX(0.15f).scaleY(0.15f)//图标缩放比例
//                        .anchor(0.5f, 1.0f).zIndex(7);//锚点和纵轴坐标
//                //添加marker
//                Marker bus = (Marker) mBaiduMap.addOverlay(marker_temp);
//                buslist.add(bus);
//            }
//        }else{
//            for(int i = 0; i < busLocations.size(); i++){
//                Marker bus = buslist.get(i);
//                BusLocationSimulator.BusLocation busLocation = busLocations.get(i);
//                bus.setPosition(busLocations.get(i).location);
//                bus.setRotate(busLocation.rotate < 90 ? busLocation.rotate : busLocation.rotate - 180);
//                bus.setIcon(busLocation.rotate < 90 ? BitmapDescriptorFactory.fromResource(R.drawable.bus_right) :
//                        BitmapDescriptorFactory.fromResource(R.drawable.bus_left));
//            }
//        }

        for(String key:locations.keySet()) {
            LatLng latLng = new LatLng(Double.valueOf(locations.get(key).split(" ")[0]),
                    Double.valueOf(locations.get(key).split(" ")[1]));
            if (busmap.get(key) == null) {
                MarkerOptions marker_temp = new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_left))
                        .scaleX(0.15f).scaleY(0.15f)//图标缩放比例
                        .anchor(0.5f, 1.0f).zIndex(7);
                //添加marker
                busmap.put(key, (Marker) mBaiduMap.addOverlay(marker_temp));
            }else{
                busmap.get(key).setPosition(latLng);
            }
        }
    }

    private void initBitmap(){
        BitmapDescriptor bd_temp;
        View v_temp = LayoutInflater.from(App.getInstance()).inflate(R.layout.map_marker, null);//加载自定义的布局
        ImageView img_temp = v_temp.findViewById(R.id.baidumap_custom_img);//获取自定义布局中的imageview
        img_temp.setImageResource(R.mipmap.icon_station_64);//设置marker的图标
        TextView tv_temp = v_temp.findViewById(R.id.baidumap_custom_text);//获取自定义布局中的textview

        for(Station station : stations){
            tv_temp.setText(station.getName());//设置站点名
            bd_temp = BitmapDescriptorFactory.fromView(v_temp);
            bitmaps.put(station.getName() + "_bigZoom",bd_temp);

            tv_temp.setText("");//小缩略图时站点名不显示
            bd_temp = BitmapDescriptorFactory.fromView(v_temp);
            bitmaps.put(station.getName() + "_smallZoom",bd_temp);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(myListener);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        if(mSearch!=null) mSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        if(NetworkUtils.isNetworkAvailable(MapActivity.this)){
            startBus();
        }else{
            ToastUtils.showShort("当前网络不可用!请检查网络连接!");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if (!mLocationClient.isStarted()){
            mLocationClient.start();//开启定位
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if(task!=null) task.cancel();
        if(pool!=null) pool.shutdown();
        mMapView.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();//停止定位
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "成功获取位置~", Toast.LENGTH_SHORT).show();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        private MyDrivingRouteOverlay(BaiduMap baiduMap,boolean setMarker) {
            super(baiduMap, setMarker);
        }

            @Override
            public BitmapDescriptor getStartMarker() {
                if (useDefaultIcon) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
                }
                return null;
            }

            /*@Override
            public BitmapDescriptor getPassMarker() {
                if (useDefaultIcon) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
                }
                return null;
            }*/

            @Override
            public BitmapDescriptor getTerminalMarker() {
                if (useDefaultIcon) {
                    return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
                }
                return null;
            }
    }

    private class MyGetRoutePlanResultListener implements
            OnGetRoutePlanResultListener {

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            if (result.error == SearchResult.ERRORNO.PERMISSION_UNFINISHED ||
                    result.error == SearchResult.ERRORNO.REQUEST_ERROR) {
                mSearch.drivingSearch(routePlan);
            }
            else if (result.error != SearchResult.ERRORNO.NO_ERROR ) {
                Toast.makeText(MapActivity.this, result.error.toString(), Toast.LENGTH_SHORT).show();
            }

            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteLine route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap, false);
                overlay.setData(route);  //设置路线数据
                overlay.addToMap(); //将所有overlay添加到地图中
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult arg0) {
        }

        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult result) {
        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult result) {
        }
    }

    //选择站点
    private void doChooseStation(String stationName){
        //将目标站点设置为中心并且放大
        for(Station station : stations) {
            if(station.getName().equals(stationName)){
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18.0f));// 设置地图初始化缩放比例
                LatLng p = new LatLng(station.getLatitude(),station.getLongitude());
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(p));// 设置地图初始中心
                break;
            }
        }
        //将所有站点改为显示站点名模式
        Marker mMarker = null;
        for(Marker marker : markers){
            //从marker中获取info信息
            Bundle bundle = marker.getExtraInfo();
            Station station = (Station) bundle.getSerializable("info");

            BitmapDescriptor bd_temp = bitmaps.get(station.getName() + "_bigZoom");
            marker.setIcon(bd_temp);

            //显示站点的时刻信息
            if(marker.getTitle().equals(stationName)){
                mMarkClickListener.onMarkerClick(marker);
            }
        }
    }

    //监听选择站点菜单
    private class ChooseStationListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.nearestStation:
                    BDLocation myLocation = myListener.getLocation();
                    Double minLength = 10000000.0d;
                    String stationName = null;
                    for(Station station : stations) {
                        Double a = station.getLatitude() - myLocation.getLatitude();
                        Double b = station.getLongitude() - myLocation.getLongitude();
                        Double Length = a * a + b * b;
                        if( Length < minLength){
                            minLength = Length;
                            stationName = station.getName();
                        }
                    }
                    System.out.print(stationName);
                    doChooseStation(stationName);
                    chooseStation_layout.setToExit();
                    break;
                case R.id.JJT:
                    doChooseStation("菁菁堂");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.XYY:
                    doChooseStation("校医院");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.DSY:
                    doChooseStation("东上院");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.DZY:
                    doChooseStation("东中院");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.XTSG:
                    doChooseStation("新图书馆");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.XZBL:
                    doChooseStation("行政B楼");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.DXXY:
                    doChooseStation("电信学院");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.KXM:
                    doChooseStation("凯旋门");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.JDXY:
                    doChooseStation("机动学院");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.MM:
                    doChooseStation("庙门");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.CJXY:
                    doChooseStation("船建学院");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.WXYXL:
                    doChooseStation("文选医学楼");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.XSFWZX:
                    doChooseStation("学生服务中心");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.XQXSGY:
                    doChooseStation("西区学生公寓");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.DSCYDL:
                    doChooseStation("第四餐饮大楼");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.HLSHZX:
                    doChooseStation("华联生活中心");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.BYGTSG:
                    doChooseStation("包玉刚图书馆");
                    chooseStation_layout.setToExit();
                    break;
                case R.id.CLXY:
                    doChooseStation("材料学院");
                    chooseStation_layout.setToExit();
                    break;

                default:
                    break;
            }
        }
    }

    //实现滑动时的背景变化
    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                layoutStationInfo.getBackground().setAlpha(255 - (int) precent);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if(currentStatus.equals(ScrollLayout.Status.CLOSED))
                chooseStation_btn.setVisibility(View.GONE);
            else chooseStation_btn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    //数据应当从数据库读取
    private void retrieveData(){
        RetrofitClient.getBusApi()
                .getLineStation("LoopLineClockwise")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StationResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(StationResponse response) {
                        setStations(response.getStations());
                        initRoutePlan();
                        addMarker();
                        Log.d(TAG, "onNext: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showShort("网络请求失败！请检查你的网络！");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }
}