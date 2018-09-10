package com.sjtubus.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.AppointShortInfo;
import com.sjtubus.model.response.AppointResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.MyDateUtils;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.AppointAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class AppointDoubleActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private AppointAdapter appointAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private String departure_place_str;
    private String arrive_place_str;
    private String date_str;
    private int year,month,day;
    private String line_name;
    private String line_type;

    private TextView left_appoint;
    private TextView yesterday_btn;
    private TextView nextday_btn;
    private TextView date;
    private ImageView calendar_btn;
    private ImageView next_btn;

    private boolean isTodayFlag = false;

    private String single_date_str;
    private String double_date_str;
    private AppointInfo info_single = new AppointInfo();
    private boolean isSecondPageFlag = false;
    private int FIRST_PAGE = 0, SECOND_PAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPassData();
        initToolbar();
        initView();
//        if (isSecondPageFlag)
//            ToastUtils.showShort("第二页");
//        else
//            ToastUtils.showShort("第一页");
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_appoint;
    }

    private void initPassData() {
        Intent intent = getIntent();
        int page_num = intent.getIntExtra("target_page", 0);
        //navi 到 第一页
        if (page_num == FIRST_PAGE) {
            departure_place_str = intent.getStringExtra("departure_place");
            arrive_place_str = intent.getStringExtra("arrive_place");
            single_date_str = intent.getStringExtra("singleway_date");
            double_date_str = intent.getStringExtra("doubleway_date");
            date_str = single_date_str;
            isSecondPageFlag = false;

            line_name = ShiftUtils.getLineByDepartureAndArrive(departure_place_str, arrive_place_str);
            if(line_name.equals("error")) ToastUtils.showShort("地址翻译出现错误！");
        }
        // 第一页 到 第二页
        else if (page_num == SECOND_PAGE){
            info_single.setDeparture_place(intent.getStringExtra("single_departure_place"));
            info_single.setArrive_place(intent.getStringExtra("single_arrive_place"));
            info_single.setDeparture_time(intent.getStringExtra("single_departure_time"));
           // ToastUtils.showShort(intent.getStringExtra("single_departure_time"));
            info_single.setArrive_time(intent.getStringExtra("single_arrive_time"));
            info_single.setDate(intent.getStringExtra("single_departure_date"));
            info_single.setShiftid(intent.getStringExtra("single_shiftid"));
            info_single.setLine_type(intent.getStringExtra("single_shift_type"));

            departure_place_str = intent.getStringExtra("single_arrive_place");
            arrive_place_str = intent.getStringExtra("single_departure_place");
            double_date_str = intent.getStringExtra("double_departure_date");
            date_str = double_date_str;
            isSecondPageFlag = true;

            line_name = ShiftUtils.getLineByDepartureAndArrive(departure_place_str, arrive_place_str);
            if(line_name.equals("error")) ToastUtils.showShort("地址翻译出现错误！");
        }
        else{
            ToastUtils.showShort("网络请求失败！请检查你的网络！");
        }
    }

    private void initToolbar(){
        Toolbar mToolbar = findViewById(R.id.toolbar_appointment);
        if (!isSecondPageFlag)
            mToolbar.setTitle("去程：" + departure_place_str + "->" + arrive_place_str);
        else
            mToolbar.setTitle("返程：" + departure_place_str + "->" + arrive_place_str);
        mToolbar.setNavigationIcon(R.mipmap.icon_back_128);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 第一页 回 navi
                if (!isSecondPageFlag) {
//                    String dateText = (String) date.getText();
//                    Intent intent = new Intent(AppointDoubleActivity.this, AppointNaviActivity.class);
//                    intent.putExtra("departure_place", departure_place_str);
//                    intent.putExtra("arrive_place", arrive_place_str);
//                    intent.putExtra("singleway_date", dateText);
//                   // startActivity(intent);
//                    setResult(RESULT_OK, intent);
                    finish();
                }
                // 第二页 回 第一页
                else{
                    Intent appointIntent = new Intent(AppointDoubleActivity.this, AppointDoubleActivity.class);
                    appointIntent.putExtra("departure_place", info_single.getDeparture_place());
                    appointIntent.putExtra("arrive_place", info_single.getArrive_place());
                    appointIntent.putExtra("singleway_date", info_single.getDate());
                    appointIntent.putExtra("doubleway_date", date.getText());
                    appointIntent.putExtra("target_page", 0);
//                    startActivity(appointIntent);
                    setResult(RESULT_OK, appointIntent);
                    finish();
                }
            }
        });
    }

    private void initView() {
        yesterday_btn = findViewById(R.id.appoint_yesterday);
        nextday_btn = findViewById(R.id.appoint_nextday);
        date = findViewById(R.id.appoint_date);
        calendar_btn = findViewById(R.id.appoint_calendar);
        next_btn = findViewById(R.id.appoint_next);
        left_appoint = findViewById(R.id.appoint_left);

        yesterday_btn.setOnClickListener(this);
        nextday_btn.setOnClickListener(this);
        date.setOnClickListener(this);
        calendar_btn.setOnClickListener(this);
        next_btn.setOnClickListener(this);


        date.setText(date_str);

        if (StringCalendarUtils.isToday((String) date.getText())){
            // yesterday_btn.setEnabled(false);
            yesterday_btn.setTextColor(getResources().getColor(R.color.dark_gray));
            isTodayFlag = true;
        }

        recyclerView = findViewById(R.id.appoint_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (!isSecondPageFlag)
            appointAdapter = new AppointAdapter(this, double_date_str);
        else
            appointAdapter = new AppointAdapter(this, info_single);
        recyclerView.setAdapter(appointAdapter);

        //滚动监听
        appointAdapter.setOnScrollListener(new AppointAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                recyclerView.scrollToPosition(pos);
            }
        });

        swipeRefresh = findViewById(R.id.refresh_appoint);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
            }
        });

        getCurrentDay();
        retrieveData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appoint_calendar:
                break;
            case R.id.appoint_next:
                break;
            case R.id.appoint_date:
                yesterday_btn.setEnabled(true);
                yesterday_btn.setTextColor(getResources().getColor(R.color.primary_white));
                isTodayFlag = false;

                final TextView textView_date = (TextView)v;

                new DatePickerDialog(AppointDoubleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year_choose, int month_choose, int dayOfMonth_choose) {

                        String datestr = year_choose+"-"+(month_choose+1)+"-"+dayOfMonth_choose;

                        if (StringCalendarUtils.isBeforeCurrentDate(datestr)){
                            ToastUtils.showShort("不能预约已经发出的班次~");
                            return;
                        } else if (!MyDateUtils.isWithinOneWeek(datestr)){
                            ToastUtils.showShort("仅可预约一周以内的班次哦~");
//                            return;
                        }
                        //textView_date.setText(year_choose+"-"+(month_choose+1)+"-"+dayOfMonth_choose);
                        String monthStr = StringCalendarUtils.getDoubleDigitMonth(month_choose);
                        String dayStr = StringCalendarUtils.getDoubleDigitDay(dayOfMonth_choose);
                        String dateStr = year_choose+"-"+monthStr+"-"+dayStr; //格式2018-07-19

                        /* 去程 和 返程 */
                        if (! isSecondPageFlag && StringCalendarUtils.isBeforeDateOfSecondPara(double_date_str, dateStr)){
                            ToastUtils.showShort("返程的时间不能早于去程~");
                            return;
                        } else if (isSecondPageFlag && StringCalendarUtils.isBeforeDateOfSecondPara(dateStr, info_single.getDate())){
                            ToastUtils.showShort("返程的时间不能早于去程~");
                            return;
                        }

                        textView_date.setText(dateStr);
                        // 统一日期格式为 yyyy-MM-dd

                        year = year_choose;
                        month = month_choose+1;
                        day = dayOfMonth_choose;
                        retrieveData();
                    }
                }, year,month,day).show();

                //如果当前日期是今天，则前一天不可用
                if (StringCalendarUtils.isToday((String) date.getText())){
                    // yesterday_btn.setEnabled(false);
                    yesterday_btn.setTextColor(getResources().getColor(R.color.dark_gray));
                    isTodayFlag = true;
                }

                break;
            case R.id.appoint_yesterday:
                if (isTodayFlag){
                    yesterday_btn.setEnabled(false);
                    ToastUtils.showShort("不能预约更前面的班次了~");
                    break;
                }

                String yesterday = MyDateUtils.getYesterdayStr((String) date.getText());
                /* 去程 和 返程 */
                if (! isSecondPageFlag && StringCalendarUtils.isBeforeDateOfSecondPara(double_date_str, yesterday)){
                    ToastUtils.showShort("返程的时间不能早于去程~");
                    break;
                } else if (isSecondPageFlag && StringCalendarUtils.isBeforeDateOfSecondPara(yesterday, info_single.getDate())){
                    ToastUtils.showShort("返程的时间不能早于去程~");
                    break;
                }

                date.setText(yesterday);
                if (StringCalendarUtils.isToday((String) date.getText())){
                    //yesterday_btn.setEnabled(false);
                    yesterday_btn.setTextColor(getResources().getColor(R.color.dark_gray));
                    isTodayFlag = true;
                }
                retrieveData();
                break;
            case R.id.appoint_nextday:
                //modifyDate(1);
                String tomorrow = MyDateUtils.getTomorrowStr((String) date.getText());

                if (!MyDateUtils.isWithinOneWeek(tomorrow)){
                    ToastUtils.showShort("仅可预约一周以内的班次~");
//                    return;
                }
                /* 去程 和 返程 */
                if (! isSecondPageFlag && StringCalendarUtils.isBeforeDateOfSecondPara(double_date_str, tomorrow)){
                    ToastUtils.showShort("返程的时间不能早于去程~");
                    break;
                } else if (isSecondPageFlag && StringCalendarUtils.isBeforeDateOfSecondPara(tomorrow, info_single.getDate())){
                    ToastUtils.showShort("返程的时间不能早于去程~");
                    break;
                }

                date.setText(tomorrow);
                yesterday_btn.setEnabled(true);
                yesterday_btn.setTextColor(getResources().getColor(R.color.primary_white));
                isTodayFlag = false;
                retrieveData();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //第一页 回 navi
        if (! isSecondPageFlag){
//            String data1 = departure_place_str;
//            String data2 = arrive_place_str;
//            String data3 = (String) date.getText();
//            Intent intent = new Intent(AppointDoubleActivity.this, AppointNaviActivity.class);
//            intent.putExtra("departure_place", data1);
//            intent.putExtra("arrive_place", data2);
//            intent.putExtra("singleway_date", data3);
//          //  startActivity(intent);
//            setResult(RESULT_OK, intent);
            finish();
        }
        // 第二页 回 第一页
        else{
            Intent appointIntent = new Intent(AppointDoubleActivity.this, AppointDoubleActivity.class);
            appointIntent.putExtra("departure_place", info_single.getDeparture_place());
            appointIntent.putExtra("arrive_place", info_single.getArrive_place());
            appointIntent.putExtra("singleway_date", info_single.getDate());
            appointIntent.putExtra("doubleway_date", date.getText());
            appointIntent.putExtra("target_page", 0);
//            startActivity(appointIntent);
            setResult(RESULT_OK, appointIntent);
            finish();
        }
    }

    private void retrieveData() {
        Log.d("retrivedata", "start");

        Calendar calendar = StringCalendarUtils.StringToCalendar((String) date.getText());
        line_type = ShiftUtils.getTypeByCalendar(calendar);
        final String appoint_date = (String) date.getText();

        RetrofitClient.getBusApi()
                .getAppointment(line_name, line_type, appoint_date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppointResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(AppointResponse response) {
                        Log.d(TAG, "onNext: ");
                        List<AppointInfo> infos = new ArrayList<>();
                        int i = 0;
                        for(AppointShortInfo shortinfo:response.getAppointment()){
                            AppointInfo info = new AppointInfo();
                            info.setShiftid(shortinfo.getShiftid());
                            info.setArrive_time(shortinfo.getArrive_time());
                            info.setDeparture_time(shortinfo.getDeparture_time());
                            info.setRemain_seat(shortinfo.getRemain_seat());
                            info.setId(i+"");
                            info.setLine_type(line_type);
                            info.setType(0);
                            info.setDate(appoint_date);
                            info.setArrive_place(arrive_place_str);
                            info.setDeparture_place(departure_place_str);
                            info.setAppoint_status(shortinfo.getRemain_seat()>0?1:0) ;
                            i++;
                            infos.add(info);
                        }

                        String left_appoint_info = "";
                        String legalholiday = MyDateUtils.isLegalHoliday(appoint_date);
                        if (! legalholiday.equals("无")) {
                            left_appoint_info = ShiftUtils.getChiLineName(line_name) + "的班车" + legalholiday + "停运哦~";
                        }
//                    else if ((line_name.equals("MinHangToQiBao") || line_name.equals("QiBaoToMinHang")) &&
//                            (StringCalendarUtils.isWeekend(StringCalendarUtils.StringToCalendar(appoint_date)))){
//                        left_appoint_info = ShiftUtils.getChiLineName(line_name) + "的班车双休日停运哦~";
//                    }
                        else if (infos.size() == 0){
                            left_appoint_info = "今日所有班次都已发出,去预约其他班次吧~";
                        } else {
                            int size = infos.size();
                            for (AppointInfo shortInfo : infos){
                                if (shortInfo.getRemain_seat() == 0){
                                    size -= 1;
                                }
                            }
                            left_appoint_info = "当日剩余可预约班次: " + size;
                        }
                        left_appoint.setText(left_appoint_info);
                        appointAdapter.setDataList(infos);
                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showShort("网络请求失败！请检查你的网络！");
                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        //mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void getCurrentDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(MyDateUtils.getBeginOfDay((String) date.getText()));
        year = cal.get(Calendar.YEAR);       //获取年月日时分秒
        month = cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            //第二页 回 第一页
            case 1:
                if (resultCode == RESULT_OK){

                    departure_place_str = data.getStringExtra("departure_place");
                    arrive_place_str = data.getStringExtra("arrive_place");
                    single_date_str = data.getStringExtra("singleway_date");
                    double_date_str = data.getStringExtra("doubleway_date");
                    date_str = single_date_str;
                    isSecondPageFlag = false;
                }
            // order 到 第二页
            case 2:
                info_single.setDeparture_place(data.getStringExtra("single_departure_place"));
                info_single.setArrive_place(data.getStringExtra("single_arrive_place"));
                info_single.setDeparture_time(data.getStringExtra("single_departure_time"));
                info_single.setArrive_time(data.getStringExtra("single_arrive_time"));
                info_single.setDate(data.getStringExtra("single_departure_date"));
                info_single.setShiftid(data.getStringExtra("single_shiftid"));
                info_single.setLine_type(data.getStringExtra("single_shift_type"));

                departure_place_str = data.getStringExtra("single_arrive_place");
                arrive_place_str = data.getStringExtra("single_departure_place");
                double_date_str = data.getStringExtra("double_departure_date");
                date_str = double_date_str;
                isSecondPageFlag = true;
                break;
            default:
                break;
        }
    }
}
