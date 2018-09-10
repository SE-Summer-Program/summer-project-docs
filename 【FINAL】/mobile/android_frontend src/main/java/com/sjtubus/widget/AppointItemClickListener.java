package com.sjtubus.widget;

import com.sjtubus.model.AppointInfo;

public interface AppointItemClickListener {

    void onExpandChildItem(AppointInfo appointInfo);

    void onHideChildItem(AppointInfo appointInfo);

}