package com.sjtubus.user;

import android.content.SharedPreferences;

import com.sjtubus.App;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Allen on 2018/7/3.
 */

public class ReminderManager {
    private final static String REMINDER = "reminder";
    private SharedPreferences preferences = App.getSharedPrefsReminder();

    public void addReminder(String actId) {
        Set<String> reminders = getReminders();
        reminders.add(actId);
        preferences.edit().putStringSet(REMINDER, reminders).apply();
    }

    public void removeReminder(String actId) {
        Set<String> reminders = getReminders();
        reminders.remove(actId);
        SharedPreferences preferences = App.getSharedPrefsReminder();
        preferences.edit().putStringSet(REMINDER, reminders).apply();

    }

    private Set<String> getReminders() {
        Set<String> reminders = preferences.getStringSet(REMINDER, null);
        if (reminders == null) {
            reminders = new HashSet<>();
        }
        return reminders;
    }

    public void synchronize() {
        final Date now = new Date();
        Set<String> reminders = getReminders();
//        Observable.from(reminders)
//                .flatMap(actId -> getActDetail(actId))
//                .filter(detail -> detail.getStartTime().after(now))
//                .map(detail -> detail.getId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        actId -> removeReminder(actId),
//                        error -> Log.e("ReminderManager", error.getMessage())
//                );
    }

//    private Observable<ActDetail> getActDetail(Schedule actId) {
//        return RetrofitClient
//                .getTongquApi()
//                .getActDetail(actId)
//                .flatMap(NetworkErrorHandler.tongquErrorFilter)
//                .map(response -> ((ActDetailResponse) response).getActDetail());
//    }
}
