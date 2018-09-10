package com.sjtubus.user;

import android.util.Log;

import com.sjtubus.model.Administrator;
import com.sjtubus.model.Driver;
import com.sjtubus.model.User;
import com.sjtubus.model.response.ProfileResponse;
import com.sjtubus.network.RetrofitClient;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;


/**
 * Created by Allen on 2018/7/3.
 */

public class UserManager {
    static private UserManager instance;
    private User user;
    private boolean login = false;
    private String role = "";

    private UserManager() {
    }

    public static UserManager getInstance() {
        return instance;
    }

    public static void init() {
        instance = new UserManager();
        instance.refresh();
    }

    public boolean isLogin() {
        return login;
    }

    public User getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }

    public void login(User user,String role) {
        this.user = user;
        this.login = true;
        this.role = role;
        Log.d("EventBus", "UserChange true");
        EventBus.getDefault().post(new UserChangeEvent(true));
    }

    public void logout() {
        user = null;
        login = false;
        role = "";
        Log.d("EventBus", "UserChange false");
        EventBus.getDefault().post(new UserChangeEvent(false));
    }

    public void refresh() {
        RetrofitClient
            .getBusApi()
            .getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ProfileResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(ProfileResponse response) {
                    if(response.getError()==0){
                        if(response.getRole().equals("user")||response.getRole().equals("jaccountuser")) {
                            login(response.getUser(),response.getRole());
                        }else if(response.getRole().equals("driver")){
                            User user = new User();
                            Driver driver = response.getDriver();
                            user.setUsername(driver.getUsername());
                            user.setPhone(driver.getPhone());
                            user.setCredit(0);
                            user.setTeacher(false);
                            login(user,"driver");
                        } else if(response.getRole().equals("admin")){
                            User user = new User();
                            Administrator admin = response.getAdmin();
                            user.setUsername(admin.getUsername());
                            user.setCredit(0);
                            user.setTeacher(false);
                            login(user,"admin");
                        }
                    }
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
}
