package com.dinodemo.activity.clock;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.dinodemo.R;

/**
 * Created by Coder on 2015/9/10.
 */
public class PlayAlarmAty extends AppCompatActivity {
  private MediaPlayer mp;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.alarm_player_aty);

    mp = MediaPlayer.create(this,R.raw.sleep);
    mp.start();
  }

  @Override protected void onPause() {
    super.onPause();
    finish();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mp.stop();
    mp.release();
  }
}
