package com.dinodemo.activity.clock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Coder on 2015/9/9.
 */
public class TabTimer extends BaseTabFragment implements View.OnClickListener {

  private static final int MSG_WHAT_TIME_TICK = 1;
  private static final int MSG_WHAT_TIME_IS_UP = 2;
  @Bind(R.id.btnStart) Button btnStart;
  @Bind(R.id.btnPause) Button btnPause;
  @Bind(R.id.btnResume) Button btnResume;
  @Bind(R.id.btnReset) Button btnReset;
  @Bind(R.id.etHour) EditText etHour;
  @Bind(R.id.etMin) EditText etMin;
  @Bind(R.id.etSec) EditText etSec;

  private TimerTask timerTask = null;
  private int allTimerCount = 0;
  private Timer timer = new Timer();

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.tabtimer, null);
    ButterKnife.bind(this, rootView);
    init();
    return rootView;
  }

  private void init() {
    btnStart.setOnClickListener(this);
    btnPause.setOnClickListener(this);
    btnResume.setOnClickListener(this);
    btnReset.setOnClickListener(this);

    etHour.setText("00");
    etHour.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
          int value = Integer.parseInt(s.toString());

          if (value > 59) {
            etHour.setText("59");
          } else if (value < 0) {
            etHour.setText("0");
          }
        }
        checkToEnableBtnStart();
      }

      @Override public void afterTextChanged(Editable s) {

      }
    });
    etMin.setText("00");
    etMin.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
          int value = Integer.parseInt(s.toString());

          if (value > 59) {
            etMin.setText("59");
          } else if (value < 0) {
            etMin.setText("0");
          }
        }
        checkToEnableBtnStart();
      }

      @Override public void afterTextChanged(Editable s) {

      }
    });
    etSec.setText("00");
    etSec.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
          int value = Integer.parseInt(s.toString());

          if (value > 59) {
            etSec.setText("59");
          } else if (value < 0) {
            etSec.setText("0");
          }
        }
        checkToEnableBtnStart();
      }

      @Override public void afterTextChanged(Editable s) {

      }
    });

    btnStart.setVisibility(View.VISIBLE);
    btnStart.setEnabled(false);
    btnPause.setVisibility(View.GONE);
    btnResume.setVisibility(View.GONE);
    btnReset.setVisibility(View.GONE);
  }

  private void checkToEnableBtnStart() {
    btnStart.setEnabled(
        (!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0)
            ||
            (!TextUtils.isEmpty(etMin.getText())
                && Integer.parseInt(etMin.getText().toString()) > 0)
            ||
            (!TextUtils.isEmpty(etSec.getText())
                && Integer.parseInt(etSec.getText().toString()) > 0));
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btnStart:
        startTimer();
        btnStart.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.VISIBLE);
        break;
      case R.id.btnPause:
        stopTimer();
        btnResume.setVisibility(View.GONE);
        btnPause.setVisibility(View.VISIBLE);
        break;
      case R.id.btnResume:
        startTimer();
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.VISIBLE);
        break;
      case R.id.btnReset:
        stopTimer();
        etHour.setText("0");
        etMin.setText("0");
        etSec.setText("0");
        btnReset.setVisibility(View.GONE);
        btnResume.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        btnStart.setVisibility(View.VISIBLE);
        break;
    }
  }

  private void startTimer() {
    if (timerTask == null) {
      allTimerCount = Integer.parseInt(etHour.getText().toString()) * 60 * 60
          + Integer.parseInt(etMin.getText().toString()) * 60
          + Integer.parseInt(etSec.getText().toString());
      timerTask = new TimerTask() {
        @Override public void run() {
          allTimerCount--;
          handler.sendEmptyMessage(MSG_WHAT_TIME_TICK);

          if (allTimerCount <= 0) {
            handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
            stopTimer();
          }
        }
      };
      timer.schedule(timerTask, 1000, 1000);
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
        case MSG_WHAT_TIME_TICK:
          int hour = allTimerCount / 60 / 60;
          int min = (allTimerCount / 60) % 60;
          int sec = allTimerCount % 60;
          etHour.setText(hour + "");
          etMin.setText(min + "");
          etSec.setText(sec + "");
          break;
        case MSG_WHAT_TIME_IS_UP:
          new AlertDialog.Builder(getContext()).setTitle("Time is up")
              .setMessage("Time is up")
              .setNegativeButton("Cancel", null)
              .show();
          btnReset.setVisibility(View.GONE);
          btnResume.setVisibility(View.GONE);
          btnPause.setVisibility(View.GONE);
          btnStart.setVisibility(View.VISIBLE);
          break;
      }
    }
  };

}

