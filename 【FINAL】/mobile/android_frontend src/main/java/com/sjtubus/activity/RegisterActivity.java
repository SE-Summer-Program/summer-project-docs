package com.sjtubus.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.utils.ToastUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private Button verify_btn;
    private EditText username_edit,phonenum_edit, password_edit, password_confirm, verify_edit;
    private String username;
    private String phoneNum;
    private String password;
    private CheckBox checkbtn_protocol;
    private TextView txt_protocol;
    private boolean isVerifying = false;
    private boolean hasReadProtocol = false;
    private boolean checkFromDialog = false;

    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {//提交验证码成功,如果验证成功会在data里返回数据。data数据类型为HashMap<number,code>
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    HashMap<String, Object> mData = (HashMap<String, Object>) data;
                    String country = (String) mData.get("country");//返回的国家编号
                    String phone = (String) mData.get("phone");//返回用户注册的手机号

                    if (phone.equals(phoneNum)) {
                        userRegister();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort("验证码输入不正确~");
                            }
                        });
                    }
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    changeBtnGetCode();
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("验证失败");
                    }
                });
                ((Throwable) data).printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SMSSDK.registerEventHandler(eventHandler);
        initViews();
    }

    public void userRegister() {
        RequestBody requestBody = new FormBody.Builder()
                .add("token","dks3824LHEBF92IUD2RG709")
                .add("username", username)
                .add("password", password)
                .add("phone",phoneNum).build();
        RetrofitClient.getBusApi()
                .register(requestBody)
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
                            ToastUtils.showShort(response.getMsg());
                            finish();
                        }
                        ToastUtils.showShort(response.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        //mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    public void initViews(){
        Toolbar toolbar = findViewById(R.id.toolbar_register);
        username_edit = findViewById(R.id.username_edit);
        password_edit = findViewById(R.id.pwd_edit);
        password_confirm = findViewById(R.id.pwd_confirm);
        phonenum_edit = findViewById(R.id.phone_edit);
        verify_edit = findViewById(R.id.verify_edit);
        txt_protocol = findViewById(R.id.txt_protocol);
        Button register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(this);
        verify_btn = findViewById(R.id.verify_btn);
        verify_btn.setOnClickListener(this);

        String protocol = "我已阅读并同意<u>《SJTUBUS用户协议》</u>";
        txt_protocol.setText(Html.fromHtml(protocol));
        txt_protocol.setOnClickListener(this);

        checkbtn_protocol = findViewById(R.id.checkbtn_protocol);
        checkbtn_protocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (hasReadProtocol && !checkFromDialog) {
                   hasReadProtocol = false;
//                   checkbtn_protocol.setChecked(false);
               } else {
                   hasReadProtocol = true;
                   checkFromDialog = false;
//                   checkbtn_protocol.setChecked(true);
               }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case  R.id.verify_btn:
                getVerifyCode();
                break;
            case R.id.txt_protocol:
                String protocol = "我已阅读并同意<u><font color='#0000CD'>《SJTUBUS用户协议》</font></u>";
                txt_protocol.setText(Html.fromHtml(protocol));
                showProtocol();
                break;
            case R.id.register_btn:
                testVerifyCode();
                break;
        }
    }

    public void getVerifyCode() {
        phoneNum = phonenum_edit.getText().toString().trim();
        //检查号码格式是否正确
        Pattern pattern= Pattern.compile("[1][358]\\d{9}");
        Matcher matcher = pattern.matcher(phoneNum);
        //发送短信，传入国家号和电话---使用SMSSDK核心类之前一定要在MyApplication中初始化，否侧不能使用
        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showShort("手机号码不能为空~");
        }else if(!matcher.matches()){
            ToastUtils.showShort("手机号码格式不正确~");
        }
        else {
            SMSSDK.getVerificationCode("86", phoneNum);
            ToastUtils.showShort("正在发送验证码...");
        }
    }

    public void showProtocol(){
        new AlertDialog.Builder(this)
                .setTitle("SJTUBUS用户协议")
                .setMessage("不用在意这些细节同意就完事了2333")
                .setPositiveButton("我同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hasReadProtocol = true;
                        checkFromDialog = true;
                        checkbtn_protocol.setChecked(true);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void testVerifyCode() {
        String verify_code = verify_edit.getText().toString().trim();
        username = username_edit.getText().toString().trim();
        password = password_edit.getText().toString().trim();
        String confirm = password_confirm.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            ToastUtils.showShort("用户名不能为空~");
        }
        else if (TextUtils.isEmpty(password)){
            ToastUtils.showShort("密码不能为空~");
        }
        else if (password.length() < 6 || password.length() > 16){
            ToastUtils.showShort("请设置6-16位的密码~");
        }
        else if (! password.equals(confirm)){
            ToastUtils.showShort("两次密码输入不一致~");
        }
        else if (TextUtils.isEmpty(verify_code)) {
            ToastUtils.showShort("验证码不能为空~");
        }
        else if (! hasReadProtocol){
            ToastUtils.showShort("请确保您已阅读并同意《SJTUBUS用户协议》~");
        }else {
            //提交短信验证码
            SMSSDK.submitVerificationCode("86", phoneNum, verify_code);//国家号，手机号码，验证码
        }
    }

    /*
     * 改变按钮样式    60s倒计时
     * */
    private void changeBtnGetCode() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                int i = 60;
                if (!isVerifying) {
                    isVerifying = true;
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (RegisterActivity.this == null) {
                            break;
                        }
                        final int index = i;
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String verify_text = index+"s后重新获取";
                                verify_btn.setText(verify_text);
                                verify_btn.setBackgroundResource(R.drawable.button_shape_gray);
                                verify_btn.setTextColor(Color.GRAY);
                                verify_btn.setClickable(false);
                            }
                        });
                        //睡一秒
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                isVerifying = false;

                if (RegisterActivity.this != null) {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            verify_btn.setText("获取验证码");
                            verify_btn.setClickable(true);
                            verify_btn.setBackgroundColor(getResources().getColor(R.color.primary_red));
                        }
                    });
                }
            }
        };
        thread.start();
    }

    public int getContentViewId(){
        return R.layout.activity_register;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //要在activity销毁时反注册，否侧会造成内存泄漏问题
        SMSSDK.unregisterAllEventHandler();
    }
}
