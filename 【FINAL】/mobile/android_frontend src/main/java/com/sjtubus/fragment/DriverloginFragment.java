package com.sjtubus.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sjtubus.R;
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

public class DriverloginFragment extends BaseFragment implements View.OnClickListener{
    private EditText username_edit,password_edit;

    private static DriverloginFragment mFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public static DriverloginFragment getInstance() {
        if(mFragment == null){
            mFragment = new DriverloginFragment();
        }
        return mFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driverlogin, container, false);
        Button login_btn = view.findViewById(R.id.btn_driverlogin);
        username_edit = view.findViewById(R.id.driverlogin_username_edit);
        password_edit = view.findViewById(R.id.driverlogin_pwd_edit);
        login_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_driverlogin:
                driverLogin();
                break;
        }
    }

    public void driverLogin(){
        String username = username_edit.getText().toString().trim();
        String password = password_edit.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            ToastUtils.showShort("用户名不能为空哦~");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ToastUtils.showShort("密码不能为空哦~");
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("password", password)
                .add("username",username).build();
        //登录
        RetrofitClient.getBusApi()
                .driverlogin(requestBody)
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
