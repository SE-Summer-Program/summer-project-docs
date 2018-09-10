package com.sjtubus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.User;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.person_username)
    TextView username;
    @BindView(R.id.person_isteacher)
    TextView isteacher;
    @BindView(R.id.person_phone)
    TextView phone;
    @BindView(R.id.person_realname)
    TextView realname;
    @BindView(R.id.person_credit)
    TextView credit;
    @BindView(R.id.person_studentnum)
    TextView studentnum;
    @BindView(R.id.person_phone_bar)
    LinearLayout phone_bar;
    @BindView(R.id.person_realname_bar)
    LinearLayout realname_bar;
    @BindView(R.id.person_studentnum_bar)
    LinearLayout studentnum_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initView();
        initUser();
    }

    public void initView(){
        Button logout_btn = findViewById(R.id.btn_logout);
        logout_btn.setOnClickListener(this);

//        phone_bar = findViewById(R.id.person_phone_bar);
//        phone_bar.setOnClickListener(this);
//        phone_bar.setEnabled(false);
        realname_bar.setOnClickListener(this);
        studentnum_bar.setOnClickListener(this);
        realname_bar.setEnabled(false);
        studentnum_bar.setEnabled(false);

        //设置点击权限（乘客不能点手机，jaccount不能点学号和真实姓名
//        if (UserManager.getInstance().getUser() != null
//                && UserManager.getInstance().getRole().equals("user")){
//            phone_bar.setEnabled(false);
//        } else if (UserManager.getInstance().getUser() != null
//                && UserManager.getInstance().getRole().equals("jaccountuser")){
//            realname_bar.setEnabled(false);
//            studentnum_bar.setEnabled(false);
//        }

        if (UserManager.getInstance().getUser() != null){
            if (UserManager.getInstance().getRole().equals("user")) {
                realname_bar.setEnabled(true);
                studentnum_bar.setEnabled(true);
            } else if (UserManager.getInstance().getRole().equals("admin")) {
                ToastUtils.showShort("管理员请在后台修改个人数据哦~");
            } else if (UserManager.getInstance().getRole().equals("driver")) {
                ToastUtils.showShort("司机请在后台修改个人数据哦~");
            } else if (UserManager.getInstance().getRole().equals("jaccountuser")) {
                ToastUtils.showShort("Jaccount认证用户无法修改个人数据哦~");
            }
        }
    }

    public void initUser(){
        UserManager.getInstance().refresh();
        User user = UserManager.getInstance().getUser();
        username.setText(user.getUsername());
        isteacher.setText(user.getTeacher()?"教工":"非教工");
        phone.setText(user.getPhone());
        credit.setText(String.valueOf(user.getCredit()));
        realname.setText(user.getRealname());
        studentnum.setText(user.getStudentNumber());
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_personinfo;
    }

    public void userLogout(){
        RetrofitClient
            .getBusApi()
            .logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HttpResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(HttpResponse response) {
                    if(response.getError()==0){
                        UserManager.getInstance().logout();
                        ToastUtils.showShort("已退出登录~");
                        finish();
                    }else {
                        ToastUtils.showShort(response.getError());
                    }
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
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_logout:
                userLogout();
                break;
            case R.id.person_phone_bar:
                showUpdateDialog("完善手机号码信息", phone, true);
                break;
            case R.id.person_studentnum_bar:
                showUpdateDialog("完善学号/工号信息", studentnum, false);
                break;
            case R.id.person_realname_bar:
//                final EditText editText = new EditText(this);
//                editText.setMinLines(2);
//                new AlertDialog.Builder(this)
//                        .setTitle("完善真实姓名信息")
//                        .setIcon(android.R.drawable.ic_dialog_info)
//                        .setView(editText)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface arg0, int arg1) {
//                                realname.setText(editText.getText().toString());
//                                completeInfos();
//                            }
//                        })
//                        .setNegativeButton("取消", null)
//                        .show();
                showUpdateDialog("完善真实姓名信息", realname, false);
                break;
            default:
                break;
        }
    }

    public void completeInfos(){
        String userId_txt = UserManager.getInstance().getUser().getUserId();
        String phone_txt = (String) phone.getText();
        String studentnum_txt = (String) studentnum.getText();
        String realname_txt = (String) realname.getText();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", userId_txt)
                .add("phone", phone_txt)
                .add("studentnum", studentnum_txt)
                .add("realname", realname_txt)
                .build();
        RetrofitClient
            .getBusApi()
            .updatePersonInfos(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<HttpResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.i("personinfo", "onsubscribe");
                }

                @Override
                public void onNext(HttpResponse response) {
                   ToastUtils.showShort("个人信息已更新~");
                    Log.i("personinfo", "onnext");
                    UserManager.getInstance().refresh();
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    Log.i("personinfo", "onerror");
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: ");
                }
            });
    }

    private void showUpdateDialog(String title, final TextView textView, final boolean isPhoneUpdate){
        final EditText editText = new EditText(this);
        editText.setMinLines(2);
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(R.mipmap.update)
            .setView(editText)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    String text = editText.getText().toString().trim();
                    if (isPhoneUpdate){
                        Pattern pattern= Pattern.compile("[1][358]\\d{9}");
                        Matcher matcher = pattern.matcher(text);
                        //发送短信，传入国家号和电话---使用SMSSDK核心类之前一定要在MyApplication中初始化，否侧不能使用
                        if(!matcher.matches()){
                            ToastUtils.showShort("手机号码格式不正确~");
                            return;
                        }
                    }
                    textView.setText(text);
                    completeInfos();
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }
}
