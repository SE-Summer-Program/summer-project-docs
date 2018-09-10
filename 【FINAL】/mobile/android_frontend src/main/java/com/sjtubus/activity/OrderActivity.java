package com.sjtubus.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.model.ShiftInfo;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.ShiftInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.CalendarReminder;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.ClearEditText;

import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class OrderActivity extends BaseActivity implements View.OnClickListener{

    private String departure_place_str, arrive_place_str;
    private String departure_time_str, arrive_time_str;
    private String date_str;
    private String shiftid_str;
    private String shift_type_str;

    private String double_departure_place_str, double_arrive_place_str;
    private String double_departure_time_str, double_arrive_time_str;
    private String double_date_str;
    private String double_shiftid_str;
    private String double_shift_type_str;

    private ImageView need_icon;
    private LinearLayout remind_time;
    private LinearLayout need_front, need_back, need_window, need_road, need_other;
    private boolean isNeedRemind = false, isNeedExtend = false;
    private boolean[] checked_array = {false, false, false, false, false};
    private String[] check_msg = {"前排座位", "后排座位", "靠窗座位", "靠过道座位","其他特殊要求"};
    private String appoint_comment;
    private ClearEditText editText;

    private int remind_minutes = 10, remindlist_select = 0;
    private int[] remindtime_list = {10,30,60,120};
    private String[] remind_list = {"提前10分钟","提前30分钟","提前1小时","提前2小时"};

    private int SINGLE_WAY = 0, DOUBLE_WAY = 1;
    private boolean isSingleWayFlag = true;
    private boolean isOrderFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPassData();
        initToolbar();
        initRemindView();
        initNeedView();
        initExtend();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order;
    }

    private void initPassData(){
        // 单程 到 order
        Intent intent = getIntent();
        departure_place_str = intent.getStringExtra("departure_place");
        arrive_place_str = intent.getStringExtra("arrive_place");
        departure_time_str = intent.getStringExtra("departure_time");
        arrive_time_str = intent.getStringExtra("arrive_time");
        date_str = intent.getStringExtra("departure_date");
        shiftid_str = intent.getStringExtra("shiftid");
        shift_type_str = intent.getStringExtra("shift_type");

        int appoint_type = intent.getIntExtra("appoint_type",0);

        // 第二页 到 order
        if (appoint_type == DOUBLE_WAY){
            double_departure_place_str = intent.getStringExtra("double_departure_place");
            double_arrive_place_str = intent.getStringExtra("double_arrive_place");
            double_departure_time_str = intent.getStringExtra("double_departure_time");
            double_arrive_time_str = intent.getStringExtra("double_arrive_time");
            double_date_str = intent.getStringExtra("double_departure_date");
            double_shiftid_str = intent.getStringExtra("double_shiftid");
            double_shift_type_str = intent.getStringExtra("double_shift_type");

            isSingleWayFlag = false;
        }
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar_order);
        mToolbar.setNavigationIcon(R.drawable.back_32);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // order 回 单程
                if (isSingleWayFlag)
                    finish();
                // order 回 第二页
                else{
                    Intent appointDoubleIntent = new Intent(OrderActivity.this, AppointDoubleActivity.class);

                    appointDoubleIntent.putExtra("single_departure_place", departure_place_str);
                    appointDoubleIntent.putExtra("single_arrive_place", arrive_place_str);
                    appointDoubleIntent.putExtra("single_departure_time", departure_time_str);
                    //ToastUtils.showShort(StringCalendarUtils.HHmmssToHHmm(departure_time));
                    appointDoubleIntent.putExtra("single_arrive_time", arrive_time_str);
                    appointDoubleIntent.putExtra("single_departure_date", date_str);
                    appointDoubleIntent.putExtra("single_shiftid", shiftid_str);
                    appointDoubleIntent.putExtra("single_shift_type", shift_type_str);

                    appointDoubleIntent.putExtra("double_departure_date", double_date_str);
                    appointDoubleIntent.putExtra("target_page", 1);

//                    startActivity(appointDoubleIntent);
                    setResult(RESULT_OK, appointDoubleIntent);
                    finish();
                }
               // finish();
            }
        });

        initShiftInfo();
    }

    private void initShiftInfo() {
        TextView departure_place = findViewById(R.id.order_departureplace);
        TextView departure_time = findViewById(R.id.order_departuretime);
        TextView departure_date = findViewById(R.id.order_departuredate);
        TextView arrive_place = findViewById(R.id.order_arriveplace);
        TextView arrive_time = findViewById(R.id.order_arrivetime);
        TextView arrive_date = findViewById(R.id.order_arrivedate);
        TextView shiftid = findViewById(R.id.order_shiftid);
        TextView shift_type = findViewById(R.id.order_shifttype);
        TextView comment = findViewById(R.id.order_comment);

        departure_place.setText(departure_place_str);
        arrive_place.setText(arrive_place_str);
        departure_time.setText(departure_time_str);
        arrive_time.setText(arrive_time_str);
        departure_date.setText(date_str);
        arrive_date.setText(date_str);
        shiftid.setText(shiftid_str);
        shift_type.setText(ShiftUtils.getChiType(shift_type_str));
        comment.setOnClickListener(this);

        LinearLayout linearLayout = findViewById(R.id.order_linearlayout_double);

        if (isSingleWayFlag){
            linearLayout.setVisibility(View.GONE);
        } else {
            TextView double_departure_place = findViewById(R.id.order_departureplace_double);
            TextView double_departure_time = findViewById(R.id.order_departuretime_double);
            TextView double_departure_date = findViewById(R.id.order_departuredate_double);
            TextView double_arrive_place = findViewById(R.id.order_arriveplace_double);
            TextView double_arrive_time = findViewById(R.id.order_arrivetime_double);
            TextView double_arrive_date = findViewById(R.id.order_arrivedate_double);
            TextView double_shiftid = findViewById(R.id.order_shiftid_double);
            TextView double_shift_type = findViewById(R.id.order_shifttype_double);
            TextView double_comment = findViewById(R.id.order_comment_double);

            double_departure_place.setText(double_departure_place_str);
            double_arrive_place.setText(double_arrive_place_str);
            double_departure_time.setText(double_departure_time_str);
            double_arrive_time.setText(double_arrive_time_str);
            double_departure_date.setText(double_date_str);
            double_arrive_date.setText(double_date_str);
            double_shiftid.setText(double_shiftid_str);
            double_shift_type.setText(ShiftUtils.getChiType(double_shift_type_str));

            double_comment.setOnClickListener(this);
        }

        Button submit_btn = findViewById(R.id.order_confirm);
        submit_btn.setOnClickListener(this);
    }

    private void getCalendarPermission(){
        //获得读写日历权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED
                ) {
            Toast.makeText(getApplicationContext(),"没有读写系统日历权限,请手动开启权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(OrderActivity.this,new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 100);
        }
    }

    private void initRemindView() {
        TextView time_set = findViewById(R.id.order_setremindtime);
        CheckBox ifSetRemind = findViewById(R.id.order_setremind);
        ifSetRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getCalendarPermission();
                    isNeedRemind = true;
                    setViewsVisible(remind_time);
                }else {
                    isNeedRemind = false;
                    setViewsGone(remind_time);
                }
            }
        });
        remind_time = findViewById(R.id.order_remind_time);
        time_set.setOnClickListener(this);
    }

    private void initNeedView(){
        TextView need_bar = findViewById(R.id.order_setneed);
        need_icon = findViewById(R.id.order_setneed_icon);
        CheckBox checkBox1 = findViewById(R.id.order_need_checkbox1);
        CheckBox checkBox2 = findViewById(R.id.order_need_checkbox2);
        CheckBox checkBox3 = findViewById(R.id.order_need_checkbox3);
        CheckBox checkBox4 = findViewById(R.id.order_need_checkbox4);
        CheckBox checkBox5 = findViewById(R.id.order_need_checkbox5);
        need_front = findViewById(R.id.order_need1);
        need_back = findViewById(R.id.order_need2);
        need_window = findViewById(R.id.order_need3);
        need_road = findViewById(R.id.order_need4);
        need_other = findViewById(R.id.order_need5);

        need_bar.setOnClickListener(this);
        need_icon.setOnClickListener(this);

        setCheckChangeListener(checkBox1, 0);
        setCheckChangeListener(checkBox2, 1);
        setCheckChangeListener(checkBox3, 2);
        setCheckChangeListener(checkBox4, 3);
        setCheckChangeListener(checkBox5, 4);

        editText = findViewById(R.id.order_edittext);
    }

    private void initExtend(){
        remind_time.setVisibility(View.GONE);

        need_front.setVisibility(View.GONE);
        need_back.setVisibility(View.GONE);
        need_window.setVisibility(View.GONE);
        need_road.setVisibility(View.GONE);
        need_other.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.order_comment:
                retrofitShiftInfo(shiftid_str);
                break;
            case R.id.order_setneed:
            case R.id.order_setneed_icon:
                if (isNeedExtend){
                    setViewsGone(need_front, need_back, need_window, need_road, need_other);
                    isNeedExtend = false;
                    need_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.more_48));
                }
                else{
                    setViewsVisible(need_front, need_back, need_window, need_road, need_other);
                    isNeedExtend = true;
                    need_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.less_48));
                }
                break;
            case R.id.order_setremindtime:
                final TextView settime =  v.findViewById(v.getId());
                new AlertDialog.Builder(OrderActivity.this)
                    .setTitle("设置提醒时间")
                    .setSingleChoiceItems(remind_list, remindlist_select, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String text = remind_list[which];
                        remind_minutes = remindtime_list[which];
                        remindlist_select = which;
                        settime.setText(text);
                    }
                }).setNegativeButton("取消", null).create().show();
                break;
            case R.id.order_confirm:
                // 生成备注
                appoint_comment = "";
                for (int i = 0; i < 4 ;i++){
                    if (checked_array[i])
                        appoint_comment += check_msg[i] + " ";
                }
                appoint_comment += editText.getTextContent();

