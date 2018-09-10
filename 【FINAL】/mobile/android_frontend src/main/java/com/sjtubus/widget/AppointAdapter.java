package com.sjtubus.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sjtubus.R;
import com.sjtubus.activity.AppointDoubleActivity;
import com.sjtubus.activity.OrderActivity;
import com.sjtubus.model.AppointInfo;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.ShiftInfo;
import com.sjtubus.model.User;
import com.sjtubus.model.response.CollectionResponse;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.model.response.ShiftInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.MyDateUtils;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;

import java.util.ArrayList;
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

public class AppointAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;
    private List<AppointInfo> appointInfoList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnScrollListener onScrollListener;
    private CompositeDisposable compositeDisposable;

    private boolean hasConflictSchedule = false;

    private boolean isSingleWayFlag;
    private boolean isSecondPageFlag;
    private String double_date_str;
    private AppointInfo appointInfo = new AppointInfo();

    /* 单程构造函数 */
    public AppointAdapter(Context context){
        this.context = context;
        this.isSingleWayFlag = true;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /* 往返第一页构造函数 */
    public AppointAdapter(Context context, String double_date_str){
        this.context = context;
        this.isSingleWayFlag = false;
        this.isSecondPageFlag = false;
        this.double_date_str = double_date_str;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /* 往返第二页构造函数 */
    public AppointAdapter(Context context, AppointInfo appointInfo){
        this.context = context;
        this.isSingleWayFlag = false;
        this.isSecondPageFlag = true;
        this.appointInfo.copy(appointInfo);
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setDataList(List<AppointInfo> appointInfoList){
        this.appointInfoList = appointInfoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case AppointInfo.PARENT_ITEM:
                view = layoutInflater.inflate(R.layout.item_appoint_parent, parent, false);
                return new AppointParentViewHolder(context, view);
            case AppointInfo.CHILD_ITEM:
                view = layoutInflater.inflate(R.layout.item_appoint_child, parent, false);
                return new AppointChildViewHolder(context, view);
            default:
                view = layoutInflater.inflate(R.layout.item_appoint_parent, parent, false);
                return new AppointParentViewHolder(context, view);
        }
    }

    /*
     * 根据不同的类型绑定不同的view
     */
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case AppointInfo.PARENT_ITEM:
                AppointParentViewHolder parentViewHolder = (AppointParentViewHolder)holder;
                parentViewHolder.bindView(appointInfoList.get(position), position, appointItemClickListener);
                break;
            case AppointInfo.CHILD_ITEM:
                AppointChildViewHolder childViewHolder = (AppointChildViewHolder)holder;
                childViewHolder.bindView(appointInfoList.get(position), position, ChildListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return appointInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return appointInfoList.get(position).getType();
    }

    private AppointItemClickListener appointItemClickListener = new AppointItemClickListener() {
        @Override
        public void onExpandChildItem(AppointInfo bean) {
            int position = getCurrentPosition(bean.getId()); //确定当前点击的item位置
            AppointInfo child = getChildDataBean(bean); //获取要展示的子布局数据对象
            if (child == null){
                return;
            }
            add(child, position+1); //在当前的item下方插入
            if (position == appointInfoList.size()-2 && onScrollListener != null){
                onScrollListener.scrollTo(position + 1); //向下滚动，使得子布局能够完全展示
            }
        }

        @Override
        public void onHideChildItem(AppointInfo bean) {
            int position = getCurrentPosition(bean.getId()); //确定当前点击的item位置
            AppointInfo child = getChildDataBean(bean);
            if (child == null){
                return;
            }
            remove(position + 1); //删除
            if (onScrollListener != null){
                onScrollListener.scrollTo(position);
            }
        }
    };

    private View.OnClickListener ChildListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            AppointInfo info = appointInfoList.get((int)v.getTag()-1);
            String departure_time = info.getDeparture_time();
            String arrive_time = info.getArrive_time();
            String departure_date = info.getDate();
            String shiftid = info.getShiftid();

            switch (v.getId()){
                case R.id.appointitem_reservebtn:
                    if (! MyDateUtils.isWithinOneWeek(departure_date)){
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("预约失败")
                                .setContentText("仅可预约一周以内的班次哦，一周以后的只能看看~")
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

                    Button reserve_btn = (Button)v.findViewById(R.id.appointitem_reservebtn);
                    if (reserve_btn.getText().toString().equals("无座")){
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("预约失败")
                                .setContentText("该班次已经没有座位了，不能预约了哦~")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                        return;
                    }
                    hasConflictSchedule = false;
                    //Log.i(TAG, departure_time + " " + arrive_time+ " " + departure_date);
                    retrofitRecord(departure_time, arrive_time, departure_date, info);
                    break;
                case R.id.appointitem_collectbtn:
                    retrofitCollection(shiftid);
                    break;

                case R.id.appointitem_infobtn:
                    retrofitShiftInfo(shiftid);
                    break;
                case R.id.import_rideinfo_btn:
//                    if (! StringCalendarUtils.isBeforeCurrentTime(departure_date + " " + departure_time)){
//                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("录入信息失败")
//                                .setContentText("该班次还未发出")
//                                .setConfirmText("确定")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sDialog) {
//                                        sDialog.cancel();
//                                    }
//                                })
//                                .show();
//                        return;
//                    }
                    importRideInfo(info);
                    break;
            }
        }
    };

    /*
     * 在父布局下方插入一条数据
     */
    public void add(AppointInfo bean, int position) {
        appointInfoList.add(position, bean);
        notifyItemInserted(position);
    }

    /*
     *移除子布局数据
     */
    private void remove(int position) {
        appointInfoList.remove(position);
        notifyItemRemoved(position);
    }

    /*
     * 确定当前点击的item位置并返回
     */
    private int getCurrentPosition(String uuid) {
        for (int i = 0; i < appointInfoList.size(); i++) {
            if (uuid.equalsIgnoreCase(appointInfoList.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    /*
     * 封装子布局数据对象并返回
     * 注意，此处只是重新封装一个DataBean对象，为了标注Type为子布局数据，进而展开，展示数据
     * 要和onHideChildren方法里的getChildBean()区分开来
     */
    private AppointInfo getChildDataBean(AppointInfo bean) {
        AppointInfo child = new AppointInfo();
        child.setLine_type(bean.getLine_type());
        child.setDate(bean.getDate());
        child.setAppoint_status(bean.getAppoint_status());
        child.setShiftid(bean.getShiftid());
        child.setId(bean.getId());
        child.setRemain_seat(bean.getRemain_seat());
        child.setDeparture_time(bean.getDeparture_time());
        child.setArrive_time(bean.getArrive_time());
        child.setDeparture_place(bean.getDeparture_place());
        child.setArrive_place(bean.getArrive_place());
        child.setType(1);
        //child.setChild_msg(bean.getChild_msg());
        return child;
    }

    public interface OnScrollListener{
        void scrollTo(int pos);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    private void retrofitRecord(final String departure_time, final String arrive_time, final String departure_date, final AppointInfo info_reserve){
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
                    Log.d(TAG, "onNext: ");
//                    if(response.getRecordInfos()!=null && response.getRecordInfos().size()!=0){
////                        RecordAdapter recordAdapter = new RecordAdapter( );
////                        recordAdapter.setDataList(response.getRecordInfos());
//                        hasConflictSchedule = false;
//                    }

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
                            Log.i(TAG, "false");
                            Log.i(TAG, "--------");
                        }
                    }

                    if (hasConflictSchedule){
                        ToastUtils.showShort("不能预约行程冲突的班次哦~");
                        return;
                    }

                    pressReserveButton(departure_time, arrive_time, departure_date, info_reserve);
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
                        new AlertDialog.Builder(context)
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

    private void retrofitCollection(final String shiftid){
        String username = UserManager.getInstance().getUser().getUsername();

        RetrofitClient.getBusApi()
                .getCollection(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(CollectionResponse response) {
                        List<String> shifts = response.getShifts();
                        Log.i(TAG, shifts.size()+"");

                        if (shifts != null && shifts.size() != 0) {
                            for (String shift : shifts) {
                                if (shift.equals(shiftid)) {
                                    new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText("您已收藏过该班次啦~")
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
                            }
                        }
                        Log.i(TAG, "addcollection");
                        addCollection(shiftid);
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

    private void addCollection(final String shiftid){
        User user = UserManager.getInstance().getUser();

        RequestBody requestBody = new FormBody.Builder()
                .add("userid", user.getUserId())
                .add("username", user.getUsername())
                .add("shiftid", shiftid)
                .build();

        RetrofitClient.getBusApi()
                .addCollection(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(HttpResponse response) {
                        Log.i(TAG, "addcollection success");
                        String message = "您已成功收藏班次" + shiftid + "~";
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("收藏成功~")
                                .setContentText(message)
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        //sDialog.dismiss();
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "addcollection fail");
                        e.printStackTrace();
                        ToastUtils.showShort("网络请求失败！请检查你的网络！");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public void addDisposable(Disposable s) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }

        this.compositeDisposable.add(s);
    }

    private void pressReserveButton(String departure_time, String arrive_time, String departure_date, AppointInfo info_reserve){
        /* 单程 */
        // 单程 到 order
        if (isSingleWayFlag) {
            Intent orderIntent = new Intent(context, OrderActivity.class);
            orderIntent.putExtra("departure_place", info_reserve.getDeparture_place());
            orderIntent.putExtra("arrive_place", info_reserve.getArrive_place());
            orderIntent.putExtra("departure_time", StringCalendarUtils.HHmmssToHHmm(departure_time));
            orderIntent.putExtra("arrive_time", StringCalendarUtils.HHmmssToHHmm(arrive_time));
            orderIntent.putExtra("departure_date", departure_date);
            orderIntent.putExtra("shiftid", info_reserve.getShiftid());
            orderIntent.putExtra("shift_type", info_reserve.getLine_type());

            orderIntent.putExtra("appoint_type", 0);
            context.startActivity(orderIntent);
        }

        /* 往返去程 */
        // 第一页 到 第二页
        else if (! isSecondPageFlag){
            Intent appointDoubleIntent = new Intent(context, AppointDoubleActivity.class);

            appointDoubleIntent.putExtra("single_departure_place", info_reserve.getDeparture_place());
            appointDoubleIntent.putExtra("single_arrive_place", info_reserve.getArrive_place());
            appointDoubleIntent.putExtra("single_departure_time", StringCalendarUtils.HHmmssToHHmm(departure_time));
            //ToastUtils.showShort(StringCalendarUtils.HHmmssToHHmm(departure_time));
            appointDoubleIntent.putExtra("single_arrive_time", StringCalendarUtils.HHmmssToHHmm(arrive_time));
            appointDoubleIntent.putExtra("single_departure_date", departure_date);
            appointDoubleIntent.putExtra("single_shiftid", info_reserve.getShiftid());
            appointDoubleIntent.putExtra("single_shift_type", info_reserve.getLine_type());

            appointDoubleIntent.putExtra("double_departure_date", double_date_str);
            appointDoubleIntent.putExtra("target_page", 1);

            ((AppointDoubleActivity)context).startActivityForResult(appointDoubleIntent, 1);
        }

        /* 往返返程 */
        // 第二页 到 order
        else{
            String single_starttime = appointInfo.getDate() + " " + appointInfo.getDeparture_time();
            String single_endtime =  appointInfo.getDate() + " " + appointInfo.getArrive_time();
            String double_starttime = departure_date + " " + StringCalendarUtils.HHmmssToHHmm(departure_time);
            String double_endtime = departure_date + " " + StringCalendarUtils.HHmmssToHHmm(arrive_time);
            // ToastUtils.showLong(single_starttime + " " + single_endtime + " " + double_starttime + " " + double_endtime);

            if (StringCalendarUtils.isBeforeTimeOfSecondParaHHmm(double_endtime, single_starttime)){
                ToastUtils.showShort("返程的时间不能早于去程哦~");
                return;
            } else if (! StringCalendarUtils.isBeforeTimeOfSecondParaHHmm(single_endtime, double_starttime)){
                ToastUtils.showShort("不能预约行程冲突的班次哦~~");
                return;
            }

            Intent orderDoubleIntent = new Intent(context, OrderActivity.class);

            orderDoubleIntent.putExtra("departure_place", appointInfo.getDeparture_place());
            orderDoubleIntent.putExtra("arrive_place", appointInfo.getArrive_place());
            orderDoubleIntent.putExtra("departure_time", appointInfo.getDeparture_time());
            orderDoubleIntent.putExtra("arrive_time", appointInfo.getArrive_time());
            orderDoubleIntent.putExtra("departure_date", appointInfo.getDate());
            orderDoubleIntent.putExtra("shiftid", appointInfo.getShiftid());
            orderDoubleIntent.putExtra("shift_type", appointInfo.getLine_type());

            orderDoubleIntent.putExtra("double_departure_place", info_reserve.getDeparture_place());
            orderDoubleIntent.putExtra("double_arrive_place", info_reserve.getArrive_place());
            orderDoubleIntent.putExtra("double_departure_time", StringCalendarUtils.HHmmssToHHmm(departure_time));
            orderDoubleIntent.putExtra("double_arrive_time", StringCalendarUtils.HHmmssToHHmm(arrive_time));
            orderDoubleIntent.putExtra("double_departure_date", departure_date);
            orderDoubleIntent.putExtra("double_shiftid", info_reserve.getShiftid());
            orderDoubleIntent.putExtra("double_shift_type", info_reserve.getLine_type());

            orderDoubleIntent.putExtra("appoint_type", 1);
//            context.startActivity(orderDoubleIntent);
            ((AppointDoubleActivity)context).startActivityForResult(orderDoubleIntent, 2);
        }
    }

    private void importRideInfo(final AppointInfo info){
        View view = LayoutInflater.from(context).inflate(R.layout.alertdialog_importinfo,null);//获得布局信息
        final EditText bus_plate = view.findViewById(R.id.bus_plate);
        final EditText seat_num = view.findViewById(R.id.seat_num);
        final EditText teacher_num =  view.findViewById(R.id.teacher_num);
        final EditText student_num = view.findViewById(R.id.student_num);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("录入发车信息");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                ToastUtils.showShort("正在录入信息..");
                RequestBody requestBody = new FormBody.Builder()
                        .add("ride_date", info.getDate())
                        .add("shift_id", info.getShiftid())
                        .add("bus_plate", bus_plate.getText().toString())
                        .add("line_type", info.getLine_type())
                        .add("teacher_num", teacher_num.getText().toString())
                        .add("student_num", student_num.getText().toString())
                        .add("remain_num", String.valueOf(info.getRemain_seat()))
                        .add("seat_num",seat_num.getText().toString())
                        .build();

                if(bus_plate.getText().toString().equals("")||teacher_num.getText().toString().equals("")||
                        seat_num.getText().toString().equals("")||student_num.getText().toString().equals("")){
                    ToastUtils.showShort("信息填写不完整!");
                    return;
                }

                RetrofitClient.getBusApi()
                        .importRideInfo(requestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<HttpResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }
                            @Override
                            public void onNext(HttpResponse response) {
                                Log.d(TAG, "onNext: ");
                                if(response.getMsg().equals("success")){
                                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("录入发车信息完成!")
                                            .setContentText("录入发车信息完成")
                                            .setConfirmText("确定")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.cancel();
                                                }
                                            })
                                            .show();
                                }else{
                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("录入发车信息失败!")
                                            .setContentText("录入发车信息失败!")
                                            .show();
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                ToastUtils.showShort("网络请求失败！请检查你的网络！");
                            }
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                                dialogInterface.dismiss();
                            }
                        });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
