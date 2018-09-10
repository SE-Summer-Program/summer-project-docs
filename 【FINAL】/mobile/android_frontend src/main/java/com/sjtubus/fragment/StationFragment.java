package com.sjtubus.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtubus.R;
import com.sjtubus.model.response.StationResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.StationAdapter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class StationFragment extends BaseFragment {
    private static StationFragment mFragment = null;

    private String line_name = "MinToXu";

    private StationAdapter mAdapter;

    public static StationFragment getInstance(String line_name) {
        if(mFragment == null){
            mFragment = new StationFragment();
        }
        mFragment.setLine(line_name);
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
        mAdapter = new StationAdapter(mRecyclerView.getContext());
        mRecyclerView.setAdapter(mAdapter);
        retrieveData();
        return view;
    }

    private void setLine(String line_name){
        this.line_name = line_name;
    }

    private void retrieveData() {
        RetrofitClient.getBusApi()
            .getLineStation(line_name)
            .subscribeOn(Schedulers.io()) //在子线程里运行请求
            .observeOn(AndroidSchedulers.mainThread()) //请求结束 主线程回调
            .subscribe(new Observer<StationResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(StationResponse response) {
                    Log.d(TAG, "onNext: ");
                    mAdapter.setDataList(response.getStations());
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


}
