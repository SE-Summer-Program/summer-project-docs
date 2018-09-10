package com.sjtubus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.sjtubus.model.response.LineInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.LineAdapter;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class LineActivity extends BaseActivity implements LineAdapter.OnItemClickListener{

    @BindView(R.id.toolbar_line)
    Toolbar mToolbar;
    @BindView(R.id.recycle_schedule)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_schedule)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.line_total)
    TextView line_total;

    private LineAdapter adapter;
    private MyDialogListener dialogListener = new MyDialogListener();

    private String[] type_list = {"在校期-工作日", "在校期-双休日/节假日", "寒暑假-工作日","寒暑假-双休日"};
    private String[] type_list_E = {"NormalWorkday","NormalWeekendAndLegalHoliday","HolidayWorkday","HolidayWeekend"};
    private int select;

    private String type;

    //private SwipeRefreshView mSwipeRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        //initSwipeRefreshView();
    }

    public int getContentViewId(){
        return R.layout.activity_line;
    }

    public void initViews(){
        mToolbar.setNavigationIcon(R.mipmap.menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putDialog();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); //设置为垂直布局，默认
        recyclerView.setLayoutManager(layoutManager); //设置布局管理器
       // recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //分割线
        adapter = new LineAdapter(this);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
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

        type = ShiftUtils.getTypeOfToday();
        initSelected(type);
        setAndShowSchedule(type);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSchedule(type_list_E[select]);
            }
        });

    }

    private void initSelected(String type){
        for (int i = 0; i < type_list_E.length; i++){
            if (type.equals(type_list_E[i])){
                select = i;
                return;
            }
        }
        select = 2;
    }

    public void setAndShowSchedule(String type){
        //Log.d("LineActivity", type);
        RetrofitClient.getBusApi()
            .getLineInfos(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<LineInfoResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(LineInfoResponse response) {
                    Log.d(TAG, "onNext: ");
                    adapter.setDataList(response.getLineInfos());

                    int total_amount = response.getLineInfos().size();
                    String line_info = type_list[select] + "有 " + total_amount +  " 条线路正常运行";
                    line_total.setText(line_info);
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

    @Override
    public void onItemClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        String line_name = adapter.getLinename(position);
        Intent schedule_intent = new Intent(LineActivity.this, ScheduleActivity.class);
        schedule_intent.putExtra("LINE_TYPE",type_list_E[select]);
        schedule_intent.putExtra("LINE_NAME",line_name);
        startActivity(schedule_intent);
    }

    public void putDialog() {
        new AlertDialog.Builder((LineActivity.this))
                .setTitle("选择时间段")
                .setIcon(R.mipmap.type)
                .setSingleChoiceItems(type_list, select, dialogListener)
                .setCancelable(false)
                .setPositiveButton("确定",dialogListener)
                .setNegativeButton("取消",dialogListener)
                .create().show();
    }

    public void refreshSchedule(String type){
        RetrofitClient.getBusApi()
            .getLineInfos(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<LineInfoResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(LineInfoResponse response) {
                    Log.d(TAG, "onNext: ");
                    adapter.setDataList(response.getLineInfos());
                    swipeRefresh.setRefreshing(false);
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

    private class MyDialogListener implements DialogInterface.OnClickListener{
        private int temp_select = 0;
        @Override
        public void onClick(DialogInterface dialog, int which){
            if(which == BUTTON_POSITIVE){
                select = temp_select;
                setAndShowSchedule(type_list_E[select]);
            }else if(which == BUTTON_NEGATIVE){
                temp_select = 0;
            }else{
                temp_select = which;
            }
        }
    }


//    private void initSwipeRefreshView(){
//        mSwipeRefreshView = (SwipeRefreshView) findViewById(R.id.srl);
//        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
//        // 设置下拉进度的背景颜色，默认就是白色的
//        mSwipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
//        // 设置下拉进度的主题颜色
//        mSwipeRefreshView.setColorSchemeResources(R.color.colorAccent,
//                android.R.color.holo_blue_bright, R.color.colorPrimaryDark,
//                android.R.color.holo_orange_dark, android.R.color.holo_red_dark, android.R.color.holo_purple);
//
//        mSwipeRefreshView.setItemCount(20);
//
//        // 手动调用,通知系统去测量
//        mSwipeRefreshView.measure(0, 0);
//        mSwipeRefreshView.setRefreshing(true);
//        initEvent();
//    }
//
//    private void initEvent() {
//
//        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
//        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshSchedule()
//            }
//        });
//
//        // 设置下拉加载更多
//        mSwipeRefreshView.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                loadMoreData();
//            }
//        });
//    }
//
//    private void loadMoreData() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                mList.clear();
//                mList.addAll(DataResource.getMoreData());
//                Toast.makeText(MainActivity.this, "加载了" + 20 + "条数据", Toast.LENGTH_SHORT).show();
//
//                // 加载完数据设置为不加载状态，将加载进度收起来
//                mSwipeRefreshView.setLoading(false);
//            }
//        }, 2000);
//    }
}
