package com.sjtubus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.sjtubus.R;
import com.sjtubus.fragment.AppointNaviDoubleFragment;
import com.sjtubus.fragment.AppointNaviFragment;
import com.sjtubus.user.UserManager;
import com.sjtubus.widget.AppointPagerAdapter;

import java.util.ArrayList;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class AppointNaviActivity extends BaseActivity implements View.OnClickListener{

    CoordinatorTabLayout mCoordinatorTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragment;
    private final String[] mTitles = {"单程", "往返"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_appointnavi;
    }

    private void initView(){
        initFragments();
        initViewPager();
        mCoordinatorTabLayout = findViewById(R.id.appoint_coordinatortablayout);
        int[] mImageArray = new int[]{
                R.drawable.background3,
                R.drawable.background5};
        int[] mColorArray = new int[]{
                R.color.primary_red,
                R.color.primary_red};

        mCoordinatorTabLayout.setTranslucentStatusBar(this)
                .setBackEnable(true)
                .setImageArray(mImageArray, mColorArray)
                .setupWithViewPager(mViewPager);

        if (!UserManager.getInstance().getRole().equals("admin")&&
                !UserManager.getInstance().getRole().equals("driver")){
            mCoordinatorTabLayout.setTitle("预约班车");
        } else {
            mCoordinatorTabLayout.setTitle("录入发车信息");
        }
    }

    private void initFragments(){
        mFragment = new ArrayList<>();
        mFragment.add(AppointNaviFragment.getInstance());
        //管理员和司机看不到往返那个fragment
        if(!UserManager.getInstance().getRole().equals("admin")&&
                !UserManager.getInstance().getRole().equals("driver")) {
            mFragment.add(AppointNaviDoubleFragment.getInstance());
        }
    }

    private void initViewPager(){
        mViewPager = findViewById(R.id.appoint_vp);
        AppointPagerAdapter adapter = new AppointPagerAdapter(getSupportFragmentManager(),mFragment,mTitles);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
