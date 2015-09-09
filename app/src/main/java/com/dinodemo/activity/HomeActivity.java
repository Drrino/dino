package com.dinodemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;

/**
 * Created by Coder on 2015/8/29.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
  @Bind(R.id.item_1) LinearLayout ml1;
  @Bind(R.id.item_2) LinearLayout ml2;
  @Bind(R.id.item_3) LinearLayout ml3;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_pager);
    ButterKnife.bind(this);
    ml1.setOnClickListener(this);
    ml3.setOnClickListener(this);
    ml2.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.item_1:
        startActivity(new Intent().setClass(HomeActivity.this, MapActivity.class));
        finish();
        break;
      case R.id.item_2:
        startActivity(new Intent().setClass(HomeActivity.this, TimerPickerActivity.class));
        finish();
        break;
      case R.id.item_3:
        startActivity(new Intent().setClass(HomeActivity.this, DatePickerActivity.class));
        finish();
        break;
    }
  }
}
