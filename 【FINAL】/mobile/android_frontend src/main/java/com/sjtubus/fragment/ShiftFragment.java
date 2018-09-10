package com.sjtubus.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.response.ScheduleResponse;
import com.sjtubus.model.response.StationSingleResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.ShiftAdapter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by Allen
 * Time  2018/7/8
 */

public class ShiftFragment extends BaseFragment implements View.OnClickListener{
    private static ShiftFragment mFragment = null;

//    private String type = "NormalWorkday";
//    private String line_name = "MinToXu";
    private String type;
    private String line_name;

    private ShiftAdapter mAdapter;

    private TextView choose_station, choose_hint;
    private String[] stationList = {"菁菁堂","校医院","东上院","东中院","新图书馆","行政B楼","电信学院","凯旋门","机动学院","庙门",
            "船建学院","文选医学楼","学生服务中心","西区学生公寓","第四餐饮大楼","华联生活中心","包玉刚图书馆","材料学院"};
    private int choose_index = 0;

    public static ShiftFragment getInstance(String type,String line_name) {
        if(mFragment == null){
            mFragment = new ShiftFragment();
        }
        mFragment.setTypeAndLine(type,line_name);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shift, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.shift_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));

        if (ShiftUtils.isLoopLine(line_name)) {
            LinearLayout linearLayout = view.findViewById(R.id.choose_bar);
            linearLayout.setVisibility(View.VISIBLE);
            choose_station = view.findViewById(R.id.choose_station);
            choose_station.setOnClickListener(this);
            choose_hint = view.findViewById(R.id.choose_hint);
            choose_hint.setOnClickListener(this);

            mAdapter = new ShiftAdapter(mRecyclerView.getContext(), true);
            mRecyclerView.setAdapter(mAdapter);
            retrieveDataOfLoopLine((String) choose_station.getText());
        } else {
            mAdapter = new ShiftAdapter(mRecyclerView.getContext(), false);
            mRecyclerView.setAdapter(mAdapter);
            retrieveData();
        }

        return view;
    }

    private void setTypeAndLine(String type,String line_name){
        this.type = type;
        this.line_name = line_name;
    }

    private void retrieveData() {
        RetrofitClient.getBusApi()
            .getSchedule(type,line_name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ScheduleResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(ScheduleResponse response) {
                    Log.i(TAG, "onNext: ");

                    if (response.getSchedule() == null){
                        ToastUtils.showShort("网络请求失败！");
                        return;
                    }
                    mAdapter.setDataList(response.getSchedule());
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

    private void retrieveDataOfLoopLine(String station) {
        RetrofitClient.getBusApi()
            .getScheduleOfLoopLine(station)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<StationSingleResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(StationSingleResponse response) {
                    Log.d(TAG, "onNext: ");
                    if (line_name.equals("LoopLineAntiClockwise") && type.equals("NormalWorkday")) {
                        mAdapter.setDataListOfLoopLine(response.getStation().getAntiClockTotal());
                    }
                    else if (line_name.equals("LoopLineAntiClockwise") && type.equals("HolidayWorkday"))
                        mAdapter.setDataListOfLoopLine(response.getStation().getVacAntiClockTotal());
                    else if (line_name.equals("LoopLineClockwise") && type.equals("NormalWorkday"))
                        mAdapter.setDataListOfLoopLine(response.getStation().getClockTotal());
                    else if (line_name.equals("LoopLineClockwise") && type.equals("HolidayWorkday"))
                        mAdapter.setDataListOfLoopLine(response.getStation().getVacClockTotal());
                    else
                        ToastUtils.showShort("发生未知错误！");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_hint:
            case R.id.choose_station:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("选择站点");
                builder.setSingleChoiceItems(stationList, choose_index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose_index = which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //choose_index = which;
                        choose_station.setText(stationList[choose_index]);
                        retrieveDataOfLoopLine((String) choose_station.getText());
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            default:
                break;
        }
    }

}