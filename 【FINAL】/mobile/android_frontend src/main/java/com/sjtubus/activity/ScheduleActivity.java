package com.sjtubus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.sjtubus.R;
import com.sjtubus.fragment.ShiftFragment;
import com.sjtubus.fragment.StationFragment;
import com.sjtubus.utils.ShiftUtils;
import com.sjtubus.widget.MyPagerAdapter;

import java.util.ArrayList;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class ScheduleActivity extends BaseActivity {
    private String line_name;
    private String line_type;

    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private final String[] mTitles = {"时刻表", "路线站点"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        line_name = intent.getStringExtra("LINE_NAME");
        line_type = intent.getStringExtra("LINE_TYPE");
        initView();
    }

    public void initView(){
        initFragments();
        initViewPager();
        CoordinatorTabLayout mCoordinatorTabLayout = findViewById(R.id.schedule_coordinatortablayout);
        int[] mImageArray = new int[]{
                R.drawable.background1,
                R.drawable.background2};
        int[] mColorArray = new int[]{
                R.color.primary_red,
                R.color.primary_red};

        String line_name_cn = ShiftUtils.getChiLineName(line_name);
        String line_type_cn = ShiftUtils.getChiType(line_type);

        mCoordinatorTabLayout.setTranslucentStatusBar(this);
        mCoordinatorTabLayout.setTitle(line_name_cn+"("+line_type_cn+")")
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);
    }

    public void initFragments(){
        mFragments = new ArrayList<>();
        mFragments.add(ShiftFragment.getInstance(line_type,line_name));
        mFragments.add(StationFragment.getInstance(line_name));
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.schedule_vp);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_schedule;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
