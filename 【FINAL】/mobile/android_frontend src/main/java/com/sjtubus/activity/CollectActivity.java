package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.response.CollectionResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.CollectAdapter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class CollectActivity extends BaseActivity implements View.OnClickListener, CollectAdapter.OnItemClickListener{

    private CollectAdapter collectAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private TextView collect_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_collect;
    }

    private void initView(){
        Toolbar mToolbar = findViewById(R.id.toolbar_collect);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.back_32);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collect_total = (TextView) findViewById(R.id.collect_total);

        RecyclerView recyclerView = findViewById(R.id.recycle_collect);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //设置布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//      recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //分割线

        collectAdapter = new CollectAdapter(this);
        collectAdapter.setItemClickListener(this);
        recyclerView.setAdapter(collectAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        swipeRefresh = findViewById(R.id.refresh_collect);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCollect();
            }
        });

        refreshCollect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onItemClick(View view) {

    }

    private void refreshCollect(){
        //获取当前用户的username
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
                    if (response.getCollections()!=null && response.getCollections().size()!=0){
                        collectAdapter.setDataList(response.getCollections());
                    }
                    swipeRefresh.setRefreshing(false);

                    int total_amount = response.getCollections()==null?0:response.getCollections().size();
                    String collect_info = "共计 " + total_amount + " 条班次收藏";
                    collect_total.setText(collect_info);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    swipeRefresh.setRefreshing(false);
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
