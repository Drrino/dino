package com.dinodemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;

/**
 * Created by Coder on 2015/8/29.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
  @Bind(R.id.item_1) CardView cv1;
  @Bind(R.id.item_2) CardView cv2;
  @Bind(R.id.item_3) CardView cv3;
  @Bind(R.id.item_4) CardView cv4;
  @Bind(R.id.item_5) CardView cv5;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_pager);
    ButterKnife.bind(this);
    cv1.setOnClickListener(this);
    cv2.setOnClickListener(this);
    cv3.setOnClickListener(this);
    cv4.setOnClickListener(this);
    cv5.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.item_1:
        startActivity(new Intent().setClass(HomeActivity.this, MapActivity.class));
        break;
      case R.id.item_2:
        startActivity(new Intent().setClass(HomeActivity.this, TimerPickerActivity.class));
        break;
      case R.id.item_3:
        startActivity(new Intent().setClass(HomeActivity.this, DatePickerActivity.class));
        break;
      case R.id.item_4:
        startActivity(new Intent().setClass(HomeActivity.this, FastScrollerActivity.class));
        break;
      case R.id.item_5:
        startActivity(new Intent().setClass(HomeActivity.this, GameActivity.class));
        break;
    }
  }
}
