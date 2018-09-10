//package com.sjtubus.activity;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.model.LatLng;
//import com.sjtubus.R;
//import com.sjtubus.model.response.HttpResponse;
//import com.sjtubus.network.RetrofitClient;
//import com.sjtubus.user.UserManager;
//import com.sjtubus.utils.RippleImageView;
//import com.sjtubus.utils.ShiftUtils;
//import com.sjtubus.utils.StringCalendarUtils;
//import com.sjtubus.utils.ToastUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import cn.pedant.SweetAlert.SweetAlertDialog;
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.FormBody;
//import okhttp3.RequestBody;
//
//import static android.content.ContentValues.TAG;
//
//public class PositionActivity extends BaseActivity implements View.OnClickListener{
//
//    private LocationClient locationClient;
////    private EditText editText;
//    private RippleImageView rippleImageView;
//    private MapView mapView;
//    private TextView currentStreet;
//
//    private BaiduMap baiduMap;
//    private boolean isFirstLocate = true;
//
////    private BDLocation location;
//
////    private double latitude, longitude;
//    private boolean isAnimationShown = true;
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        locationClient.stop();
//        mapView.onDestroy();
//        baiduMap.setMyLocationEnabled(false);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//    }
//
//    @Override
//    public int getContentViewId() {
//        return R.layout.activity_position;
//    }
//
//    private void initView(){
//        Toolbar mToolbar = findViewById(R.id.toolbar_position);
//        mToolbar.setTitle("");
//        mToolbar.setNavigationIcon(R.mipmap.icon_back_128);
//
//        rippleImageView=(RippleImageView)findViewById(R.id.position_rippleImageView);
//        rippleImageView.startWaveAnimation();
//        rippleImageView.setOnClickListener(this);
//
//        currentStreet = findViewById(R.id.position_text);
//        currentStreet.setText("定位已关闭");
//
////        Button btn_start = (Button) findViewById(R.id.position_btn_start);
////        Button btn_stop = (Button) findViewById(R.id.position_btn_stop);
////        btn_start.setOnClickListener(this);
////        btn_stop.setOnClickListener(this);
////
////        editText = (EditText) findViewById(R.id.position_edittext);
//
//        locationClient = new LocationClient(getApplicationContext());
//        locationClient.registerLocationListener(new MyLocationListener());
//        SDKInitializer.initialize(getApplicationContext());
//        mapView = (MapView) findViewById(R.id.position_map);
//        baiduMap = mapView.getMap();
//        baiduMap.setMyLocationEnabled(true);
//
//        List<String> permissionList = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(PositionActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add( Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//
//        /* 手机权限和存储权限 */
////        if (ContextCompat.checkSelfPermission(PositionActivity.this,
////                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
////            permissionList.add( Manifest.permission.READ_PHONE_STATE);
////        }
////        if (ContextCompat.checkSelfPermission(PositionActivity.this,
////                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
////            permissionList.add( Manifest.permission.WRITE_EXTERNAL_STORAGE);
////        }
//
//        if (!permissionList.isEmpty()){
//            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
//            ActivityCompat.requestPermissions(PositionActivity.this, permissions, 1);
//        } else {
//            requestLocation();
//        }
//    }
//
//    private void requestLocation(){
//        initLocation();
//        locationClient.start();
//    }
//
//    private void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//        option.setScanSpan(500);
//        option.setIsNeedAddress(true);
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
//        locationClient.setLocOption(option);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
//        switch (requestCode){
//            case 1:
//                if (grantResults.length > 0){
//                    for (int result : grantResults){
//                        if (result != PackageManager.PERMISSION_GRANTED){
//                            ToastUtils.showShort("必须同意所有权限才可以使用哦~");
//                            finish();
//                            return;
//                        }
//                    }
//                    requestLocation();;
//                } else {
//                    ToastUtils.showShort("发生未知错误！");
//                    finish();
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    public class MyLocationListener implements BDLocationListener{
//
//        @Override
//        public void onReceiveLocation(final BDLocation location){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
////                    this.location = location;
//                    StringBuilder currentPosition = new StringBuilder();
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//                    currentPosition.append("纬度: ").append(latitude).append("\n");
//                    currentPosition.append("经度: ").append(longitude).append("\n");
////                    currentPosition.append("国家: ").append(location.getCountry()).append("\n");
////                    currentPosition.append("省: ").append(location.getProvince()).append("\n");
////                    currentPosition.append("市: ").append(location.getCity()).append("\n");
////                    currentPosition.append("区: ").append(location.getDistrict()).append("\n");
//                    currentPosition.append("街道: ").append(location.getStreet()).append("\n");
//                    currentPosition.append("定位方式: ");
//
//                    String street_txt = "定位已开启 当前位置：" + location.getStreet();
//                    currentStreet.setText(street_txt);
//
//                    location.setLatitude(latitude + 0.00643);
//                    location.setLongitude(longitude + 0.0064);
//
//                    if (location.getLocType() == BDLocation.TypeGpsLocation){
//                        currentPosition.append("GPS");
//                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//                        currentPosition.append("网络");
//                    }
////                    editText.setText(currentPosition);
//
//
//                    if (location.getLocType() == BDLocation.TypeGpsLocation
//                            || location.getLocType() == BDLocation.TypeNetWorkLocation){
//                        navigateTo(location);
//                    }
//
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("latitude", String.valueOf(latitude))
//                            .add("longitude", String.valueOf(longitude))
//                            .build();
//                    sendLocation(requestBody);
//                }
//            });
//        }
//
////        @Override
////        public void onConnectHotSpotMessage(String s, int i){
////
////        }
//    }
//
//    private void navigateTo(BDLocation location){
//        if (isFirstLocate){
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
//            baiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomTo(16f);
//            baiduMap.animateMapStatus(update);
//            isFirstLocate = false;
//        }
//        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
//        locationBuilder.latitude(location.getLatitude());
//        locationBuilder.longitude(location.getLongitude());
//        MyLocationData locationData = locationBuilder.build();
//        baiduMap.setMyLocationData(locationData);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.position_rippleImageView:
//                // 定位开着，点了以后就关了
//                if (isAnimationShown) {
//                    isAnimationShown = false;
//                    rippleImageView.changeViewIcon(R.mipmap.point_start);
//                    currentStreet.setText("定位已关闭");
//                }
//                // 点了以后就开了
//                else {
//                    isAnimationShown = true;
//                    rippleImageView.changeViewIcon(R.mipmap.point_end);
//                   initView();
//                    //问题在这里，关掉以后开不起来
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void sendLocation(RequestBody requestBody){
//        RetrofitClient.getBusApi()
//                .postLocation(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HttpResponse>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        addDisposable(d);
//                    }
//
//                    @Override
//                    public void onNext(HttpResponse response) {
//                        ToastUtils.showShort("向后台发送数据了~");
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete: ");
//                        //mProgressBar.setVisibility(View.GONE);
//                    }
//                });
//    }
//}
