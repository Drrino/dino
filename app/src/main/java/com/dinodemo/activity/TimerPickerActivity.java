package com.dinodemo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.TabHost;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import com.dinodemo.activity.clock.TabAlarm;
import com.dinodemo.activity.clock.TabStopWatch;
import com.dinodemo.activity.clock.TabTime;
import com.dinodemo.activity.clock.TabTimer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Coder on 2015/9/9.
 */
public class TimerPickerActivity extends FragmentActivity {
  @Bind(R.id.tabhost) FragmentTabHost mTabHost;
  @Bind(R.id.pager) ViewPager mViewPager;
  private List<Fragment> list = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragmenttabtost);
    ButterKnife.bind(this);
    initTab();

  }

  private void initTab() {
    mTabHost.setup(TimerPickerActivity.this, getSupportFragmentManager(), R.id.content);
    mTabHost.addTab(mTabHost.newTabSpec("tabTime").setIndicator("时钟"), TabTime.class, null);
    mTabHost.addTab(mTabHost.newTabSpec("tabAlarm").setIndicator("闹钟"), TabAlarm.class, null);
    mTabHost.addTab(mTabHost.newTabSpec("tabTimer").setIndicator("计时器"), TabTimer.class, null);
    mTabHost.addTab(mTabHost.newTabSpec("tabStopWatch").setIndicator("秒表"), TabStopWatch.class, null);
    mTabHost.setCurrentTab(0);

    mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
      @Override public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position);
      }
    });

    TabTime p1 = new TabTime();
    TabAlarm p2 = new TabAlarm();
    TabTimer p3 = new TabTimer();
    TabStopWatch p4 = new TabStopWatch();
    list.add(p1);
    list.add(p2);
    list.add(p3);
    list.add(p4);
    mViewPager.setAdapter(new MenuAdapter(getSupportFragmentManager()));
    mViewPager.addOnPageChangeListener(new ViewPagerListener());


  }

  class MenuAdapter extends FragmentStatePagerAdapter{
    public MenuAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      return list.get(position);
    }

    @Override public int getCount() {
      return list.size();
    }
  }

  class ViewPagerListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {
      mTabHost.setCurrentTab(position);
    }

    @Override public void onPageScrollStateChanged(int state) {

    }
  }
}