//                ToastUtils.showShort(appoint_comment);

                new AlertDialog.Builder(this)
                        .setMessage("确认提交预约申请吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RequestBody requestBody = new FormBody.Builder()
                                    .add("line_name", ShiftUtils.getLineByDepartureAndArrive(departure_place_str,arrive_place_str))
                                    .add("shift_id",shiftid_str)
                                    .add("appoint_date",date_str)
                                    .add("submit_time", StringCalendarUtils.getCurrentTime())
                                    .add("username",UserManager.getInstance().getUser().getUsername())
                                    .add("user_role",UserManager.getInstance().getRole())
                                    .add("comment", appoint_comment)
                                    .build();

                                submitAppoint(requestBody);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // order 到 单程
        if (isSingleWayFlag)
            finish();
        // order 到 第二页
        else{
            Intent appointDoubleIntent = new Intent(OrderActivity.this, AppointDoubleActivity.class);

            appointDoubleIntent.putExtra("single_departure_place", departure_place_str);
            appointDoubleIntent.putExtra("single_arrive_place", arrive_place_str);
            appointDoubleIntent.putExtra("single_departure_time", departure_time_str);
            appointDoubleIntent.putExtra("single_arrive_time", arrive_time_str);
            appointDoubleIntent.putExtra("single_departure_date", date_str);
            appointDoubleIntent.putExtra("single_shiftid", shiftid_str);
            appointDoubleIntent.putExtra("single_shift_type", shift_type_str);

            appointDoubleIntent.putExtra("double_departure_date", double_date_str);
            appointDoubleIntent.putExtra("target_page", 1);

//            startActivity(appointDoubleIntent);
            setResult(RESULT_OK, appointDoubleIntent);
            finish();
        }

       // finish();
    }

    private static String TAG = "orderactivity";

    public void submitAppoint(RequestBody requestBody){
        RetrofitClient.getBusApi()
            .appoint(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HttpResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.i(TAG, "onsubscribe");
                    addDisposable(d);
                }

                @Override
                public void onNext(HttpResponse response) {
                    if(response.getError()==0){
                        //如果需要预约，则向日历中添加时间
                        if(isNeedRemind) {
                            Date departure_time = StringCalendarUtils.StringToTime(date_str + " " + departure_time_str);
                            long begintime = departure_time.getTime();
                            String description = "您预约的于" + date_str + " " + departure_time_str + "从" + departure_place_str
                                    + "开往" + arrive_place_str + "的" + shiftid_str + "号校区巴士即将于" + remind_list[remindlist_select].substring(2) + "后发车，请记得按时前去乘坐哦~";
                            CalendarReminder.addCalendarEventRemind(App.getInstance(), "校车发车提醒", description, begintime, begintime, remind_minutes, new CalendarReminder.onCalendarRemindListener() {
                                public void onFailed(CalendarReminder.onCalendarRemindListener.Status error_code) {
                                    ToastUtils.showShort("预约提醒设置失败~");
                                }

                                public void onSuccess() {
                                    ToastUtils.showShort("预约提醒设置成功~");
                                }
                            });
                        }
                        //显示预约成功
                        String message = "";
                        if (isSingleWayFlag || (! isSingleWayFlag && !isOrderFinished)) {
                            message = "您已成功预约【" + date_str + " " + departure_time_str + "从" + departure_place_str
                                    + "开往" + arrive_place_str + "的" + shiftid_str + "号校区巴士】，请记得按时前去乘坐哦~";
                        } else if (isOrderFinished){
                            message = "您已成功预约【" + double_date_str + " " + double_departure_time_str + "从" + double_departure_place_str
                                    + "开往" + double_arrive_place_str + "的" + double_shiftid_str + "号校区巴士】，请记得按时前去乘坐哦~";
                        }

                        new SweetAlertDialog(OrderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("预约成功~")
                                .setContentText(message)
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        if (isSingleWayFlag) {
                                            OrderActivity.this.startActivity(new Intent(OrderActivity.this, MainActivity.class));
                                        } else if (! isOrderFinished){
                                            isOrderFinished = true;
                                            RequestBody anotherRequestBody = new FormBody.Builder()
                                                .add("line_name", ShiftUtils.getLineByDepartureAndArrive(double_departure_place_str,double_arrive_place_str))
                                                .add("shift_id",double_shiftid_str)
                                                .add("appoint_date",double_date_str)
                                                .add("submit_time", StringCalendarUtils.getCurrentTime())
                                                .add("username",UserManager.getInstance().getUser().getUsername())
                                                .add("user_role",UserManager.getInstance().getRole())
                                                .build();
                                            submitAppoint(anotherRequestBody);

                                        } else {
                                            OrderActivity.this.startActivity(new Intent(OrderActivity.this, MainActivity.class));
                                        }
                                    }
                                })
                                .show();
                    }else{
                        new SweetAlertDialog(OrderActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("预约失败!")
                                .setContentText(response.getMsg())
                                .show();
                    }
                }
                @Override
                public void onError(Throwable e) {
                    Log.i("order", "onerror");
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.i(TAG, "onComplete ");
                }
            });
    }

    private void retrofitShiftInfo(final String shiftid){
        //获取当前用户的username
//        String username = UserManager.getInstance().getUser().getUsername();

        RetrofitClient.getBusApi()
                .getShiftInfos(shiftid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShiftInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(ShiftInfoResponse response) {
                        ShiftInfo shiftInfo = response.getShiftInfo();
                        String message = "班次序列号：" + shiftInfo.getShiftid()
                                + "\n线路名称：" + shiftInfo.getLineNameCn()
                                + "\n发车时间：" + shiftInfo.getDepartureTime()
                                + "\n车牌号：" + shiftInfo.getBusPlateNum()
                                + "\n核载人数：" + shiftInfo.getBusSeatNum()
                                + "\n司机姓名：" + shiftInfo.getDriverName()
                                + "\n司机联系方式：" + shiftInfo.getDriverPhone()
                                + "\n备注：" + shiftInfo.getComment();
                        new AlertDialog.Builder(OrderActivity.this)
                                .setTitle("班次详细信息")
                                .setMessage(message)
                                .setPositiveButton("确定", null)
                                .show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showShort("网络请求失败！请检查你的网络！");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        //mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setViewsVisible(LinearLayout... linearLayouts){
        for (LinearLayout layout : linearLayouts){
            layout.setVisibility(View.VISIBLE);
        }
    }

    private void setViewsGone(LinearLayout... linearLayouts){
        for (LinearLayout layout : linearLayouts){
            layout.setVisibility(View.GONE);
        }
    }

    private void setCheckChangeListener(final CheckBox checkBox, final int pos){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ((checked_array[0] && !checked_array[1] && pos == 1) ||
                        (checked_array[1] && !checked_array[0] && pos == 0)){
                    ToastUtils.showShort("别闹~一个人是没法同时坐前后排的哦");
                }
                if ((checked_array[2] && !checked_array[3] && pos == 3) ||
                        (checked_array[3] && !checked_array[2] && pos == 2)){
                    ToastUtils.showShort("别闹~一个人是没法同时靠窗和过道的哦");
                }
                checked_array[pos] = isChecked;
            }
        });
    }
}