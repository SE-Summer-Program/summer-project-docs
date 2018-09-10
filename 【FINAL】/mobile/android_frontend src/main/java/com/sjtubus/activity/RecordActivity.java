package com.sjtubus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.RecordInfo;
import com.sjtubus.model.response.RecordInfoResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;
import com.sjtubus.widget.RecordAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class RecordActivity extends BaseActivity implements View.OnClickListener, RecordAdapter.OnItemClickListener{

    private RecordInfo recordInfo;
    private List<RecordInfo> recordInfos;
    private RecordAdapter recordAdapter;
    @BindView(R.id.refresh_record)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.record_total)
    TextView record_total;
    @BindView(R.id.recycle_record)
    RecyclerView recyclerView;

    private String[] filter_list = {"仅显示近一周", "仅显示近一月", "仅显示近三个月"};
    private String[] sort_list = {"按预定时间由近到远", "按班次时间由近到远"};
    private int filter_select = 0;
    private int sort_select = 0;
    private int filter_amount = 0;

    private boolean sortBySubmitTime = false;
    private boolean sortByDepartureTime = false;
    private boolean showNotTravelOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        Toolbar mToolbar = findViewById(R.id.toolbar_record);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.back_32);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView record_filter = findViewById(R.id.record_filter);
        TextView record_sort = findViewById(R.id.record_sort);
        record_filter.setOnClickListener(this);
        record_sort.setOnClickListener(this);
        CheckBox validcheckbox = findViewById(R.id.record_valid);

        validcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showNotTravelOnly = isChecked;
                filter_amount = 0;
                ToastUtils.showShort("筛选成功~");
                refreshRecord();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //设置布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//      recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //分割线

        recordAdapter = new RecordAdapter(this);
        recordAdapter.setItemClickListener(this);
        recyclerView.setAdapter(recordAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                filter_amount = 0;
                refreshRecord();
            }
        });

        filter_amount = 0;
        refreshRecord();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_record;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_filter:
                setAlertDialog("选择筛选方式", filter_list, true);
                break;
            case R.id.record_sort:
                setAlertDialog("选择排序方式", sort_list, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view) {

    }

    public void refreshRecord(){
        //获取当前用户的username
        String username = UserManager.getInstance().getUser().getUsername();
        String user_role = UserManager.getInstance().getRole();
        //获取当前的时间
        String currenttime = StringCalendarUtils.getCurrentTime();

        /*
         * 改动了函数的参数，删去了currenttime
         */
        RetrofitClient.getBusApi()
            .getRecordInfos(username,user_role)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<RecordInfoResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(RecordInfoResponse response) {
                    Log.i(TAG, "onNext: ");
                    if(response.getRecordInfos()!=null && response.getRecordInfos().size()!=0){
                        List<RecordInfo> recordInfosRaw = response.getRecordInfos();

                        List<RecordInfo> recordInfos = new ArrayList<>();

                        if (showNotTravelOnly){
                            for (RecordInfo info : recordInfosRaw){
                                if (StringCalendarUtils.isBeforeCurrentTime(info.getDepartureTimeComplete())) {
                                    filter_amount++;
                                    continue;
                                }
                                recordInfos.add(info);
                            }
                        } else {
                            recordInfos = recordInfosRaw;
                        }

                        /* 排序 */
                        if (sortBySubmitTime){
                            Collections.sort(recordInfos, new Comparator<RecordInfo>() {
                                @Override
                                public int compare(RecordInfo o1, RecordInfo o2) {
                                    return (o2.getSubmiTime()).compareTo(o1.getSubmiTime());
                                }
                            });
                            sortByDepartureTime = false;
                        } else if (sortByDepartureTime){
                            Collections.sort(recordInfos, new Comparator<RecordInfo>() {
                                @Override
                                public int compare(RecordInfo o1, RecordInfo o2) {
                                    return (o2.getDepartureDate() + " "+ o2.getDepartureTime())
                                            .compareTo(o1.getDepartureDate() + " " + o1.getDepartureTime());
                                }
                            });
                            sortBySubmitTime = false;
                        }

                        recordAdapter.setDataList(recordInfos);
                    }
                    swipeRefresh.setRefreshing(false);

                    int total_amount = response.getRecordInfos()==null?0:response.getRecordInfos().size();
                    String record_info = "共计 " + total_amount + " 条预约记录，当前显示 " + (total_amount - filter_amount) + " 条";
                    record_total.setText(record_info);
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
                }
            });
    }

    private void setAlertDialog(String title, final String[] list, final boolean isFliterFlag){
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isFliterFlag) {
                    filter_select = which;
                } else {
                    sort_select = which;
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isFliterFlag){
//                    filter_select = which;
//                    ToastUtils.showShort(filter_list[filter_select]);
                    ToastUtils.showShort("筛选完成~");
                } else {
                    switch (sort_select){
                        case 0:
                            sortByDepartureTime = true;
                            filter_amount = 0;
                            refreshRecord();
                            break;
                        case 1:
                            sortByDepartureTime = true;
                            filter_amount = 0;
                            refreshRecord();
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    }
                    ToastUtils.showShort("排序完成~");
                }
            }
        });

        builder.setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
