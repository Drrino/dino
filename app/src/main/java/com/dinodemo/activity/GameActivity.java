package com.dinodemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import com.dinodemo.activity.config.Config;
import com.dinodemo.activity.view.GameView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
  @Bind(R.id.score) TextView mTvScore;
  @Bind(R.id.tv_Goal) TextView mTvGoal;
  @Bind(R.id.record) TextView mTvHighScore;
  @Bind(R.id.btn_restart) Button mBtnRestart;
  @Bind(R.id.btn_revert) Button mBtnRevert;
  @Bind(R.id.btn_option) Button mBtnOptions;
  @Bind(R.id.game_panel) FrameLayout frameLayout;
  //为了GameView能够居中
  @Bind(R.id.game_panel_rl) RelativeLayout relativeLayout;

  //历史记录分数
  private int mHightScore;
  //目标分数
  private int mGoal;
  //游戏面板
  private GameView mGameView;

  //Activity引用
  private static GameActivity mGame;

  public GameActivity() {
    mGame = this;
  }

  public static GameActivity getGameActivity() {
    return mGame;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.game_pager);
    ButterKnife.bind(this);
    initView();

    mGameView = new GameView(this);
    relativeLayout.addView(mGameView);
  }

  private void initView() {
    mBtnRestart.setOnClickListener(this);
    mBtnRevert.setOnClickListener(this);
    mBtnOptions.setOnClickListener(this);
    mHightScore = Config.mSp.getInt(Config.KEY_HIGH_SCROE, 0);
    mGoal = Config.mSp.getInt(Config.KEY_GAME_GOAL, 2048);
    mTvHighScore.setText("" + mHightScore);
    mTvGoal.setText("" + mGoal);
    mTvScore.setText("0");
    setScore(0, 0);
  }

  /**
   * 修改得分
   */
  public void setScore(int score, int flag) {
    switch (flag) {
      case 0:
        mTvScore.setText("" + score);
        break;
      case 1:
        mTvHighScore.setText("" + score);
        break;
    }
  }

  public void setGoal(int num) {
    mTvGoal.setText(String.valueOf(num));
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_restart:
        mGameView.startGame();
        setScore(0, 0);
        break;
      case R.id.btn_revert:
        mGameView.revertGame();
        break;
      case R.id.btn_option:
        Intent intent = new Intent(this, ConfigPreference.class);
        startActivityForResult(intent, 0);
        break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      mGoal = Config.mSp.getInt(Config.KEY_GAME_GOAL, 2048);
      mTvGoal.setText("" + mGoal);
      getHighScore();
      mGameView.startGame();
    }
  }

  /**
   * 获取最高记录
   */
  private void getHighScore() {
    int score = Config.mSp.getInt(Config.KEY_HIGH_SCROE, 0);
    setScore(score, 1);
  }
}
