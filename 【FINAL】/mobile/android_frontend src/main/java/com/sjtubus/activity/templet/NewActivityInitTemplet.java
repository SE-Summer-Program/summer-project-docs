package com.sjtubus.activity.templet;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sjtubus.R;
import com.sjtubus.activity.BaseActivity;

public class NewActivityInitTemplet extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    private void initView(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
