package com.sjtubus.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.sjtubus.R;
import com.sjtubus.model.Route;
import com.sjtubus.model.Station;
import com.sjtubus.utils.MyLocationListener;
import com.sjtubus.utils.MyMapStatusChangeListener;
import com.yinglan.scrolllayout.ScrollLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteActivity extends BaseActivity {

    //百度地图相关
    private MapView mMapView;
    private MapStatusUpdate msu = null;
    private BaiduMap mBaiduMap;

    //定位相关
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener;
    private static final int BAIDU_READ_PHONE_STATE =100;

    //Marker相关
    private List<LatLng> latLng = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private List<OverlayOptions> overlayOptions = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();

    //搜索相关
    private RoutePlanSearch mSearch = null;
    private RouteLine route = null;  //路线
    private OverlayManager routeOverlay = null;  //该类提供一个能够显示和管理多个Overlay的基类
    private DrivingRoutePlanOption routePlan = new DrivingRoutePlanOption();
    private Map<String, Route> routeMap = new ArrayMap<>();
    boolean useDefaultIcon = true;

    //路线选择相关
    private ScrollLayout mScrollLayout;
    private MyMapStatusChangeListener mMapStatusChangeListener = null;
    private FloatingActionButton chooseRoute_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initPermission();
        initView();
        initLocation();
        initRoutePlan();
        doRoutePlan("MinToXuA");
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_route;
    }

    private void initPermission(){
        //h获得定位权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(RouteActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
        }
    }

    private void initView(){
        //获取地图控件引用
        mMapView = findViewById(R.id.routeview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setCompassEnable(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置为卫星显示

        chooseRoute_btn = findViewById(R.id.chooseRoute_btn);
        chooseRoute_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.setToClosed();
            }
        });

        mScrollLayout = findViewById(R.id.chooseRoute_Layout);
        /*设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset(-100);
        mScrollLayout.setExitOffset(-100);
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(false);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToOpen();
        mScrollLayout.getBackground().setAlpha(0);

        ChooseRouteListener listener = new ChooseRouteListener();
        Button MinToXuA_btn = findViewById(R.id.MinToXuA);
        MinToXuA_btn.setOnClickListener(listener);
        Button MinToXuB_btn = findViewById(R.id.MinToXuB);
        MinToXuB_btn.setOnClickListener(listener);
        Button MinToXuC_btn = findViewById(R.id.MinToXuC);
        MinToXuC_btn.setOnClickListener(listener);
        Button MinToXuD_btn = findViewById(R.id.MinToXuD);
        MinToXuD_btn.setOnClickListener(listener);
        Button XuToMinA_btn = findViewById(R.id.XuToMinA);
        XuToMinA_btn.setOnClickListener(listener);
        Button XuToMinB_btn = findViewById(R.id.XuToMinB);
        XuToMinB_btn.setOnClickListener(listener);
        Button XuToMinC_btn = findViewById(R.id.XuToMinC);
        XuToMinC_btn.setOnClickListener(listener);
        Button XuToMinD_btn = findViewById(R.id.XuToMinD);
        XuToMinD_btn.setOnClickListener(listener);
        Button XuToMinE_btn = findViewById(R.id.XuToMinE);
        XuToMinE_btn.setOnClickListener(listener);
        Button XuToMinF_btn = findViewById(R.id.XuToMinF);
        XuToMinF_btn.setOnClickListener(listener);

    }

    private void initLocation(){
        myListener = new MyLocationListener(mBaiduMap);
        myListener.setLocation(false);//设置不以自己为中心
        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
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

    private void initRoutePlan(){
        //初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        MyGetRoutePlanResultListener routeListener = new MyGetRoutePlanResultListener();
        mSearch.setOnGetRoutePlanResultListener(routeListener);
        //此处暂时为硬编码，应导入数据库数据
        getData();
    }

    private void doRoutePlan(String route_name){
        //拾取坐标系统给的经纬度是反的！！！切记！！！
        List<LatLng> Node = routeMap.get(route_name).getLocation();
        Integer N = Node.size();
        PlanNode stNode = PlanNode.withLocation(Node.get(0));
        PlanNode enNode = PlanNode.withLocation(Node.get(N - 1));
        List<PlanNode> paNode = new ArrayList<PlanNode>();
        for(int i = 1; i < N - 2; i++)
            paNode.add(PlanNode.withLocation(Node.get(i)));

        //开始规划路线
        routePlan.from(stNode).passBy(paNode).to(enNode);
        mSearch.drivingSearch(routePlan);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(myListener);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onStart(){
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
    public void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();//停止定位
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

        public MyDrivingRouteOverlay(BaiduMap baiduMap, boolean setMarker) {
            super(baiduMap, setMarker);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyGetRoutePlanResultListener implements OnGetRoutePlanResultListener {

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            if (result.error == SearchResult.ERRORNO.PERMISSION_UNFINISHED ||
                    result.error == SearchResult.ERRORNO.REQUEST_ERROR) {
                mSearch.drivingSearch(routePlan);
            }
            else if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR ) {
                Toast.makeText(RouteActivity.this, result.error.toString(), Toast.LENGTH_SHORT).show();
            }

            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                //nodeIndex = -1;
                //mBtnPre.setVisibility(View.VISIBLE);
                //mBtnNext.setVisibility(View.VISIBLE);
                mBaiduMap.clear();
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap,true);
                routeOverlay = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));  //设置路线数据
                overlay.addToMap(); //将所有overlay添加到地图中
                overlay.zoomToSpan();//缩放地图
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

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        //实现滑动时的背景变化
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if(currentStatus.equals(ScrollLayout.Status.CLOSED))
                chooseRoute_btn.setVisibility(View.GONE);
            else chooseRoute_btn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    private class ChooseRouteListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.MinToXuA:
                    doRoutePlan("MinToXuA");
                    mScrollLayout.setToExit();
                    break;
                case R.id.MinToXuB:
                    doRoutePlan("MinToXuB");
                    mScrollLayout.setToExit();
                    break;
                case R.id.MinToXuC:
                    doRoutePlan("MinToXuC");
                    mScrollLayout.setToExit();
                    break;
                case R.id.MinToXuD:
                    doRoutePlan("MinToXuD");
                    mScrollLayout.setToExit();
                    break;
                case R.id.XuToMinA:
                    doRoutePlan("XuToMinA");
                    mScrollLayout.setToExit();
                    break;
                case R.id.XuToMinB:
                    doRoutePlan("XuToMinB");
                    mScrollLayout.setToExit();
                    break;
                case R.id.XuToMinC:
                    doRoutePlan("XuToMinC");
                    mScrollLayout.setToExit();
                    break;
                case R.id.XuToMinD:
                    doRoutePlan("XuToMinD");
                    mScrollLayout.setToExit();
                    break;
                case R.id.XuToMinE:
                    doRoutePlan("XuToMinE");
                    mScrollLayout.setToExit();
                    break;
                case R.id.XuToMinF:
                    doRoutePlan("XuToMinF");
                    mScrollLayout.setToExit();
                    break;

                default:
                    break;
            }
        }
    }

    private void getData(){
        List<Double> latitude = new ArrayList<>();
        List<Double> longtitude = new ArrayList<>();
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        latitude.add(31.206204);longtitude.add(121.439497);//徐汇
        Route MinToXuA = new Route("MinToXuA", latitude, longtitude);
        routeMap.put("MinToXuA", MinToXuA);

        latitude.clear();longtitude.clear();
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        latitude.add(31.022281);longtitude.add(121.432624);//交大新村
        latitude.add(31.128109);longtitude.add(121.417986);//罗阳
        latitude.add(31.137863);longtitude.add(121.424893);//上中
        latitude.add(31.191928);longtitude.add(121.452459);//天钥
        latitude.add(31.206204);longtitude.add(121.439497);//徐汇
        Route MinToXuB = new Route("MinToXuB", latitude, longtitude);
        routeMap.put("MinToXuB", MinToXuB);

        latitude.clear();longtitude.clear();
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        latitude.add(31.181237);longtitude.add(121.428312);//田林
        Route MinToXuC = new Route("MinToXuC", latitude, longtitude);
        routeMap.put("MinToXuC", MinToXuC);

        latitude.clear();longtitude.clear();
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        latitude.add(31.148032);longtitude.add(121.414226);//古美
        Route MinToXuD = new Route("MinToXuD", latitude, longtitude);
        routeMap.put("MinToXuD", MinToXuD);

        latitude.clear();longtitude.clear();
        latitude.add(31.206204);longtitude.add(121.439497);//徐汇
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        Route XuToMinA = new Route("XuToMinA", latitude, longtitude);
        routeMap.put("XuToMinA", XuToMinA);

        latitude.clear();longtitude.clear();
        latitude.add(31.191928);longtitude.add(121.452459);//天钥
        latitude.add(31.137863);longtitude.add(121.424893);//上中
        latitude.add(31.128109);longtitude.add(121.417986);//罗阳
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        Route XuToMinB = new Route("XuToMinB", latitude, longtitude);
        routeMap.put("XuToMinB", XuToMinB);

        latitude.clear();longtitude.clear();
        latitude.add(31.181237);longtitude.add(121.428312);//田林
        latitude.add(31.148032);longtitude.add(121.414226);//古美
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        Route XuToMinC = new Route("XuToMinC", latitude, longtitude);
        routeMap.put("XuToMinC", XuToMinC);

        latitude.clear();longtitude.clear();
        latitude.add(31.022281);longtitude.add(121.432624);//交大新村
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        Route XuToMinD = new Route("XuToMinD", latitude, longtitude);
        routeMap.put("XuToMinD", XuToMinD);

        latitude.clear();longtitude.clear();
        latitude.add(31.181237);longtitude.add(121.428312);//田林
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        Route XuToMinE = new Route("XuToMinE", latitude, longtitude);
        routeMap.put("XuToMinE", XuToMinE);

        latitude.clear();longtitude.clear();
        latitude.add(31.148032);longtitude.add(121.414226);//古美
        latitude.add(31.02858);longtitude.add(121.437534);//闵行
        Route XuToMinF = new Route("XuToMinF", latitude, longtitude);
        routeMap.put("XuToMinF", XuToMinF);
    }
}