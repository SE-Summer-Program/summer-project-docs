package com.sjtubus.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.activity.RecordActivity;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.CalendarReminder;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.utils.ZxingUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{

    private List<RecordInfo> recordInfos = new ArrayList<>();
    private RecordActivity context;
    private RecordAdapter.OnItemClickListener mItemClickListener;
    private CompositeDisposable compositeDisposable;

    private int remind_minutes = 10, remindlist_select = 0, temp_select;
    private int[] remindtime_list = {10,30,60,120};
    private String[] remind_list = {"提前10分钟","提前30分钟","提前1小时","提前2小时"};
    private String[] remind_list_short = {"10分钟","30分钟","1小时","2小时"};

    public void setItemClickListener(RecordAdapter.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setDataList(List<RecordInfo> recordInfos) {
        this.recordInfos = recordInfos;
        notifyDataSetChanged();
    }

    public RecordAdapter(RecordActivity context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView submittime;
        TextView linename;
        TextView status;
        TextView departuremsg;
        TextView shiftid;
        TextView comment;
        Button remindbtn;
        Button cancelbtn;
        ImageView qrcode;

        ViewHolder(View view ){
            super(view);
            submittime = view.findViewById(R.id.record_submittime);
            linename = view.findViewById(R.id.record_linename);
            departuremsg = view.findViewById(R.id.record_departuremsg);
            shiftid = view.findViewById(R.id.record_shiftid);
            status = view.findViewById(R.id.record_status);
            comment = view.findViewById(R.id.record_comment);
            remindbtn = view.findViewById(R.id.record_remindbtn);
            cancelbtn = view.findViewById(R.id.record_cancelbtn);
            qrcode = view.findViewById(R.id.record_qrcode);

//            remindbtn.setOnClickListener();
        }
    }

    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        final RecordAdapter.ViewHolder holder = new RecordAdapter.ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v);
            }
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
        String confirm_time = recordInfos.get(position).getConfirmDate();
        String line_name = recordInfos.get(position).getLineName();
        String departure_msg = recordInfos.get(position).getDepartureMsg() + "发车";
        String shift_id = recordInfos.get(position).getShiftid();
        String submit_time = "预定时间： " + recordInfos.get(position).getSubmiTime();
        String comment = recordInfos.get(position).getComment();
        holder.submittime.setText(confirm_time);
        holder.linename.setText(line_name);
        holder.departuremsg.setText(departure_msg);
        holder.shiftid.setText(shift_id);
        holder.submittime.setText(submit_time);
        String info = shift_id + ";" + recordInfos.get(position).getDepartureDate() + ";"
                + UserManager.getInstance().getUser().getUsername();
        holder.qrcode.setImageBitmap(ZxingUtils.createQRImage(info,300,300));

        if (comment == null || comment.equals(""))
            holder.comment.setVisibility(View.GONE);
        else
            holder.comment.setText("特殊需求： " + comment);

        String status_str;
        if (StringCalendarUtils.isBeforeCurrentTime(recordInfos.get(position).getDepartureTimeComplete())){
            status_str = "已发车 ";
        } else {
            status_str = "未发车 ";
        }
        status_str += recordInfos.get(position).getStatus();
        holder.status.setText(status_str);


        holder.remindbtn.setOnClickListener(ChildListener);
        holder.cancelbtn.setOnClickListener(ChildListener);
        holder.remindbtn.setTag(position);
        holder.cancelbtn.setTag(position);
    }

    @Override
    public int getItemCount(){
        return recordInfos.size();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view);
    }

    private View.OnClickListener ChildListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v){
            switch (v.getId()){
                case R.id.record_remindbtn:

                    getCalendarPermission();
//                    ToastUtils.showShort("预约提醒功能还不能使用哦~");

                    final RecordInfo info_remind = recordInfos.get((int)v.getTag());

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("设置预约提醒")
                            .setIcon(R.mipmap.remind_128)
                            .setSingleChoiceItems(remind_list, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                   temp_select = which;
                                }
                            })
//                            .setCancelable(false)
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String shiftid_str = info_remind.getShiftid();
                                    String appoint_date_str = info_remind.getDepartureDate();
                                    String departure_time_str = info_remind.getDepartureTime();
                                    String line_name_str = info_remind.getLineName();

                                    remindlist_select = temp_select;
                                    remind_minutes = remindtime_list[remindlist_select];

                                    Date departure_time = StringCalendarUtils.StringToTime(appoint_date_str +" "+ departure_time_str);
                                    long begintime = departure_time.getTime();
//                                    Log.i("DEPART_TIME",departure_time.getDay()+":"+departure_time.getHours()+":"+departure_time.getMinutes());
                                    String description = "您预约的于" + appoint_date_str + " " + departure_time_str + "从" + line_name_str
                                            + "的" + shiftid_str + "号校区巴士即将于"+remind_list_short[remindlist_select]+"后发车，请记得按时前去乘坐哦~";

                                    CalendarReminder.addCalendarEventRemind(App.getInstance(), "校车发车提醒", description, begintime, begintime, remind_minutes, new CalendarReminder.onCalendarRemindListener() {
                                        public void onFailed(CalendarReminder.onCalendarRemindListener.Status error_code) {
                                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("预约提醒设置失败")
                                                    .setContentText("请检查你的网络~")
                                                    .setConfirmText("确定")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .show();
                                        }
                                        public void onSuccess() {
                                            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("预约提醒设置成功!")
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
                                    });
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            temp_select = 0;
                                        }
                                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    break;
                case R.id.record_cancelbtn:
                    final RecordInfo info_cancel = recordInfos.get((int)v.getTag());

                    new AlertDialog.Builder(context)
                        .setMessage("确认取消预约吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String username = UserManager.getInstance().getUser().getUsername();
                                String shiftid = info_cancel.getShiftid();
                                String appoint_date = info_cancel.getDepartureDate();
                                String departure_time = info_cancel.getDepartureTime();

                                if (StringCalendarUtils.isBeforeCurrentTime(appoint_date + " " + departure_time)){
                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("取消预约失败")
                                            .setContentText("该班次已经发出，不能取消预约了哦~")
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

                                RequestBody requestBody = new FormBody.Builder()
                                        .add("username", username)
                                        .add("user_role",UserManager.getInstance().getRole())
                                        .add("shiftid", shiftid)
                                        .add("appoint_date", appoint_date)
                                        .build();
                                retrieveData(requestBody);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                    break;
            }
        }
    };

    private void getCalendarPermission(){
        //获得读写日历权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED
                ) {
            Toast.makeText(context,"没有读写系统日历权限,请手动开启权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 100);
        }
    }

    public void addDisposable(Disposable s) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }

        this.compositeDisposable.add(s);
    }

    private void retrieveData(RequestBody requestBody) {
        RetrofitClient.getBusApi()
            .deleteAppoint(requestBody)
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
                                .setTitleText("取消预约成功!")
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
                    Log.i("record", "onError: record ");
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

    /*
     * 确定当前点击的item位置并返回
     */
    private int getCurrentPosition(String uuid) {
        for (int i = 0; i < recordInfos.size(); i++) {
            if (uuid.equalsIgnoreCase(recordInfos.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }
}
