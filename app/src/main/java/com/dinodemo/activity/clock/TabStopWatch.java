package com.dinodemo.activity.clock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Coder on 2015/9/9.
 */
public class TabStopWatch extends BaseTabFragment implements View.OnClickListener {
  @Bind(R.id.btnSWStart) Button btnStart;
  @Bind(R.id.btnSWResume) Button btnResume;
  @Bind(R.id.btnSWReset) Button btnReset;
  @Bind(R.id.btnSWPause) Button btnPause;
  @Bind(R.id.btnSWLap) Button btnLap;
  @Bind(R.id.timeHour) TextView tvHour;
  @Bind(R.id.timeMin) TextView tvMin;
  @Bind(R.id.timeSec) TextView tvSec;
  @Bind(R.id.timeMSec) TextView tvMSec;
  @Bind(R.id.lvWatchTimeList) ListView lvTimeList;

  private int tenMSecs = 0;
  private Timer timer = new Timer();
  private TimerTask timerTask = null;
  private TimerTask showTimeTask = null;

  private static final int MSG_WHAT_SHOW_TIME = 1;
  private ArrayAdapter<String> adapter;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.tabstopwatch, null);
    ButterKnife.bind(this, rootView);
    init();
    return rootView;
  }

  private void init() {
    tvHour.setText("0");
    tvMin.setText("0");
    tvSec.setText("0");
    tvMSec.setText("0");

    btnStart.setOnClickListener(this);
    btnResume.setOnClickListener(this);
    btnReset.setOnClickListener(this);
    btnPause.setOnClickListener(this);
    btnLap.setOnClickListener(this);

    btnLap.setVisibility(View.GONE);
    btnPause.setVisibility(View.GONE);
    btnReset.setVisibility(View.GONE);
    btnResume.setVisibility(View.GONE);

    adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
    lvTimeList.setAdapter(adapter);

    showTimeTask = new TimerTask() {

      @Override public void run() {
        handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
      }
    };
    timer.schedule(showTimeTask, 200, 200);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnSWStart:
        startTimer();
        btnStart.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        btnLap.setVisibility(View.VISIBLE);
        break;
      case R.id.btnSWResume:
        startTimer();
        btnResume.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.GONE);
        btnLap.setVisibility(View.VISIBLE);
        break;
      case R.id.btnSWReset:
        stopTimer();
        tenMSecs = 0;
        adapter.clear();

        btnLap.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
        break;
      case R.id.btnSWPause:
        stopTimer();

        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.VISIBLE);
        btnLap.setVisibility(View.GONE);
        btnReset.setVisibility(View.VISIBLE);
        break;
      case R.id.btnSWLap:
        adapter.insert(
            String.format("%d:%d:%d:%d", tenMSecs / 100 / 60 / 60, tenMSecs / 100 / 60 % 60,
                tenMSecs / 100 % 60, tenMSecs % 100), 0);
        break;
    }
  }

  private void startTimer() {
    if (timerTask == null) {
      timerTask = new TimerTask() {

        @Override public void run() {
          tenMSecs++;
        }
      };
      timer.schedule(timerTask, 10, 10);
    }
  }

  private void stopTimer() {
    if (timerTask != null) {
      timerTask.cancel();
      timerTask = null;
    }
  }

  private Handler handler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_WHAT_SHOW_TIME:
          tvHour.setText(tenMSecs / 100 / 60 / 60 + "");
          tvMin.setText(tenMSecs / 100 / 60 % 60 + "");
          tvSec.setText(tenMSecs / 100 % 60 + "");
          tvMSec.setText(tenMSecs % 100 + "");
          break;
      }
    }
  };

  @Override public void onPause() {
    super.onPause();
    handler.removeMessages(MSG_WHAT_SHOW_TIME);
  }
}
