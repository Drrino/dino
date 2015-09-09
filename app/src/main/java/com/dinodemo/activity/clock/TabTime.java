package com.dinodemo.activity.clock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import java.util.Calendar;

/**
 * Created by Coder on 2015/9/9.
 */
public class TabTime extends Fragment {
  @Bind(R.id.text) TextView tvTime;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.tabtime, null);
    ButterKnife.bind(this,v);
    timerHandler.sendEmptyMessage(0);
    return v;
  }

  private Handler timerHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      refreshTime();
      timerHandler.sendEmptyMessageDelayed(0,1000);
    }
  };

  private void refreshTime() {
    Calendar c = Calendar.getInstance();
    tvTime.setText(String.format("%d:%d:%d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)));
  }

  @Override public void onPause() {
    super.onPause();
    timerHandler.removeMessages(0);
  }
}
