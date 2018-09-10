package com.sjtubus.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.model.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.NetworkChangeEvent;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.receiver.NetworkConnectChangedReceiver;
import com.sjtubus.user.UserChangeEvent;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.GlideImageLoader;
import com.sjtubus.utils.NetworkUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.MarqueeViewAdapter;
import com.stx.xmarqueeview.XMarqueeView;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class MainActivity extends BaseActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.billboard)
    XMarqueeView billboard;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.reserve_btn)
    Button reserve_btn;
    @BindView(R.id.scan_btn)
    Button scan_btn;
    @BindView(R.id.position_btn)
    Button position_btn;
    @BindView(R.id.schedule_btn)
    Button schedule_btn;
    @BindView(R.id.map_btn)
    Button map_btn;
    @BindView(R.id.navigate_btn)
    Button navigate_btn;
    @BindView(R.id.message_btn)
    Button message_btn;
    @BindView(R.id.record_btn)
    Button record_btn;
    @BindView(R.id.write_btn)
    Button write_btn;

    //Views in navigate menu
    private TextView username;
    private TextView userinfo;
    private TextView login_tips;
    private TextView login_txt;
    private TextView register_txt;

    private List<String> images = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    private boolean mCheckNetWork = true; //默认检查网络状态
    private View mTipView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private NetworkConnectChangedReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initView();
        loadImages();
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
        EventBus.getDefault().register(this);
        initTipView();//初始化提示View
        registerReceiver();
        checkRole();
    }

    private void registerReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.NET.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.Net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mReceiver = new NetworkConnectChangedReceiver();
        this.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        //在无网络情况下打开APP时，系统不会发送网络状况变更的Intent，需要自己手动检查
        hasNetWork(NetworkUtils.isNetworkAvailable(MainActivity.this));
    }

    public int getContentViewId(){
        return R.layout.activity_main;
    }

    public void initView(){
        //设置ToolBar
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.person);

        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        reserve_btn = findViewById(R.id.reserve_btn); //预约班车
        scan_btn = findViewById(R.id.scan_btn); //管理员扫描
        position_btn = findViewById(R.id.position_btn); //司机定位
        schedule_btn = findViewById(R.id.schedule_btn); //班次信息
        map_btn = findViewById(R.id.map_btn); //路线查询
        navigate_btn = findViewById(R.id.navigate_btn); //实时位置
        record_btn = findViewById(R.id.record_btn);
        message_btn = findViewById(R.id.message_btn);
        write_btn = findViewById(R.id.write_btn);

        reserve_btn.setOnClickListener(this);
        scan_btn.setOnClickListener(this);
        position_btn.setOnClickListener(this);
        schedule_btn.setOnClickListener(this);
        map_btn.setOnClickListener(this);
        navigate_btn.setOnClickListener(this);
        record_btn.setOnClickListener(this);
        message_btn.setOnClickListener(this);
        write_btn.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout nav_header_layout = (LinearLayout) navigationView.getHeaderView(0);
        username = nav_header_layout.findViewById(R.id.username_txt);
        userinfo = nav_header_layout.findViewById(R.id.shortinfo_txt);
        login_tips = nav_header_layout.findViewById(R.id.login_tips);
        login_txt = nav_header_layout.findViewById(R.id.login_txt);
        register_txt = nav_header_layout.findViewById(R.id.register_txt);
        //设置下划线
        login_txt.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        register_txt.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        //设置滚动轮播
       // billboard = findViewById(R.id.billboard);
        messages.add("2018.8.19即明日,校园巴士停运一天!");
        messages.add("好消息！校车时速已达到100km/S!");
        messages.add("恭喜学号为2333的同学喜提校车一辆!");
        MarqueeViewAdapter billboard_adapter = new MarqueeViewAdapter(messages, this);
        //刷新公告
        billboard.setAdapter(billboard_adapter);

        checkRole();
    }

    public void loadImages(){
        images.add("http://106.14.181.49:8080/images/brand_image1.jpg");
        images.add("http://106.14.181.49:8080/images/brand_image2.jpg");
        images.add("http://106.14.181.49:8080/images/brand_image3.jpg");
    }

    public void checkRole(){
        User user = UserManager.getInstance().getUser();
        String role = UserManager.getInstance().getRole();
//        boolean isLogin = UserManager.getInstance().isLogin();

        if (UserManager.getInstance().getUser() != null){
            switch (role) {
                case "admin":
                    record_btn.setVisibility(View.GONE);
                    position_btn.setVisibility(View.GONE);
                    scan_btn.setVisibility(View.VISIBLE);
                    reserve_btn.setVisibility(View.GONE);
                    write_btn.setVisibility(View.VISIBLE);
                    break;
                case "driver":
                    record_btn.setVisibility(View.GONE);
                    scan_btn.setVisibility(View.GONE);
                    position_btn.setVisibility(View.VISIBLE);
                    reserve_btn.setVisibility(View.GONE);
                    write_btn.setVisibility(View.VISIBLE);
                    break;
                default:
                    scan_btn.setVisibility(View.GONE);
                    position_btn.setVisibility(View.GONE);
                    record_btn.setVisibility(View.VISIBLE);
                    reserve_btn.setVisibility(View.VISIBLE);
                    write_btn.setVisibility(View.GONE);
                    break;
            }
        } else {
            scan_btn.setVisibility(View.GONE);
            position_btn.setVisibility(View.GONE);
            record_btn.setVisibility(View.VISIBLE);
            reserve_btn.setVisibility(View.VISIBLE);
            write_btn.setVisibility(View.GONE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserStatChange(UserChangeEvent event) {
        Log.d("SJTUBUS", "User changed!");
        updateUser();
        checkRole();
    }

    public void updateUser(){
        User user = UserManager.getInstance().getUser();
        boolean isLogin = UserManager.getInstance().isLogin();
        if (isLogin && user != null) {
            username.setText(user.getUsername());
            username.setVisibility(View.VISIBLE);
            String role = user.getTeacher()?"教工":"普通用户";
            switch (UserManager.getInstance().getRole()){
                case "driver":
                    role = "司机";
                    break;
                case "admin":
                    role = "管理员";
                    break;
                case "jaccountuser":
                    role = "Jaccount认证用户";
                    break;
                default:
                    break;
            }

            String userinfo_str = "";
            if (UserManager.getInstance().getRole().equals("admin") ||
                    UserManager.getInstance().getRole().equals("driver")){
                userinfo_str = "身份:" + role;
            } else {
                userinfo_str = "身份:" + role + "   " + "信用积分:" + user.getCredit();
            }
            userinfo.setText(userinfo_str);
            userinfo.setVisibility(View.VISIBLE);
            login_tips.setVisibility(View.GONE);
            login_txt.setVisibility(View.GONE);
            register_txt.setVisibility(View.GONE);
        } else {
            login_tips.setVisibility(View.VISIBLE);
            login_txt.setVisibility(View.VISIBLE);
            register_txt.setVisibility(View.VISIBLE);
            username.setVisibility(View.GONE);
            userinfo.setVisibility(View.GONE);
            //Picasso.with(this).load(R.drawable.logo_grey).into(imageAvatar);
        }
    }

    @Override
    public void onClick(View v){
        checkRole();
        switch (v.getId()){
            case R.id.reserve_btn:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent reserveIntent = new Intent(MainActivity.this, AppointNaviActivity.class);
                startActivity(reserveIntent);
                break;
            case R.id.write_btn:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent writeIntent = new Intent(MainActivity.this, AppointNaviActivity.class);
                startActivity(writeIntent);
                break;
            case R.id.scan_btn:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                if(UserManager.getInstance().getRole().equals("user")||UserManager.getInstance().getRole().equals("jaccountuser")){
                    ToastUtils.showShort("抱歉~您没有管理员权限哦~");
                    break;
                }
                // 初始化扫描，进入扫码界面
                new IntentIntegrator(this)
                        .setOrientationLocked(false)
                        .setCaptureActivity(SimpleScanActivity.class)
                        .initiateScan();
                break;
            case R.id.position_btn:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                if(UserManager.getInstance().getRole().equals("user")||UserManager.getInstance().getRole().equals("jaccountuser")){
                    ToastUtils.showShort("抱歉~您没有司机权限哦~");
                    break;
                }
                Intent positionIntent = new Intent(MainActivity.this, GPSPositionActivity.class);
                startActivity(positionIntent);
                break;
            case R.id.schedule_btn:
                Intent scheduleIntent = new Intent(MainActivity.this, LineActivity.class);
                startActivity(scheduleIntent);
                break;
                //保留疑问
            case R.id.navigate_btn:
                Intent mapIntent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(mapIntent);
                break;
            //保留疑问
            case R.id.map_btn:
                Intent navigateIntent = new Intent(MainActivity.this,RouteActivity.class);
                startActivity(navigateIntent);
                break;
            case R.id.message_btn:
                Intent messageIntent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(messageIntent);
                break;
            case R.id.record_btn:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent recordIntent = new Intent(MainActivity.this,RecordActivity.class);
                startActivity(recordIntent);
                break;
//            case R.id.idea_btn:
//                FeedbackAgent agent = new FeedbackAgent(App.getInstance());
//                agent.startDefaultThreadActivity();
//                break;
            case R.id.login_txt:
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.register_txt:
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        checkRole();
        switch (menuItem.getItemId()){
            case R.id.navigation_item_person:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent person_intent = new Intent(MainActivity.this,PersonInfoActivity.class);
                startActivity(person_intent);
                break;
            case R.id.navigation_item_reserve:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent recordIntent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(recordIntent);
                break;
            case R.id.navigation_item_collect:
                if(UserManager.getInstance().getUser() == null){
                    ToastUtils.showShort("请先登录~");
                    break;
                }
                Intent collectIntent = new Intent(MainActivity.this, CollectActivity.class);
                startActivity(collectIntent);
                break;
            case R.id.navigation_item_message:
                Intent messageIntent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(messageIntent);
                break;
            case R.id.navigation_item_idea:
                FeedbackAgent agent = new FeedbackAgent(App.getInstance());
                agent.startDefaultThreadActivity();
                break;
            case R.id.navigation_item_aboutus:
                new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.icon_aboutus_128)
                    .setTitle("关于我们")
                    .setMessage(R.string.about_us_content)
                    .create().show();
                break;
            case R.id.navigation_item_help:
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.icon_aboutus_128)
                        .setTitle("用户指南")
                        .setMessage(R.string.user_guide)
                        .create().show();
                break;
            default:
                break;
        }
        return true;
    }

//    private void init(){
//        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
//                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        //判断当前版本在4.0以上并且存在虚拟按键，否则不做操作
//        if (!checkDeviceHasNavigationBar()) {
//            //一定要判断是否存在按键，否则在没有按键的手机调用会影响别的功能。如之前没有考虑到，导致图传全屏变成小屏显示。
//        } else {
//            // 获取属性
//            decorView.setSystemUiVisibility(flag);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        billboard.startFlipping();
        UserManager.getInstance().refresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        billboard.stopFlipping();
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * 判断是否存在虚拟按键
     * @ return
     */
    public boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            /*
             * 不是很清楚这个注解加了是干嘛的
             */
            @SuppressLint("PrivateApi") Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    @Override
    // 通过 onActivityResult的方法获取 扫描回来的 值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("扫描失败!")
                        .setContentText("二维码内容为空!")
                        .setConfirmButton("确定",new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            } else {
                // ScanResult 为 获取到的字符串
                String scanResult = intentResult.getContents();
                String[] info = scanResult.split(";");
                if(info.length < 3){
                    ToastUtils.showShort("二维码格式不正确哦~");
                    return;
                }
                RequestBody requestBody = new FormBody.Builder()
                        .add("username", info[2])
                        .add("shift_id",info[0])
                        .add("departure_date",info[1])
                        .build();
                //登录
                RetrofitClient.getBusApi()
                        .verifyUser(requestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<HttpResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(HttpResponse response) {
                                if (response.getMsg().equals("班次已到站") || response.getMsg().equals("班次未发出")){
                                    String message = response.getMsg();
                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("验证失败")
                                            .setContentText(message)
                                            .setConfirmText("确定")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.cancel();
                                                }
                                            })
                                            .show();
                                    return;
                                }

                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("验证完成!")
                                        .setContentText(response.getMsg())
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                            }
                        });
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void finish() {
        super.finish();
        //当提示View被动态添加后直接关闭页面会导致该View内存溢出，所以需要在finish时移除
        if (mTipView != null && mTipView.getParent() != null) {
            mWindowManager.removeView(mTipView);
        }
    }

    private void initTipView() {
        LayoutInflater inflater = getLayoutInflater();
        mTipView = inflater.inflate(R.layout.layout_network_tip, null); //提示View布局
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        //使用非CENTER时，可以通过设置XY的值来改变View的位置
        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChangeEvent(NetworkChangeEvent event) {
        hasNetWork(event.isConnected);
    }

    private void hasNetWork(boolean has) {
        if (mCheckNetWork) {
            if (has) {
                if (mTipView != null && mTipView.getParent() != null) {
                    mWindowManager.removeView(mTipView);
                }
            } else {
                if (mTipView.getParent() == null) {
                    mWindowManager.addView(mTipView, mLayoutParams);
                }
            }
        }
    }
}
