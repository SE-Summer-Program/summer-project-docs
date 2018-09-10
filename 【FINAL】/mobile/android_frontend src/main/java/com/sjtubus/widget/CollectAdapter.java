package com.sjtubus.widget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.activity.CollectActivity;
import com.sjtubus.activity.MainActivity;
import com.sjtubus.model.Collection;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.ShiftInfo;
import com.sjtubus.model.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.model.response.ShiftInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.MyDateUtils;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder>{

    private List<Collection> collections;
    private CollectActivity context;
    private CollectAdapter.OnItemClickListener mItemClickListener;
    private CompositeDisposable compositeDisposable;

    private String TAG = "collectadapter";

    public void setItemClickListener(CollectAdapter.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setDataList(List<Collection> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    public CollectAdapter(CollectActivity context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView linename;
        TextView shiftid;
        TextView detail;
        Button cancelbtn;
        Button fastappointbtn;

        ViewHolder(View view ){
            super(view);
            linename = view.findViewById(R.id.collect_linename);
            shiftid = view.findViewById(R.id.collect_shiftid);
            detail = view.findViewById(R.id.collect_shiftdetail);
            cancelbtn = view.findViewById(R.id.collect_cancel);
            fastappointbtn = view.findViewById(R.id.collect_fast);

//            remindbtn.setOnClickListener();
        }
    }

    @NonNull
    @Override
    public CollectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, parent, false);
        final CollectAdapter.ViewHolder holder = new CollectAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CollectAdapter.ViewHolder holder, final int position) {
        final String shiftid = collections.get(position).getShiftid();

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
                    Log.i(TAG, "onbindviewholder - getshiftinfos");
                    ShiftInfo shiftInfo = response.getShiftInfo();
                    String linename = shiftInfo.getLineNameCn();
                    String detail = "发车时间：" + shiftInfo.getDepartureTime()
                            + "\n车牌号：" + shiftInfo.getBusPlateNum()
                            + "\n核载人数：" + shiftInfo.getBusSeatNum()
                            + "\n司机姓名：" + shiftInfo.getDriverName()
                            + "\n司机联系方式：" + shiftInfo.getDriverPhone()
                            + "\n备注：" + shiftInfo.getComment();

                    holder.linename.setText(linename);
                    holder.shiftid.setText(shiftid);
                    holder.detail.setText(detail);

                    holder.cancelbtn.setOnClickListener(ChildListener);
                    holder.cancelbtn.setTag(position);
                    holder.fastappointbtn.setOnClickListener(ChildListener);
                    holder.fastappointbtn.setTag(position);
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

    public void addDisposable(Disposable s) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }

        this.compositeDisposable.add(s);
    }


    @Override
    public int getItemCount(){
        return collections==null ? 0 : collections.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
    }

    private View.OnClickListener ChildListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v){
            switch (v.getId()){
                case R.id.collect_cancel:
                    final Collection info_cancel = collections.get((int)v.getTag());

                    new AlertDialog.Builder(context)
                            .setMessage("确认取消收藏吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    User user = UserManager.getInstance().getUser();
                                    String shiftid = info_cancel.getShiftid();

                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("userid", user.getUserId())
                                            .add("username", user.getUsername())
                                            .add("shiftid", shiftid)
                                            .build();
                                    deleteCollection(requestBody);
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();

                    break;

                case R.id.collect_fast:
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);       //获取年月日时分秒
                    int month = calendar.get(Calendar.MONTH);   //获取到的月份是从0开始计数
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    final String shiftid = collections.get((int)v.getTag()).getShiftid();

                    new DatePickerDialog(Objects.requireNonNull(context), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year_choose, int month_choose, int dayOfMonth_choose) {

                            /* 这里改过 */
                            String monthStr = StringCalendarUtils.getDoubleDigitMonth(month_choose);
                            String dayStr = StringCalendarUtils.getDoubleDigitDay(dayOfMonth_choose);
                            final String dateStr = year_choose+"-"+monthStr+"-"+dayStr;

                            if (StringCalendarUtils.isBeforeCurrentDate(dateStr)) {
                                ToastUtils.showShort("不能预约已经发出的班次~");
                                return;
                            } else if (! MyDateUtils.isWithinOneWeek(dateStr)){
                                ToastUtils.showShort("仅可预约一周以内的班次~");
                                return;
                            }

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
                                            final String linename = shiftInfo.getLineName();
                                            final String linenameCn = shiftInfo.getLineNameCn();
                                            final String departure_time = shiftInfo.getDepartureTime();
                                            final String arrive_time = shiftInfo.getArriveTime();


                                            if (StringCalendarUtils.isBeforeCurrentTime(dateStr + " " + departure_time)){
                                                ToastUtils.showShort("不能预约已经发出的班次~");
                                                return;
                                            }

                                            retrofitRecord(departure_time, arrive_time, dateStr, shiftid, linename, linenameCn);
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
                    }, year,month,day).show();

                    break;
                default:
                    break;
            }
        }
    };

    private void deleteCollection(RequestBody requestBody){
        RetrofitClient.getBusApi()
                .deleteCollection(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HttpResponse response) {
                        if(response.getError()==0){
                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("取消收藏成功!")
                                    .setConfirmText("确定")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                            context.finish();
                                        }
                                    })
                                    .show();
                        }
                        Log.i(TAG, "onNext: ");
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

    private void retrofitRecord(final String departure_time, final String arrive_time, final String departure_date,
                                final String shiftid, final String linename, final String linenameCn){
        //获取当前用户的username
        String username = UserManager.getInstance().getUser().getUsername();
        String user_role = UserManager.getInstance().getRole();

        RetrofitClient.getBusApi()
                .getRecordInfos(username, user_role)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecordInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        /*
                         * 因为飘红删去了
                         */
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(RecordInfoResponse response) {
                        boolean hasConflictSchedule = false;

                        String appoint_starttime = departure_date + " " + departure_time;
                        String appoint_endtime = departure_date + " " + arrive_time;

                        Log.i(TAG, "appoint_starttime" + appoint_starttime);
                        Log.i(TAG, "appoint_endtime" + appoint_endtime);
                        List<RecordInfo> recordInfos = response.getRecordInfos();
                        //输出为空
                        if(recordInfos != null) {
                            for (RecordInfo recordInfo : recordInfos) {
                                String record_starttime = recordInfo.getDepartureDate() + " " + recordInfo.getDepartureTime();
                                String record_endtime = recordInfo.getDepartureDate() + " " + recordInfo.getArriveTime();

                                //ToastUtils.showShort(record_starttime + " " + record_endtime);
                                //记录上出发时间比预约的结束时间晚，或者结束时间比预约的出发时间早，就没问题
                                if (StringCalendarUtils.isBeforeTimeOfSecondPara(appoint_endtime, record_starttime))
                                    hasConflictSchedule = false;
                                else if (StringCalendarUtils.isBeforeTimeOfSecondPara(record_endtime, appoint_starttime))
                                    hasConflictSchedule = false;
                                else {
                                    hasConflictSchedule = true;
                                    break;
                                }
                            }
                        }

                        if (hasConflictSchedule){
                            ToastUtils.showShort("不能预约行程冲突的班次哦~");
                            return;
                        }

                        new AlertDialog.Builder(context)
                                .setMessage("确认快速预约" + departure_date + "发出的" + shiftid + "列班车吗？")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        submitAppoint(linename, linenameCn, shiftid, departure_date, departure_time);
                                    }
                                })
                                .setNegativeButton("取消", null)
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

    private void submitAppoint(String linename, final String linenameCn, final String shiftid, final String datestr, final String departure_time){
        RequestBody requestBody = new FormBody.Builder()
                .add("line_name", linename)
                .add("shift_id", shiftid)
                .add("appoint_date", datestr)
                .add("submit_time", StringCalendarUtils.getCurrentTime())
                .add("username",UserManager.getInstance().getUser().getUsername())
                .add("user_role",UserManager.getInstance().getRole())
                .add("comment", "")
                .build();

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
                            //显示预约成功
                            String message = "您已成功预约【" + datestr + " " + departure_time + linenameCn +
                                    "的" + shiftid + "号校区巴士】，请记得按时前去乘坐哦~";

                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("预约成功~")
                                    .setContentText(message)
                                    .setConfirmText("确定")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            context.startActivity(new Intent(context, MainActivity.class));
                                        }
                                    })
                                    .show();
                        }else{
//                        Log.i(TAG, "fail");
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("预约失败!")
                                    .setContentText(response.getMsg())
                                    .show();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onerror");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete ");
                        //mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    /*
     * 确定当前点击的item位置并返回
     */
    private int getCurrentPosition(String uuid) {
        for (int i = 0; i < collections.size(); i++) {
            if (uuid.equalsIgnoreCase(collections.get(i).getId())) { //有改动
                return i;
            }
        }
        return -1;
    }
}
