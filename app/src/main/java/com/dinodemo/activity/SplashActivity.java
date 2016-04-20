package com.dinodemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import com.dinodemo.utils.PrefUtils;

/**
 * Created by Coder on 2015/8/30.
 */
public class SplashActivity extends AppCompatActivity {
  @Bind(R.id.iv) ImageView iv;

  private static final long SPLASH_DELAY_MILLIS = 1000;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_pager);
    ButterKnife.bind(this);
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        startAnim();
      }
    }, SPLASH_DELAY_MILLIS);
  }

  /**
   * 渐变动画
   */
  private void startAnim() {
    AlphaAnimation alph = new AlphaAnimation(1, 0);
    alph.setDuration(1500);
    alph.setFillAfter(true);//保持动画状态
    iv.startAnimation(alph);
    jump();
  }

  //判断显示过splash
  private void jump() {
    boolean isSet = PrefUtils.getBoolean(this, "is_show", false);
    if (!isSet) {
      startActivity(new Intent(this, GuideActivity.class));
    } else {
      startActivity(new Intent(this, MainActivity.class));
    }
    finish();
  }
}
