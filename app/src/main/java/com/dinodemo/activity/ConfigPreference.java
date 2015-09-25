package com.dinodemo.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dinodemo.R;
import com.dinodemo.activity.config.Config;

/**
 * Created by Coder on 2015/9/25.
 */
public class ConfigPreference extends AppCompatActivity implements View.OnClickListener {
  @Bind(R.id.btn_gameLines) Button mBtnGameLines;
  @Bind(R.id.btn_goal) Button mBtnGoal;
  @Bind(R.id.btn_back) Button mBtnBack;
  @Bind(R.id.btn_done) Button mBtnDone;

  private String[] mGameLinesList;
  private String[] mGameGoalList;

  private AlertDialog.Builder mBuilder;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.set_config);
    ButterKnife.bind(this);

    initView();
  }

  private void initView() {
    mBtnGameLines.setText("" + Config.mSp.getInt(Config.KEY_GAME_LINES, 4));
    mBtnGoal.setText("" + Config.mSp.getInt(Config.KEY_GAME_GOAL, 2048));
    mBtnGameLines.setOnClickListener(this);
    mBtnGoal.setOnClickListener(this);
    mBtnBack.setOnClickListener(this);
    mBtnDone.setOnClickListener(this);
    mGameLinesList = new String[] { "4", "5", "6" };
    mGameGoalList = new String[] { "1024", "2048", "4096" };
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_gameLines:
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("选择游戏行列数");
        mBuilder.setItems(mGameLinesList, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            mBtnGameLines.setText(mGameLinesList[which]);
          }
        }).create().show();
        break;
      case R.id.btn_goal:
        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("选择目标数字");
        mBuilder.setItems(mGameGoalList, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            mBtnGoal.setText(mGameGoalList[which]);
          }
        }).create().show();
        break;
      case R.id.btn_back:
        this.finish();
        break;
      case R.id.btn_done:
        saveConfig();
        setResult(RESULT_OK);
        this.finish();
        break;
    }
  }

  /**
   * 保存设置
   */
  private void saveConfig() {
    SharedPreferences.Editor editor = Config.mSp.edit();
    editor.putInt(Config.KEY_GAME_LINES, Integer.parseInt(mBtnGameLines.getText().toString()));
    editor.putInt(Config.KEY_GAME_GOAL, Integer.parseInt(mBtnGoal.getText().toString()));
    editor.commit();
  }
}
