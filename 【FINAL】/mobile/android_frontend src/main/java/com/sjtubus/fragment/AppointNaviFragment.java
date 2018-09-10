package com.sjtubus.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.activity.AppointActivity;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.MyDateUtils;
import com.sjtubus.utils.StringCalendarUtils;
import com.sjtubus.utils.ToastUtils;

import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AppointNaviFragment extends BaseFragment {
    @SuppressLint("StaticFieldLeak")
    private static AppointNaviFragment fragment;

    private TextView singleway;
    private TextView doubleway;
    private TextView departure_place;
    private TextView arrive_place;
    private TextView singleway_date;
    private TextView appoint_comment;
    private TextView appoint_commenttitle;

    private int departure_index = 0;//闵行校区
    private int arrive_index = 1;  //徐汇校区

    private MyListener listener = new MyListener();

    private String[] station_list = {"闵行校区", "徐汇校区", "七宝校区"};
    private int year, month, day;

    public static AppointNaviFragment getInstance() {
        if(fragment == null){
            fragment = new AppointNaviFragment();
        }
        return new AppointNaviFragment();
        //FIXME： 这里需要利用传过来的参数，由于暂时没啥用就先这样返回了
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appoint, container, false);

        departure_place = view.findViewById(R.id.appoint_departureplace);
        arrive_place = view.findViewById(R.id.appoint_arriveplace);
        singleway_date = view.findViewById(R.id.appoint_singlewaydate);
        TextView doubleway_date = view.findViewById(R.id.appoint_doublewaydate);
        Button search_btn = view.findViewById(R.id.appoint_searchbtn);
        ImageView revert_btn = view.findViewById(R.id.revert_place_btn);
        appoint_comment = view.findViewById(R.id.appoint_comment);
        appoint_commenttitle = view.findViewById(R.id.appoint_commenttitle);

        if (!UserManager.getInstance().getRole().equals("admin")&&
                !UserManager.getInstance().getRole().equals("driver")){
            initAppointComment();
        } else {
            appoint_commenttitle.setVisibility(View.GONE);
        }

        departure_place.setOnClickListener(listener);
        arrive_place.setOnClickListener(listener);
        singleway_date.setOnClickListener(listener);
//        doubleway_date.setOnClickListener(listener);
        search_btn.setOnClickListener(listener);
        revert_btn.setOnClickListener(listener);

        getCurrentDay();
        String monthStr = StringCalendarUtils.getDoubleDigitMonth(month);
        String dayStr = StringCalendarUtils.getDoubleDigitDay(day);
        String dateStr = year+"-"+monthStr+"-"+dayStr;
        singleway_date.setText(dateStr);
        doubleway_date.setText("");
        /*
         * 标记单程往返区别，单程的doublewaydate值为空
         */

        return view;
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //交换目的地与出发地
                case R.id.revert_place_btn:
                    int temp = departure_index;
                    departure_index = arrive_index;
                    arrive_index = temp;
                    departure_place.setText(station_list[departure_index]);
                    arrive_place.setText(station_list[arrive_index]);
                    break;
                case R.id.appoint_departureplace:
                    final TextView depart_txt = v.findViewById(v.getId());
                    new AlertDialog.Builder(getActivity())
                        .setTitle("选择出发地")
                        .setNegativeButton("取消", null)
                        .setSingleChoiceItems(station_list, departure_index, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            depart_txt.setText(station_list[which]);
                            departure_index = which;
                            dialog.dismiss();
                        }
                    }).create().show();
                    break;
                case R.id.appoint_arriveplace:
                    final TextView arrive_txt = v.findViewById(v.getId());
                    new AlertDialog.Builder(getActivity())
                        .setTitle("选择目的地")
                        .setNegativeButton("取消", null)
                        .setSingleChoiceItems(station_list, arrive_index, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            arrive_txt.setText(station_list[which]);
                            arrive_index = which;
                            dialog.dismiss();
                        }
                    }).create().show();
                    break;

                case R.id.appoint_singlewaydate:
               // case R.id.appoint_doublewaydate:
                    final TextView textView_date = v.findViewById(v.getId());

                    new DatePickerDialog(Objects.requireNonNull(getActivity()), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year_choose, int month_choose, int dayOfMonth_choose) {

                            /* 这里改过 */
                            String monthStr = StringCalendarUtils.getDoubleDigitMonth(month_choose);
                            String dayStr = StringCalendarUtils.getDoubleDigitDay(dayOfMonth_choose);
                            String dateStr = year_choose+"-"+monthStr+"-"+dayStr;

                            if (StringCalendarUtils.isBeforeCurrentDate(dateStr)){
                                ToastUtils.showShort("不能预约已经发出的班次~");
                                return;
                            } else if (! MyDateUtils.isWithinOneWeek(dateStr)){
                                ToastUtils.showShort("仅可预约一周以内的班次哦~");
//                                return;
                            }
                            textView_date.setText(dateStr);

                            year = year_choose;
                            month = month_choose;
                            day = dayOfMonth_choose;
                        }
                    }, year,month,day).show();
                    break;

                    //navi 到 单程
                case R.id.appoint_searchbtn:
                    if (departure_index == arrive_index){
                        ToastUtils.showShort("起点和终点不能相同~");
                        break;
                    } else if (departure_index == 1 && arrive_index == 2){
                        ToastUtils.showShort("还没有徐汇校区到七宝校区的班车~");
                        break;
                    } else if (departure_index == 2 && arrive_index == 1){
                        ToastUtils.showShort("还没有七宝校区到徐汇校区的班车~");
                        break;
                    }
                    Intent appointIntent = new Intent(getActivity(), AppointActivity.class);
                    appointIntent.putExtra("departure_place", (String) departure_place.getText());
                    appointIntent.putExtra("arrive_place", (String) arrive_place.getText());
                    appointIntent.putExtra("singleway_date", (String) singleway_date.getText());
                    startActivityForResult(appointIntent, 1);
                   // startActivity(appointIntent);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            // 单程 回 navi
            case 1:
                if (resultCode == RESULT_OK){
                   //Log.d("appointfragment", data.getStringExtra("singleway_date"));
//                    ToastUtils.showShort(data.getStringExtra("singleway_date"));
                    departure_place.setText(data.getStringExtra("departure_place"));
                    arrive_place.setText(data.getStringExtra("arrive_place"));
                    singleway_date.setText(data.getStringExtra("singleway_date"));
                    //doubleway_date.setText(data.getStringExtra("doubleway_date"));
                }
            default:
                break;
        }
    }

    private void getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month = calendar.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initAppointComment(){
        String comment = "1. 用户仅可预约未来一周以内的班次。" + "<br>" +
                "2. 教职工、研究生在校区巴士的高优先级不变。" + "<br>" +
                "3. 点击出发地、目的地、出发时间即可调整。" + "<br>" +
                "4. 暂无卢湾校区、法华校区的班车；暂无徐汇校区与七宝校区之间的班车。" + "<br>" +
                "5. 问询电话：暂无" + "<br>";

        appoint_comment.setText(Html.fromHtml(comment));
    }

}