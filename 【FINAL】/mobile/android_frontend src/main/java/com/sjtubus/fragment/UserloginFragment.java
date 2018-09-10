package com.sjtubus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sjtubus.R;
import com.sjtubus.activity.JaccountActivity;
import com.sjtubus.activity.RegisterActivity;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.network.RetrofitClient;
import com.sjtubus.user.UserManager;
import com.sjtubus.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

/**
 * Created by Allen on 2018/7/3.
 */

public class UserloginFragment extends BaseFragment implements View.OnClickListener{
    private EditText phone_edit,password_edit;

    private static UserloginFragment mFragment = null;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public static UserloginFragment getInstance() {
        if(mFragment == null){
            mFragment = new UserloginFragment();
        }
        return mFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userlogin, container, false);
        TextView register_txt = view.findViewById(R.id.txt_register);
        Button login_btn = view.findViewById(R.id.btn_login);
        TextView jaccount_btn = view.findViewById(R.id.jaccount_btn);
        phone_edit = view.findViewById(R.id.login_phone_edit);
        password_edit = view.findViewById(R.id.login_pwd_edit);
        jaccount_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        register_txt.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.txt_register:
                Intent registerIntent = new Intent(getActivity(),RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.btn_login:
                userLogin();
                break;
            case R.id.jaccount_btn:
                Intent jaccountIntent = new Intent(getActivity(),JaccountActivity.class);
                startActivity(jaccountIntent);
                break;
        }
    }

    public void userLogin(){
        String phone = phone_edit.getText().toString().trim();
        String password = password_edit.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.showShort("手机号码不能为空哦~");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showShort("密码不能为空哦~");
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("password", password)
                .add("phone",phone).build();
        //登录
        RetrofitClient.getBusApi()
                .login(requestBody)
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
                            //更新用户信息
                            UserManager.getInstance().refresh();
                            if(getActivity()!=null) getActivity().finish();
                        }
                        ToastUtils.showShort(response.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showShort("服务器错误!登录失败orz");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                        //mProgressBar.setVisibility(View.GONE);
                    }
                });
    }
}

